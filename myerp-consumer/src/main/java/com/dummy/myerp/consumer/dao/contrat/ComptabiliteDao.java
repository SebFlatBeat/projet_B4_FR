package com.dummy.myerp.consumer.dao.contrat;

import java.util.List;

import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.NotFoundException;


/**
 * Interface de DAO des objets du package Comptabilite
 */
public interface ComptabiliteDao {

    /**
     * Renvoie la liste des Comptes Comptables
     * @return {@link List}
     */
    List<CompteComptable> getListCompteComptable();


    /**
     * Renvoie la liste des Journaux Comptables
     * @return {@link List}
     */
    List<JournalComptable> getListJournalComptable();


    // ==================== EcritureComptable ====================

    /**
     * Renvoie la liste des Écritures Comptables
     * @return {@link List}
     */
    List<EcritureComptable> getListEcritureComptable();

    /**
     * Renvoie l'Écriture Comptable d'id {@code pId}.
     *
     * @param pId l'id de l'écriture comptable
     * @return {@link EcritureComptable}
     * @throws NotFoundException : Si l'écriture comptable n'est pas trouvée
     */
    EcritureComptable getEcritureComptable(Integer pId);

    /**
     * Renvoie l'Écriture Comptable de référence {@code pRef}.
     *
     * @param pReference la référence de l'écriture comptable
     * @return {@link EcritureComptable}
     * @throws NotFoundException : Si l'écriture comptable n'est pas trouvée
     */
    EcritureComptable getEcritureComptableByRef(String pReference) throws NotFoundException;

    /**
     * Charge la liste des lignes d'écriture de l'écriture comptable {@code pEcritureComptable}
     *
     * @param pEcritureComptable -
     */
    void loadListLigneEcriture(EcritureComptable pEcritureComptable);

    /**
     * Insert une nouvelle écriture comptable.
     *
     * @param pEcritureComptable -
     */
    void insertEcritureComptable(EcritureComptable pEcritureComptable);

    /**
     * Met à jour l'écriture comptable.
     *
     * @param pEcritureComptable -
     */
    void updateEcritureComptable(EcritureComptable pEcritureComptable);

    /**
     * Supprime l'écriture comptable d'id {@code pId}.
     *
     * @param pId l'id de l'écriture
     */
    void deleteEcritureComptable(Integer pId);

    // ==================== SequenceEcritureComptable ====================

    /**
     * Renvoie la liste des Sequences d'Ecritures Comptable
     *
     * @return {@link List}
     */
    List<SequenceEcritureComptable> getListSequenceEcritureComptable();

    /**
     * Renvoie la liste des Sequences d'Ecritures Comptable avec l'année en argument
     *
     * @return {@link List}
     */
    List<SequenceEcritureComptable> getListSequenceEcritureComptable(Integer pAnnee);

    /**
     * Insert une nouvelle Sequence d'Ecriture Comptable
     *
     * @param pSequenceEcritureComptable sequence de l'ecriture comptable
     */
    void insertSequenceEcritureComptable(SequenceEcritureComptable pSequenceEcritureComptable);

    /**
     * Met à jour la Sequence d'Ecriture Comptable
     *
     * @param pSequenceEcritureComptable sequence de l'ecriture comptable
     */
    void updateSequenceEcritureComptable(SequenceEcritureComptable pSequenceEcritureComptable);

    /**
     * Supprime une Sequence d'Ecriture Comptable
     *
     * @param pSequenceEcritureComptable sequence de l'ecriture comptable
     */
    void deleteSequenceEcritureComptable(SequenceEcritureComptable pSequenceEcritureComptable);

}
