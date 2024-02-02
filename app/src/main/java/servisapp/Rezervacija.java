package servisapp;

public class Rezervacija {
    private int id;
    private String client;
    private String date;
    private String car;
    private String explain;

    // Empty constructor
    public Rezervacija() {
        // Default constructor
    }

    // Full constructor
    public Rezervacija(String client, String date, String car, String explain) {
        this.client = client;
        this.date = date;
        this.car = car;
        this.explain = explain;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    // toString method
    @Override
    public String toString() {
        return "Rezervacija{" +
                "client='" + client + '\'' +
                ", date='" + date + '\'' +
                ", car='" + car + '\'' +
                ", explain='" + explain + '\'' +
                '}';
    }
}
