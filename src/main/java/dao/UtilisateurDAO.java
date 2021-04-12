package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import modele.Histoire;
import modele.HistoriqueModele;
import modele.Paragraphe;
import modele.Utilisateur;

public class UtilisateurDAO extends AbstractDataBaseDAO {
	
	public UtilisateurDAO(DataSource ds) {
		super(ds);
	}
	
	public Utilisateur getUser(String email, String password){
		try (Connection conn = dataSource.getConnection()){
            PreparedStatement s = conn.prepareStatement(
                "SELECT idUtil, nom, prenom FROM Utilisateur WHERE email = ? AND password = ?"
            );
            s.setString(1, email);
            s.setString(2, password);
            
            ResultSet r = s.executeQuery();
            if (r.next()) {
                int id = r.getInt("idUtil");
                String nom = r.getString("nom");
                String prenom = r.getString("prenom");
                Utilisateur user = new Utilisateur(id, nom, prenom, email, password);
                return user;
                
            }
            else {
            	return null;
            }
        } catch (SQLException e) {
        	throw new DAOException("Erreur BD" + e.getMessage(), e);
        }
	}
	
	public boolean createUser(String nom, String prenom, String email, String pass){
		try (Connection c = dataSource.getConnection()) {
            PreparedStatement ps =
                c.prepareStatement("INSERT INTO Utilisateur (nom, prenom, email, password) VALUES (?, ?, ?, ?)");
            ps.setString(1, nom);
            ps.setString(2, prenom);
            ps.setString(3, email);
            ps.setString(4, pass);
            ps.executeUpdate();
            return true;
        } catch (SQLException sqle) {
            return false;
        }

	}
	
	public List<Utilisateur> getUserExceptMe(int userId) {
		List<Utilisateur> result = new ArrayList<Utilisateur>();
		try (Connection conn = dataSource.getConnection()){
            PreparedStatement s = conn.prepareStatement(
                "SELECT idUtil, nom, prenom, email, password FROM Utilisateur WHERE idUtil != ?"
            );
            s.setInt(1, userId);
            ResultSet r = s.executeQuery();
            while (r.next()) {
            	Utilisateur user = new Utilisateur(r.getInt("idUtil"), r.getString("nom"), r.getString("prenom"), r.getString("email"), r.getString("password"));
                result.add(user);
            }
        } catch (SQLException e) {
        	throw new DAOException("Erreur BD" + e.getMessage(), e);
        }
		return result;
	}
	
	public List<Histoire> getStoryRead(int userId){
		List<Histoire> result = new ArrayList<Histoire>();
		try (Connection conn = dataSource.getConnection()){
            PreparedStatement s = conn.prepareStatement(
                "SELECT DISTINCT idHist FROM hasRead WHERE idUtil=?"
            );
            s.setInt(1, userId);
            ResultSet r = s.executeQuery();
            Statement st = conn.createStatement();
            ResultSet rs = null;
            int idHist = -1;
            while (r.next()) {
            	idHist = r.getInt("idHist");
            	rs = st.executeQuery("SELECT idHist, titre, datePubli, idAuteur FROM histoire WHERE idHist = " + Integer.toString(idHist));
            	if(rs.next()) {
            		Histoire story = new Histoire(idHist, rs.getString("titre"), rs.getDate("datePubli"), rs.getInt("idAuteur"));
            		result.add(story);
            	} else {
            		throw new DAOException("Une histoire a été cherchée mais n'est pas contenu dans la bdd");
            	}
            }
        } catch (SQLException e) {
        	throw new DAOException("Erreur BD" + e.getMessage(), e);
        }
		return result;
		
	}
	
	public void saveHistorique(int userId, HistoriqueModele historique){
		List<Integer> listeHistoire = historique.getStories();
		List<ArrayList<Paragraphe>> listeParagraphe;
		for(int idHist: listeHistoire) {
			listeParagraphe = historique.getTree(idHist);
			if(listeParagraphe != null) {
				try (
						Connection conn = getConn();	
						Statement st = conn.createStatement();
				){
						String idHistory = Integer.toString(idHist);
						String idUtil = Integer.toString(userId);
					    st.executeQuery("DELETE FROM hasRead WHERE idHist = " + idHistory + "AND idUtil =" + idUtil);
					    String numParag;
					    int compteur = 0;
					    for(ArrayList<Paragraphe> bloc : listeParagraphe) {
					    	for(Paragraphe parag : bloc) {
					    		numParag = Integer.toString(parag.getNumParag());
					    		st.executeQuery("INSERT INTO hasRead (idHist, numParag, idUtil, locationID) VALUES (" + idHistory + ", " + numParag + ", " + idUtil + ", " + Integer.toString(compteur)+ ")");
					    		compteur++;
					    	}
					    }
				} catch (SQLException e) {
					throw new DAOException("Erreur BD " + e.getMessage(), e);
				}
			}
		}
	}
	

}
