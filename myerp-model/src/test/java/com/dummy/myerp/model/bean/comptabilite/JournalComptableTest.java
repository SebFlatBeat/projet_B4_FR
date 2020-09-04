package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JournalComptableTest {

    @Test
    public void getByCode() {
        List<JournalComptable> journalComptableList =new ArrayList<JournalComptable>();
        JournalComptable journalComptable = new JournalComptable();
        journalComptable.setCode("AC");
        journalComptable.setLibelle("Achat");

        journalComptableList.add(journalComptable);
        Assert.assertNotNull(JournalComptable.getByCode(journalComptableList,"AC"));
        Assert.assertEquals(journalComptable.getLibelle(),JournalComptable.getByCode(journalComptableList,"AC").getLibelle());
        Assert.assertNull(JournalComptable.getByCode(journalComptableList,"ACC"));
    }

    @Test
    public void test_toString_JournalComptable(){
   JournalComptable vJournalComptable = new JournalComptable();
        vJournalComptable.setLibelle("test toString");
        vJournalComptable.setCode("AC");
        String testToString = "JournalComptable{code='"+vJournalComptable.getCode()+"', "
                +"libelle='"+vJournalComptable.getLibelle()+"'}";
        String finalTestToString = vJournalComptable.toString();
        assertThat(testToString).isEqualTo(finalTestToString);
    }
}
