package aropy.controller;
import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.PrintWriter;

public class FrontControllerServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        processRequest(req, res);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        processRequest(req, res);
    }
    public void processRequest(HttpServletRequest req, HttpServletResponse res)throws IOException{
        String url = req.getRequestURL().toString();
        PrintWriter out = res.getWriter();
        out.println("Voici la request Path: " + url);
    }
}
