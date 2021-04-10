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
import modele.HistoriqueModele;
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
        HistoireDAO histDAO = new HistoireDAO(ds);
        if(idHist == null) {
		    HttpSession session = request.getSession();
		    HistoriqueModele historique = (HistoriqueModele) session.getAttribute("historique");
		    List<Integer> idStories = historique.getStories();
		    request.setAttribute("histoires", histDAO.getHistoires(idStories));
		    request.getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request, response);
        } else {
        	/*TODO Mettre en place un attribut qui permet de savoir si l'on vient de l'historique + mettre en place
        	les attributs pour que le controleur ReadStory comprennent qu'on veut charger la fin de l'histoire*/
        	request.getRequestDispatcher("/WEB-INF/read_story").forward(request, response);
        }
       
    }
}
        
        