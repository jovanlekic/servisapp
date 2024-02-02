package servisapp;

import java.util.Date;
import java.util.List;

public class Report {
    private int id;
    private String model;
    private String vlasnik;
    private String registarskiBroj;
    private String telefon;
    private String brojSasije;
    private String brojMotora;
    private String boja;
    private String godiste;
    private String serviser;
    private Date datum;
    private int ruke;
    private int cenaDelova;
    private List<Materijal> materijali;
    private List<Stavka> stavke;

    public Report() {
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Report) {
            return ((Report)obj).brojSasije.equals(brojSasije) || ((Report)obj).brojMotora.equals(brojMotora) || ((Report)obj).registarskiBroj.equals(registarskiBroj) ;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Report {")
                .append("id=").append(id)
                .append(", model='").append(model).append('\'')
                .append(", vlasnik='").append(vlasnik).append('\'')
                .append(", registarskiBroj='").append(registarskiBroj).append('\'')
                .append(", telefon='").append(telefon).append('\'')
                .append(", brojSasije='").append(brojSasije).append('\'')
                .append(", brojMotora='").append(brojMotora).append('\'')
                .append(", boja='").append(boja).append('\'')
                .append(", godiste='").append(godiste).append('\'')
                .append(", serviser='").append(serviser).append('\'')
                .append(", datum=").append(datum)
                .append(", ruke=").append(ruke)
                .append(", cenaDelova=").append(cenaDelova)
                .append('}');
        for(Materijal m: materijali) {
            stringBuilder.append("[").append(m).append("]");
        }
        for(Stavka s: stavke) {
            stringBuilder.append("[").append(s).append("]");
        }
        return stringBuilder.toString();
    }

    // Getters and setters for the attributes

    public String getVlasnik() {
        return vlasnik;
    }

    public void setVlasnik(String vlasnik) {
        this.vlasnik = vlasnik;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Materijal> getMaterijali() {
        return materijali;
    }

    public List<Stavka> getStavke() {
        return stavke;
    }
    
    public void setMaterijali(List<Materijal> materijali) {
        this.materijali = materijali;
    }

    public void setStavke(List<Stavka> stavke) {
        this.stavke = stavke;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistarskiBroj() {
        return registarskiBroj;
    }

    public void setRegistarskiBroj(String registarskiBroj) {
        this.registarskiBroj = registarskiBroj;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getBrojSasije() {
        return brojSasije;
    }

    public void setBrojSasije(String brojSasije) {
        this.brojSasije = brojSasije;
    }

    public String getBrojMotora() {
        return brojMotora;
    }

    public void setBrojMotora(String brojMotora) {
        this.brojMotora = brojMotora;
    }

    public String getBoja() {
        return boja;
    }

    public void setBoja(String boja) {
        this.boja = boja;
    }

    public String getGodiste() {
        return godiste;
    }

    public void setGodiste(String godiste) {
        this.godiste = godiste;
    }

    public String getServiser() {
        return serviser;
    }

    public void setServiser(String serviser) {
        this.serviser = serviser;
    }

    public int getRuke() {
        return ruke;
    }

    public void setRuke(int ruke) {
        this.ruke = ruke;
    }

    public int getCenaDelova() {
        return cenaDelova;
    }

    public void setCenaDelova(int cenaDelova) {
        this.cenaDelova = cenaDelova;
    }
}
