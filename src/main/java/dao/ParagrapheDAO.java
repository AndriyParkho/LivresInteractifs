package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.DataSource;

import modele.HistoriqueModele;
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
                result = new Paragraphe(idHist, numParag, rs.getString("titre"), rs.getString("texte"), rs.getInt("nbChoix"));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
		}
		return result;
	}
	
	public boolean setFollowingParagToRead(Paragraphe parag, Connection conn) {
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
	            	Integer nbchoix = rs.getInt("nbchoix");
	            	if(rs.wasNull()) nbchoix = null;
					Paragraphe childParag = new Paragraphe(rs.getInt("idhist"), rs.getInt("numparag"), rs.getString("titre"), rs.getString("texte"), nbchoix);
					if(this.setFollowingParagToRead(childParag, conn)) parag.addParagSuiv(childParag);
				}
				if(parag.getParagSuiv().isEmpty()) return false;
				else return true;
			} catch (SQLException e) {
				throw new DAOException("Erreur BD " + e.getMessage(), e);
			}
		}
	}
	
	public void setFollowingParagToWrite(Paragraphe parag, Connection conn) {
		try (
	     Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	     ) {
            ResultSet rs = st.executeQuery("SELECT * FROM paragraphe JOIN isfollowing ON idhistfils = idhist AND "
            		+ "numparagfils = numparag WHERE idhistpere = " + parag.getIdHist() + 
            		" AND numparagpere = " + parag.getNumParag());
            while (rs.next()) {
            	Integer nbchoix = rs.getInt("nbchoix");
            	if(rs.wasNull()) nbchoix = null;
            	Integer idWritter = rs.getInt("idWritter");
            	if(rs.wasNull()) idWritter = null;
                Paragraphe childParag = new Paragraphe(rs.getInt("idhist"), rs.getInt("numparag"), rs.getString("titre"), 
                		rs.getString("texte"), rs.getBoolean("valide"), nbchoix, idWritter);
                this.setFollowingParagToWrite(childParag, conn); 
                parag.addParagSuiv(childParag);
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
		}
	}
	
	public Paragraphe getPragEnCours(int idUtil) {
		Paragraphe result = null;
		try (
		     Connection conn = getConn();
		     Statement st = conn.createStatement();
		     ) {
	            ResultSet rs = st.executeQuery("SELECT p.numparag, p.titre, texte, nbchoix, p.idhist, idwritter, valide FROM Paragraphe p, Histoire h WHERE P.idHist = H.idHist AND P.idWritter = " + idUtil + " AND P.valide = 0");
	            while (rs.next()) {
	            	Integer nbchoix = rs.getInt("nbchoix");
	            	if(rs.wasNull()) nbchoix = null;
	            	Integer idWritter = rs.getInt("idWritter");
	            	if(rs.wasNull()) idWritter = null;
	                result =
	                    new Paragraphe(rs.getInt("idhist"), rs.getInt("numparag"), rs.getString("titre"), 
	                    		rs.getString("texte"), rs.getBoolean("valide"), nbchoix, idWritter);
	            }
	        } catch (SQLException e) {
	            throw new DAOException("Erreur BD " + e.getMessage(), e);
			}
		return result;
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
	
	public HistoriqueModele getHistorique(int userId) {
		HistoriqueModele historique = new HistoriqueModele();
		try(
				Connection conn = getConn();	
		){
			Statement s = conn.createStatement();
			Statement ss = conn.createStatement();
			ResultSet r = s.executeQuery("SELECT DISTINCT idHist FROM hasRead WHERE idUtil=" + Integer.toString(userId));
			int idHist;
			int numParag;
			ResultSet rs;
			Paragraphe parag;
			ArrayList<Paragraphe> blocPara;
			ArrayList<ArrayList<Paragraphe>> treeOfStory;
			while(r.next()) {
				idHist = r.getInt("idHist");
				treeOfStory = new ArrayList<ArrayList<Paragraphe>>();
				blocPara = new ArrayList<Paragraphe>();
				rs = ss.executeQuery("SELECT numParag FROM hasRead WHERE idUtil=" + Integer.toString(userId) + "AND idHist = " + Integer.toString(idHist));
				Paragraphe currentParagraphe = null;
				while(rs.next()) {
					numParag = rs.getInt("numParag");
					if(numParag == 1) {
						currentParagraphe = getParagraphe(idHist, numParag);
						setFollowingParagToRead(currentParagraphe, conn);
						blocPara.add(currentParagraphe);
					}
					else {
						if(currentParagraphe.getParagSuiv().size() == 1) {
							currentParagraphe = currentParagraphe.getParagSuiv().get(0);
							blocPara.add(currentParagraphe);
						} else {
							treeOfStory.add(blocPara);
							blocPara = new ArrayList<Paragraphe>();
							currentParagraphe = currentParagraphe.getParagSuiv(numParag);
							if(currentParagraphe == null) {
								throw new IllegalArgumentException("");
							}
							blocPara.add(currentParagraphe);
						}
					}
				}
				if(!blocPara.isEmpty()) {
					treeOfStory.add(blocPara);
				}
				historique.addStory(idHist, treeOfStory);
			}	
		} catch (SQLException e) {
			throw new DAOException("Erreur BD " + e.getMessage(), e);
		}
		return historique;
	}
	
	public void deleteWritter(int idHist, int numParag) {
		try (
				Connection conn = getConn();
			    PreparedStatement ps = conn.prepareStatement("UPDATE paragraphe SET idWritter=NULL WHERE idHist=? and numParag=?");
			) {
				ps.setInt(1, idHist);
				ps.setInt(2, numParag);
			 	ps.executeQuery();
		      }catch (SQLException e) {
		    	throw new DAOException("Erreur BD " + e.getMessage(), e);
		      }
	}
}
