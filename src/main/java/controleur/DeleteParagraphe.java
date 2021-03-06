package controleur;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import dao.DAOException;
import dao.HistoireDAO;
import dao.ParagrapheDAO;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import modele.Histoire;
import modele.Paragraphe;
import modele.Utilisateur;

/**
 * Servlet implementation class ParagraphesEcrit
 */
@WebServlet(name = "DeleteParagraphe", urlPatterns = {"/delete_parag"})
public class DeleteParagraphe extends HttpServlet {

    @Resource(name = "jdbc/projetWeb")
    private DataSource ds;
    
    private void actionAfficher(HttpServletRequest request, 
            HttpServletResponse response, 
            HistoireDAO histoireDAO) throws ServletException, IOException {
    	
    	
        List<Histoire> histoires = histoireDAO.getListeHistoiresPublie();
        
        request.setAttribute("histoires", histoires);
        
        request.getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request, response);
    }
    

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
    
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession sess = request.getSession(false);
    	Utilisateur user = (Utilisateur) sess.getAttribute("user");
		if(user == null) {
    		response.sendRedirect("accueil");
    	} else {
    		supprimerParag(request, response);
    	}
	}
	
	private void supprimerParag(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException {
                ParagrapheDAO paragrapheDao = new ParagrapheDAO(ds);
                HistoireDAO histoireDao = new HistoireDAO(ds);
                Paragraphe paragrapheASupprimer = paragrapheDao.getParagraphe(Integer.parseInt(request.getParameter("idhist")), Integer.parseInt(request.getParameter("numparag")));
                if(!paragrapheDao.suppressionParagraphe(paragrapheASupprimer)){
                    request.setAttribute("suppression", false);
                }
                request.setAttribute("suppression", true);
                actionAfficher(request, response, histoireDao);
        }

}
