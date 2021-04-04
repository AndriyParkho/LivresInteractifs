package controleur;

import java.io.IOException;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import dao.DAOException;
import dao.UtilisateurDAO;
import modele.Utilisateur;

/**
 * Le contrôleur de la page d'accueil
 */
@WebServlet(name = "LoginRegister", urlPatterns = {"/login", "/register"})
public class LoginRegister extends HttpServlet {

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
    	
    	HttpSession sess = request.getSession(false);
    	if (sess != null) {
            Integer id = (Integer) sess.getAttribute("idUtil");
            if (id != null) {
            	response.sendRedirect("accueil");
            	return ;
            }
        }
    	if(request.getRequestURI().equals("/projetWeb/login")) request.setAttribute("login", true);
    	else request.setAttribute("login", false);
        request.getRequestDispatcher("/WEB-INF/loginRegister.jsp").forward(request, response);
    }

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        UtilisateurDAO userDao = new UtilisateurDAO(ds);
		if(request.getRequestURI().equals("/projetWeb/login")) {
			String email = request.getParameter("email");
	        String pass = request.getParameter("password");
	        try {
                Utilisateur user = userDao.getUser(email, pass);
                if (user != null) {
                	HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    request.setAttribute("isConnected", true);
                    request.getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request, response);
                }
                else {
                	request.setAttribute("error", true);
                	request.setAttribute("login", true);
                	request.getRequestDispatcher("/WEB-INF/loginRegister.jsp").forward(request, response);
                }
	            
	        } catch (SQLException e) {
	        	e.printStackTrace(); // permet d’avoir le détail de l’erreur dans catalina.out
	            request.setAttribute("erreurMessage", e.getMessage());
	            request.getRequestDispatcher("/WEB-INF/bdErreur.jsp").forward(request, response);
	        }
		}
		else {
			String nom = request.getParameter("nom");
	        String prenom = request.getParameter("prenom");
	        String email = request.getParameter("email");
	        String pass = request.getParameter("password");
            boolean isCreated = userDao.createUser(nom, prenom, email, pass);
            if (isCreated) {
            	request.setAttribute("isConnected", false);
                request.getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request, response);
            }
            else {
            	request.setAttribute("error", true);
            	request.setAttribute("login", false);
            	request.getRequestDispatcher("/WEB-INF/loginRegister.jsp").forward(request, response);
            } 
		}
	}

    
}
