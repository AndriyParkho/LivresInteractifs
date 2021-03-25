package servlets;

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
        String titre = request.getParameter("title");
        int confidentialité = 0;
        try {
        	confidentialité = Integer.parseInt(request.getParameter("confident"));
        } catch (Exception e){
        	
        }
        String[] auteurs = request.getParameterValues("auteurs");
        String titreParagraphe = request.getParameter("paragraphe");
        String paragraphe = request.getParameter("story");
        
        /* Envoi de la réponse */
        response.setContentType("text/html;charset=UTF-8");
        response.sendRedirect("homeConnected.html"); 
    }
    
}
