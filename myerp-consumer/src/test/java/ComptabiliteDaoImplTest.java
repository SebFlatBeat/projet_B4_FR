import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite.EcritureComptableRM;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.technical.exception.NotFoundException;
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

    @Test
    public void getListJournalComptable(){
        List<JournalComptable> journalComptableList = comptabiliteDao.getListJournalComptable();
        assertNotNull(journalComptableList);
    }

    @Test
    public void getListEcritureComptable(){
        List<EcritureComptable> ecritureComptableList = comptabiliteDao.getListEcritureComptable();
        assertNotNull(ecritureComptableList);
    }

    @Test
    public void getEcritureComptable(){
        EcritureComptable ecritureComptable = comptabiliteDao.getEcritureComptable(-1);
        assertNotNull(ecritureComptable);
    }

    @Test(expected = NotFoundException.class)
    public void getEcritureComptableByRef() throws NotFoundException {
        EcritureComptable ecritureComptable = comptabiliteDao.getEcritureComptableByRef("AC-2000/00001");
        assertNotNull(ecritureComptable);
        comptabiliteDao.getEcritureComptableByRef("AC-2000/00002");
    }
}
