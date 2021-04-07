package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
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
	

}
