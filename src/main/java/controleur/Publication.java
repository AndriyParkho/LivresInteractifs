/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import dao.DAOException;
import dao.HistoireDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import modele.Histoire;

/**
 *
 * @author louisjr
 */
@WebServlet(name = "Publication", urlPatterns = {"/publication"})
public class Publication extends HttpServlet {

    
      private void actionAfficher(HttpServletRequest request, 
            HttpServletResponse response, 
            HistoireDAO histoireDAO) throws ServletException, IOException {
    	
    	
        List<Histoire> histoires = histoireDAO.getListeHistoiresPublie();
        
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
      
      private void publicationStories(HttpServletRequest request, HttpServletResponse reponse){
          HistoireDAO histoireDAO = new HistoireDAO(ds);
          HttpSession session = request.getSession();
          Map<String, String[]> parameters = request.getParameterMap();
          
          for(Map.Entry<String, String[]> entry : parameters.entrySet()){
              Histoire histoire = histoireDAO.getHistoire(Integer.parseInt(entry.getKey()));
              histoireDAO.publierHistoire(histoire);
          }      
      }


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HistoireDAO histoireDAO = new HistoireDAO(ds);
        /* Récupération des données de formulaire */
        request.setCharacterEncoding("UTF-8");
        
        publicationStories(request, response);
        
        /* Envoi de la réponse */
        response.setContentType("text/html;charset=UTF-8");
        try {
        	actionAfficher(request, response, histoireDAO);
        } catch (DAOException e) {
            erreurBD(request, response, e);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    @Resource(name = "jdbc/projetWeb")
    private DataSource ds;
    

}
