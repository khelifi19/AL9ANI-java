package modeles.evenement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Evenement {
    private int id;
    private String nom;
    private String type;
    private int participants;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private List<models.Pass> passes;

    public Evenement(int id, String nom, String type, int participants, LocalDate dateDebut, LocalDate dateFin) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.participants = participants;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.passes = new ArrayList<>();
        int user_id;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public List<models.Pass> getPasses() {
        return passes;
    }

    public void setPasses(List<models.Pass> passes) {
        this.passes = passes;
    }

    // Method to add a pass to the event
    public void addPass(models.Pass pass) {
        passes.add(pass);
    }

    // Method to remove a pass from the event
    public void removePass(models.Pass pass) {
        passes.remove(pass);
    }
}
