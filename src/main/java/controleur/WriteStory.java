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
import javax.sql.DataSource;

import dao.DAOException;
import dao.HistoireDAO;
import modele.Paragraphe;

/**
 * Le contrôleur pour accéder à l'écriture d'une histoire
 */
@WebServlet(name = "WriteStory", urlPatterns = {"/write_story"})
public class WriteStory extends HttpServlet {

	private Paragraphe currentParag;
	private Paragraphe firstParag;
	
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
        
        List<Paragraphe> choixParagSuite = new ArrayList<Paragraphe>();
        List<Paragraphe> choixParagAEcrire = new ArrayList<Paragraphe>();
        
        try {
        	if(numChoix == null) {
        		currentParag = histoireDAO.getAllHistoireTree(idHist);
        		firstParag = currentParag;
        	}
        	else if(currentParag.getNumParag() != numParagPere) {
        		currentParag = firstParag.findParag(numParagPere).getParagSuiv().get(numChoix);
        	}
        	else {
        		currentParag = currentParag.getParagSuiv().get(numChoix);
        	}
        	for(Paragraphe parag: currentParag.getParagSuiv()) {
        		if(parag.getIdWritter() == null) {
        			choixParagAEcrire.add(parag);
        		} else {
        			choixParagSuite.add(parag);
        		}
        	}
        	request.setAttribute("currentParag", currentParag);
        	request.setAttribute("choixParagAEcrire", choixParagAEcrire);
        	request.setAttribute("choixParagSuite", choixParagSuite);
        	request.getRequestDispatcher("/WEB-INF/writeStory.jsp").forward(request, response);
        } catch (DAOException e) {
            erreurBD(request, response, e);
        }    
    }
}
