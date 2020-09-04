package com.dummy.myerp.model.bean.comptabilite;


import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class SequenceEcritureComptableTest {

    @Test
    public void test_toString_SequenceEcritureComptable(){
        SequenceEcritureComptable vSequenceEcritureComptable = new SequenceEcritureComptable();
        vSequenceEcritureComptable.setAnnee(2020);
        vSequenceEcritureComptable.setDerniereValeur(32);
        vSequenceEcritureComptable.setReferenceJournalCode("AC");
        String testToString = "SequenceEcritureComptable{referenceJournalCode="+vSequenceEcritureComptable.getReferenceJournalCode()+", "
                +"annee="+vSequenceEcritureComptable.getAnnee()+
                ", "+"derniereValeur="+vSequenceEcritureComptable.getDerniereValeur()+
                "}";
        String finalTestToString = vSequenceEcritureComptable.toString();
        assertThat(testToString).isEqualTo(finalTestToString);
    }

}
