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
import mg.itu.aropy.utils.ModelURLMapping;
import java.util.HashMap;
import java.util.Map;
public class FrontControllerServlet extends HttpServlet {
    List<String> listController;
    Map<ModelURLMapping,URLInformation> grandMap = new HashMap<>();
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
            Map<ModelURLMapping,URLInformation> tempo = Utilitaries.findURLCompleted(controllerName);
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
        res.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = res.getWriter();

        String uri = req.getRequestURI();
        String context = req.getContextPath();
        String url = uri.substring(context.length());
        String httpMethod = req.getMethod(); // GET, POST, etc.

        ModelURLMapping key = new ModelURLMapping(url, httpMethod);

        out.println("========================================");
        out.println(" FRONT CONTROLLER - RESULTAT");
        out.println("========================================");
        out.println("URI     : " + uri);
        out.println("Context : " + context);
        out.println("URL     : " + url);
        out.println("Methode : " + httpMethod);
        out.println("----------------------------------------");

        if (grandMap.containsKey(key)) {
            out.println("STATUT  : OK - URL trouvee");
            out.println("Detail  : " + grandMap.get(key));
        } else {
            out.println("STATUT  : ERREUR - URL non disponible");
            out.println("----------------------------------------");
            out.println("URLs existantes (" + grandMap.size() + ") :");
            out.println("----------------------------------------");

            if (grandMap.isEmpty()) {
                out.println("  (Aucune URL enregistree)");
            } else {
                int i = 1;
                for (Map.Entry<ModelURLMapping, URLInformation> entry : grandMap.entrySet()) {
                    ModelURLMapping m = entry.getKey();
                    out.println(i + ". urlPath=" + m.getUrlPath()
                            +". Method ="+m.getMethod()+ " -> " + entry.getValue());
                    i++;
                }
            }
        }

        out.println("========================================");
    }
}
