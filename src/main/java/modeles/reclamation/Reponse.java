package modeles.reclamation;

public class Reponse {
    private int id;
    private int reclamationId;
    private String text;

    // Empty constructor
    public Reponse() {
    }

    // Full parameter constructor
    public Reponse(int id, int reclamationId, String text) {
        this.id = id;
        this.reclamationId = reclamationId;
        this.text = text;
    }

    // Constructor without id
    public Reponse(int reclamationId, String text) {
        this.reclamationId = reclamationId;
        this.text = text;
    }

    // Constructor with only id
    public Reponse(int id) {
        this.id = id;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReclamationId() {
        return reclamationId;
    }

    public void setReclamationId(int reclamationId) {
        this.reclamationId = reclamationId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    // toString method
    @Override
    public String toString() {
        return "Reponse{" +
                "id=" + id +
                ", reclamationId=" + reclamationId +
                ", text='" + text + '\'' +
                '}';
    }
}
