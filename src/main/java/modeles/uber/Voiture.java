package modeles;

import java.util.List;

public class Voiture {
    private int id;
    private String matricule;
    private String modele;
    private boolean disponibilite;
    private List<Course> courses;
    private Chauffeur chauffeur;
    // Constructeurs
    public Voiture() {
        // Constructeur par défaut
    }

    public Voiture(int id, String matricule, String modele, boolean disponibilite, Chauffeur chauffeur) {
        this.id = id;
        this.matricule = matricule;
        this.modele = modele;
        this.disponibilite = disponibilite;
        this.chauffeur = chauffeur;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public boolean isDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(boolean disponibilite) {
        this.disponibilite = disponibilite;
    }

    @Override
    public String toString() {
        return "Voiture{" +
                "id=" + id +
                ", matricule='" + matricule + '\'' +
                ", modele='" + modele + '\'' +
                ", disponibilite=" + disponibilite +
                '}';
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }


    public Chauffeur getChauffeur() {
        return chauffeur;
    }

    public void setChauffeur(Chauffeur chauffeur) {
        this.chauffeur = chauffeur;
    }
}

