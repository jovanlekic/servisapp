package servisapp;

public class Plata {
    private int id; 
    private String mesec;
    private int plata;

    // Empty constructor
    public Plata() {
    }

    // Full constructor
    public Plata(String month, int plata) {
        this.mesec = month;
        this.plata = plata;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMesec() {
        return mesec;
    }

    public void setMesec(String mesec) {
        this.mesec = mesec;
    }

    public int getPlata() {
        return plata;
    }

    public void setPlata(int plata) {
        this.plata = plata;
    }
}
