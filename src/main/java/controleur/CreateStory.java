package controleur;

import dao.DAOException;
import dao.HistoireDAO;
import dao.UtilisateurDAO;
import java.io.IOException;
import java.io.PrintWriter;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import modele.Histoire;
import modele.Paragraphe;
import modele.Utilisateur;

@WebServlet(name = "CreateStory", urlPatterns = {"/createStory"})
public class CreateStory extends HttpServlet {
	
	/**
     * 
     * Affiche la page d’accueil avec la liste de toutes les histoires. 
     */
    
    private void actionAfficher(HttpServletRequest request, 
            HttpServletResponse response, 
            HistoireDAO histoireDAO) throws ServletException, IOException {
    	
    	
        List<Histoire> histoires = histoireDAO.getListeHistoires();
        
        request.setAttribute("histoires", histoires);
        
        request.getRequestDispatcher("/WEB-INF/accueil.jsp").forward(request, response);
    }
    
    private void erreurBD(HttpServletRequest request,
            HttpServletResponse response, DAOException e)
        throws ServletException, IOException {
    	e.printStackTrace(); // permet d’avoir le détail de l’erreur dans catalina.out
    	request.setAttribute("erreurMessage", e.getMessage());
    	request.getRequestDispatcher("/WEB-INF/bdErreur.jsp").forward(request, response);
    }
    
    private void createStory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        HistoireDAO histoireDao = new HistoireDAO(ds);
        HttpSession session = request.getSession();
        Utilisateur user = (Utilisateur)session.getAttribute("user");
        int confidentialite = 0;
       
        
        try {
            confidentialite = Integer.parseInt(request.getParameter("confident"));
        } catch (Exception e){
        	
        }
            
        String[] auteurs = request.getParameterValues("auteurs");
        for(String auteur : auteurs){
            System.out.println(auteur);
        }
        String title = request.getParameter("title");
        String titreParagraphe = request.getParameter("titreParagraphe");
        String paragraphe = request.getParameter("story");
        int nb_choix = Integer.parseInt(request.getParameter("nbChoix"));
        Histoire histoire = histoireDao.createNewStoryObjet(title, user.getId());
        Paragraphe paragrahe = new Paragraphe(histoire.getId(), 1, titreParagraphe, paragraphe, true, nb_choix, user.getId());
        if(confidentialite == 1){
            histoire.setAuteurs(auteurs);
        }
        histoire.setFirstParag(paragrahe);
        histoireDao.createStory(histoire, confidentialite);
        
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
        
        
        /* Récupération des données de formulaire */
        request.setCharacterEncoding("UTF-8");
        
        /* Mise à jour de la base de données*/
        HistoireDAO histoireDAO = new HistoireDAO(ds);
        try {
        	createStory(request, response);
        } catch (DAOException e) {
            erreurBD(request, response, e);
        }
   
        
        /* Envoi de la réponse */
        response.setContentType("text/html;charset=UTF-8");
        try {
        	actionAfficher(request, response, histoireDAO);
        } catch (DAOException e) {
            erreurBD(request, response, e);
        }
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
