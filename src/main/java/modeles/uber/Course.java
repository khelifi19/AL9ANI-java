package modeles.uber;

import modeles.user.UserModel;

import java.time.LocalDateTime;

public class Course {
    private int id;
    private String destination;
    private String depart;
    private int nbPersonne;
    private LocalDateTime date;
    private double prix;

    public UserModel getUser() {
        return User;
    }

    public void setUser(UserModel user) {
        User = user;
    }

    private UserModel User;
    private Voiture voiture;
    // Constructeurs
    public Course() {
        // Constructeur par défaut
    }

    public Course(int id, String destination, String depart, int nbPersonne, LocalDateTime date, double prix,Voiture voiture,UserModel User) {
        this.id = id;
        this.destination = destination;
        this.depart = depart;
        this.nbPersonne = nbPersonne;
        this.date = date;
        this.prix = prix;
        this.voiture=voiture;
        this.User=User;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public int getNbPersonne() {
        return nbPersonne;
    }

    public void setNbPersonne(int nbPersonne) {
        this.nbPersonne = nbPersonne;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", destination='" + destination + '\'' +
                ", depart='" + depart + '\'' +
                ", nbPersonne=" + nbPersonne +
                ", date=" + date +
                ", prix=" + prix +
                '}';
    }

    public Voiture getVoiture() {
        return voiture;
    }

    public void setVoiture(Voiture voiture) {
        this.voiture = voiture;
    }
}
