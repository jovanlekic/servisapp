package servisapp;

public class Materijal {
    private int id; 
    private String imeDela;
    private String kataloskiBroj;
    private int kolicina;
    private int cenaDela;

    public Materijal() {
    }

    public Materijal(String imeDela, String kataloskiBroj, int kolicina, int cenaDela) {
        this.imeDela = imeDela;
        this.kataloskiBroj = kataloskiBroj;
        this.kolicina = kolicina;
        this.cenaDela = cenaDela;
    }

    @Override
    public String toString() {
        return "Materijal{" +
                "id=" + id +
                ", imeDela='" + imeDela + '\'' +
                ", kataloskiBroj='" + kataloskiBroj + '\'' +
                ", kolicina=" + kolicina +
                ", cenaDela=" + cenaDela +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImeDela() {
        return imeDela;
    }

    public void setImeDela(String imeDela) {
        this.imeDela = imeDela;
    }

    public String getKataloskiBroj() {
        return kataloskiBroj;
    }

    public void setKataloskiBroj(String kataloskiBroj) {
        this.kataloskiBroj = kataloskiBroj;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public int getCenaDela() {
        return cenaDela;
    }

    public void setCenaDela(int cenaDela) {
        this.cenaDela = cenaDela;
    }
}

