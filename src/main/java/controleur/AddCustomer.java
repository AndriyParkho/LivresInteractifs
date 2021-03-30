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
    
    
    @Resource(name = "jdbc/projetWeb")
    private DataSource ds;
    
    private boolean traiteDonnees(HttpServletRequest request) {

    	String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String email = request.getParameter("email");
        String pass = request.getParameter("password");
        
 /* Vérification des données */
 
        
        /* Mise à jour de la base de données */
        
        try (Connection c = ds.getConnection()) {
            PreparedStatement ps =
                c.prepareStatement("INSERT INTO Utilisateur (nom, prenom, email, password) VALUES (?, ?, ?, ?)");
            ps.setString(1, nom);
            ps.setString(2, prenom);
            ps.setString(3, email);
            ps.setString(4, pass);
            ps.executeUpdate();
        } catch (SQLException sqle) {
        	System.out.println(sqle.getMessage());
            return false;
        }
        return true;
        
    }
}
