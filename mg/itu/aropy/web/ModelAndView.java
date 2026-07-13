package mg.itu.aropy.web;
import java.util.HashMap;
import java.util.Map;
public class ModelAndView {
    private Map<String, Object> attributes = new HashMap<>();
    private String url;
    public ModelAndView(String url) {
        this.url = url;
    }
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
