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
            	Integer nbchoix = rs.getInt("nbchoix");
				if(rs.wasNull()) nbchoix = null;
				Integer conditionParag = rs.getInt("conditionParag");
				if(rs.wasNull()) conditionParag = null;
				Integer idWritter = rs.getInt("idWritter");
				if(rs.wasNull()) idWritter = null;
                result = new Paragraphe(idHist, numParag, rs.getString("titre"), rs.getString("texte"), rs.getBoolean("valide") , nbchoix, idWritter, conditionParag);
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
	public void getBrancheConclusion(HashMap<Integer, Paragraphe> dicoParag, ArrayList<Paragraphe> paragsPere, Paragraphe paragToadd, Connection conn) {
		if(paragsPere.isEmpty() && paragToadd.getNumParag() == 1) {
			return;
		} else {
			for(Paragraphe parag: paragsPere) {
				if(paragToadd != null) parag.addParagSuiv(paragToadd);
				try (
					Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
					) {
					ResultSet rs = st.executeQuery("SELECT numparag, titre, texte, nbchoix, idhist, conditionParag FROM paragraphe JOIN "
								+ "isfollowing ON idhistparag = idhist AND numparagpere = numparag WHERE idhistparag = " + parag.getIdHist() + 
								" AND numparagfils = " + parag.getNumParag()  + "AND texte IS NOT NULL AND valide = 1");
					ArrayList<Paragraphe> newParagsPere = new ArrayList<Paragraphe>();
					while (rs.next()) {
						Integer numParag = rs.getInt("numParag");
						Paragraphe pereParag = dicoParag.get(numParag); /*On vérifie si on est déjà passé par le paragraphe*/ 
						if(pereParag == null) {/*Si ce n'est pas le cas on créer le paragraphe pere*/						
							Integer nbchoix = rs.getInt("nbchoix");
							if(rs.wasNull()) nbchoix = null;
							Integer conditionParag = rs.getInt("conditionParag");
							if(rs.wasNull()) conditionParag = null;
							pereParag = new Paragraphe(rs.getInt("idhist"), numParag, rs.getString("titre"), rs.getString("texte"), nbchoix, conditionParag);
							dicoParag.put(numParag, pereParag); /*On le rajoute à la liste des paragraphes déjà vu*/
							newParagsPere.add(pereParag);
						} else { /*Si c'est le cas on rajoute ce paragraphe pere*/
							pereParag.addParagSuiv(parag);
						}
					}
					getBrancheConclusion(dicoParag, newParagsPere, parag, conn);
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
        } catch (SQLException e) {
            throw new DAOException("Erreur BD " + e.getMessage(), e);
		}
	}
	
	public ArrayList<Paragraphe> getParagConclu(int idHist, HashMap<Integer, Paragraphe> dicoParag, Connection conn){
		try (
    			Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    			) {
    		ResultSet rs = st.executeQuery("SELECT numparag, titre, texte, nbchoix, idhist, conditionParag FROM paragraphe WHERE idhist = " + idHist + 
					" AND nbchoix = 0 AND texte IS NOT NULL AND valide = 1");
    		ArrayList<Paragraphe> paragsConclu = new ArrayList<Paragraphe>();
    		while(rs.next()) {
    			Integer numParag = rs.getInt("numParag");
				if(rs.wasNull()) numParag = null;
				Integer conditionParag = rs.getInt("conditionParag");
				if(rs.wasNull()) conditionParag = null;
    			Paragraphe concluParag = new Paragraphe(idHist, numParag, rs.getString("titre"), rs.getString("texte"), 0, conditionParag);
    			paragsConclu.add(concluParag);
    			dicoParag.put(numParag, concluParag);
    		}
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
				rs = ss.executeQuery("SELECT numParag, locationid FROM hasRead WHERE idUtil=" + Integer.toString(userId) + "AND idHist = " + Integer.toString(idHist) + " ORDER BY locationid");
				Paragraphe currentParagraphe = null;
				while(rs.next()) {
					numParag = rs.getInt("numParag");
					if(numParag == 1) {
						currentParagraphe = getHistoireTreeToRead(idHist);
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
    		ResultSet rs = st.executeQuery("SELECT numparag, titre, texte, nbchoix, idhist, conditionParag FROM paragraphe WHERE idhist = " + idHist + 
					" AND numParag = " + numParag);
    		ArrayList<Paragraphe> parag = new ArrayList<Paragraphe>();
    		HashMap<Integer, Paragraphe> dicoParag = new HashMap<Integer, Paragraphe>();
    		if(rs.next()) {
				Integer conditionParag = rs.getInt("conditionParag");
				if(rs.wasNull()) conditionParag = null;
    			Paragraphe concluParag = new Paragraphe(idHist, numParag, rs.getString("titre"), rs.getString("texte"), 0, conditionParag);
    			parag.add(concluParag);
    			dicoParag.put(numParag, concluParag);
    		}
    		this.getBrancheConclusion(dicoParag, parag, null, conn);
    		dicoParag.remove(numParag);
    		return new ArrayList<Paragraphe>(dicoParag.values());
    	} catch (SQLException e) {
    		throw new DAOException("Erreur BD " + e.getMessage(), e);
    	}
	}
        
        public boolean suppressionParagraphe(Paragraphe paragraphe){
            try(Connection c = getConn()){
                PreparedStatement ps_paraASupp = c.prepareStatement("SELECT P.idHist,P.numParag FROM Paragraphe P WHERE P.idHist = ? AND P.numParag = ? AND (SELECT COUNT(*) FROM Paragraphe P JOIN isFollowing isF ON P.idHist = isF.idHistParag WHERE P.idHist = ? AND isF.NumParagPere = ? AND P.idWritter is NOT NULL)=0");
                ps_paraASupp.setInt(1, paragraphe.getIdHist());
                ps_paraASupp.setInt(2, paragraphe.getNumParag());
                ps_paraASupp.setInt(3, paragraphe.getIdHist());
                ps_paraASupp.setInt(4, paragraphe.getNumParag());
                ResultSet rs = ps_paraASupp.executeQuery();
                System.out.println(rs);
                if(!rs.next()){
                    System.out.println("On ne peut pas supprimer ce paragraphe");
                    return false;
                }
                else{
                    PreparedStatement ps_supp = c.prepareStatement("DELETE FROM Paragraphe P WHERE P.idHist = ? AND P.numParag = ? AND (SELECT COUNT(*) FROM Paragraphe P JOIN isFollowing isF ON P.idHist = isF.idHistParag WHERE P.idHist = ? AND isF.NumParagPere = ? AND P.idWritter is NOT NULL)=0");
                    ps_supp.setInt(1, paragraphe.getIdHist());
                    ps_supp.setInt(2, paragraphe.getNumParag());
                    ps_supp.setInt(3, paragraphe.getIdHist());
                    ps_supp.setInt(4, paragraphe.getNumParag());
                    ps_supp.execute();
                    System.out.println("Paragraphe supprimé");
                    return true;
                }
                
            }catch(SQLException sqle){
            throw new DAOException("Erreur BD " + sqle.getMessage(), sqle);
        }
    }
}
