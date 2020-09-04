package com.dummy.myerp.model.bean.comptabilite;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class LigneEcritureComptableTest {

    @Test
    public void test_toString_LigneEcritureComptable(){
        LigneEcritureComptable vLigneEcritureComptable = new LigneEcritureComptable();
        vLigneEcritureComptable.setLibelle("test toString");
        vLigneEcritureComptable.setDebit(new BigDecimal(511));
        vLigneEcritureComptable.setCredit(new BigDecimal(511));
        String testToString = "LigneEcritureComptable{compteComptable="+vLigneEcritureComptable.getCompteComptable()+", "
                +"libelle='"+vLigneEcritureComptable.getLibelle()+
                "', "+"debit="+vLigneEcritureComptable.getDebit()+
                ", "+"credit="+vLigneEcritureComptable.getCredit()+
                "}";
        String finalTestToString = vLigneEcritureComptable.toString();
        assertThat(testToString).isEqualTo(finalTestToString);
    }

}
