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
    public List<Histoire> getListeHistoiresPublie() {
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
    
    public Paragraphe getHistoireTreeToRead(int idHist) {
    	ParagrapheDAO paragrapheDAO = new ParagrapheDAO(super.dataSource);
    	Paragraphe firstParag = paragrapheDAO.getParagraphe(idHist, 1);
    	try (
    			Connection conn = getConn();
    			) {
    		paragrapheDAO.setFollowingParagToRead(firstParag, conn);    	
    	} catch (SQLException e) {
    		throw new DAOException("Erreur BD " + e.getMessage(), e);
    	}
    	return firstParag;
    }
    
    public Paragraphe getHistoireTreeToWrite(int idHist) {
    	ParagrapheDAO paragrapheDAO = new ParagrapheDAO(super.dataSource);
    	Paragraphe firstParag = paragrapheDAO.getParagraphe(idHist, 1);
    	try (
		     Connection conn = getConn();
		     ) {
    			paragrapheDAO.setFollowingParagToWrite(firstParag, conn);    	
	        } catch (SQLException e) {
	            throw new DAOException("Erreur BD " + e.getMessage(), e);
			}
    	return firstParag;
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
    	} catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
		}
    	return hist;
    }
}
