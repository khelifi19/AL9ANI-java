package modeles.evenement;

import modeles.evenement.Evenement;

public class Pass {
    int id;
    private int prixPass;
    private String type;
    private Evenement evenement;

    public Pass(int id,int prixPass, String type,Evenement evenement) {
        this.prixPass = prixPass;
        this.type = type;
        this.id=id;
        this.evenement = evenement;

    }

    public Evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrixPass() {
        return prixPass;
    }

    public void setPrixPass(int prixPass) {
        this.prixPass = prixPass;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Override
    public String toString() {
        return "Type: " + type + " - Prix: " + prixPass + " - Événement: " + evenement.getNom();
    }
}
