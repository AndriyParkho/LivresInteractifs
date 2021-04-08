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
    
    private boolean traiteDonnees(HttpServletRequest request) {
        
        HttpSession session = request.getSession();
        Utilisateur user = (Utilisateur)session.getAttribute("user");
        int confidentialité = 0;
       
        
        try {
            confidentialité = Integer.parseInt(request.getParameter("confident"));
        } catch (Exception e){
        	
        }
            
        String[] auteurs = request.getParameterValues("auteurs");
        String title = request.getParameter("title");
        String titreParagraphe = request.getParameter("titreParagraphe");
        String paragraphe = request.getParameter("story");
        int nb_choix = Integer.parseInt(request.getParameter("nbChoix"));
        
        try(Connection c = ds.getConnection()){
            
            PreparedStatement ps_histoire = c.prepareStatement("INSERT INTO Histoire (idHist, titre, datePubli, idAuteur) VALUES ( ?, ?, NULL, ?)");
            PreparedStatement ps_paragraphe = c.prepareStatement("INSERT INTO Paragraphe (numParag, titre, texte, valide, nbChoix, idWritter, idHist) VALUES (?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement ps_numero_histoire = c.prepareStatement("SELECT idHist_seq.nextval FROM DUAL");
            int numero_histoire = 1;
            
            try{
                ResultSet result_set = ps_numero_histoire.executeQuery();
                while(result_set.next()){
                    numero_histoire = result_set.getInt(1);
                }
            }catch(SQLException sqle){
                System.out.println(sqle.getMessage());
            } 
            ps_histoire.setInt(1, numero_histoire);
            ps_histoire.setString(2, title);
            ps_histoire.setInt(3, 1); //par défaut utilisateur 1, a changer futurement
            
            ps_paragraphe.setInt(1, 1); //on laisse a 1, c'est le premier paragraphe
            ps_paragraphe.setString(2, titreParagraphe);
            ps_paragraphe.setString(3, paragraphe);
            ps_paragraphe.setInt(4, 0); //pas valide au début
            ps_paragraphe.setInt(5, nb_choix);
            ps_paragraphe.setInt(6, user.getId()); 
            ps_paragraphe.setInt(7, numero_histoire);
            
            
            ps_histoire.executeUpdate();
            ps_paragraphe.executeUpdate();
            
            
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
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
