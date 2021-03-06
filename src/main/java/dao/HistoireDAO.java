package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import modele.Histoire;
import modele.Paragraphe;

public class HistoireDAO extends AbstractDataBaseDAO {

	public HistoireDAO(DataSource ds) {
		super(ds);
	}
	
	
	/**
     * Renvoie la liste des histoires publiée.
     */
    public List<Histoire> getListeHistoiresPublie() {
        List<Histoire> result = new ArrayList<Histoire>();
        try (
	     Connection conn = getConn();
	     Statement st = conn.createStatement();
	     ) {
            ResultSet rs = st.executeQuery("SELECT * FROM histoire WHERE datePubli IS NOT NULL ORDER BY datepubli desc");
            while (rs.next()) {
                Histoire histoire =
                    new Histoire(rs.getInt("idHist"), rs.getString("titre"), rs.getDate("datePubli"), rs.getInt("idAuteur"));
                result.add(histoire);
            }
            st.close();
            conn.close();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
		}
		return result;
	}
    
    /**
     * Renvoie la liste des histoires ou l'on peut écrire une partie.
     */
    public List<Histoire> getListeHistoiresAEcrire(int idUtil) {
        List<Histoire> result = new ArrayList<Histoire>();
        try (
	     Connection conn = getConn();
	     Statement st = conn.createStatement();
	     ) {
            ResultSet rs = st.executeQuery("SELECT * FROM histoire h WHERE prive = 0 AND (SELECT COUNT(*) FROM paragraphe p WHERE p.idhist = h.idhist AND idwritter IS NULL) <> 0");
            while (rs.next()) {
                Histoire histoire =
                    new Histoire(rs.getInt("idHist"), rs.getString("titre"), rs.getDate("datePubli"), rs.getInt("idAuteur"));
                result.add(histoire);
            }
            rs = st.executeQuery("SELECT * FROM histoire h JOIN isInvited isI ON h.idHist = isI.idHist WHERE isI.idUtil = " + Integer.toString(idUtil) + " AND (SELECT COUNT(*) FROM paragraphe p WHERE p.idhist = h.idhist AND idwritter IS NULL) <> 0");
            while (rs.next()) {
                Histoire histoire =
                    new Histoire(rs.getInt("idHist"), rs.getString("titre"), rs.getDate("datePubli"), rs.getInt("idAuteur"));
                result.add(histoire);
            }
            st.close();
            conn.close();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
		}
		return result;
	}
    
