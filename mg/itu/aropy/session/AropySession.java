package mg.itu.aropy.session;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import jakarta.servlet.http.HttpSession;

/** API du framework pour accéder à la session HTTP sans exposer son type servlet. */
public class AropySession {
    private final HttpSession session;

    public AropySession(HttpSession session) {
        this.session = Objects.requireNonNull(session, "La session HTTP ne peut pas être null.");
    }

    public void setAttribute(String key, Object value) {
        session.setAttribute(key, value);
    }

    public Object getAttribute(String key) {
        return session.getAttribute(key);
    }

    public <T> T getAttribute(String key, Class<T> type) {
        Objects.requireNonNull(type, "Le type demandé ne peut pas être null.");
        Object value = session.getAttribute(key);
        if (value == null) {
            return null;
        }
        if (!type.isInstance(value)) {
            throw new IllegalArgumentException("L'attribut de session '" + key + "' est de type "
                    + value.getClass().getName() + ", et non " + type.getName() + ".");
        }
        return type.cast(value);
    }

    /** Retourne une copie indépendante des attributs actuels. */
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new LinkedHashMap<>();
        Enumeration<String> names = session.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            attributes.put(name, session.getAttribute(name));
        }
        return attributes;
    }

    public void removeAttribute(String key) {
        session.removeAttribute(key);
    }

    public void clear() {
        for (String key : getAttributes().keySet()) {
            session.removeAttribute(key);
        }
    }

    public String getId() {
        return session.getId();
    }
}
