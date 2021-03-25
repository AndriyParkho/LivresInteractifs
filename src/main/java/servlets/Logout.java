package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "Logout", urlPatterns = {"/logout"})
public class Logout extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        /* Récupération de la valeur de session nommé "utilisateur" */
        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect("index.jsp"); 
    }
}
