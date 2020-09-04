package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dummy.myerp.model.bean.comptabilite.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import com.dummy.myerp.technical.exception.FunctionalException;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

public class ComptabiliteManagerImplTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ComptabiliteManagerImpl manager;

    private EcritureComptable vEcritureComptable;

    private JournalComptable journalComptable;

    private SequenceEcritureComptable sequenceEcritureComptable;

    private List<SequenceEcritureComptable> listSequenceEcritureComptable;

    private LigneEcritureComptable ligneEcritureCredit;

    private LigneEcritureComptable ligneEcritureDebit;

    @Before
    public void initComptabiliteManagerImpl(){
        manager = new ComptabiliteManagerImpl();

        listSequenceEcritureComptable = new ArrayList<>();

        vEcritureComptable = new EcritureComptable();

        sequenceEcritureComptable = new SequenceEcritureComptable("AC", 2018, 32);

        vEcritureComptable.setId(1);
        journalComptable = new JournalComptable("AC", "Achat");

        vEcritureComptable.setJournal(journalComptable);
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setReference("AC-2020/00001");
        vEcritureComptable.setLibelle("Libelle");

        ligneEcritureCredit = new LigneEcritureComptable(new CompteComptable(1),null, new BigDecimal(123),null);
        ligneEcritureDebit = new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(123));
    }

    @Test
    public void checkEcritureComptableUnit() throws Exception {
        vEcritureComptable.getListLigneEcriture().add(ligneEcritureCredit);
        vEcritureComptable.getListLigneEcriture().add(ligneEcritureDebit);

        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitViolation() throws Exception {
        EcritureComptable pEcritureComptable = new EcritureComptable();

        manager.checkEcritureComptableUnit(pEcritureComptable);
    }

    @Test
    public void referenceTest(){
        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable("AC",2020,00001);

        assertThat(manager.reference(sequenceEcritureComptable)).isNotEqualTo("AC-2018/00032");
        assertThat(manager.reference(sequenceEcritureComptable)).isEqualTo("AC-2020/00001");

        sequenceEcritureComptable = new SequenceEcritureComptable("BQ",2016,1);

        Assert.assertNotEquals("AV-2019/00001",manager.reference(sequenceEcritureComptable));
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG2() throws Exception {
        EcritureComptable vEcritureComptable = new EcritureComptable();

        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(ligneEcritureCredit);
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),null, null, new BigDecimal(1234)));

        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG3UneLigneEcriture() throws Exception {
        EcritureComptable vEcritureComptable = new EcritureComptable();

        vEcritureComptable.getListLigneEcriture().clear();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                null));

        manager.checkEcritureComptableUnit(vEcritureComptable);

        vEcritureComptable.getListLigneEcriture().clear();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                new BigDecimal(123)));

        manager.checkEcritureComptableUnit(vEcritureComptable);

        vEcritureComptable.getListLigneEcriture().clear();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));

        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG3DeuxLignesDebit() throws Exception{
        EcritureComptable vEcritureComptable = new EcritureComptable();

        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
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

}
