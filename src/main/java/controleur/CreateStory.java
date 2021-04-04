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

@WebServlet(name = "CreateStory", urlPatterns = {"/createStory"})
public class CreateStory extends HttpServlet {
	
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
        
        
        /* Récupération des données de formulaire */
        request.setCharacterEncoding("UTF-8");
        
        /* Mise à jour de la base de données*/
        
        traiteDonnees(request);
        
        /* Envoi de la réponse */
        response.setContentType("text/html;charset=UTF-8");
        response.sendRedirect("homeConnected.html"); 
    }
    
    @Resource(name = "jdbc/projetWeb")
    private DataSource ds;
    
    private boolean traiteDonnees(HttpServletRequest request) {
        
        String titre = request.getParameter("title");
            
        int confidentialité = 0;
       
        
        try {
            confidentialité = Integer.parseInt(request.getParameter("confident"));
        } catch (Exception e){
        	
        }
            
        String[] auteurs = request.getParameterValues("auteurs");
        String titreParagraphe = request.getParameter("paragraphe");
        String paragraphe = request.getParameter("story");
        int nb_choix = Integer.parseInt(request.getParameter("nbChoix"));
        
        try(Connection c = ds.getConnection()){
            
            PreparedStatement ps_histoire = c.prepareStatement("INSERT INTO Histoire (idHist, titre, datePubli, idAuteur) VALUES ( ?, ?, NULL, ?)");
            PreparedStatement ps_paragraphe = c.prepareStatement("INSERT INTO Paragraphe (numParag, titre, texte, valide, nbChoix, idWritter, idHist) VALUES (?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement ps_numero_histoire = c.prepareStatement("SELECT idHist_seq.nextval FROM DUAL");
            int numero_histoire = 1;
            
            try{
                numero_histoire = ps_numero_histoire.getResultSet().getInt(1) + 1;
            }catch(SQLException sqle){
                System.out.println(sqle.getMessage());
            } 
            ps_histoire.setInt(1, numero_histoire);
            ps_histoire.setString(2, "Titre");
            ps_histoire.setInt(3, 1); //par défaut utilisateur 1, a changer futurement
            
            ps_paragraphe.setInt(1, 1);
            ps_paragraphe.setString(2, titreParagraphe);
            ps_paragraphe.setString(3, paragraphe);
            ps_paragraphe.setInt(4, 0);
            ps_paragraphe.setInt(5, nb_choix);
            ps_paragraphe.setInt(6, 1); //par défaut je mets 1, avant qu'il y ait plus d'utilisateur
            System.out.println("Numero d'histoire : "+numero_histoire);
            ps_paragraphe.setInt(7, numero_histoire);
            
            
            ps_histoire.executeUpdate();
            ps_paragraphe.executeUpdate();
            
            
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
            System.out.println("Total");
            return false;
        }
        
        return true;
    }
    
    /*
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
            HttpSession session = request.getSession();
            if(session == null){
                response.sendRedirect("index.html");
            }
    }
    */
    
}
