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
import modele.ParagrapheConditionnel;

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
                result = new Paragraphe(idHist, numParag, rs.getString("titre"), rs.getString("texte"), rs.getBoolean("valide") , nbchoix, idWritter, rs.getInt("idModifier"));
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
	
	public Paragraphe getParagEnCours(int idUtil) {
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
			    PreparedStatement ps = conn.prepareStatement("UPDATE paragraphe SET idWritter=?, idModifier =? WHERE idHist=? and numParag=?");
			) {
				ps.setInt(1, userId);
				ps.setInt(2, userId);
				ps.setInt(3, histId);
				ps.setInt(4, paragraphNum);
			 	ps.executeQuery();
			 	ps.close();
			 	conn.close();
		      }catch (SQLException e) {
		    	throw new DAOException("Erreur BD " + e.getMessage(), e);
		      }
		return true;
		
	}
	
	public int getWritter(Paragraphe parag) {
		int id = 0;
		try (
				Connection conn = getConn();
			    PreparedStatement ps = conn.prepareStatement("SELECT idWritter FROM paragraphe WHERE idHist=? AND numParag=?");
			) {
				ps.setInt(1, parag.getIdHist());
				ps.setInt(2, parag.getNumParag());
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					id = rs.getInt("idWritter");
				}
			 	ps.close();
			 	conn.close();
		      }catch (SQLException e) {
		    	throw new DAOException("Erreur BD " + e.getMessage(), e);
		      }
		return id;
		
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
	
	public void resetParagWrite(Paragraphe parag) {
		deleteLink(parag);
		try (
				Connection conn = getConn();
			) {
				Statement st = conn.createStatement();
				st.executeQuery("UPDATE paragraphe SET idWritter=NULL, idModifier=NULL, texte=NULL, nbChoix=0 WHERE idHist=" + parag.getIdHist() +" AND numParag=" + parag.getNumParag());
				
			 	conn.close();
		      }catch (SQLException e) {
		    	throw new DAOException("Erreur BD " + e.getMessage(), e);
		      }
	}
	
	public ArrayList<Paragraphe> getParagrapheFromHist(int histId){
		ArrayList<Paragraphe> paragrapheRedige = new ArrayList<Paragraphe>();
		try (
				Connection conn = getConn();
			    PreparedStatement ps = conn.prepareStatement("SELECT numParag, titre, texte, nbChoix FROM paragraphe WHERE idHist = ? AND valide = 1");
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
	
	public void saveParagraph(Paragraphe parag, int valide) {
		if(valide == 1) {
			try (
				     Connection conn = getConn();
							PreparedStatement ps = conn.prepareStatement("UPDATE paragraphe SET valide=1, idModifier=NULL, texte=? WHERE idHist=? and numParag=?");
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
		} else {
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
	
	public void setFollowing(Paragraphe parag1, Paragraphe parag2, Paragraphe parag3) {
		try (
			     Connection conn = getConn();
						PreparedStatement ps = conn.prepareStatement("INSERT INTO isFollowing (idHistParag, numParagPere, numParagFils, conditionParag) VALUES (?, ?, ?, ?)");
			     ) {
					ps.setInt(1, parag1.getIdHist());
		            ps.setInt(2, parag1.getNumParag());
		            ps.setInt(3, parag2.getNumParag());
		            ps.setInt(4, parag3.getNumParag());
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
        
        public boolean suppressionParagraphe(Paragraphe paragraphe){
            try(Connection c = getConn()){
                PreparedStatement ps_paraASupp = c.prepareStatement("SELECT P.idHist, P.numParag FROM Paragraphe P WHERE P.idHist = ? AND P.numParag = ? AND (SELECT COUNT(*) FROM isFollowing isF JOIN Paragraphe P ON P.idHist = isF.idHistParag AND isF.numParagPere = ? WHERE P.idHist = ? and P.numParag <> ? AND P.idWritter IS NOT NULL) = 0");
                ps_paraASupp.setInt(1, paragraphe.getIdHist());
                ps_paraASupp.setInt(2, paragraphe.getNumParag());
                ps_paraASupp.setInt(3, paragraphe.getNumParag());
                ps_paraASupp.setInt(4, paragraphe.getIdHist());
                ps_paraASupp.setInt(5, paragraphe.getNumParag());
                
                ResultSet rs = ps_paraASupp.executeQuery();
                if(!rs.next()){
                    return false;
                }
                else{
                    PreparedStatement ps_supp = c.prepareStatement("DELETE FROM Paragraphe P WHERE P.idHist = ? AND P.numParag = ? ");
                    ps_supp.setInt(1, paragraphe.getIdHist());
                    ps_supp.setInt(2, paragraphe.getNumParag());
                    ArrayList<Paragraphe> paragraphesFils = getFils(paragraphe);
                    for(Paragraphe paragrapheFils : paragraphesFils){
                        suppressionParagraphe(paragrapheFils);
                    }
                    ps_supp.execute();
                    c.close();
                    return true;
                }
                
            }catch(SQLException sqle){
            throw new DAOException("Erreur BD " + sqle.getMessage(), sqle);
        }
    }
	
	public ArrayList<Paragraphe> getParagToDelete(Paragraphe parag){
		ArrayList<Paragraphe> paragToDelete = new ArrayList<Paragraphe>();
		try (
			     Connection conn = getConn();
						PreparedStatement ps = conn.prepareStatement("SELECT numParag FROM paragraphe JOIN isFollowing ON paragraphe.numParag=isFollowing.numParagFils WHERE paragraphe.valide=0 AND isFollowing.numParagPere = ? AND paragraphe.idHist = ?");
			     ) {
		            ps.setInt(1, parag.getNumParag());
		            ps.setInt(2, parag.getIdHist());
		            ResultSet rs = ps.executeQuery();
		            int numParag;
		            Paragraphe paraDelete = null;
					while(rs.next()) {
						numParag = rs.getInt("numParag");
						paraDelete = new Paragraphe(parag.getIdHist(), numParag);
						paragToDelete.add(paraDelete);
					}
					ps.close();
					conn.close();
		        } catch (SQLException e) {
		            throw new DAOException("Erreur BD " + e.getMessage(), e);
				}
		return paragToDelete;
		
	}
	
	public void delete(Paragraphe parag) {
		try (
			     Connection conn = getConn();
						PreparedStatement ps = conn.prepareStatement("DELETE FROM paragraphe WHERE idHist = ? AND numParag = ? ");
			     ) {
		            ps.setInt(1, parag.getIdHist());
		            ps.setInt(2, parag.getNumParag());
		            ps.executeQuery();
					ps.close();
					conn.close();
		        } catch (SQLException e) {
		            throw new DAOException("Erreur BD " + e.getMessage(), e);
				}
	}
	
	public ArrayList<ParagrapheConditionnel> getFollowingParag(Paragraphe parag){
		ArrayList<ParagrapheConditionnel> paragFollowing = new ArrayList<ParagrapheConditionnel>();
		try (
			     Connection conn = getConn();
						PreparedStatement ps = conn.prepareStatement("SELECT numParag, texte, titre, valide FROM paragraphe JOIN isFollowing ON (paragraphe.numParag=isFollowing.numParagFils AND paragraphe.idHist = isFollowing.idHistParag) WHERE isFollowing.numParagPere = ? AND paragraphe.idHist = ?");
			     ) {
		            ps.setInt(1, parag.getNumParag());
		            ps.setInt(2, parag.getIdHist());
		            ResultSet rs = ps.executeQuery();
		            int numParag;
		            String texte; 
		            String titre; 
		            int valide;
		            ParagrapheConditionnel paraFollow = null;
		            Paragraphe condition;
					while(rs.next()) {
						numParag = rs.getInt("numParag");
						texte = rs.getString("texte");
						titre = rs.getString("titre");
						valide = rs.getInt("valide");
						condition = getCondition(parag.getIdHist(), parag.getNumParag(), numParag);
						if(condition != null) {
							paraFollow = new ParagrapheConditionnel(parag.getIdHist(), numParag, titre, valide, condition);
						}else {
							paraFollow = new ParagrapheConditionnel(parag.getIdHist(), numParag, titre, valide);
						}
						paragFollowing.add(paraFollow);
					}
					ps.close();
					conn.close();
		        } catch (SQLException e) {
		            throw new DAOException("Erreur BD " + e.getMessage(), e);
				}
		return paragFollowing;
		
	}
	
	public void deleteChoice(Paragraphe para, Paragraphe paragPere) {
		try (
			     Connection conn = getConn();
			     ) {
					Statement st = conn.createStatement();
					st.executeQuery("DELETE FROM paragraphe WHERE idHist = " + para.getIdHist() + " AND numParag = " + para.getNumParag() + " AND valide=0");
		            Statement s = conn.createStatement();
					s.executeQuery("DELETE FROM isFollowing WHERE idHistParag = " + para.getIdHist() + " AND numParagFils = " + para.getNumParag() + " AND numParagPere = " + paragPere.getNumParag());
		            conn.close();
		        } catch (SQLException e) {
		            throw new DAOException("Erreur BD " + e.getMessage(), e);
				}
	}
	
	public void deleteLink(Paragraphe parag) {
		try (
				Connection conn = getConn();
			) {
				Statement s = conn.createStatement();
				s.executeQuery("DELETE FROM isFollowing WHERE idHistParag = " + parag.getIdHist() + "AND numParagPere=" + parag.getNumParag());
			 	conn.close();
		      }catch (SQLException e) {
		    	throw new DAOException("Erreur BD " + e.getMessage(), e);
		      }
	}
	
	public Paragraphe getCondition(int idHist, int numParag, int numParagFils) {
		Paragraphe condition;
		try (
			     Connection conn = getConn();
						PreparedStatement ps = conn.prepareStatement("SELECT conditionParag FROM isFollowing WHERE idHistParag = ? AND numParagPere = ? AND numParagFils = ?");
			     ) {
		            ps.setInt(1, idHist);
		            ps.setInt(2, numParag);
		            ps.setInt(3, numParagFils);
		            ResultSet rs = ps.executeQuery();
		            if(rs.next() && (rs.getInt("conditionParag") != 0)) {
		            	int numConditionParag = rs.getInt("conditionParag");
		            	condition = getParagraphe(idHist, numConditionParag);
		            }
		            else {
		            	condition = null;
		            }
					ps.close();
					conn.close();
		        } catch (SQLException e) {
		            throw new DAOException("Erreur BD " + e.getMessage(), e);
				}
		return condition;
	}
	
	public void setNbChoix(Paragraphe parag, int nbChoix) {
		try (
			     Connection conn = getConn();
						PreparedStatement ps = conn.prepareStatement("UPDATE paragraphe SET nbChoix=? WHERE idHist=? and numParag=?");
			     ) {
		            ps.setInt(1, nbChoix);
		            ps.setInt(2, parag.getIdHist());
		            ps.setInt(3, parag.getNumParag());
		            ps.executeQuery();
					ps.close();
					conn.close();
		        } catch (SQLException e) {
		            throw new DAOException("Erreur BD " + e.getMessage(), e);
				}
	}
	
	public int getNumChoix(Paragraphe parag) {
		int nbChoix = 0;
		try (
			     Connection conn = getConn();
						PreparedStatement ps = conn.prepareStatement("SELECT nbChoix FROM paragraphe WHERE idHist=? and numParag=?");
			     ) {
		            ps.setInt(1, parag.getIdHist());
		            ps.setInt(2, parag.getNumParag());
		            ResultSet rs = ps.executeQuery();
		            if(rs.next()) {
		            	nbChoix = rs.getInt("nbChoix");
		            }
					ps.close();
					conn.close();
		        } catch (SQLException e) {
		            throw new DAOException("Erreur BD " + e.getMessage(), e);
				}
		return nbChoix;
	}
        
        private ArrayList<Paragraphe> getFils(Paragraphe paragraphe){
            ArrayList<Paragraphe> paragraphesSuivants = new ArrayList<Paragraphe>();
            ParagrapheDAO paragrapheDao = new ParagrapheDAO(dataSource);
            try(Connection c = getConn()){
                PreparedStatement ps_getFils = c.prepareStatement("SELECT isFollowing.numParagFils FROM isFollowing JOIN Paragraphe P ON P.numParag=isFollowing.numParagPere AND P.idHist = isFollowing.idHistParag WHERE isFollowing.numParagPere = "+paragraphe.getNumParag()+"AND isFollowing.idHistParag = "+paragraphe.getIdHist());
                ResultSet rs = ps_getFils.executeQuery();                
                while(rs.next()){
                    int numParagFils = rs.getInt("numParagFils");
                    paragraphesSuivants.add(paragrapheDao.getParagraphe(paragraphe.getIdHist(), numParagFils));
                }
                c.close();
                return paragraphesSuivants;
            }catch(SQLException sqle){
                throw new DAOException("Erreur BD : "+sqle.getMessage(), sqle);
            }
        }
        
        public void setModifier(Paragraphe parag, int userId) {
        	try (
        			Connection conn = getConn();
        		    PreparedStatement ps = conn.prepareStatement("UPDATE paragraphe SET idModifier =? WHERE idHist=? and numParag=?");
        		) {
        			ps.setInt(1, userId);
        			ps.setInt(2, parag.getIdHist());
        			ps.setInt(3, parag.getNumParag());
        		 	ps.executeQuery();
        		 	ps.close();
        		 	conn.close();
        	      }catch (SQLException e) {
        	    	throw new DAOException("Erreur BD " + e.getMessage(), e);
        	      }
        }
        
        public void deleteModifier(Paragraphe parag) {
        	try (
        			Connection conn = getConn();
        		    PreparedStatement ps = conn.prepareStatement("UPDATE paragraphe SET idModifier = NULL WHERE idHist=? and numParag=?");
        		) {
        			ps.setInt(1, parag.getIdHist());
        			ps.setInt(2, parag.getNumParag());
        		 	ps.executeQuery();
        		 	ps.close();
        		 	conn.close();
        	      }catch (SQLException e) {
        	    	throw new DAOException("Erreur BD " + e.getMessage(), e);
        	      }
        }
        
        public void modifParag(Paragraphe parag) {
        	try (
				     Connection conn = getConn();
							PreparedStatement ps = conn.prepareStatement("UPDATE paragraphe SET idModifier=NULL, texte=? WHERE idHist=? and numParag=?");
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
        
}

