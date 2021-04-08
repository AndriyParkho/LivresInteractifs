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

/**
 * Le contrôleur de la page d'accueil
 */
@WebServlet(name = "ReadStory", urlPatterns = {"/read_story"})
public class ReadStory extends HttpServlet {

	private Paragraphe firstParag;
	private Paragraphe currentParag;  
	
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
        int numParag = Integer.parseInt(request.getParameter("numParag"));
        Integer numChoix;
        if(request.getParameter("choix") == null) numChoix = null;
        else numChoix = Integer.valueOf(request.getParameter("choix"));
        HistoireDAO histoireDAO = new HistoireDAO(ds);
        List<Paragraphe> paragsToRead = new ArrayList<Paragraphe>();
        List<Paragraphe> choixParag;
        try {
        	if(numChoix == null) {
        		currentParag = histoireDAO.getHistoireTree(idHist);
        		firstParag = currentParag;
        	}
        	else if(currentParag.getNumParag() != numParag) currentParag = firstParag.findParag(numParag);
        	else currentParag = currentParag.getParagSuiv().get(numChoix);
        	while(currentParag.getParagSuiv().size() == 1) {
        		paragsToRead.add(currentParag);
        		currentParag = currentParag.getParagSuiv().get(0);
        	}
        	paragsToRead.add(currentParag);
        	choixParag = currentParag.getParagSuiv();
        	request.setAttribute("paragsToRead", paragsToRead);
        	request.setAttribute("choixParag", choixParag);
        	request.getRequestDispatcher("/WEB-INF/readStory.jsp").forward(request, response);
        } catch (DAOException e) {
            erreurBD(request, response, e);
        }
    }
}
