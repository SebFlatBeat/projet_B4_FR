import org.junit.Assert;
import org.junit.Test;


/**
 * Classe de test de l'initialisation du contexte Spring
 */
public class TestInitSpring {

    /**
     * Constructeur.
     */
    public TestInitSpring() {
        super();
    }


    /**
     * Teste l'initialisation du contexte Spring
     */
    @Test
    public void testInit() {
        SpringRegistry.init();
        Assert.assertNotNull(SpringRegistry.getDaoProxy());
    }
}
