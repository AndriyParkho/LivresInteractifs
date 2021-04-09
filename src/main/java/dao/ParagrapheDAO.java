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
	
	public boolean setFollowingParag(Paragraphe parag, Connection conn) {
		if(parag.getNbChoix() == 0) {
			return true;
		} else {
			try (
		     Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		     ) {
	            ResultSet rs = st.executeQuery("SELECT numparag, titre, texte, nbchoix, idhist FROM paragraphe JOIN "
	            		+ "isfollowing ON idhistfils = idhist AND numparagfils = numparag WHERE idhistpere = " + parag.getIdHist() + 
	            		" AND numparagpere = " + parag.getNumParag()  + "AND texte IS NOT NULL AND valide = 1");
	            if(!rs.next()) return false;
	            rs.beforeFirst();
	            while (rs.next()) {
	                Paragraphe childParag = new Paragraphe(rs.getInt("idhist"), rs.getInt("numparag"), rs.getString("titre"), rs.getString("texte"), rs.getInt("nbchoix"));
	                if(this.setFollowingParag(childParag, conn)) parag.addParagSuiv(childParag);
	            }
	            if(parag.getParagSuiv().isEmpty()) return false;
	            else return true;
	        } catch (SQLException e) {
	            throw new DAOException("Erreur BD " + e.getMessage(), e);
			}
		}
	}
	
	public boolean setWritter(int histId, int paragraphNum, int userId) {
		try (
				Connection conn = getConn();
			    PreparedStatement ps = conn.prepareStatement("UPDATE paragraphe SET idWritter=? WHERE idHist=? and numParag=?");
			) {
				ps.setInt(1, userId);
				ps.setInt(2, histId);
				ps.setInt(3, paragraphNum);
			 	ps.executeQuery();
		      }catch (SQLException e) {
		    	throw new DAOException("Erreur BD " + e.getMessage(), e);
		      }
		return true;
		
	}
}
