package mg.itu.aropy.utils;
import java.lang.reflect.Method;
public class URLInformation {
    private Class<?> clazz;
    private Method fonction;
    public URLInformation(Class<?> clazz,Method fonction){
        setClazz(clazz);
        setFonction(fonction);
    }
    public Class<?> getClazz(){ return this.clazz; }
    public Method getFonction(){ return this.fonction; }
    public void setClazz(Class<?> clazz){this.clazz = clazz;}
    public void setFonction(Method fonction){this.fonction = fonction;}
    @Override
    public String toString(){
        String ret = "class=" + this.clazz.getName() + " -> fonction=" + this.fonction.getName();
        return ret;
    }
}
