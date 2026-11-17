package mg.itu.aropy.utils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

/** Réponses HTTP cohérentes pour les refus d'accès. */
public final class ResponseHelper {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ResponseHelper() {
    }

    public static void handleUnauthorized(HttpServletResponse response, boolean api) throws IOException {
        writeAccessDenied(response, api, HttpServletResponse.SC_UNAUTHORIZED,
                "unauthorized", "Authentification requise.");
    }

    public static void handleForbidden(HttpServletResponse response, boolean api) throws IOException {
        writeAccessDenied(response, api, HttpServletResponse.SC_FORBIDDEN,
                "forbidden", "Vous ne possédez pas le rôle requis.");
    }

    private static void writeAccessDenied(HttpServletResponse response, boolean api, int status,
            String error, String message) throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding("UTF-8");
        if (api) {
            response.setContentType("application/json");
            Map<String, String> body = new LinkedHashMap<>();
            body.put("error", error);
            body.put("message", message);
            response.getWriter().write(MAPPER.writeValueAsString(body));
        } else {
            response.setContentType("text/html");
            response.getWriter().write("<!doctype html><html lang=\"fr\"><head><meta charset=\"UTF-8\"><title>"
                    + status + " - Accès refusé</title></head><body><h1>" + status
                    + " - Accès refusé</h1><p>" + message + "</p></body></html>");
        }
    }
}
