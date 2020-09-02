package com.dummy.myerp.testbusiness.business;

import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
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

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();

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
    }

    @Test
    public void insertEcritureComptable() throws FunctionalException{
       vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
       vEcritureComptable.setReference("AC-2020/00005");
       vEcritureComptable.setLibelle("Insertion nouvelle écriture");
       vEcritureComptable.getListLigneEcriture().add(ligneEcritureCredit);
       vEcritureComptable.getListLigneEcriture().add(ligneEcritureDebit);

       manager.insertEcritureComptable(vEcritureComptable);

    }

    @Test
    public void updateEcritureComptable() throws FunctionalException{
        EcritureComptable ecritureComptable = manager.getListEcritureComptable().get(0);
        assertThat(ecritureComptable.getId()).isEqualTo(-1);
        ecritureComptable.setLibelle("Update_Test");
        manager.updateEcritureComptable(ecritureComptable);
        assertThat(ecritureComptable.getLibelle()).isEqualTo("Update_Test");
    }

    @Test(expected = FunctionalException.class)
    public void deleteEcritureComptable() throws FunctionalException {
        vEcritureComptable.setJournal(new JournalComptable("AC","Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Insertion écriture");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),"Facture C1",new BigDecimal(411),null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2), "Facture c2",null,new BigDecimal(411)));
        manager.insertEcritureComptable(vEcritureComptable);
        manager.deleteEcritureComptable(vEcritureComptable.getId());
    }

}
