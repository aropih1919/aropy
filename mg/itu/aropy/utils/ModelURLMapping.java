package mg.itu.aropy.utils;

import java.util.Objects;

public class ModelURLMapping {
    String urlPath;
    String method;

    public ModelURLMapping(String urlPath, String method){
        this.urlPath = urlPath;
        this.method = method;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ModelURLMapping other = (ModelURLMapping) obj;
        return urlPath.equals(other.urlPath) && method.equals(other.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(urlPath, method);
    }
}