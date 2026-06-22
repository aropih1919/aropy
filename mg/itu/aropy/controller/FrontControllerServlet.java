package mg.itu.aropy.controller;
import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.PrintWriter;
import java.lang.annotation.Target;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import mg.itu.aropy.annotation.AropyController;
import mg.itu.aropy.utils.Utilitaries;


public class FrontControllerServlet extends HttpServlet {
    List<String> listController;
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        processRequest(req, res);
    }
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        processRequest(req, res);
    }
    @Override
    public void init() throws ServletException {
        listController = new ArrayList<>();
        //Récuperer les variables déclarés dans web.xml comme init (on obtiendra une list de package)
        String packages = getServletConfig().getInitParameter("packages");
        String[] listPackages = packages.split(",");
        //Boucler la list de package et utiliser la fonction qui retourne list string du full name des classes contenant l annotation Controller 
        for (int i = 0; i < listPackages.length; i++) {
            List<String> clazzes = Utilitaries.findClassesWithAnnotation(listPackages[i],AropyController.class, null);
            //add dans listController
            listController.addAll(clazzes);
        }
    }
    public void processRequest(HttpServletRequest req, HttpServletResponse res)throws IOException{
        String url = req.getRequestURL().toString();
        PrintWriter out = res.getWriter();
        out.println("Voici la request Path: " + url);
        out.println("Voici la list des classes identifie comme controller: "+ listController.size());
        for(String s : listController){
            out.println(s);
        }
    }
}
