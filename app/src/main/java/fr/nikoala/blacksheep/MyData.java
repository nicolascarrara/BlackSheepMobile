package fr.nikoala.blacksheep;

/**
 * Created by nicolascarrara on 13/05/2018.
 */

public class MyData {
    private int id;
    private String titre;
    private String synopsys;
    private float note;
    private String date_sortie;
    private String image;

    public MyData(int id, String titre, String synopsys, float note, String date_sortie, String image) {
        this.id = id;
        this.titre = titre;
        this.synopsys = synopsys;
        this.note = note;
        this.date_sortie = date_sortie;
        this.image = image;
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

    public String getSynopsys() {
        return synopsys;
    }

    public void setSynopsys(String synopsys) {
        this.synopsys = synopsys;
    }

    public float getNote() {
        return note;
    }

    public void setNote(float note) {
        this.note = note;
    }

    public String getDate_sortie() {
        return date_sortie;
    }

    public void setDate_sortie(String date_sortie) {
        this.date_sortie = date_sortie;
    }

    public String getImage() {
        String img = "https://image.tmdb.org/t/p/w300"+image;
        return img;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
