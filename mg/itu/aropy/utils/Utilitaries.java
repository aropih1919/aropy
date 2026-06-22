package mg.itu.aropy.utils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    // Target ou seulement du string
    public static List<String> findClassesWithAnnotation(String packageName, Class<? extends Annotation> annotationClass, String target){
        //utilisation du findAllClassesIn pour avoir la list des classe dispo
        List<String> tempo = new ArrayList<>();
        try{
            List <Class<?>> listClass = findAllClassesIn(packageName);
            //boucler list des classes et verification de chaque classe si contenant l annotation ou pas
            for(Class clazz : listClass){
                Annotation[] annotations = clazz.getAnnotations();
                for (Annotation a : annotations) {
                    if (a.annotationType().equals(annotationClass)) {
                        tempo.add(clazz.getName());
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return tempo;
        
        
    }
}
