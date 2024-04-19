package modeles;

import java.util.List;

public class Chauffeur {
    private int id;
    private String nom;
    private String prenom;
    private String ville;
    private double salaire;
    private int age;
    private String numero;
    private List<Voiture> voitures;

    // Constructeur par défaut
    public Chauffeur() {
    }

    // Constructeur avec tous les attributs
    public Chauffeur(int id, String nom, String prenom, int age, String ville, String numero, double salaire) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.ville = ville;
        this.numero = numero;
        this.salaire = salaire;
    }


    // Getters et Setters pour tous les attributs
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public double getSalaire() {
        return salaire;
    }

    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    // Méthode toString pour l'affichage des informations du chauffeur
    @Override
    public String toString() {
        return "Chauffeur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", ville='" + ville + '\'' +
                ", salaire=" + salaire +
                ", age=" + age +
                ", numero='" + numero + '\'' +
                '}';
    }

    public List<Voiture> getVoitures() {
        return voitures;
    }

    public void setVoitures(List<Voiture> voitures) {
        this.voitures = voitures;
    }
}

