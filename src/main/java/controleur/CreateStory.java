package controleur;

import dao.HistoireDAO;
import dao.UtilisateurDAO;
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
import modele.Histoire;
import modele.Utilisateur;

@WebServlet(name = "CreateStory", urlPatterns = {"/createStory"})
public class CreateStory extends HttpServlet {
	
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
        
        
        /* Récupération des données de formulaire */
        request.setCharacterEncoding("UTF-8");
        
        /* Mise à jour de la base de données*/
        HistoireDAO histoireDAO = new HistoireDAO(ds);
        histoireDAO.createStory(request);
        //traiteDonnees(request);
        
        /* Envoi de la réponse */
        response.setContentType("text/html;charset=UTF-8");
        request.getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request, response);
    }
    
    @Resource(name = "jdbc/projetWeb")
    private DataSource ds;
    
    
    
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
