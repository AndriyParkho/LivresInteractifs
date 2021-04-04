package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import modele.Histoire;
import modele.Paragraphe;

public class ParagrapheDAO extends AbstractDataBaseDAO {

	public ParagrapheDAO(DataSource ds) {
		super(ds);
	}
	
	public Paragraphe getParagraphe(int idHist, int numParag) {
		Paragraphe result = null;
		try (
	     Connection conn = getConn();
	     Statement st = conn.createStatement();
	     ) {
            ResultSet rs = st.executeQuery("SELECT * FROM paragraphe WHERE idHist =" + idHist + "AND numParag = " + numParag);
            if (rs.next()) {
                result = new Paragraphe(idHist, numParag, rs.getString("titre"), rs.getString("texte"), rs.getInt("nbchoix"));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
		}
		return result;
	}
	
	public List<Paragraphe> getFollowingParag(int idHist, int numParag) {
		List<Paragraphe> result = new ArrayList<Paragraphe>();
//        try (
//	     Connection conn = getConn();
//	     Statement st = conn.createStatement();
//	     ) {
//            ResultSet rs = st.executeQuery("SELECT * FROM histoire WHERE datePubli IS NOT NULL");
//            while (rs.next()) {
//                Histoire histoire =
//                    new Histoire(rs.getInt("idHist"), rs.getString("titre"), rs.getDate("datePubli"), rs.getInt("idAuteur"));
//                result.add(histoire);
//            }
//        } catch (SQLException e) {
//            throw new DAOException("Erreur BD " + e.getMessage(), e);
//		}
		return result;
	}
}
