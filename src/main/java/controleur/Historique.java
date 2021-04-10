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
import dao.HistoireDAO;
import dao.UtilisateurDAO;
import modele.Histoire;
import modele.Paragraphe;
import modele.Utilisateur;

/**
 * Le contrôleur de l'historique
 */
@WebServlet(name = "Historique", urlPatterns = {"/historique"})
public class Historique extends HttpServlet { 
	
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
        String idHist = request.getParameter("idHist");
        if(idHist == null) {
		    HttpSession session = request.getSession();
		    Utilisateur user = (Utilisateur) session.getAttribute("user");
		    if(user != null) {
		    	UtilisateurDAO userDAO = new UtilisateurDAO(ds);
		        try {
		        	List<Histoire> storyRead = userDAO.getStoryRead(user.getId());
		        	request.setAttribute("histoires", storyRead);
		        	request.getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request, response);
		        } catch (DAOException e) {
		        	erreurBD(request, response, e);
		        }
		    }
        } else {
        	//TODO charger l'arbre
        }
       
    }
}
        
        