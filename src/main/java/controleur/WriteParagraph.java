package controleur;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import dao.DAOException;
import dao.ParagrapheDAO;
import modele.Utilisateur;
import modele.Paragraphe;
import modele.ParagrapheConditionnel;

/**
 * Le contrôleur pour accéder à l'écriture d'une histoire
 */
@WebServlet(name = "WriteParagraph", urlPatterns = {"/write_paragraph"})
public class WriteParagraph extends HttpServlet { 
	
    @Resource(name = "jdbc/projetWeb")
    private DataSource ds;

    /* pages d’erreurs */
    private void invalidParameters(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/controleurErreur.jsp").forward(request, response);        
    }

    private void erreurBD(HttpServletRequest request,
                HttpServletResponse response, DAOException e)
            throws ServletException, IOException {
        e.printStackTrace(); // permet d’avoir le détail de l’erreur dans catalina.out
        request.setAttribute("erreurMessage", e.getMessage());
        request.getRequestDispatcher("/WEB-INF/bdErreur.jsp").forward(request, response);
    }
  
    /**
     * Actions possibles en GET : afficher (correspond à l’absence du param), getOuvrage.
     */
    public void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
    	int idHist = Integer.parseInt(request.getParameter("idHist"));
    	int numParag = Integer.parseInt(request.getParameter("numParag"));
    	String action = (String) request.getParameter("action");
    	if(action == null) {
    		ParagrapheDAO paragrapheDAO = new ParagrapheDAO(ds);
    		Paragraphe paragToEdit = paragrapheDAO.getParagraphe(idHist, numParag);
    		HttpSession sess = request.getSession(false);
    		Utilisateur user = (Utilisateur) sess.getAttribute("user");
    		
    		if(paragToEdit.getIdWritter() != null && paragToEdit.getIdWritter() != user.getId()) {
    			response.sendRedirect("accueil?button=storyToWrite&warning=paragIndisponible");
    		} else {    			
    			String titreParag = request.getParameter("titreParag");
    			Paragraphe parag = new Paragraphe(idHist, numParag);
    			String texte = paragrapheDAO.getTexte(parag);
    			if(texte != null) {
    				request.setAttribute("texte", texte);
    			}
    			request.setAttribute("titreParag", titreParag);
    			request.setAttribute("idHist", idHist);
    			request.setAttribute("numParag", numParag);
    			
    			try {
    				int numChoix = paragrapheDAO.getNumChoix(parag);
    				List<Paragraphe> choixRedige = paragrapheDAO.getParagrapheFromHist(idHist);
    				List<Paragraphe> choixCondition = paragrapheDAO.getConditionParag(idHist, numParag);
    				List<ParagrapheConditionnel> choixDejaFait = paragrapheDAO.getFollowingParag(parag);
    				System.out.println(numChoix);
    				request.setAttribute("nbChoix", numChoix);
    				request.setAttribute("paragrapheRedige", choixRedige);
    				request.setAttribute("paragrapheCondition", choixCondition);
    				request.setAttribute("ancienChoix", choixDejaFait);
    				paragrapheDAO.setWritter(idHist, numParag, user.getId());
    			} catch (DAOException e) {
    				erreurBD(request, response, e);
    			}
    			request.setCharacterEncoding("UTF-8");
    			request.getRequestDispatcher("/WEB-INF/writeParagraph.jsp").forward(request, response);
    		}
    	}
    	else if(action.equals("erase")) {
        	ParagrapheDAO paragrapheDAO = new ParagrapheDAO(ds);
        	Paragraphe parag = new Paragraphe(idHist, numParag);
            try {
            	List<Paragraphe> paragToDelete = paragrapheDAO.getParagToDelete(parag);
            	for(Paragraphe paraDelete : paragToDelete) {
            		paragrapheDAO.delete(paraDelete);
            	}
            	paragrapheDAO.resetParagWrite(parag);
            } catch (DAOException e) {
            	erreurBD(request, response, e);
            }
            response.sendRedirect("accueil"); 
        } else {
            invalidParameters(request, response);
        }
    }
    
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	int idHist = Integer.parseInt(request.getParameter("idHist"));
    	int numParagActuel = Integer.parseInt(request.getParameter("numParag"));
    	ParagrapheDAO paragDao = new ParagrapheDAO(ds);
    	String action = (String) request.getParameter("action");
    	if(action == null) {
    		String titre = request.getParameter("titre");
            String paragraphe = request.getParameter("story");
            int value = Integer.parseInt(request.getParameter("isConclusion"));
            if(value == 0) {
            	int nbChoix = Integer.parseInt(request.getParameter("nbChoix"));
            	Paragraphe paragActuel = null;
            	try {
            		paragActuel = new Paragraphe(idHist, numParagActuel, titre, paragraphe);
            		if(request.getParameter("save") == null) {
            			paragDao.saveParagraph(paragActuel, 1);
            		}
            		else {
            			paragDao.saveParagraph(paragActuel, 0);
            		}
                } catch (DAOException e) {
                	erreurBD(request, response, e);
                }
            	String newChoixTitle;
            	int oldChoixNum;
            	int numParagCondi;
            	int nbParagMax = 1;
            	Paragraphe parag;
            	Paragraphe paraCondition;
            	try {
                	List<Paragraphe> paragToDelete = paragDao.getParagToDelete(paragActuel);
                	for(Paragraphe paraDelete : paragToDelete) {
                		paragDao.delete(paraDelete);
                	}
                	paragDao.deleteLink(paragActuel);
                	nbParagMax = paragDao.getMaxNbParag(idHist);
                } catch (DAOException e) {
                	erreurBD(request, response, e);
                }
            	for(int i = 1; i <= nbChoix; i++) {
            		newChoixTitle = request.getParameter("choix" + Integer.toString(i));
            		if((request.getParameter("paragrapheCondition" + Integer.toString(i))) != null) {
            			numParagCondi = Integer.parseInt(request.getParameter("paragrapheCondition" + Integer.toString(i)));
            			paraCondition = new Paragraphe(idHist, numParagCondi);
            		} else {
            			paraCondition = null;
            		}
            		if(newChoixTitle != null) {
            			nbParagMax++;
            			parag = new Paragraphe(idHist, nbParagMax, newChoixTitle);
            			try {
                    		paragDao.setParagraphe(parag);
                    		if(paraCondition == null) {
                    			paragDao.setFollowing(paragActuel, parag);
                    		}
                    		else {
                    			paragDao.setFollowing(paragActuel, parag, paraCondition);
                    		}
                        } catch (DAOException e) {
                        	erreurBD(request, response, e);
                        }
            		}
            		else {
            			oldChoixNum = Integer.parseInt(request.getParameter("paragrapheRedige" + Integer.toString(i)));
            			parag = new Paragraphe(idHist, oldChoixNum);
            			try {
            				if(paraCondition == null) {
                    			paragDao.setFollowing(paragActuel, parag);
                    		}
                    		else {
                    			paragDao.setFollowing(paragActuel, parag, paraCondition);
                    		}
                        } catch (DAOException e) {
                        	erreurBD(request, response, e);
                        }
            		}
            	}
            	
            	String choixTitle;
            	int suppression;
            	int nbOldChoice = Integer.parseInt(request.getParameter("nbOldChoix"));
            	for(int i = 1; i <= nbOldChoice; i++) {
        			//Pour chaque ancien choix
        			suppression = Integer.parseInt(request.getParameter("supressOldChoix"+i));
//        			Si on ne supprime pas le choix, on doit donc le rajouter en bdd
        			if(suppression == 0) {
        				nbChoix++;
        				choixTitle = request.getParameter("oldChoix" + Integer.toString(i));
                		if((request.getParameter("oldParagrapheCondition" + Integer.toString(i))) != null) {
                			numParagCondi = Integer.parseInt(request.getParameter("oldParagrapheCondition" + Integer.toString(i)));
                			paraCondition = new Paragraphe(idHist, numParagCondi);
                		} else {
                			paraCondition = null;
                		}
                		if(choixTitle != null) {
                			nbParagMax++;
                			parag = new Paragraphe(idHist, nbParagMax, choixTitle);
                			try {
                        		paragDao.setParagraphe(parag);
                        		if(paraCondition == null) {
                        			paragDao.setFollowing(paragActuel, parag);
                        		}
                        		else {
                        			paragDao.setFollowing(paragActuel, parag, paraCondition);
                        		}
                            } catch (DAOException e) {
                            	erreurBD(request, response, e);
                            }
                		}
                		else {
                			oldChoixNum = Integer.parseInt(request.getParameter("oldParagrapheRedige" + Integer.toString(i)));
                			parag = new Paragraphe(idHist, oldChoixNum);
                			try {
                				if(paraCondition == null) {
                        			paragDao.setFollowing(paragActuel, parag);
                        		}
                        		else {
                        			paragDao.setFollowing(paragActuel, parag, paraCondition);
                        		}
                            } catch (DAOException e) {
                            	erreurBD(request, response, e);
                            }
                		}
        			}
        		}
            	
            	try {
            			paragDao.setNbChoix(paragActuel, nbChoix);
                } catch (DAOException e) {
                	erreurBD(request, response, e);
                }
            	
            }
            else {
            	Paragraphe paragActuel = new Paragraphe(idHist, numParagActuel, titre, paragraphe, 0);
            	try {
            		List<Paragraphe> paragToDelete = paragDao.getParagToDelete(paragActuel);
                	for(Paragraphe paraDelete : paragToDelete) {
                		paragDao.delete(paraDelete);
                	}
                	paragDao.deleteLink(paragActuel);
            		if(request.getParameter("save") == null) {
            			paragDao.saveParagraph(paragActuel, 1);
            		}
            		else {
            			paragDao.saveParagraph(paragActuel, 0);
            		}
                } catch (DAOException e) {
                	erreurBD(request, response, e);
                }
            }
            response.sendRedirect("accueil");
    	}else if(action.equals("modify")) {
    		int nbOldChoice = Integer.parseInt(request.getParameter("nbOldChoix"));
    		int suppression;
    		int numParag;
    		int nbParagMax = 1;
    		int numParagCondi;
    		int oldChoixNum;
    		Paragraphe paraCondition;
    		String choixTitle;
    		Paragraphe parag;
        	try {
        		nbParagMax = paragDao.getMaxNbParag(idHist);
            } catch (DAOException e) {
            	erreurBD(request, response, e);
            }
    		Paragraphe choice;
    		Paragraphe paragActuel = new Paragraphe(idHist, numParagActuel);
    		//On reset la bdd
 
    		response.sendRedirect("accueil");
    	}else {
            invalidParameters(request, response);
        }
		 
    }
    
}