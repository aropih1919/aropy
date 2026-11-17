package mg.itu.aropy.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Protège un contrôleur ou une méthode. Un rôle vide exige seulement un
 * attribut d'authentification présent dans la session; un rôle renseigné
 * exige en plus que cet attribut contienne ce rôle.
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {
    String role() default "";
}
