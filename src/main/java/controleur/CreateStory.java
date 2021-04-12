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
import dao.HistoireDAO;
import modele.Histoire;
import modele.Paragraphe;
import modele.Utilisateur;

@WebServlet(name = "CreateStory", urlPatterns = {"/createStory"})
public class CreateStory extends HttpServlet {
	
    @Resource(name = "jdbc/projetWeb")
    private DataSource ds;
    
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
       
        if(request.getParameter("confident") != null) confidentialite = Integer.parseInt(request.getParameter("confident"));
            
        String[] auteurs = request.getParameterValues("auteurs");
        
        String title = request.getParameter("title");
        String titreParagraphe = request.getParameter("titreParagraphe");
        String paragraphe = request.getParameter("story");
        int nb_choix = Integer.parseInt(request.getParameter("nbChoix"));
        Histoire histoire = histoireDao.createNewStoryObjet(title, user.getId());
        Paragraphe paragrahe = new Paragraphe(histoire.getId(), 1, titreParagraphe, paragraphe, true, nb_choix, user.getId());
        
        histoire.setFirstParag(paragrahe);
        histoireDao.createStory(histoire, confidentialite);
        if(confidentialite == 1){
            histoire.setAuteurs(auteurs);
            histoireDao.inviteUsers(histoire);
        }
        
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
   
        response.sendRedirect("accueil");
    }    
    
 
        
    }
