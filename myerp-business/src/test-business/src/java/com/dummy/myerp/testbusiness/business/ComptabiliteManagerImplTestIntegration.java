package com.dummy.myerp.testbusiness.business;

import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:bootstrapContext.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ComptabiliteManagerImplTestIntegration extends BusinessTestCase{

    private ComptabiliteManagerImpl manager;

    private EcritureComptable vEcritureComptable;
    private ComptabiliteDaoImpl comptabiliteDao = ComptabiliteDaoImpl.getInstance();
    private TransactionManager transactionManager = TransactionManager.getInstance();

    private List<CompteComptable> compteComptableList = new ArrayList<>();
    private List<JournalComptable> journalComptableList = new ArrayList<>();
    private JournalComptable journalComptable;
    private SequenceEcritureComptable sequenceEcritureComptable;
    private List<SequenceEcritureComptable> listSequenceEcritureComptable;
    private LigneEcritureComptable ligneEcritureCredit;
    private LigneEcritureComptable ligneEcritureDebit;
    private LigneEcritureComptable ligneEcritureComptableCreditReference;
    private LigneEcritureComptable ligneEcritureComptableDebitReference;

    @Before
    public void initAll()  {
        SpringRegistry.init();

        compteComptableList = comptabiliteDao.getListCompteComptable();
        journalComptableList = comptabiliteDao.getListJournalComptable();
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(1);
        journalComptable = new JournalComptable("AC", "Achat");
        vEcritureComptable.setJournal(journalComptable);
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setReference("AC-2020/00001");
        vEcritureComptable.setLibelle("Libelle");
        ligneEcritureCredit = new LigneEcritureComptable(new CompteComptable(401),null, new BigDecimal(123),null);
        ligneEcritureDebit = new LigneEcritureComptable(new CompteComptable(411), null, null, new BigDecimal(123));
        ligneEcritureComptableCreditReference = new LigneEcritureComptable(new CompteComptable(606),null, new BigDecimal(123),null);
        ligneEcritureComptableDebitReference = new LigneEcritureComptable(new CompteComptable(706),null, null,new BigDecimal(123));
    }

    @After
    public void clearInit(){
        comptabiliteDao.clearInit();
    }

    @Test
    public void insertEcritureComptable() throws FunctionalException{
        manager = new ComptabiliteManagerImpl();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setReference("AC-2020/00005");
        vEcritureComptable.setLibelle("Insertion nouvelle écriture");
        vEcritureComptable.getListLigneEcriture().add(ligneEcritureCredit);
        vEcritureComptable.getListLigneEcriture().add(ligneEcritureDebit);

        int listCompare = manager.getListEcritureComptable().size();
        manager.insertEcritureComptable(vEcritureComptable);
        assertThat(manager.getListEcritureComptable().size()).isEqualTo(listCompare+1);
        assertThat(vEcritureComptable.getReference()).isEqualTo("AC-2020/00005");

    }

    @Test
    public void updateEcritureComptable() throws FunctionalException{
        manager = new ComptabiliteManagerImpl();
        EcritureComptable ecritureComptable = manager.getListEcritureComptable().get(0);
        assertThat(ecritureComptable.getId()).isEqualTo(-1);
        ecritureComptable.setLibelle("Update_Test");
        manager.updateEcritureComptable(ecritureComptable);
        assertThat(ecritureComptable.getLibelle()).isEqualTo("Update_Test");
    }

    @Test
    public void deleteEcritureComptable() throws FunctionalException {
        manager = new ComptabiliteManagerImpl();
        vEcritureComptable.setJournal(new JournalComptable("AC","Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Insertion écriture");
        vEcritureComptable.getListLigneEcriture().add(ligneEcritureCredit);
        vEcritureComptable.getListLigneEcriture().add(ligneEcritureDebit);

        manager.insertEcritureComptable(vEcritureComptable);
        int listCompare = manager.getListEcritureComptable().size();
        manager.deleteEcritureComptable(vEcritureComptable.getId());
        assertThat(manager.getListEcritureComptable().size());
    }

    @Test
    public void addReferenceTest() throws FunctionalException{
        manager = new ComptabiliteManagerImpl();
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(3);
        vEcritureComptable.setLibelle("libelle addReference");
        vEcritureComptable.setDate(new Date());
        journalComptable = (new JournalComptable("BQ","Banque"));
        vEcritureComptable.setJournal(journalComptable);
        vEcritureComptable.getListLigneEcriture().add(ligneEcritureComptableCreditReference);
        vEcritureComptable.getListLigneEcriture().add(ligneEcritureComptableDebitReference);

        manager.addReference(vEcritureComptable);
        manager.insertEcritureComptable(vEcritureComptable);
        assertThat(vEcritureComptable.getReference()).isEqualTo("BQ-2020/00001");
    }

    @Test(expected = FunctionalException.class )
    public void checkEcritureComptableContextRG6_New() throws FunctionalException {
        manager = new ComptabiliteManagerImpl();
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setLibelle("Nombre écriture valide : au moins 1  ligne de débit et 1 ligne de crédit");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),"Facture C1",new BigDecimal(411),null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2), "Facture c2",new BigDecimal(401),null));
        manager.checkEcritureComptable(vEcritureComptable);
        vEcritureComptable.setReference("AC-2016/00001");
        manager.checkEcritureComptable(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class )
    public void checkEcritureComptableContextRG6_Exist() throws FunctionalException {
        manager = new ComptabiliteManagerImpl();
        vEcritureComptable.setId(-2);
        manager.checkEcritureComptable( vEcritureComptable);
        vEcritureComptable.setReference("VE-2016/00004");
        manager.checkEcritureComptable( vEcritureComptable );
    }

    @Test(expected = FunctionalException.class )
    public void checkEcritureComptableContextRG6_isEmpty() throws FunctionalException {
        manager = new ComptabiliteManagerImpl();
        vEcritureComptable.setId(-2);
        vEcritureComptable.setReference(null);
        manager.checkEcritureComptable( vEcritureComptable);
        vEcritureComptable.setReference("VE-2016/00004");
        manager.checkEcritureComptable( vEcritureComptable );
    }

}
