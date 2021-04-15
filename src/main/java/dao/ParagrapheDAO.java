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
import modele.Pair;
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
            	Integer nbchoix = rs.getInt("nbchoix");
				if(rs.wasNull()) nbchoix = null;
				Integer idWritter = rs.getInt("idWritter");
				if(rs.wasNull()) idWritter = null;
                result = new Paragraphe(idHist, numParag, rs.getString("titre"), rs.getString("texte"), rs.getBoolean("valide") , nbchoix, idWritter);
            }
            st.close();
            conn.close();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
		}
		return result;
	}
	
	/*
	 * @param dicoParag : stock les paragraphes qu'on a déjà vu
	 * @param paragsPere : liste des paragraphes père avec leur condition pour mener au paragrapheToAdd 
	 * @param paragToAdd : paragraphe à ajouter aux paragraphes père
	 */
	public void getBrancheConclusion(HashMap<Integer, Paragraphe> dicoParag, ArrayList<Pair<Paragraphe, Integer>> paragsPere, Paragraphe paragToadd, Connection conn) {
		if(paragsPere.isEmpty() && paragToadd.getNumParag() == 1) {
			return;
		} else {
			for(Pair<Paragraphe, Integer> pair: paragsPere) {
				Paragraphe parag = pair.getLeft();
				if(paragToadd != null) {
					parag.addParagSuiv(paragToadd, pair.getRight());
				}
				try (
					Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
					) {
					ResultSet rs = st.executeQuery("SELECT numparag, titre, texte, nbchoix, idhist, conditionParag FROM paragraphe JOIN "
								+ "isfollowing ON idhistparag = idhist AND numparagpere = numparag WHERE idhistparag = " + parag.getIdHist() + 
								" AND numparagfils = " + parag.getNumParag()  + "AND texte IS NOT NULL AND valide = 1");
					ArrayList<Pair<Paragraphe, Integer>> newParagsPere = new ArrayList<Pair<Paragraphe,Integer>>();
					while (rs.next()) {
						Integer numParag = rs.getInt("numParag");
						Paragraphe pereParag = dicoParag.get(numParag); /*On vérifie si on est déjà passé par le paragraphe*/ 
						Integer conditionParag = rs.getInt("conditionParag");
						if(rs.wasNull()) conditionParag = null;
						if(pereParag == null) {/*Si ce n'est pas le cas on créer le paragraphe pere*/						
							Integer nbchoix = rs.getInt("nbchoix");
							if(rs.wasNull()) nbchoix = null;
							pereParag = new Paragraphe(rs.getInt("idhist"), numParag, rs.getString("titre"), rs.getString("texte"), nbchoix);
							dicoParag.put(numParag, pereParag); /*On le rajoute à la liste des paragraphes déjà vu*/
							newParagsPere.add(new Pair(pereParag, conditionParag));
						} else { /*Si c'est le cas on rajoute ce paragraphe pere*/
							pereParag.addParagSuiv(parag, conditionParag);
						}
					}
					getBrancheConclusion(dicoParag, newParagsPere, parag, conn);
					st.close();
					} catch (SQLException e) {
						throw new DAOException("Erreur BD " + e.getMessage(), e);
					}
			}
		}
	}
	
	public void setFollowingParagToWrite(HashMap<Integer, Paragraphe> dicoParag, Paragraphe parag, Connection conn) {
		try (
	     Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	     ) {
            ResultSet rs = st.executeQuery("SELECT * FROM paragraphe JOIN isfollowing ON idhistparag = idhist AND "
            		+ "numparagfils = numparag WHERE idhistparag = " + parag.getIdHist() + 
            		" AND numparagpere = " + parag.getNumParag());
            while (rs.next()) {
            	Integer numParag = rs.getInt("numParag");
            	if(rs.wasNull()) numParag = null;
            	Paragraphe childParag = dicoParag.get(numParag);
            	if(childParag == null) {            		
            		Integer nbchoix = rs.getInt("nbchoix");
            		if(rs.wasNull()) nbchoix = null;
            		Integer idWritter = rs.getInt("idWritter");
            		if(rs.wasNull()) idWritter = null;
            		childParag = new Paragraphe(rs.getInt("idhist"), rs.getInt("numparag"), rs.getString("titre"), 
            				rs.getString("texte"), rs.getBoolean("valide"), nbchoix, idWritter);
            		dicoParag.put(numParag, childParag);
            		this.setFollowingParagToWrite(dicoParag, childParag, conn); 
            	}
                parag.addParagSuiv(childParag);
            }
            st.close();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
		}
	}
	
	public ArrayList<Pair<Paragraphe, Integer>> getParagConclu(int idHist, HashMap<Integer, Paragraphe> dicoParag, Connection conn){
		try (
    			Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    			) {
    		ResultSet rs = st.executeQuery("SELECT numparag, titre, texte, nbchoix, idhist FROM paragraphe WHERE idhist = " + idHist + 
					" AND nbchoix = 0 AND texte IS NOT NULL AND valide = 1");
    		ArrayList<Pair<Paragraphe, Integer>> paragsConclu = new ArrayList<Pair<Paragraphe, Integer>>();
    		while(rs.next()) {
    			Integer numParag = rs.getInt("numParag");
				if(rs.wasNull()) numParag = null;
    			Paragraphe concluParag = new Paragraphe(idHist, numParag, rs.getString("titre"), rs.getString("texte"), 0);
    			paragsConclu.add(new Pair(concluParag, null));
    			dicoParag.put(numParag, concluParag);
    		}
    		st.close();
    		return paragsConclu;
    	} catch (SQLException e) {
    		throw new DAOException("Erreur BD " + e.getMessage(), e);
    	}
	}
	
    public Paragraphe getHistoireTreeToRead(int idHist) {
    	Paragraphe firstParag = null;
    	try (
    			Connection conn = getConn();
    			){
    		HashMap<Integer, Paragraphe> dicoParag = new HashMap<Integer, Paragraphe>();
    		this.getBrancheConclusion(dicoParag, getParagConclu(idHist, dicoParag, conn), null, conn);
    		firstParag = dicoParag.get(1);
    		conn.close();
    	} catch (SQLException e) {
    		throw new DAOException("Erreur BD " + e.getMessage(), e);
    	}
    	return firstParag;
    }
    
    public Paragraphe getHistoireTreeToWrite(int idHist) {
    	Paragraphe firstParag = this.getParagraphe(idHist, 1);
    	try (
		     Connection conn = getConn();
		     ) {
    			HashMap<Integer, Paragraphe> dicoParag = new HashMap<Integer, Paragraphe>();
    			dicoParag.put(1, firstParag);
    			this.setFollowingParagToWrite(dicoParag, firstParag, conn);    
    			conn.close();
	        } catch (SQLException e) {
	            throw new DAOException("Erreur BD " + e.getMessage(), e);
			}
    	return firstParag;
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
	            st.close();
	            conn.close();
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
			 	ps.close();
			 	conn.close();
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
				rs = ss.executeQuery("SELECT numParag, locationid FROM hasRead WHERE idUtil=" + Integer.toString(userId) + "AND idHist = " + Integer.toString(idHist) + " ORDER BY locationid");
				Paragraphe currentParagraphe = null;
				while(rs.next()) {
					numParag = rs.getInt("numParag");
					if(numParag == 1) {
						HashMap<Integer, Paragraphe> dicoParag = new HashMap<Integer, Paragraphe>();
			    		this.getBrancheConclusion(dicoParag, getParagConclu(idHist, dicoParag, conn), null, conn);
						currentParagraphe = dicoParag.get(1);
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
			s.close();
			ss.close();
			conn.close();
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
			 	ps.close();
			 	conn.close();
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
				ps.close();
				conn.close();
		      }catch (SQLException e) {
		    	throw new DAOException("Erreur BD " + e.getMessage(), e);
		      }
		return paragrapheRedige;
	}
	
	public int getMaxNbParag(int idHist) {
		try (
				Connection conn = getConn();
			    PreparedStatement ps = conn.prepareStatement("SELECT MAX(numParag) FROM paragraphe WHERE idHist = ?");
			) {
				ps.setInt(1, idHist);
				ResultSet rs = ps.executeQuery();
				int max = 1;
				if(rs.next()) {
					max = rs.getInt("max(numParag)");
				}
				ps.close();
				conn.close();
				return max;
		      }catch (SQLException e) {
		    	throw new DAOException("Erreur BD " + e.getMessage(), e);
		      }
	}
	
	public void setParagraphe(Paragraphe parag) {
		try (
	     Connection conn = getConn();
				PreparedStatement ps = conn.prepareStatement("INSERT INTO Paragraphe (numParag, titre, idHist) VALUES (?, ?, ?)");
	     ) {
            ps.setInt(1, parag.getNumParag());
            ps.setString(2, parag.getTitre());
            ps.setInt(3, parag.getIdHist());
            ps.executeQuery();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
		}
	}
	
	public void valideParagraph(Paragraphe parag) {
		try (
			     Connection conn = getConn();
						PreparedStatement ps = conn.prepareStatement("UPDATE paragraphe SET valide=1, texte=?, nbChoix=? WHERE idHist=? and numParag=?");
			     ) {
		            ps.setString(1, parag.getTexte());
		            ps.setInt(2, parag.getNbChoix());
		            ps.setInt(3, parag.getIdHist());
		            ps.setInt(4, parag.getNumParag());
		            ps.executeQuery();
		            ps.close();
		            conn.close();
		        } catch (SQLException e) {
		            throw new DAOException("Erreur BD " + e.getMessage(), e);
				}
	}
	
	public void setFollowing(Paragraphe parag1, Paragraphe parag2) {
		try (
			     Connection conn = getConn();
						PreparedStatement ps = conn.prepareStatement("INSERT INTO isFollowing (idHistParag, numParagPere, numParagFils) VALUES (?, ?, ?)");
			     ) {
					ps.setInt(1, parag1.getIdHist());
		            ps.setInt(2, parag1.getNumParag());
		            ps.setInt(3, parag2.getNumParag());
		            ps.executeQuery();
		            ps.close();
		            conn.close();
		        } catch (SQLException e) {
		            throw new DAOException("Erreur BD " + e.getMessage(), e);
				}
	}
	
	public void setTexte(Paragraphe parag) {
		try (
			     Connection conn = getConn();
						PreparedStatement ps = conn.prepareStatement("UPDATE paragraphe SET texte=? WHERE idHist=? and numParag=?");
			     ) {
					ps.setString(1, parag.getTexte());
		            ps.setInt(2, parag.getIdHist());
		            ps.setInt(3, parag.getNumParag());
		            ps.executeQuery();
		            ps.close();
		            conn.close();
		        } catch (SQLException e) {
		            throw new DAOException("Erreur BD " + e.getMessage(), e);
				}
	}
	
	public String getTexte(Paragraphe parag) {
		String texte = null;
		try (
			     Connection conn = getConn();
						PreparedStatement ps = conn.prepareStatement("SELECT texte FROM paragraphe WHERE idHist=? and numParag=?");
			     ) {
		            ps.setInt(1, parag.getIdHist());
		            ps.setInt(2, parag.getNumParag());
		            ResultSet rs = ps.executeQuery();
					if(rs.next()) {
						texte = rs.getString("texte");
					}
					ps.close();
					conn.close();
		        } catch (SQLException e) {
		            throw new DAOException("Erreur BD " + e.getMessage(), e);
				}
		return texte;
	}
	
	public void setFollowingParag(Paragraphe parag) {
		try (
		     Connection conn = getConn();
		     Statement st = conn.createStatement();
		     ) {
				ResultSet rs = st.executeQuery("SELECT * FROM paragraphe JOIN isfollowing ON idhistparag = idhist AND " +
						"numparagfils = numparag WHERE idhistparag = " + parag.getIdHist() +
						" AND numparagpere = " + parag.getNumParag());					
				while (rs.next()) {
					Integer numParag = rs.getInt("numParag");
	            	if(rs.wasNull()) numParag = null;     		
            		Integer nbchoix = rs.getInt("nbchoix");
            		if(rs.wasNull()) nbchoix = null;
            		Integer idWritter = rs.getInt("idWritter");
            		if(rs.wasNull()) idWritter = null;
            		Paragraphe childParag = new Paragraphe(rs.getInt("idhist"), rs.getInt("numparag"), rs.getString("titre"), 
            				rs.getString("texte"), rs.getBoolean("valide"), nbchoix, idWritter);
	                parag.addParagSuiv(childParag);
				}
				st.close();
				conn.close();
			} catch (SQLException e) {
	            throw new DAOException("Erreur BD " + e.getMessage(), e);
	        }
	}
	
	public HashMap<String, ArrayList<Paragraphe>> setParagrapheRedige(int idUtil) {
		try (
		     Connection conn = getConn();
		     Statement st = conn.createStatement();
		     ) {
				HashMap<String, ArrayList<Paragraphe>> paragrapheRedige = new HashMap<String, ArrayList<Paragraphe>>();
				ResultSet rsPPublie = st.executeQuery("SELECT numparag, p.idhist, h.titre as htitre, p.titre as ptitre, texte, valide, nbchoix, idwritter FROM paragraphe p JOIN histoire h ON h.idhist = p.idhist WHERE idwritter = " + idUtil +" AND valide = 1");					
				while (rsPPublie.next()) {
					String histTitre = rsPPublie.getString("htitre");
					String paragTitre = rsPPublie.getString("ptitre");
					int numParag = rsPPublie.getInt("numparag");
					Paragraphe paragRedige = new Paragraphe(rsPPublie.getInt("idhist"), numParag, paragTitre, rsPPublie.getString("texte"));
					setFollowingParag(paragRedige);
					ArrayList<Paragraphe> listParag = paragrapheRedige.get(histTitre);
					if(listParag == null) {
						listParag = new ArrayList<Paragraphe>();
						listParag.add(paragRedige);
						paragrapheRedige.put(histTitre, listParag);
					} else {
						listParag.add(paragRedige);
					}	
				}
				st.close();
				conn.close();
				return paragrapheRedige;
			} catch (SQLException e) {
	            throw new DAOException("Erreur BD " + e.getMessage(), e);
	        }
	}
	
	public ArrayList<Paragraphe> getConditionParag(int idHist, int numParag){
		try (
				Connection conn = getConn();
    			Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    			) {
    		ResultSet rs = st.executeQuery("SELECT numparag, titre, texte, nbchoix, idhist FROM paragraphe WHERE idhist = " + idHist + 
					" AND numParag = " + numParag);
    		ArrayList<Pair<Paragraphe, Integer>> parags = new ArrayList<Pair<Paragraphe, Integer>>();
    		HashMap<Integer, Paragraphe> dicoParag = new HashMap<Integer, Paragraphe>();
    		if(rs.next()) {
    			Paragraphe parag = new Paragraphe(idHist, numParag, rs.getString("titre"), rs.getString("texte"), 0);
    			parags.add(new Pair(parag, null));
    			dicoParag.put(numParag, parag);
    		}
    		this.getBrancheConclusion(dicoParag, parags, null, conn);
    		dicoParag.remove(numParag);
    		st.close();
    		conn.close();
    		return new ArrayList<Paragraphe>(dicoParag.values());
    	} catch (SQLException e) {
    		throw new DAOException("Erreur BD " + e.getMessage(), e);
    	}
	}
	
	public void setCondition(Paragraphe parag, Paragraphe paraCondition) {
		try (
			     Connection conn = getConn();
						PreparedStatement ps = conn.prepareStatement("UPDATE paragraphe SET conditionparag=? WHERE idHist=? and numParag=?");
			     ) {
					ps.setInt(1, paraCondition.getNumParag());
		            ps.setInt(2, parag.getIdHist());
		            ps.setInt(3, parag.getNumParag());
		            ps.executeQuery();
		            ps.close();
		            conn.close();
		        } catch (SQLException e) {
		            throw new DAOException("Erreur BD " + e.getMessage(), e);
				}
	}
}
