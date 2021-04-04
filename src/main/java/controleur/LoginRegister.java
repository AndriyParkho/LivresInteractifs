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
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(arg0, arg1);
	}

    
}
