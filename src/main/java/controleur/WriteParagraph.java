package controleur;

import java.io.IOException;

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

import java.sql.PreparedStatement;

import modele.HistoriqueModele;
import modele.Paragraphe;
import modele.Utilisateur;

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
            response.sendRedirect("accueil");  //.getRequestDispatcher("accueil").forward(request, response);
        } else {
            invalidParameters(request, response);
        }
    }
    
}