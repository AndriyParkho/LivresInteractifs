package controleur;

import java.io.IOException;
import java.io.PrintWriter;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;

@WebServlet(name = "CheckUser", urlPatterns = {"/checkuser"})
public class CheckUser extends HttpServlet {
	
	
	@Resource(name = "jdbc/projetWeb")
    private DataSource ds;
	
	private boolean isLoginValid(String email, String pwd, HttpServletRequest request) throws SQLException{
        try (Connection conn = ds.getConnection()){
        	/* Un PreparedStatement évite les injections SQL */
            PreparedStatement s = conn.prepareStatement(
                "SELECT idUtil FROM Utilisateur WHERE email = ? AND password = ?"
            );
            s.setString(1, email);
            s.setString(2, pwd);
            ResultSet r = s.executeQuery();
            if (r.next()) {
            	HttpSession session = request.getSession();
                session.setAttribute("idUtil", r.getInt(1));
                return true;
            }
            else {
            	return false;
            }
        }
    }
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /* Récupération des données de formulaire */
        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");
        String pass = request.getParameter("password");
        
        /* Vérification et traitement des données */
        boolean isValidAccount = true;
        try {
        	if (!(email != null && pass != null && isLoginValid(email, pass, request))) {
                isValidAccount = false;
            }
        } catch (SQLException e) {
        	isValidAccount = false;
        }
        
        /* Envoi de la réponse */
        response.setContentType("text/html;charset=UTF-8");
        if (isValidAccount) {
        	response.sendRedirect("homeConnected.html"); 
        }
        else {
        	response.sendRedirect("login2.html");
        }
    }
    
}
