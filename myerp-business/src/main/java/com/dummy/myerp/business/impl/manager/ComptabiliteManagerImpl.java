package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.dummy.myerp.model.bean.comptabilite.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.TransactionStatus;
import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;

import static java.util.Calendar.*;


/**
 * Comptabilite manager implementation.
 */
public class ComptabiliteManagerImpl extends AbstractBusinessManager implements ComptabiliteManager {

    // ==================== Attributs ====================


    // ==================== Constructeurs ====================
    /**
     * Instantiates a new Comptabilite manager.
     */
    public ComptabiliteManagerImpl() {
    }


    // ==================== Getters/Setters ====================
    @Override
    public List<CompteComptable> getListCompteComptable() {
        return getDaoProxy().getComptabiliteDao().getListCompteComptable();
    }


    @Override
    public List<JournalComptable> getListJournalComptable() {
        return getDaoProxy().getComptabiliteDao().getListJournalComptable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EcritureComptable> getListEcritureComptable() {
        return getDaoProxy().getComptabiliteDao().getListEcritureComptable();
    }

    @Override
    public List<SequenceEcritureComptable> getListSequenceEcritureComptable() {
        return getDaoProxy().getComptabiliteDao().getListSequenceEcritureComptable();
    }

    @Override
    public List<SequenceEcritureComptable> getListSequenceEcritureComptable(Integer pAnnee){
        return getDaoProxy().getComptabiliteDao().getListSequenceEcritureComptable(pAnnee);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void addReference(EcritureComptable pEcritureComptable) {
        /* Le principe :
                1.  Remonter depuis la persitance la dernière valeur de la séquence du journal pour l'année de l'écriture
                    (table sequence_ecriture_comptable)*/
        Date ecritureComptableDate = pEcritureComptable.getDate();
        Calendar calendar = getInstance();
        calendar.setTime(ecritureComptableDate);
        int anneeEcritureComptable = calendar.get(YEAR);

        List<SequenceEcritureComptable> sequenceEcritureComptableList = this.getListSequenceEcritureComptable(anneeEcritureComptable);
               /* 2.  * S'il n'y a aucun enregistrement pour le journal pour l'année concernée :
                        1. Utiliser le numéro 1.
                    * Sinon :
                        1. Utiliser la dernière valeur + 1*/
        Integer valeurSequenceInteger;
        String valeurSequence = "";
        List<JournalComptable> journalComptableList = getListJournalComptable();
        if (sequenceEcritureComptableList.isEmpty()){
            valeurSequence = "00001";
        }else{
            for (int i = 0; i<sequenceEcritureComptableList.size();i++)
            {if (pEcritureComptable.getJournal().getCode().equals(sequenceEcritureComptableList.get(i).getReferenceJournalCode())){
                valeurSequenceInteger = sequenceEcritureComptableList.get(i).getDerniereValeur() + 1;
                valeurSequence = "0000"+valeurSequenceInteger;
            }
            }
        }
        /* 3.  Mettre à jour la référence de l'écriture avec la référence calculée (RG_Compta_5)*/
        String reference = pEcritureComptable.getJournal().getCode()+"-" +anneeEcritureComptable+"/"+valeurSequence;
        pEcritureComptable.setReference(reference);

              /*
                4.  Enregistrer (insert/update) la valeur de la séquence en persitance
                    (table sequence_ecriture_comptable)
         */
        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable();
        sequenceEcritureComptable.setAnnee(anneeEcritureComptable);
        sequenceEcritureComptable.setReferenceJournalCode(pEcritureComptable.getJournal().getCode());
        if (valeurSequence.equals("00001")) {
            Integer integerSeq1 = Integer.parseInt(valeurSequence);
            sequenceEcritureComptable.setDerniereValeur(integerSeq1);
            insertSequenceEcritureComptable(sequenceEcritureComptable);
        }else {
            Integer integerNewSeq = Integer.parseInt(valeurSequence);
            sequenceEcritureComptable.setDerniereValeur(integerNewSeq);
            updateSequenceEcritureComptable(sequenceEcritureComptable);
        }
    }

    /**
     *
     * @param sequenceEcritureComptable
     * @return reference
     */
    public String reference(SequenceEcritureComptable sequenceEcritureComptable){
        String reference;
        reference = sequenceEcritureComptable.getReferenceJournalCode()+ "-"
                + sequenceEcritureComptable.getAnnee() + "/"
                + String.format("%05d", sequenceEcritureComptable.getDerniereValeur() );
        return  reference;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptableUnit(pEcritureComptable);
        this.checkEcritureComptableContext(pEcritureComptable);
    }


    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion unitaires,
     * c'est à dire indépendemment du contexte (unicité de la référence, exercie comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */

    protected void checkEcritureComptableUnit(EcritureComptable pEcritureComptable) throws FunctionalException {

        // ===== RG_Compta_3 : une écriture comptable doit avoir au moins 2 lignes d'écriture (1 au débit, 1 au crédit)
        int vNbrCredit = 0;
        int vNbrDebit = 0;
        for (LigneEcritureComptable vLigneEcritureComptable : pEcritureComptable.getListLigneEcriture()) {
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getCredit(), BigDecimal.ZERO)) != 0) {
                vNbrCredit++;
            }
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getDebit(),BigDecimal.ZERO)) != 0) {
                vNbrDebit++;
            }
        }
        // On test le nombre de lignes car si l'écriture à une seule ligne
        //      avec un montant au débit et un montant au crédit ce n'est pas valable
        if (pEcritureComptable.getListLigneEcriture().size() < 2
                || vNbrCredit < 1
                || vNbrDebit < 1) {
            throw new FunctionalException(
                    "L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");
        }

        // ===== Vérification des contraintes unitaires sur les attributs de l'écriture
        Set<ConstraintViolation<EcritureComptable>> vViolations = getConstraintValidator().validate(pEcritureComptable);
        if (!vViolations.isEmpty()) {
            throw new FunctionalException("L'écriture comptable ne respecte pas les règles de gestion.",
                    new ConstraintViolationException(
                            "L'écriture comptable ne respecte pas les contraintes de validation",
                            vViolations));
        }

        // ===== RG_Compta_2 : Pour qu'une écriture comptable soit valide, elle doit être équilibrée
        if (!pEcritureComptable.isEquilibree()) {
            throw new FunctionalException("L'écriture comptable n'est pas équilibrée.");
        }

        // RG_Compta_5 : vérifier que l'année dans la référence correspond bien à la date de l'écriture, idem pour le code journal...
        Date date = pEcritureComptable.getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String valueOfDate = String.valueOf(calendar.get(YEAR));
        if (!valueOfDate.equals(pEcritureComptable.getReference().substring(3,7))){
            throw new FunctionalException("l'année de l'écriture comptable n'est pas conforme");
        }else if (!pEcritureComptable.getJournal().getCode().equals(pEcritureComptable.getReference().substring(0,2))){
            throw new FunctionalException("le code de référence comptable n'est pas conforme ");
        }
    }


    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion liées au contexte
     * (unicité de la référence, année comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    protected void checkEcritureComptableContext(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== RG_Compta_6 : La référence d'une écriture comptable doit être unique
        if (StringUtils.isNoneEmpty(pEcritureComptable.getReference())) {
            try {
                // Recherche d'une écriture ayant la même référence
                EcritureComptable vECRef = getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(pEcritureComptable.getReference());

                // Si l'écriture à vérifier est une nouvelle écriture (id == null),
                // ou si elle ne correspond pas à l'écriture trouvée (id != idECRef),
                // c'est qu'il y a déjà une autre écriture avec la même référence
                if (pEcritureComptable.getId() == null
                        || !pEcritureComptable.getId().equals(vECRef.getId())) {
                    throw new FunctionalException("Une autre écriture comptable existe déjà avec la même référence.");
                }
            } catch (NotFoundException vEx) {
                // Dans ce cas, c'est bon, ça veut dire qu'on n'a aucune autre écriture avec la même référence.
            }
        }
    }

    /**
     *
     *
     * @param amount
     * @return
     */
    public boolean isAmountExist(BigDecimal amount){
        return ( BigDecimal.ZERO.compareTo( ObjectUtils.defaultIfNull(amount, BigDecimal.ZERO )  ) != 0 );
    }

    /**
     * RG_Compta_3 : une écriture comptable doit avoir au moins 2 lignes d'écriture (1 au débit, 1 au crédit)
     *
     * @param pEcritureComptable
     * @return
     */
    public boolean isNumberValidEcritureComptable(EcritureComptable pEcritureComptable) {

        if(pEcritureComptable.getListLigneEcriture().size() < 2) return false;

        int vNbrCredit = 0;
        int vNbrDebit = 0;

        for (LigneEcritureComptable vLigneEcritureComptable : pEcritureComptable.getListLigneEcriture()) {

            if(isAmountExist(vLigneEcritureComptable.getCredit()))
                vNbrCredit++;

            if(isAmountExist(vLigneEcritureComptable.getDebit()))
                vNbrDebit++;
        }

        return (vNbrCredit>0 && vNbrDebit>0);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptable(pEcritureComptable);
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().insertEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptable(pEcritureComptable);
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEcritureComptable(Integer pId) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().deleteEcritureComptable(pId);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    @Override
    public void insertSequenceEcritureComptable(SequenceEcritureComptable pSequenceEcritureComptable) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().insertSequenceEcritureComptable(pSequenceEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    @Override
    public void updateSequenceEcritureComptable(SequenceEcritureComptable pSequenceEcritureComptable) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().updateSequenceEcritureComptable(pSequenceEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    @Override
    public void deleteSequenceEcritureComptable(SequenceEcritureComptable pSequenceEcritureComptable) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().deleteSequenceEcritureComptable(pSequenceEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }
}
