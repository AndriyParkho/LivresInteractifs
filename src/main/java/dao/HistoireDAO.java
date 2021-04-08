package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
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
            ResultSet rs = st.executeQuery("SELECT * FROM histoire WHERE datePubli IS NULL AND prive = 0");
            while (rs.next()) {
                Histoire histoire =
                    new Histoire(rs.getInt("idHist"), rs.getString("titre"), rs.getDate("datePubli"), rs.getInt("idAuteur"));
                result.add(histoire);
            }
            rs = st.executeQuery("SELECT * FROM histoire JOIN isInvited ON histoire.idHist = isInvited.idHist WHERE isInvited.idUtil = " + Integer.toString(idUtil));
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
}
