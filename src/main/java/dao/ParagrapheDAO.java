package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

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
	/*
	 * @param dicoParag : stock les paragraphes qu'on a déjà vu
	 * @param parag : paragraphe sur le quel on est dans notre parcours de l'arbre
	 */
	public boolean setFollowingParagToRead(HashMap<Integer, Paragraphe> dicoParag ,Paragraphe parag, Connection conn) {
		/*Si le paragraphe est un paragraphe de conclusion ses pères peuvent l'ajouter à leur liste de ParagSuiv*/
		if(parag.getNbChoix() == 0) {
			return true;
		} else { /*Sinon on récupère les fils du paragraphe qui ont du texte et qui sont validé*/
			try (
					Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
					) {
				ResultSet rs = st.executeQuery("SELECT numparag, titre, texte, nbchoix, idhist FROM paragraphe JOIN "
						+ "isfollowing ON idhistfils = idhist AND numparagfils = numparag WHERE idhistpere = " + parag.getIdHist() + 
						" AND numparagpere = " + parag.getNumParag()  + "AND texte IS NOT NULL AND valide = 1");
				/*Si il n'a pas de fils avec du texte et qui sont validé alors il dit à ses pères qu'il ne menne pas à une conclusion*/
				if(!rs.next()) return false;
				rs.beforeFirst(); /*On se replace au début dans le cas où le if précédent était false*/
				while (rs.next()) { /*On parcours les fils qui semble bon*/
					Integer numParag = rs.getInt("numParag");
					Paragraphe childParag = dicoParag.get(numParag); /*On vérifie si on est déjà passé par le paragraphe*/ 
					if(childParag == null) {/*Si ce n'est pas le cas on créer le paragraphe fils*/						
						Integer nbchoix = rs.getInt("nbchoix");
						if(rs.wasNull()) nbchoix = null;
						childParag = new Paragraphe(rs.getInt("idhist"), numParag, rs.getString("titre"), rs.getString("texte"), nbchoix);
						dicoParag.put(numParag, childParag); /*On le rajoute à la liste des paragraphes déjà vu*/
						/*On rajoute le fils que s'il nous dit qu'on peut (en relancant la fonction sur lui)*/
						if(this.setFollowingParagToRead(dicoParag, childParag, conn)) parag.addParagSuiv(childParag);
					} else { /*Si c'est le cas on rajoute ce paragraphe fils*/
						parag.addParagSuiv(childParag);
						return true; /*Et on dit au père qu'il peut nous ajouter comme fils (Ce qui n'est pas vraiment 
										vrai car il ne mène pas forcément à une conclu*/
					}
				}
				if(parag.getParagSuiv().isEmpty()) return false; /*Si aucun fils n'a été ajouté à la liste car aucun ne menait à une 
																	conclusion, on dit aux pères qu'on ne mène pas à une conclu*/
				else return true; /*Sinon on dit qu'on mène à une conclusion*/
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
						HashMap<Integer, Paragraphe> dicoParag = new HashMap<Integer, Paragraphe>();
						dicoParag.put(numParag, currentParagraphe);
						setFollowingParagToRead(dicoParag ,currentParagraphe, conn);
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
	
	public ArrayList<Paragraphe> getParagrapheFromHist(int histId){
		ArrayList<Paragraphe> paragrapheRedige = new ArrayList<Paragraphe>();
		try (
				Connection conn = getConn();
			    PreparedStatement ps = conn.prepareStatement("SELECT numParag, titre, texte, nbChoix FROM paragraphe WHERE idHist = ?");
			) {
				ps.setInt(1, histId);
				ResultSet rs = ps.executeQuery();
				Paragraphe parag;
				while(rs.next()) {
					parag = new Paragraphe(histId, rs.getInt("numParag"), rs.getString("titre"),rs.getString("texte"), rs.getInt("nbChoix"));
					paragrapheRedige.add(parag);
				}
		      }catch (SQLException e) {
		    	throw new DAOException("Erreur BD " + e.getMessage(), e);
		      }
		return paragrapheRedige;
	}
}
