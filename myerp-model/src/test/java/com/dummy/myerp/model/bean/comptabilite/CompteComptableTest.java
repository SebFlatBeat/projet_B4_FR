package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CompteComptableTest {

    private Integer numero;

    @Test
    public void getByNumero(){
        List<CompteComptable> compteComptableList = new ArrayList<CompteComptable>();
        compteComptableList.add(new CompteComptable(11223344,"Compte courant"));
        Assert.assertNotNull(CompteComptable.getByNumero(compteComptableList,11223344));
        Assert.assertNull(CompteComptable.getByNumero(compteComptableList,11223345));
    }

    @Test
    public void test_toString_CompteComptable(){
        CompteComptable vCompteComptable = new CompteComptable();
        vCompteComptable.setLibelle("test toString");
        vCompteComptable.setNumero(11223345);
        String testToString = "CompteComptable{numero="+vCompteComptable.getNumero()+", "
                +"libelle="+vCompteComptable.getLibelle()+"'}";
        String finalTestToString = vCompteComptable.toString();
        assertThat(testToString).isEqualTo(finalTestToString);
    }
}
