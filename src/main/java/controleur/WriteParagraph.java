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
    		HttpSession sess = request.getSession(false);
        	Utilisateur user = (Utilisateur) sess.getAttribute("user");
        	ParagrapheDAO paragraph = new ParagrapheDAO(ds);
        	String titreParag = request.getParameter("titreParag");
        	request.setAttribute("titreParag", titreParag);
        	request.setAttribute("idHist", idHist);
        	request.setAttribute("numParag", numParag);
        	
        	try {
        		List<Paragraphe> choixRedige = paragraph.getParagrapheFromHist(idHist);
        		request.setAttribute("paragrapheRedige", choixRedige);
        		paragraph.setWritter(idHist, numParag, user.getId());
        	} catch (DAOException e) {
                erreurBD(request, response, e);
            }
            request.setCharacterEncoding("UTF-8");
            request.getRequestDispatcher("/WEB-INF/writeParagraph.jsp").forward(request, response);
    	}
    	else if(action.equals("erase")) {
        	ParagrapheDAO paragraph = new ParagrapheDAO(ds);
            try {
            	paragraph.deleteWritter(idHist, numParag);
            } catch (DAOException e) {
            	erreurBD(request, response, e);
            }
            response.sendRedirect("accueil"); 
        } else if(action.equals("save")) {
        	String paragraphe = request.getParameter("story");
        	System.out.println(paragraphe);
        } else {
            invalidParameters(request, response);
        }
    }
    
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String titre = request.getParameter("titre");
        int idHist = Integer.parseInt(request.getParameter("idHist"));
        int numParagActuel = Integer.parseInt(request.getParameter("numParag"));
        String paragraphe = request.getParameter("story");
        int value = Integer.parseInt(request.getParameter("isConclusion"));
        if(value == 0) {
        	int nbChoix = Integer.parseInt(request.getParameter("nbChoix"));
        	ParagrapheDAO paragDao = new ParagrapheDAO(ds);
        	Paragraphe paragActuel = null;
        	try {
        		paragActuel = new Paragraphe(idHist, numParagActuel, titre, paragraphe, nbChoix);
        		paragDao.valideParagraph(paragActuel);
            } catch (DAOException e) {
            	erreurBD(request, response, e);
            }
        	String[] choix = request.getParameterValues("choixRemplis");
        	if(choix != null) {
        		Paragraphe paragChoix;
        		for(String choice : choix) {
        			paragChoix = new Paragraphe(idHist, Integer.parseInt(choice));
        			try {
                		paragDao.setFollowing(paragActuel, paragChoix);
                    } catch (DAOException e) {
                    	erreurBD(request, response, e);
                    }
            	}
        	}
        	String newChoix;
        	int nbParagMax = 1;
        	try {
        		nbParagMax = paragDao.getMaxNbParag(idHist);
            } catch (DAOException e) {
            	erreurBD(request, response, e);
            }
        	Paragraphe parag;
        	for(int i = 1; i <= nbChoix; i++) {
        		newChoix = request.getParameter("choix" + Integer.toString(i));
        		if(newChoix != null) {
        			nbParagMax++;
        			parag = new Paragraphe(idHist, nbParagMax, newChoix);
        			try {
                		paragDao.setParagraphe(parag);
                		paragDao.setFollowing(paragActuel, parag);
                    } catch (DAOException e) {
                    	erreurBD(request, response, e);
                    }
        		}
        	}
        }
        else {
        	ParagrapheDAO paragDao = new ParagrapheDAO(ds);
        	try {
        		Paragraphe paragActuel = new Paragraphe(idHist, numParagActuel, titre, paragraphe, 0);
        		paragDao.valideParagraph(paragActuel);
            } catch (DAOException e) {
            	erreurBD(request, response, e);
            }
        }
    }
    
}