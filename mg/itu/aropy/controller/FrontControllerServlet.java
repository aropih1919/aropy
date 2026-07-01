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
import java.lang.annotation.ElementType;
import mg.itu.aropy.annotation.AropyController;
import mg.itu.aropy.utils.Utilitaries;
import mg.itu.aropy.utils.URLInformation;
import java.util.HashMap;
import java.util.Map;
public class FrontControllerServlet extends HttpServlet {
    List<String> listController;
    Map<String,URLInformation> grandMap = new HashMap<>();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        processRequest(req, res);
    }
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        processRequest(req, res);
    }
    public void fillGrandMap(){
        System.out.println("Nombre de controllers : " + listController.size());
        for(String controllerName : listController){
            System.out.println("Traitement de : " + controllerName);
            Map<String,URLInformation> tempo = Utilitaries.findURLCompleted(controllerName);
            System.out.println("  -> " + tempo.size() + " URL(s) trouvée(s)");
            grandMap.putAll(tempo);
        }
    }
    @Override
    public void init() throws ServletException {
        listController = new ArrayList<>();
        //Récuperer les variables déclarés dans web.xml comme init (on obtiendra une list de package)
        String packages = getServletConfig().getInitParameter("packages");
        String[] listPackages = packages.split(",");
        //Boucler la list de package et utiliser la fonction qui retourne list string du full name des classes contenant l annotation Controller 
        for (int i = 0; i < listPackages.length; i++) {
            List<String> clazzes = Utilitaries.findClassesWithAnnotation(listPackages[i],AropyController.class, ElementType.TYPE);
            //add dans listController
            listController.addAll(clazzes);
        }
        fillGrandMap();
    }
    public void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {

        PrintWriter out = res.getWriter();

        String uri = req.getRequestURI();          // /MonProjet/login
        String context = req.getContextPath();     // /MonProjet

        String url = uri.substring(context.length()); // /login

        if (grandMap.containsKey(url)) {
            out.println("L'URL est bien trouvée : " + url);
        } else {
            out.println("L'URL demandée n'est pas disponible.");
            out.println("URLs existantes :");
            out.println("Taille de grandMap : " + grandMap.size());
            for (Map.Entry<String, URLInformation> entry : grandMap.entrySet()) {
                out.println(entry.getKey() + " -> " + entry.getValue());
            }
        }
    }
}
