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
	
	/*
	@Resource(name = "jdbc/interactiveStory")
    private DataSource ds;*/
	
	private boolean isLoginValid(String login, String pwd) throws SQLException{
//        try (Connection conn = ds.getConnection()){
//        	/* Un PreparedStatement évite les injections SQL */
//            PreparedStatement s = conn.prepareStatement(
//                "SELECT login FROM users WHERE login = ? AND password = ?"
//            );
//            s.setString(1, login);
//            s.setString(2, pwd);
//            ResultSet r = s.executeQuery();
//            /* r.next() renvoie vrai si et seulement si la réponse contient au moins 1 ligne */
//            return r.next();
//        }
		return true;
    }
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /* Récupération des données de formulaire */
        request.setCharacterEncoding("UTF-8");
        String login = request.getParameter("login");
        String pass = request.getParameter("password");
        
        /* Vérification et traitement des données */
        boolean isValidAccount = true;
        try {
        	if (login != null && pass != null && isLoginValid(login, pass)) {
                HttpSession session = request.getSession();
                session.setAttribute("utilisateur", login);
                isValidAccount = true;
            }
            else {
                isValidAccount = false;
            }
        } catch (SQLException e) {
        	//TODO
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
