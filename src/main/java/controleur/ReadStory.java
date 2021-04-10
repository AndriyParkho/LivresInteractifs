package controleur;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.stream.FileCacheImageInputStream;
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
import modele.Paragraphe;
import modele.Utilisateur;

/**
 * Le contrôleur de la page d'accueil
 */
@WebServlet(name = "ReadStory", urlPatterns = {"/read_story"})
public class ReadStory extends HttpServlet {

	List<Paragraphe> paragsToRead = new ArrayList<Paragraphe>();
	
	
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

        request.setCharacterEncoding("UTF-8");
        int idHist = Integer.parseInt(request.getParameter("idHist"));
        int numParagPere = Integer.parseInt(request.getParameter("numParagPere"));
        Integer numChoix;
        if(request.getParameter("choix") == null) numChoix = null;
        else numChoix = Integer.valueOf(request.getParameter("choix"));
        
        HistoireDAO histoireDAO = new HistoireDAO(ds);
        
        Paragraphe currentParag;
        if(this.paragsToRead.isEmpty()) currentParag = null;
        else currentParag = this.paragsToRead.get(paragsToRead.size()-1);
        List<Paragraphe> choixParag;
        
        HttpSession session = request.getSession();
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        
        try {
        	if(numChoix == null) {
        		currentParag = histoireDAO.getHistoireTree(idHist);
        		this.paragsToRead = new ArrayList<Paragraphe>();
        		while(currentParag.getParagSuiv().size() == 1) {
	        		paragsToRead.add(currentParag);
	        		currentParag = currentParag.getParagSuiv().get(0);
	        	}
        		paragsToRead.add(currentParag);
        	}
        	else if(currentParag.getNumParag() != numParagPere) {
        		// Ajouter l'attribut pour mettre la pop-up
        	}
        	else { 
        		this.paragsToRead = new ArrayList<Paragraphe>();
        		currentParag = currentParag.getParagSuiv().get(numChoix);
	        	while(currentParag.getParagSuiv().size() == 1) {
	        		paragsToRead.add(currentParag);
	        		currentParag = currentParag.getParagSuiv().get(0);
	        	}
	        	paragsToRead.add(currentParag);
        	}
        	choixParag = currentParag.getParagSuiv();
        	request.setAttribute("current", currentParag);
        	request.setAttribute("paragsToRead", paragsToRead);
        	request.setAttribute("choixParag", choixParag);
        	request.getRequestDispatcher("/WEB-INF/readStory.jsp").forward(request, response);
        } catch (DAOException e) {
            erreurBD(request, response, e);
        }
    }
}
