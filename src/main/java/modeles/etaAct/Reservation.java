package modeles.etaAct;


import java.time.LocalDate;

public class Reservation implements Comparable<Reservation> {

    private int id;
    private LocalDate date;
    private int nombreDePersonnes;

    private Etablissement etablissement;


    public Reservation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getNombreDePersonnes() {
        return nombreDePersonnes;
    }

    public void setNombreDePersonnes(int nombreDePersonnes) {
        this.nombreDePersonnes = nombreDePersonnes;
    }

    public Etablissement getEtablissement() {
        return etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

    public String toString() {
        return etablissement.toString() + " - " + date + " - " + nombreDePersonnes + " personnes";
    }

    public static String compareVar = "";

    public int compareTo(Reservation reservation) {
        return switch (compareVar) {
            case "Tri par date" -> reservation.getDate().compareTo(this.getDate());
            case "Tri par nombre de personnes" -> reservation.getNombreDePersonnes() - this.getNombreDePersonnes();
            case "Tri par etablissement" ->
                    reservation.getEtablissement().toString().compareToIgnoreCase(this.etablissement.toString());
            default -> 0;
        };
    }
}