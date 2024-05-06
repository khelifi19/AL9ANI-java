package modeles.etaAct;


import java.time.LocalDate;

public class Post {

    private int id;
    private String titre;
    private String description;
    private String localisation;
    private LocalDate date;
    private String image;

    public Post() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String toString() {
        return titre;
    }

    public String allAttrToString() {
        return " - Post - " +
                "\n id : " + id +
                "\n titre : " + titre +
                "\n description : " + description +
                "\n localisation : " + localisation +
                "\n date : " + date;
    }
}