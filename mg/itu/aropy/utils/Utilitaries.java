package mg.itu.aropy.utils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mg.itu.aropy.annotation.URLMapping;

public class Utilitaries {
    public static List<Class<?>> findAllClassesIn(String packageName)throws Exception{
        //fonction rechercher les classes dispo dans packageName
        String pathPackage = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(pathPackage);
        File directory = new File(resource.getFile());
        List <Class<?>> listClassName = new ArrayList<>();

        // System.out.println(directory.getName());
        if (directory.exists()) {
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().endsWith(".class") && files[i].isFile()) {
                    String className = packageName +"."+files[i].getName().substring(0, files[i].getName().length() - 6);
                    Class<?> tempo = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
                    listClassName.add(tempo);
                }
            }
            return listClassName;
        }
        //else manquant 
        else{
            throw new Exception(" not found is the "+directory.getName());
        }
    }
    public static boolean verifierAnnotationEtNiveau(AnnotatedElement element, Class<? extends Annotation> annotationClass, ElementType niveauRecherche) {
        
        Annotation[] annotations = element.getAnnotations();
        System.out.println("  [debug] element=" + element + " nb annotations=" + annotations.length);
        for (Annotation a : annotations) {
            System.out.println("  [debug] annotation trouvée : " + a.annotationType().getName());
            //verfication de son existance d abord
            if (a.annotationType().equals(annotationClass)) {
                //verificagtion du niveau
                Target target = annotationClass.getAnnotation(Target.class);
                //sans specification précise, disponible partout
                if (target == null) {
                    return true; 
                }
                if(Arrays.asList(target.value()).contains(niveauRecherche)){
                    return true;
                }
            }
        }
        return false;
    }
    // Target ou seulement du string
    public static List<String> findClassesWithAnnotation(String packageName, Class<? extends Annotation> annotationClass, ElementType niveauRecherche){
        //utilisation du findAllClassesIn pour avoir la list des classe dispo
        List<String> tempo = new ArrayList<>();
        try{
            List <Class<?>> listClass = findAllClassesIn(packageName);
            //boucler list des classes et verification de chaque classe si contenant l annotation ou pas
            for(Class clazz : listClass){
                boolean annotationPresent = verifierAnnotationEtNiveau(clazz,annotationClass,niveauRecherche);
                if(annotationPresent){
                    tempo.add(clazz.getName());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return tempo;
    }
    //Recherche de method dans une classe
    public static List<Method> findAllMethodIn(Class<?> clazz){
        List<Method> listMethods = new ArrayList<>();
        System.out.println("###### Taille listeMethods avant ###### =" + listMethods.size());

        Method[] methods = clazz.getDeclaredMethods();
        for(int j = 0; j < methods.length; j++){
            listMethods.add(methods[j]);
        }
        System.out.println("###### Taille listeMethods apres ###### =" + listMethods.size());

        return listMethods;
    }
    //Fonction qui retourne Map<String,URLInformation> grandMap
    public static Map<ModelURLMapping,URLInformation> findURLCompleted(String className){
        Map<ModelURLMapping,URLInformation> grandMap = new HashMap<>();
        System.out.println("###### VERSION TEST 12345 ###### className=" + className);
        try{
            //par reflection, on cherche laa classe correspondant au string 
            System.out.println("###### AVANT Class.forName");
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            System.out.println("###### Classloader utilisé : " + cl);
            Class<?> clazz = Class.forName(className, false, cl);
            //pour une classe , obtenir la liste des methods
            System.out.println("###### AVANT findAllMethodIn");
            List<Method> methods = findAllMethodIn(clazz);
            System.out.println("###### APRES ");

            //parcourir method et verifier si urlMapping présent
            System.out.println("[debug] classe=" + className + " nb methods=" + methods.size());
            for(Method method : methods){
                System.out.println("[debug] examen de la méthode : " + method.getName());
                boolean check = verifierAnnotationEtNiveau(method,URLMapping.class,ElementType.METHOD);
                System.out.println("[debug] résultat check URLMapping pour " + method.getName() + " : " + check);
                if(check){
                //si oui, on essaie de obtenir la valeur de l attribut. On fait push avec clés la valeur de l url, et urlInformation pour controller et method
                    URLMapping ump = method.getAnnotation(URLMapping.class);
                    URLInformation ui = new URLInformation(clazz, method);
                    ModelURLMapping modelURLMapping = new ModelURLMapping(ump.urlPath(), ump.method());
                    grandMap.put(modelURLMapping, ui);
                }
            }
        }
        catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        catch(Throwable t){
            System.out.println("[debug] THROWABLE ATTRAPÉ : " + t.getClass().getName() + " - " + t.getMessage());
            t.printStackTrace(System.out); // forcer la sortie vers System.out, pas System.err
        }
        //à la fin, on retourne 
        
        return grandMap;
        
    }
}