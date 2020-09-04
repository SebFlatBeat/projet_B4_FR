package com.dummy.myerp.model.bean.comptabilite;


/**
 * Bean représentant une séquence pour les références d'écriture comptable
 */
public class SequenceEcritureComptable {

    // ==================== Attributs ====================
    /**la référence du code du journal*/
    private String referenceJournalCode;
    /** L'année */
    private Integer annee;
    /** La dernière valeur utilisée */
    private Integer derniereValeur;

    // ==================== Constructeurs ====================
    /**
     * Constructeur
     */
    public SequenceEcritureComptable() {
    }

    /**
     * Constructeur
     *
     * @param pAnnee -
     * @param pDerniereValeur -
     * @param pReferenceJournalCode -
     */
    public SequenceEcritureComptable( String pReferenceJournalCode, Integer pAnnee, Integer pDerniereValeur) {
        referenceJournalCode = pReferenceJournalCode;
        annee = pAnnee;
        derniereValeur = pDerniereValeur;
    }


    // ==================== Getters/Setters ====================

    public String getReferenceJournalCode() {return referenceJournalCode;}
    public void setReferenceJournalCode(String referenceJournalCode) {this.referenceJournalCode = referenceJournalCode;}
    public Integer getAnnee() {
        return annee;
    }
    public void setAnnee(Integer pAnnee) {
        annee = pAnnee;
    }
    public Integer getDerniereValeur() {
        return derniereValeur;
    }
    public void setDerniereValeur(Integer pDerniereValeur) {
        derniereValeur = pDerniereValeur;
    }


    // ==================== Méthodes ====================
    @Override
    public String toString() {
        final StringBuilder vStB = new StringBuilder(this.getClass().getSimpleName());
        final String vSEP = ", ";
        vStB.append("{")
                .append("referenceJournalCode=").append(referenceJournalCode)
                .append("annee=").append(annee)
                .append(vSEP).append("derniereValeur=").append(derniereValeur)
                .append("}");
        return vStB.toString();
    }

}
