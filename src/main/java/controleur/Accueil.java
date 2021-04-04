package controleur;

import java.io.IOException;
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
import modele.Histoire;

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
        HistoireDAO histoireDAO = new HistoireDAO(ds);

        try {
            if (action == null) {
                actionAfficher(request, response, histoireDAO);
            } else if (action.equals("bouton")){
                actionBouton(request, response);
            } else {
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
    	
    	/* On vérifie si l'utilisateur est connecté*/
    	HttpSession sess = request.getSession(false);       
    	boolean isConnected = false;
        if (sess != null) {
            Integer id = (Integer) sess.getAttribute("idUtil");
            if (id != null) {
            	isConnected = true;
            }
        }
        List<Histoire> histoires = histoireDAO.getListeHistoires();
        
        request.setAttribute("isConnected", isConnected);
        request.setAttribute("histoires", histoires);
        
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
    	case "storyToRead":
    		response.sendRedirect("accueil");
    		break;
    	case "storyToWrite":
            System.err.println("Action non encore implémentée");
    		break;
    	case "createStory":
    		request.getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request, response);
    		break;
    	case "historique":
            System.err.println("Action non encore implémentée");
    		break;
    	case "logout":
    		HttpSession session = request.getSession();
            session.invalidate();
            response.sendRedirect("accueil");
    		break;
    	}
    }
}