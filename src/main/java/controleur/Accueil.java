package controleur;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import dao.HistoireDAO;
import dao.ParagrapheDAO;
import dao.UtilisateurDAO;
import modele.Histoire;
import modele.HistoriqueModele;
import modele.Paragraphe;
import modele.Utilisateur;

/**
 * Le contrôleur de la page d'accueil
 */
@WebServlet(name = "Accueil", urlPatterns = {"/accueil"})
public class Accueil extends HttpServlet { 
	
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
     * Actions possibles en GET : afficher (correspond à l’absence du param), getHistoire.
     */
    public void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String warning = request.getParameter("warning");
        if(warning != null) request.setAttribute("warning", warning);
        HistoireDAO histoireDAO = new HistoireDAO(ds);
        UtilisateurDAO userDAO = new UtilisateurDAO(ds);
        HttpSession session = request.getSession();
        if(session.getAttribute("historique") == null) {
        	HistoriqueModele historique = new HistoriqueModele();
        	session.setAttribute("historique", historique);
        }
        try {
            if (action == null) {
                actionAfficher(request, response, histoireDAO);
            } else if (action.equals("bouton")){
                actionBouton(request, response);
            } else if (action.equals("save")){
            	try {
            		HistoriqueModele historique = (HistoriqueModele) session.getAttribute("historique");
            		userDAO.saveHistorique(((Utilisateur) session.getAttribute("user")).getId(), historique);
            		historique.setModified(false);
                	actionAfficher(request, response, histoireDAO);
            	} catch (DAOException e) {
            		erreurBD(request, response, e);
            	}
            }
            else {
                invalidParameters(request, response);
            }
        } catch (DAOException e) {
            erreurBD(request, response, e);
        }
    }

    /**
     * 
     * Affiche la page d’accueil avec la liste de toutes les histoires. 
     */
    
    private void actionAfficher(HttpServletRequest request, 
            HttpServletResponse response, 
            HistoireDAO histoireDAO) throws ServletException, IOException {
    	
    	
        List<Histoire> histoires = histoireDAO.getListeHistoiresPublie();
        
        request.setAttribute("histoires", histoires);
        
        request.getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request, response);
    }
    
    /**
     * 
     * Fonction qui renvoie la liste des histoires qu'il est possible d'écrire pour l'utilisateur
     */
    private void actionAfficherHistoireAEcrire(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException {
    	
    	HistoireDAO histoireDAO = new HistoireDAO(ds);
    	ParagrapheDAO paragrapheDAO = new ParagrapheDAO(ds);
    	
    	HttpSession sess = request.getSession(false);
    	Utilisateur user = (Utilisateur) sess.getAttribute("user");
        Paragraphe paragraphe =  paragrapheDAO.getParagEnCours(user.getId());
    	
        List<Histoire> histoires = histoireDAO.getListeHistoiresAEcrire(user.getId());
        
        request.setAttribute("histoires", histoires);
        request.setAttribute("paragEnCours", paragraphe);
        
        request.getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request, response);
    }
    
    /**
     * 
     * Fonction qui renvoie la liste des utilisateurs
     */
    private void actionAfficherListeUtilisateur(HttpServletRequest request, 
    		HttpServletResponse response) throws ServletException, IOException {
    	
    	UtilisateurDAO userDAO = new UtilisateurDAO(ds);
    	ParagrapheDAO paragrapheDAO = new ParagrapheDAO(ds);
    	
    	HttpSession sess = request.getSession(false);
    	Utilisateur user = (Utilisateur) sess.getAttribute("user");
    	Paragraphe paragraphe =  paragrapheDAO.getParagEnCours(user.getId());
    	
    	List<Utilisateur> users = null;
    	try {
    		users = userDAO.getUserExceptMe(user.getId());
    	} catch (DAOException e) {
    		erreurBD(request, response, e);
    	}
    	
    	request.setAttribute("paragEnCours", paragraphe);
    	request.setAttribute("user", users);
    	request.getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request, response);
    }
    
    
    /**
     * Fonction pour afficher les histoires déjà lues
     */
    private void actionHistorique(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException {
    	String idHist = request.getParameter("idHist");
        HistoireDAO histDAO = new HistoireDAO(ds);
	    HttpSession session = request.getSession();
	    HistoriqueModele historique = (HistoriqueModele) session.getAttribute("historique");
	    List<Integer> idStories = historique.getStories();
	    request.setAttribute("histoires", histDAO.getHistoires(idStories));
	    request.setAttribute("isModified", historique.isModified());
	    request.getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request, response);
    }
    
    private void actionAfficherListePublication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	HttpSession sess = request.getSession(false);
    	Utilisateur user = (Utilisateur) sess.getAttribute("user");
    	
    	HistoireDAO histoireDAO = new HistoireDAO(ds);
    	
    	List<Histoire> histoiresAPublier = histoireDAO.getHistoiresAPublier(user.getId());
    	
    	request.setAttribute("histoiresAPublier", histoiresAPublier);
    	
    	request.getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request, response);
    }
    
    
    private void actionAfficherListeParagraphes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        HttpSession sess = request.getSession(false);
    	Utilisateur user = (Utilisateur) sess.getAttribute("user");
    	ParagrapheDAO paragrapheDAO = new ParagrapheDAO(ds);
    	
        HashMap<String, ArrayList<Paragraphe>> paragrapheRedige = paragrapheDAO.setParagrapheRedige(user.getId());
        request.setAttribute("paragrapheRedige", paragrapheRedige);
        
        request.getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request, response);
    }
    
    private void actionAfficherListeDepubliable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	HttpSession sess = request.getSession(false);
    	Utilisateur user = (Utilisateur) sess.getAttribute("user");
    	
        HistoireDAO histoireDAO = new HistoireDAO(ds);
        
        List<Histoire> histoiresDepubliables = histoireDAO.getHistoiresDepubliables(user.getId());
        request.setAttribute("histoiresDepubliables", histoiresDepubliables);
        
        request.getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request, response);
    }
    
    /**
     * 
     */
    private void actionBouton(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException {
    	String bouton = request.getParameter("bouton");
    	
    	switch(bouton) {
    	case "login":
    		response.sendRedirect("login");
    		break;
    	case "register":
    		response.sendRedirect("register");
    		break;
    	case "storyToWrite":
    		actionAfficherHistoireAEcrire(request, response);
    		break;
    	case "createStory":
    		actionAfficherListeUtilisateur(request, response);
    		break;
    	case "paragEcrit":
    		actionAfficherListeParagraphes(request, response);
    		break;
    	case "historique":
    		actionHistorique(request, response);
    		break;
        case "histoireAPublier":
                actionAfficherListePublication(request, response);
                break;
        case "histoireDepubliable":
        		actionAfficherListeDepubliable(request, response);
        		break;
    	case "logout":
    		HttpSession session = request.getSession();
            session.invalidate();
            response.sendRedirect("accueil");
    		break;
    	}
    }
}
