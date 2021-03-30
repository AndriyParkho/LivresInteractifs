package controleur;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet(name = "AddCustomer", urlPatterns = {"/add_customer"})
public class AddCustomer extends HttpServlet {

	
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /* Récupération des données de formulaire */
        request.setCharacterEncoding("UTF-8");
        boolean requeteValide = traiteDonnees(request);
        /* Envoi de la réponse */
        response.setContentType("text/html;charset=UTF-8");
        envoieReponse(requeteValide, response);  
    }
    
    private void envoieReponse(boolean requeteValide, HttpServletResponse response)
            throws IOException {
        if (requeteValide) {
        	response.sendRedirect("index.jsp"); 
        }
        else {
        	response.sendRedirect("register2.html");
        }
    }
    
    //TODO
    /*@Resource(name = "jdbc/interactiveStory")
    private DataSource ds;*/
    
    private boolean traiteDonnees(HttpServletRequest request) {

    	String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String login = request.getParameter("login");
        String pass = request.getParameter("password");
        
 /* Vérification des données */
 
        
        /* Mise à jour de la base de données */
        //TODO
        /*
        try (Connection c = ds.getConnection()) {
            PreparedStatement ps =
                c.prepareStatement("INSERT INTO Users VALUES (?,?,?)");
            ps.setString(1, login);
            ps.setString(2, pass);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            //TODO
        }*/
        return true;
        
    }
}
