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


import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
/**
 * Application listener that initializes controller mappings at startup.
 */
// @WebListener
public class AppInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String packageName = sce.getServletContext().getInitParameter("packages");
        if (packageName == null || packageName.isBlank()) {
            sce.getServletContext().log("Aropy: aucun package configuré.");
            return;
        }
        // try {
        //     String configClassName = sce.getServletContext().getInitParameter("configClass");

        //     Class<?> configClass = Class.forName(configClassName);

        //     AnnotationConfigApplicationContext context =
        //             new AnnotationConfigApplicationContext();

        //     context.register((Class<?>) configClass);
        //     context.refresh();

        //     sce.getServletContext().setAttribute("applicationContext", context);
        // } catch (ClassNotFoundException e) {
        //     throw new RuntimeException("Aropy: échec de l'initialisation du contexte de l'application.", e);
        // }
        try {
            String configClassName =
                    sce.getServletContext().getInitParameter("configClass");

            if (configClassName == null || configClassName.isBlank()) {
                throw new RuntimeException(
                    "Aropy: le paramètre 'configClass' est absent."
                );
            }

            // sce.getServletContext().log(
            //     "Aropy: chargement de la configuration : " + configClassName
            // );
            ClassLoader webappCl = sce.getServletContext().getClassLoader();
            // ou Thread.currentThread().getContextClassLoader()

            Class<?> configClass = Class.forName(configClassName, true, webappCl);

            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
            context.setClassLoader(webappCl);   // pour que @ComponentScan voie les classes de la webapp
            context.register(configClass);
            context.refresh();

            // Class<?> configClass = Class.forName(configClassName);

            // sce.getServletContext().log(
            //     "Aropy: classe de configuration trouvée : " + configClass.getName()
            // );

            // AnnotationConfigApplicationContext context =
            //         new AnnotationConfigApplicationContext();

            // context.register(configClass);

            // sce.getServletContext().log(
            //     "Aropy: configuration enregistrée."
            // );

            // context.refresh();

            // sce.getServletContext().log(
            //     "Aropy: contexte Spring initialisé."
            // );

            // sce.getServletContext().setAttribute(
            //     "applicationContext",
            //     context
            // );

        } catch (Exception e) {
            sce.getServletContext().log(
                "Aropy: erreur lors de l'initialisation du contexte Spring.",
                e
            );

            throw new RuntimeException(
                "Aropy: échec de l'initialisation du contexte de l'application.",
                e
            );
        }

        try {
            //List<Class<?>> controllerClasses = ClassScanner.getClasses(packageName, Controller.class);
            List<String> controllerNames = new ArrayList<>();
            Map<ModelURLMapping, URLInformation> routes = new HashMap<>();
            
            String viewPrefix = sce.getServletContext().getInitParameter("viewPrefix");
            String viewSuffix = sce.getServletContext().getInitParameter("viewSuffix");
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
            //fillGrandMap();
            for (String clazz : controllerNames) {
                registerController(clazz, routes);
            }

            sce.getServletContext().setAttribute("controllerNames", controllerNames);
            sce.getServletContext().setAttribute("routes", routes);
            sce.getServletContext().setAttribute("viewPrefix", viewPrefix);
            sce.getServletContext().setAttribute("viewSuffix", viewSuffix);

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