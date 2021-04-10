package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import modele.Histoire;
import modele.Paragraphe;
import modele.Utilisateur;

public class HistoireDAO extends AbstractDataBaseDAO {

	public HistoireDAO(DataSource ds) {
		super(ds);
	}
	
	
	/**
     * Renvoie la liste des histoires publiée.
     */
    public List<Histoire> getListeHistoires() {
        List<Histoire> result = new ArrayList<Histoire>();
        try (
	     Connection conn = getConn();
	     Statement st = conn.createStatement();
	     ) {
            ResultSet rs = st.executeQuery("SELECT * FROM histoire WHERE datePubli IS NOT NULL");
            while (rs.next()) {
                Histoire histoire =
                    new Histoire(rs.getInt("idHist"), rs.getString("titre"), rs.getDate("datePubli"), rs.getInt("idAuteur"));
                result.add(histoire);
            }
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
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
		}
		return result;
	}
    
    public Paragraphe getHistoireTree(int idHist) {
    	ParagrapheDAO paragrapheDAO = new ParagrapheDAO(super.dataSource);
    	Paragraphe firstParag = paragrapheDAO.getParagraphe(idHist, 1);
    	try (
    			Connection conn = getConn();
    			) {
    		paragrapheDAO.setFollowingParag(firstParag, conn);    	
    	} catch (SQLException e) {
    		throw new DAOException("Erreur BD " + e.getMessage(), e);
    	}
    	return firstParag;
    }
    
    public Paragraphe getAllHistoireTree(int idHist) {
    	ParagrapheDAO paragrapheDAO = new ParagrapheDAO(super.dataSource);
    	Paragraphe firstParag = paragrapheDAO.getParagraphe(idHist, 1);
    	try (
		     Connection conn = getConn();
		     ) {
    			paragrapheDAO.setAllFollowingParag(firstParag, conn);    	
	        } catch (SQLException e) {
	            throw new DAOException("Erreur BD " + e.getMessage(), e);
			}
    	return firstParag;
    }
    
    public Histoire getHistoireEnCours(int idUser, HttpServletRequest request){
        Histoire histoire = null;
        
        try(Connection c = dataSource.getConnection()){
            PreparedStatement ps_story = c.prepareStatement("SELECT H.idHist, H.titre, H.prive, H.datePubli, H.idAuteur, P.numParag FROM Paragraphe P, Histoire H WHERE P.idHist = H.idHist AND P.idWritter = ? AND P.valide = 0");
            ps_story.setInt(1, idUser);
            ResultSet rs = ps_story.executeQuery();
            int idHist;
            String titre;
            int prive;
            Date datePubli;
            int idAuteur;
            int numParag;
            
            if(rs == null){
                return null;
            }
            while(rs.next()){
                idHist = rs.getInt("idHist");
                titre = rs.getString("titre");
                prive = rs.getInt("prive");
                datePubli = rs.getDate("datePubli");
                idAuteur = rs.getInt("idAuteur");
                numParag = rs.getInt("numParag");
                
            histoire = new Histoire(idHist, titre, datePubli, idAuteur);
            HttpSession sess = request.getSession(false);
            sess.setAttribute("numParag", numParag);
            return histoire;
            }
        return histoire;    
        }catch(SQLException sqle){
            throw new DAOException("Erreur BD "+ sqle.getMessage(),sqle);
            
        }
        
    }
    
    public boolean createStory(HttpServletRequest request){
        HttpSession session = request.getSession();
        Utilisateur user = (Utilisateur)session.getAttribute("user");
        int confidentialite = 0;
       
        
        try {
            confidentialite = Integer.parseInt(request.getParameter("confident"));
        } catch (Exception e){
        	
        }
            
        String[] auteurs = request.getParameterValues("auteurs");
        String title = request.getParameter("title");
        String titreParagraphe = request.getParameter("titreParagraphe");
        String paragraphe = request.getParameter("story");
        int nb_choix = Integer.parseInt(request.getParameter("nbChoix"));
        
        try(Connection c = dataSource.getConnection()){
            
            PreparedStatement ps_histoire = c.prepareStatement("INSERT INTO Histoire (idHist, titre, datePubli, idAuteur, prive) VALUES ( ?, ?, NULL, ?, ?)");
            PreparedStatement ps_paragraphe = c.prepareStatement("INSERT INTO Paragraphe (numParag, titre, texte, valide, nbChoix, idWritter, idHist) VALUES (?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement ps_numero_histoire = c.prepareStatement("SELECT idHist_seq.nextval FROM DUAL");
            PreparedStatement ps_ajoutInvitation = c.prepareStatement("INSERT INTO isInvited (idHist, idUtil) VALUES (?, ?)");
            int numero_histoire = 1;
            
            try{
                ResultSet result_set = ps_numero_histoire.executeQuery();
                while(result_set.next()){
                    numero_histoire = result_set.getInt(1);
                }
            }catch(SQLException e){
            	throw new DAOException("Erreur BD" + e.getMessage(), e);
            } 
            ps_histoire.setInt(1, numero_histoire);
            ps_histoire.setString(2, title);
            ps_histoire.setInt(3, user.getId()); //par défaut utilisateur 1, a changer futurement
            ps_histoire.setInt(4, confidentialite);
            
            ps_paragraphe.setInt(1, 1); //on laisse a 1, c'est le premier paragraphe
            ps_paragraphe.setString(2, titreParagraphe);
            ps_paragraphe.setString(3, paragraphe);
            ps_paragraphe.setInt(4, 0); //pas valide au début
            ps_paragraphe.setInt(5, nb_choix);
            ps_paragraphe.setInt(6, user.getId());
            
            ps_paragraphe.setInt(7, numero_histoire);
            
            
            
            ps_histoire.executeUpdate();
            ps_paragraphe.executeUpdate();
            if(confidentialite == 1){
                ps_ajoutInvitation.setInt(1, numero_histoire);
                ps_ajoutInvitation.setInt(2, user.getId());
                ps_ajoutInvitation.executeUpdate();
            }
            
        }catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
		}
        
        return true;        
    }
    
    public boolean setReader(int idHist, int idUtil) {
    	try(Connection c = dataSource.getConnection()){
            PreparedStatement ps = c.prepareStatement("INSERT INTO hasRead (idHist, numParag, idUtil, LocationId) VALUES (?, 1, ?, 1)");
            ps.setInt(1, idHist);
            ps.setInt(2, idUtil);
            ps.executeQuery();
    	} catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
		}
    	return true;
    }
    
    public boolean setReader(int idHist, int numParag, int idUtil) {
    	try(Connection c = dataSource.getConnection()){
    		if(true){//TODO vérifier qu'il n'y a pas d'entrée pour ces valeurs (idHist, numParag, idUtil)
    			PreparedStatement getMax = c.prepareStatement("SELECT MAX(locationId) FROM hasRead WHERE idHist=? AND idUtil=?");
    			getMax.setInt(1, idHist);
                getMax.setInt(2, idUtil);
                ResultSet rs = getMax.executeQuery();
                int nextLocation = -1;
                if(rs.next()) {
                	nextLocation = rs.getInt("max(locationId)") + 1;
                } 
    			PreparedStatement ps = c.prepareStatement("INSERT INTO hasRead (idHist, numParag, idUtil, LocationId) VALUES (?, ?, ?, ?)");
                ps.setInt(1, idHist);
                ps.setInt(2, numParag);
                ps.setInt(3, idUtil);
                ps.setInt(4, nextLocation);
    		}
    		else {
    			return true;
    		}
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
		}
    	return true;
    }
}
