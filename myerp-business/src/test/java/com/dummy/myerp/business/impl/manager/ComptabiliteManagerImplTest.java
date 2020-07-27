package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.Date;

import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ComptabiliteManagerImplTest {

    @Mock
    private ComptabiliteManagerImpl manager;
    @Mock
    private EcritureComptable vEcritureComptable;
    @Mock
    private TransactionManager transactionManager;
    @Mock
    private DaoProxy daoProxy;
    @Mock
    private ComptabiliteDao comptabiliteDao;

    private LigneEcritureComptable ligneEcritureCredit;

    private LigneEcritureComptable ligneEcritureDebit;

    @Before
    public void initComptabiliteManagerImpl(){
        MockitoAnnotations.initMocks(this);
        when(this.daoProxy.getComptabiliteDao()).thenReturn(this.comptabiliteDao);
        AbstractBusinessManager.configure(null,this.daoProxy,this.transactionManager);
        manager = new ComptabiliteManagerImpl();
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(1);
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setReference("AC-2020/00001");
        vEcritureComptable.setLibelle("Libelle");
        ligneEcritureCredit = new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null);
        ligneEcritureDebit = new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123));
    }

    @Test
    public void checkEcritureComptableUnit() throws Exception {
        vEcritureComptable.getListLigneEcriture().add(ligneEcritureCredit);
        vEcritureComptable.getListLigneEcriture().add(ligneEcritureDebit);
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitViolation() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG2() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(ligneEcritureCredit);
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal(1234)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG3() throws Exception {
        vEcritureComptable.getListLigneEcriture().add(ligneEcritureCredit);
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG3DeuxLignesDebit() throws Exception{
        vEcritureComptable.getListLigneEcriture().add(ligneEcritureDebit);
        vEcritureComptable.getListLigneEcriture().add(ligneEcritureDebit);
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableRG5FormatAnnee() throws Exception{
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                "Achat numéro 1", new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                "Achat numéro 2", null,
                new BigDecimal(123)));
        vEcritureComptable.setReference("AC-2019/00001");
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableRG5FormatReference() throws Exception{
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(3),
                "Achat numéro 3", new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(4),
                "Achat numéro 4", null,
                new BigDecimal(123)));
        vEcritureComptable.setReference("BC-2020/00001");
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

}
