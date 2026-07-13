package mg.itu.aropy.listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebListener;
import mg.itu.aropy.utils.Utilitaries;
import mg.itu.aropy.utils.URLInformation;
import mg.itu.aropy.utils.ModelURLMapping;
import mg.itu.aropy.annotation.AropyController;
import mg.itu.aropy.annotation.URLMapping;
import mg.itu.aropy.utils.URLInformation;

import java.lang.ModuleLayer.Controller;
import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Application listener that initializes controller mappings at startup.
 */
@WebListener
public class AppInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String packageName = sce.getServletContext().getInitParameter("packages");
        if (packageName == null || packageName.isBlank()) {
            sce.getServletContext().log("Aropy: aucun package configuré.");
            return;
        }

        try {
            //List<Class<?>> controllerClasses = ClassScanner.getClasses(packageName, Controller.class);
            List<String> controllerNames = new ArrayList<>();
            Map<ModelURLMapping, URLInformation> routes = new HashMap<>();

            //listController = new ArrayList<>();
            //Récuperer les variables déclarés dans web.xml comme init (on obtiendra une list de package)
            //String packages = getServletConfig().getInitParameter("packages");
            String[] listPackages = packageName.split(",");
            //Boucler la list de package et utiliser la fonction qui retourne list string du full name des classes contenant l annotation Controller 
            for (int i = 0; i < listPackages.length; i++) {
                List<String> clazzes = Utilitaries.findClassesWithAnnotation(listPackages[i],AropyController.class, ElementType.TYPE);
                //add dans listController
                controllerNames.addAll(clazzes);
            }

            for (String clazz : controllerNames) {
                registerController(clazz, routes);
            }

            sce.getServletContext().setAttribute("controllerNames", controllerNames);
            sce.getServletContext().setAttribute("routes", routes);

            sce.getServletContext().log("Aropy: " + controllerNames.size() + " contrôleur(s) enregistré(s).");
            sce.getServletContext().log("Aropy: " + routes.size() + " @UrlMapping(s) enregistré(s).");
        } catch (Exception e) {
            throw new RuntimeException("Aropy: échec du scan du package " + packageName, e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup on shutdown if needed
        sce.getServletContext().removeAttribute("controllerNames");
        sce.getServletContext().removeAttribute("routes");
    }

    private void registerController(String clazz, Map<ModelURLMapping, URLInformation> routes) throws Exception {
        Map<ModelURLMapping,URLInformation> tempo = Utilitaries.findURLCompleted(clazz);
        routes.putAll(tempo);
    }
}