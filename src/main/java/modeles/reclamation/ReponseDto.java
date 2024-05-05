package modeles.reclamation;

public class ReponseDto {

    private int id ;
    private String objet;
    private String textrespons;

    //generate empty construct , parameter construct , and getter ans setters

    public ReponseDto(int id, String objet, String textrespons) {
        this.id = id;
        this.objet = objet;
        this.textrespons = textrespons;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getTextrespons() {
        return textrespons;
    }

    public void setTextrespons(String textrespons) {
        this.textrespons = textrespons;
    }

    public ReponseDto() {
    }

    @Override
    public String toString() {
        return "ReponseDto{" +
                "id=" + id +
                ", objet='" + objet + '\'' +
                ", textrespons='" + textrespons + '\'' +
                '}';
    }
}
