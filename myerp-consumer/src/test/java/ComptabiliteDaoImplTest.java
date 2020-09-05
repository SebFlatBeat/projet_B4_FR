import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = {"classpath:bootstrapContext.xml"})
public class ComptabiliteDaoImplTest {

    private ComptabiliteDaoImpl comptabiliteDao = ComptabiliteDaoImpl.getInstance();

    private List<CompteComptable> compteComptableList = new ArrayList<>();

    @Before
    public void init(){
        SpringRegistry.init();
        compteComptableList = comptabiliteDao.getListCompteComptable();
    }

    @After
    public void initialDataBase(){
        comptabiliteDao.clearInit();
    }

    @Test
    public void getListCompteComptable(){
        List<CompteComptable> compteComptableList = comptabiliteDao.getListCompteComptable();
        assertNotNull(compteComptableList);
    }
}