    public Histoire createNewStoryObjet(String title, int userId){
        try(Connection c = dataSource.getConnection()){
            PreparedStatement ps_numero_histoire = c.prepareStatement("SELECT idHist_seq.nextval FROM DUAL");
            int numero_histoire = 1;
            try{
                ResultSet result_set = ps_numero_histoire.executeQuery();
                while(result_set.next()){
                    numero_histoire = result_set.getInt(1);
                    
                }
                ps_numero_histoire.close();
                c.close();
                return new Histoire(numero_histoire, title, null, userId);
            }catch(SQLException e){
            	throw new DAOException("Erreur BD" + e.getMessage(), e);
            }
        }catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
		}
        
    }
    
    public boolean createStory(Histoire histoire, int confidentialite){
        try(Connection c = dataSource.getConnection()){
            
            PreparedStatement ps_histoire = c.prepareStatement("INSERT INTO Histoire (idHist, titre, datePubli, idAuteur, prive) VALUES ( ?, ?, NULL, ?, ?)");
            PreparedStatement ps_paragraphe = c.prepareStatement("INSERT INTO Paragraphe (numParag, titre, texte, valide, nbChoix, idWritter, idHist) VALUES (?, ?, ?, ?, ?, ?, ?)");
            
            PreparedStatement ps_ajoutInvitation = c.prepareStatement("INSERT INTO isInvited (idHist, idUtil) VALUES (?, ?)");
            
            ps_histoire.setInt(1, histoire.getId());
            ps_histoire.setString(2, histoire.getTitre());
            ps_histoire.setInt(3, histoire.getIdAuteur()); //par défaut utilisateur 1, a changer futurement
            ps_histoire.setInt(4, confidentialite);
            Paragraphe paragraphe = histoire.getFirstParag();
            ps_paragraphe.setInt(1, paragraphe.getNumParag()); //on laisse a 1, c'est le premier paragraphe
            ps_paragraphe.setString(2, paragraphe.getTitre());
            ps_paragraphe.setString(3, paragraphe.getTexte());
            ps_paragraphe.setInt(4, 1); //pas valide au début
            ps_paragraphe.setInt(5, paragraphe.getNbChoix());
            ps_paragraphe.setInt(6, histoire.getIdAuteur());
            
            ps_paragraphe.setInt(7, histoire.getId());
            
            ps_histoire.executeUpdate();
            ps_paragraphe.executeUpdate();
            if(confidentialite == 1){
                ps_ajoutInvitation.setInt(1, histoire.getId());
                ps_ajoutInvitation.setInt(2, histoire.getIdAuteur());
                ps_ajoutInvitation.executeUpdate();
            }
            ps_histoire.close();
            ps_paragraphe.close();
            ps_ajoutInvitation.close();
            c.close();
        }catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
		}
        
        return true;        
    }
    
    public List<Histoire> getHistoires(List<Integer> idStories){
    	List<Histoire> listStories = new ArrayList<Histoire>();
    	for(int id : idStories) {
    		listStories.add(getHistoire(id));
    	}
    	return listStories;
    }
    
    public Histoire getHistoire(int histId) {
    	Histoire hist = null;
    	try(Connection c = dataSource.getConnection()){
    		PreparedStatement story = c.prepareStatement("SELECT titre, datePubli, idAuteur FROM Histoire WHERE idHist=?");
    		story.setInt(1, histId);
    		ResultSet rs = story.executeQuery();
    		if(rs.next()) {
    			hist = new Histoire(histId, rs.getString("titre"), rs.getDate("datePubli"), rs.getInt("idAuteur"));
    		}
    		story.close();
    		c.close();
    	} catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
		}
    	return hist;
    }
    
    public List<Histoire> getHistoiresAPublier(int idUtilisateur){
        List<Histoire> listStories = new ArrayList<Histoire>();
        try(Connection c = dataSource.getConnection()){
            PreparedStatement storiesToPublish = c.prepareStatement("SELECT H.idHist FROM Histoire H JOIN Paragraphe P ON H.idHist = P.idHist WHERE P.nbChoix = 0 AND P.valide = 1 AND H.DatePubli IS NULL AND H.idAuteur = "+idUtilisateur);
            ResultSet rs = storiesToPublish.executeQuery();
            while(rs.next()){
                int idHist = rs.getInt("idHist");
                listStories.add(getHistoire(idHist));
            }
            storiesToPublish.close();
            c.close();
            return listStories;
        } catch (SQLException sqle){
            throw new DAOException("Erreur BD" + sqle.getMessage(), sqle);
        }  
    }
    
    public void publierHistoire(Histoire histoire){
        try{
            Connection c = dataSource.getConnection();
            PreparedStatement ps_publication = c.prepareStatement("UPDATE Histoire SET DatePubli = SYSDATE WHERE idHist = "+histoire.getId());
            ps_publication.executeUpdate();
            ps_publication.close();
            c.close();
        }catch(SQLException sqle){
            throw new DAOException("Erreur BD" + sqle.getMessage(), sqle);
        }
    }
    
    public void depublierHistoire(Histoire histoire){
        try{
            Connection c = dataSource.getConnection();
            PreparedStatement ps_publication = c.prepareStatement("UPDATE Histoire SET DatePubli = null WHERE idHist = "+histoire.getId());
            ps_publication.executeUpdate();
            ps_publication.close();
            c.close();
        }catch(SQLException sqle){
            throw new DAOException("Erreur BD" + sqle.getMessage(), sqle);
        }
    }
    
    public void inviteUsers(Histoire histoire){
        try(Connection c = dataSource.getConnection()){
            for(String auteur : histoire.getAuteurs()){
                PreparedStatement ps_newInvit = c.prepareStatement("INSERT INTO isInvited (idHist, idUtil) VALUES (?,?)");
                ps_newInvit.setInt(1, histoire.getId());
                ps_newInvit.setInt(2, Integer.parseInt(auteur));
                ps_newInvit.execute();
                ps_newInvit.close();
            }
            c.close();
        }catch(SQLException sqle){
            throw new DAOException("Erreur BD" + sqle.getMessage(), sqle);
        }
    }
    
    public List<Histoire> getHistoiresDepubliables(int idUtilisateur){
        List<Histoire> listStories = new ArrayList<Histoire>();
        try(Connection c = dataSource.getConnection()){
            PreparedStatement storiesToPublish = c.prepareStatement("SELECT H.idHist FROM Histoire H JOIN Paragraphe P ON H.idHist = P.idHist WHERE P.nbChoix = 0 AND P.valide = 1 AND H.DatePubli IS NOT NULL AND H.idAuteur = " + idUtilisateur);
            ResultSet rs = storiesToPublish.executeQuery();
            while(rs.next()){
                int idHist = rs.getInt("idHist");
                listStories.add(getHistoire(idHist));
            }
            storiesToPublish.close();
            c.close();
            return listStories;
        } catch (SQLException sqle){
            throw new DAOException("Erreur BD" + sqle.getMessage(), sqle);
        }  
    }
}
