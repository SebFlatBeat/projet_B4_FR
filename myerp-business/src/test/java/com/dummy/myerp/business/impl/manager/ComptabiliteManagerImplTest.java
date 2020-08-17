package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;

public class ComptabiliteManagerImplTest {
//enlever les mocks, apparement ils posent problemes regarde Vincent et Paul

    private ComptabiliteManagerImpl manager;

    private EcritureComptable vEcritureComptable;

    private LigneEcritureComptable ligneEcritureCredit;

    private LigneEcritureComptable ligneEcritureDebit;

    @Before
    public void initComptabiliteManagerImpl(){
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
    public void checkEcritureComptableUnitRG3UneLigneEcriture() throws Exception {
        vEcritureComptable.getListLigneEcriture().add(ligneEcritureCredit);
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG3DeuxLignesDebit() throws Exception{
        vEcritureComptable.getListLigneEcriture().add(ligneEcritureDebit);
        vEcritureComptable.getListLigneEcriture().add(ligneEcritureDebit);
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test
    public void checkEcritureComptableRG4UnitDebit() {
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(5),
                "Facture 6", null,
                new BigDecimal("123.56")));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(4),
                "Facture 5", new BigDecimal("-123.56").setScale(2, RoundingMode.HALF_EVEN),
                null));
        Assert.assertEquals(vEcritureComptable.getTotalDebit(), BigDecimal.valueOf(-123.56));
        Assert.assertNotEquals(vEcritureComptable.getTotalDebit(), BigDecimal.valueOf(123.56));
    }

    @Test
    public void checkEcritureComptableRG4UnitCredit() {
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(5),
                "Facture 6", null,
                new BigDecimal("-123.56")));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(4),
                "Facture 5", new BigDecimal("123.56").setScale(2, RoundingMode.HALF_EVEN),
                null));
        Assert.assertEquals(vEcritureComptable.getTotalCredit(), BigDecimal.valueOf(-123.56));
        Assert.assertNotEquals(vEcritureComptable.getTotalCredit(), BigDecimal.valueOf(123.56));
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

    @Test(expected = FunctionalException.class )
    public void checkEcritureComptableContextRG6_New() throws FunctionalException {
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
        vEcritureComptable.setId(-2);
        manager.checkEcritureComptable( vEcritureComptable);
        vEcritureComptable.setReference("VE-2016/00004");
        manager.checkEcritureComptable( vEcritureComptable );
    }

    @Test(expected = FunctionalException.class )
    public void checkEcritureComptableContextRG6_isEmpty() throws FunctionalException {
        vEcritureComptable.setId(-2);
        vEcritureComptable.setReference(null);
        manager.checkEcritureComptable( vEcritureComptable);
        vEcritureComptable.setReference("VE-2016/00004");
        manager.checkEcritureComptable( vEcritureComptable );
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG7() throws Exception {
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(5),
                "Facture 6", null,
                new BigDecimal("123.564")));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(4),
                "Facture 5", new BigDecimal("123.564"),
                null));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void insertEcritureComptable() throws FunctionalException {
        vEcritureComptable.setJournal(new JournalComptable("AC","Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Insertion écriture");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),"Facture C1",new BigDecimal(411),null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2), "Facture c2",new BigDecimal(401),null));
        manager.insertEcritureComptable(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void updateEcritureComptable() throws FunctionalException {
        vEcritureComptable.setJournal(new JournalComptable("AC","Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Insertion écriture");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),"Facture C1",new BigDecimal(411),null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2), "Facture c2",new BigDecimal(401),null));
        manager.insertEcritureComptable(vEcritureComptable);
        vEcritureComptable.setLibelle("Insertion nouvelle écriture");
        manager.updateEcritureComptable(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void deleteEcritureComptable() throws FunctionalException {
        vEcritureComptable.setJournal(new JournalComptable("AC","Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Insertion écriture");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),"Facture C1",new BigDecimal(411),null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2), "Facture c2",new BigDecimal(401),null));
        manager.insertEcritureComptable(vEcritureComptable);
        manager.deleteEcritureComptable(vEcritureComptable.getId());
    }

}
