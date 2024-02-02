package servisapp;

public class Stavka {
    private int id; // New field
    private String imeStavke;
    private int brojSati;

    // Empty constructor
    public Stavka() {
    }

    // Full constructor
    public Stavka(String imeStavke, int cenaStavke, int brojSati) {
        this.imeStavke = imeStavke;
        this.brojSati = brojSati;
    }

    @Override
    public String toString() {
        return "Stavka{" +
                "id=" + id +
                ", imeStavke='" + imeStavke + '\'' +
                ", brojSati=" + brojSati +
                '}';
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImeStavke() {
        return imeStavke;
    }

    public void setImeStavke(String imeStavke) {
        this.imeStavke = imeStavke;
    }

    public int getBrojSati() {
        return brojSati;
    }

    public void setBrojSati(int brojSati) {
        this.brojSati = brojSati;
    }
}

