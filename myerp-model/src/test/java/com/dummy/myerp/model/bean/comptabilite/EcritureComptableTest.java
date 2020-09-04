package com.dummy.myerp.model.bean.comptabilite;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class EcritureComptableTest {

    private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String pDebit, String pCredit) {
        BigDecimal vDebit = pDebit == null ? null : new BigDecimal(pDebit);
        BigDecimal vCredit = pCredit == null ? null : new BigDecimal(pCredit);
        String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
                .subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();
        LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero),
                vLibelle,
                vDebit, vCredit);
        return vRetour;
    }

    @Before
    public void initEcritureComptable(){
        EcritureComptable vEcriture = new EcritureComptable();

        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "40", "7"));
    }

    @Test
    public void isEquilibree() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.setLibelle("Equilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
        Assert.assertTrue(vEcriture.toString(), vEcriture.isEquilibree());

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Non équilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "20", "1"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "30"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "1", "2"));
        Assert.assertFalse(vEcriture.toString(), vEcriture.isEquilibree());
    }

    @Test
    public void getTotalDebit(){
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        Assert.assertEquals("301.00",vEcriture.getTotalDebit().toString());
        Assert.assertNotEquals(null,vEcriture.getTotalDebit().toString());
    }

    @Test
    public void getTotalCredit(){
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        Assert.assertEquals("33", vEcriture.getTotalCredit().toString());
        Assert.assertNotEquals(null, vEcriture.getTotalCredit().toString());
    }

    @Test
    public void setEcritureComptable(){
        EcritureComptable pEcritureComptable = new EcritureComptable();
        Date date = new Date();
        pEcritureComptable.setDate(date);
        pEcritureComptable.setId(1);
        pEcritureComptable.setReference("AB");
        pEcritureComptable.setLibelle("Cartouches d’imprimante");
        assertThat(pEcritureComptable.getReference()).isNotEqualTo("DC");
        assertThat(pEcritureComptable.getReference()).isEqualTo("AB");
        assertThat(pEcritureComptable.getLibelle()).isNotEqualTo("Toner");
        assertThat(pEcritureComptable.getLibelle()).isEqualTo("Cartouches d’imprimante");
        assertThat(pEcritureComptable.getDate()).isEqualTo(date);
        assertThat(pEcritureComptable.getId()).isEqualTo(1);
        assertThat(pEcritureComptable.getId()).isNotEqualTo(2);
    }

    @Test
    public void test_toString_EcritureComptable() {
        JournalComptable vJournalComptable = new JournalComptable();
        Date date = new Date();
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(4);
        vEcritureComptable.setJournal(vJournalComptable);
        vEcritureComptable.setReference("AC-2020/00001");
        vEcritureComptable.setDate(date);
        vEcritureComptable.setLibelle("test toString");
        String testToString = "EcritureComptable{id=" + vEcritureComptable.getId()
                + ", " + "journal=" + vEcritureComptable.getJournal()
                + ", " + "reference='" + vEcritureComptable.getReference() + "', "
                + "date=" + vEcritureComptable.getDate()
                + ", " + "libelle='" + vEcritureComptable.getLibelle()
                + "', " + "totalDebit=" + vEcritureComptable.getTotalDebit()
                + ", " + "totalCredit=" + vEcritureComptable.getTotalCredit() + ", "
                + "listLigneEcriture=[\n" + StringUtils.join(vEcritureComptable.getListLigneEcriture(), "\n") + "\n]}";
        String resultatTestToString = vEcritureComptable.toString();
        assertThat(testToString).isEqualTo(resultatTestToString);
    }
}
