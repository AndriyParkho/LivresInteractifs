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
import modele.HistoriqueModele;
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
        /* On récupère les différents paramètres */
        int idHist = Integer.parseInt(request.getParameter("idHist"));
        /* goBackTo est présent que si on vient de l'historique */
        Integer numParagToReset = null;
        if(request.getParameter("goBackTo")!= null) numParagToReset = Integer.parseInt(request.getParameter("goBackTo"));
        /* numParagPere est présent que si on parcourt normalement l'histoire */
        Integer numParagPere = null;
        if(request.getParameter("numParagPere") != null) 
        	numParagPere = Integer.parseInt(request.getParameter("numParagPere"));
        /* numChoix est présent que si on parcourt normalement l'histoire */
        Integer numChoix = null;
        if(request.getParameter("choix") != null) numChoix = Integer.valueOf(request.getParameter("choix"));
        
        HttpSession session = request.getSession();
        HistoriqueModele historique = ((HistoriqueModele) session.getAttribute("historique"));
        HistoireDAO histoireDAO = new HistoireDAO(ds);
        /*On récupère le dernier paragraphe affiché si y en a un à partir de la liste des paragraphes affichées sur la vue */
        Paragraphe currentParag;
        if(this.paragsToRead.isEmpty()) currentParag = null;
        else currentParag = this.paragsToRead.get(paragsToRead.size()-1);
        
        List<Paragraphe> choixParag;
        ArrayList<Paragraphe> listHisto= new ArrayList<Paragraphe>(); /* ??? */
        /*On récupère l'historique de l'histoire si y en a un */ 
		List<ArrayList<Paragraphe>> listePara = historique.getTree(idHist);
        try {
        	/*Si numChoix est null c'est qu'on arrive sur l'histoire de l'accueil et donc cette dernière n'a pas été initialisée */
        	if(numChoix == null && numParagToReset == null) {
        		/*S'il n'y a pas d'historique on récupère tout l'arbre de l'histoire à partir de la bdd */
        		if(listePara == null) {        			
        			currentParag = histoireDAO.getHistoireTreeToRead(idHist);
        			this.paragsToRead = new ArrayList<Paragraphe>();
        			while(currentParag.getParagSuiv().size() == 1) {
        				listHisto.add(currentParag);
        				paragsToRead.add(currentParag);
        				currentParag = currentParag.getParagSuiv().get(0);
        			}
        			listHisto.add(currentParag);
        			historique.addParagraph(idHist, listHisto);
        			paragsToRead.add(currentParag);
        		} else { /*Sinon on récupère le dernier paragraphe de l'historique */
        			currentParag = listePara.get(listePara.size() - 1).get(0);
        			this.paragsToRead = new ArrayList<Paragraphe>();
        			while(currentParag.getParagSuiv().size() == 1) {
        				paragsToRead.add(currentParag);
        				currentParag = currentParag.getParagSuiv().get(0);
        			}
        			paragsToRead.add(currentParag);
        		}
        	} else if(numParagToReset != null) {
        		/*Sinon si on vient de l'historique on récupère le paragraphe demandé à partir de l'historique */
        		currentParag = listePara.get(numParagToReset).get(listePara.get(numParagToReset).size() - 1);
        		/*On supprime dans l'historique tout les paragraphes qui suivent */
        		for(int i = listePara.size() - 1; i > numParagToReset; i--) {
            		listePara.remove(i);
            	}
        		/*Il n'y a pas besoin de compléter l'historique dans ce cas là étant donné qu'on a pas avancé dans l'histoire*/
        		this.paragsToRead = listePara.get(numParagToReset);
        	} else if(currentParag.getNumParag() != numParagPere) {
        		/*Sinon si l'utilisateur a fait un retour navigateur pour changer de choix on l'avertit*/
        		request.setAttribute("warning", true);
        	}
        	else { 
        		/*Sinon on récupère le paragraphe dans l'arbre par le numChoix que l'utilisateur a fait*/
        		this.paragsToRead = new ArrayList<Paragraphe>();
        		currentParag = currentParag.getParagSuiv().get(numChoix);
	        	while(currentParag.getParagSuiv().size() == 1) {
	        		listHisto.add(currentParag);
	        		paragsToRead.add(currentParag);
	        		currentParag = currentParag.getParagSuiv().get(0);
	        	}
	        	listHisto.add(currentParag);
	        	historique.addParagraph(idHist, listHisto);
	        	paragsToRead.add(currentParag);
        	}
        	/*On place les paragraphes suivants comme choix pour l'utilisateur*/
        	choixParag = currentParag.getParagSuiv();
        	request.setAttribute("current", currentParag);
        	request.setAttribute("paragsToRead", paragsToRead);
        	request.setAttribute("choixParag", choixParag);
        	List<ArrayList<Paragraphe>> treeParagraph = historique.getTree(idHist); /* ??? */
        	request.setAttribute("historique", treeParagraph);
        	request.setAttribute("StoryFirst", true);
        	request.getRequestDispatcher("/WEB-INF/readStory.jsp").forward(request, response);
        } catch (DAOException e) {
            erreurBD(request, response, e);
        }
        
    }
}
