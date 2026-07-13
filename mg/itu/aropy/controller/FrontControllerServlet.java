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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.lang.annotation.ElementType;
import mg.itu.aropy.annotation.AropyController;
import mg.itu.aropy.utils.Utilitaries;
import mg.itu.aropy.utils.URLInformation;
import mg.itu.aropy.utils.ModelURLMapping;
import java.util.HashMap;
import java.util.Map;
import mg.itu.aropy.web.ModelAndView;

public class FrontControllerServlet extends HttpServlet {
    List<String> listController;
    Map<ModelURLMapping,URLInformation> grandMap = new HashMap<>();
    String viewPrefix;
    String viewSuffix;
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
    // @Override
    // public void init() throws ServletException {
    //     listController = new ArrayList<>();
    //     //Récuperer les variables déclarés dans web.xml comme init (on obtiendra une list de package)
    //     String packages = getServletConfig().getInitParameter("packages");
    //     String[] listPackages = packages.split(",");
    //     //Boucler la list de package et utiliser la fonction qui retourne list string du full name des classes contenant l annotation Controller 
    //     for (int i = 0; i < listPackages.length; i++) {
    //         List<String> clazzes = Utilitaries.findClassesWithAnnotation(listPackages[i],AropyController.class, ElementType.TYPE);
    //         //add dans listController
    //         listController.addAll(clazzes);
    //     }
    //     fillGrandMap();
    //     viewPrefix = getServletConfig().getInitParameter("viewPrefix");
    //     viewSuffix = getServletConfig().getInitParameter("viewSuffix");
    // }
    @SuppressWarnings("unchecked")
    public void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        //res.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ServletContext sc = getServletContext();
        List<String> controllerNames = (List<String>) sc.getAttribute("controllerNames");
        Map<ModelURLMapping, URLInformation> routes = (Map<ModelURLMapping, URLInformation>) sc.getAttribute("routes");

        out.println("<html>");
        out.println("<head><title>Debug Info</title></head>");
        out.println("<body>");

        // Affichage des controllerNames
        out.println("<h2>Controller Names</h2>");
        out.println("<ul>");
        if (controllerNames != null) {
            for (String name : controllerNames) {
                out.println("<li>" + name + "</li>");
            }
        }
        out.println("</ul>");
        
        // Affichage des routes
        out.println("<h2>Routes</h2>");
        out.println("<table border='1'>");
        out.println("<tr><th>Clé</th><th>UrlEntry</th></tr>");
        if (routes != null) {
            for (Map.Entry<ModelURLMapping, URLInformation> entry : routes.entrySet()) {
                out.println("<tr>");
                out.println("<td>" + entry.getKey() + "</td>");
                out.println("<td>" + entry.getValue() + "</td>"); // appelle toString() de UrlEntry
                out.println("</tr>");
            }
        }
        out.println("</table>");

        out.println("</body>");
        out.println("</html>");
        // String uri = req.getRequestURI();
        // String context = req.getContextPath();
        // String url = uri.substring(context.length());
        // String httpMethod = req.getMethod(); // GET, POST, etc.

        // ModelURLMapping key = new ModelURLMapping(url, httpMethod);

        // out.println("========================================");
        // out.println(" FRONT CONTROLLER - RESULTAT");
        // out.println("========================================");
        // out.println("URI     : " + uri);
        // out.println("Context : " + context);
        // out.println("URL     : " + url);
        // out.println("Methode : " + httpMethod);
        // out.println("----------------------------------------");

        // if (grandMap.containsKey(key)) {
        //     out.println("STATUT  : OK - URL trouvee");
        //     out.println("Detail  : " + grandMap.get(key));
        //     URLInformation m = grandMap.get(key);
        //     Method method = m.getFonction();
                    
        //     try {
        //         Object target = m.getClazz().getDeclaredConstructor().newInstance(); // maintenant dans le try
        //         // Rendre la méthode accessible même si elle est private
        //         method.setAccessible(true);
        //         Object result = method.invoke(target);
        //         if (result instanceof ModelAndView) {
        //             ModelAndView modelAndView = (ModelAndView) result;
        //             String urlPathView = viewPrefix + modelAndView.getUrl() + viewSuffix;
        //             RequestDispatcher dispatcher = req.getRequestDispatcher(urlPathView);
        //             //Boucle des map vers request.setAttribute
        //             req.setAttribute("data",modelAndView.getAttributes());
        //             // for (Map.Entry<String, Object> entry : modelAndView.getAttributes().entrySet()) {
        //             //     req.setAttribute(entry.getKey(), entry.getValue());
        //             // }
        //             // Redirection vers la vue
        //             dispatcher.forward(req, res);
        //             // out.println("ModelAndView URL: " + modelAndView.getUrl());
        //             // out.println("ModelAndView Attributes: " + modelAndView.getAttributes());
        //         } else {
        //             out.println("Resultat de la methode : " + result);
        //         }
        //     } catch (IllegalAccessException e) {
        //         throw new RuntimeException("Impossible d'accéder à la méthode: " + method.getName(), e);
        //     } catch (InvocationTargetException e) {
        //         // C'est l'exception LEVÉE PAR la méthode invoquée elle-même
        //         Throwable cause = e.getCause();
        //         throw new RuntimeException("Erreur pendant l'exécution de: " + method.getName(), cause);
        //     } catch (IllegalArgumentException e) {
        //         throw new RuntimeException("Arguments invalides pour: " + method.getName(), e);
        //     } catch (InstantiationException | NoSuchMethodException e) {
        //         throw new RuntimeException("Impossible d'instancier la classe: " + m.getClazz().getName(), e);
        //     }
        // }
        // else {
        //     out.println("STATUT  : ERREUR - URL non disponible");
        //     out.println("----------------------------------------");
        //     out.println("URLs existantes (" + grandMap.size() + ") :");
        //     out.println("----------------------------------------");

        //     if (grandMap.isEmpty()) {
        //         out.println("  (Aucune URL enregistree)");
        //     } else {
        //         int i = 1;
        //         for (Map.Entry<ModelURLMapping, URLInformation> entry : grandMap.entrySet()) {
        //             ModelURLMapping m = entry.getKey();
        //             out.println(i + ". urlPath=" + m.getUrlPath()
        //                     +". Method ="+m.getMethod()+ " -> " + entry.getValue());
        //             i++;
        //         }
        //     }
        // }

        // out.println("========================================");
    }
}
