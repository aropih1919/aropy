package mg.itu.aropy.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Marque un contrôleur Aropy. Son instance est créée une fois au démarrage et
 * partagée entre les requêtes; les contrôleurs doivent donc éviter tout état
 * mutable propre à une requête dans leurs champs.
 */
@Target({ElementType.TYPE})

@Retention(RetentionPolicy.RUNTIME)

public @interface AropyController {
    
}
