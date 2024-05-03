package modeles.reclamation;

public class Reclamation {
    private int id;
    private int userId;
    private String objet;
    private String text;
    private int etat;

    // Empty constructor
    public Reclamation() {
    }

    // Constructor without id
    public Reclamation(int userId, String objet, String text, int etat) {
        this.userId = userId;
        this.objet = objet;
        this.text = text;
        this.etat = etat;
    }

    // Full parameter constructor
    public Reclamation(int id, int userId, String objet, String text, int etat) {
        this.id = id;
        this.userId = userId;
        this.objet = objet;
        this.text = text;
        this.etat = etat;
    }

    // Constructor with only id
    public Reclamation(int id) {
        this.id = id;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    // toString method
    @Override
    public String toString() {
        return
                "id=" + id +
                ", userId=" + userId +
                ", objet='" + objet + '\'' +
                ", text='" + text + '\'' +
                ", etat=" + etat
                ;
    }
}
