package servisapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DB {

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static final String DB_URL = "jdbc:h2:./test"; // Use a file-based database
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    public static void initTables() {
        try (Connection connection = DB.getConnection();
                Statement statement = connection.createStatement()) {

            // Create table
            String createPlataTableSQL = "CREATE TABLE IF NOT EXISTS Plata ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "mesec VARCHAR(255), "
                    + "plata INT)";

            String createMaterijalTableSQL = "CREATE TABLE IF NOT EXISTS Materijal ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "imeDela VARCHAR(255), "
                    + "kataloskiBroj VARCHAR(255), "
                    + "kolicina INT, "
                    + "cenaDela INT, "
                    + "report_id INT, "
                    + "FOREIGN KEY (report_id) REFERENCES Report(id))";

            String createStavkaTableSQL = "CREATE TABLE IF NOT EXISTS Stavka ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "imeStavke VARCHAR(255), "
                    + "brojSati INT, "
                    + "report_id INT, "
                    + "FOREIGN KEY (report_id) REFERENCES Report(id))";

            String createReportTableSQL = "CREATE TABLE IF NOT EXISTS Report ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "model VARCHAR(255), "
                    + "vlasnik VARCHAR(255), "
                    + "registarskiBroj VARCHAR(255), "
                    + "telefon VARCHAR(255), "
                    + "brojSasije VARCHAR(255), "
                    + "brojMotora VARCHAR(255), "
                    + "boja VARCHAR(255), "
                    + "godiste VARCHAR(255), "
                    + "serviser VARCHAR(255), "
                    + "datum DATE, "
                    + "ruke INT, "
                    + "cenaDelova INT)";

            String createRezervacijaTableSQL = "CREATE TABLE IF NOT EXISTS Rezervacija ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "client VARCHAR(255), "
                    + "date VARCHAR(255), "
                    + "car VARCHAR(255), "
                    + "explain VARCHAR(255))";

            statement.execute(createReportTableSQL);
            statement.execute(createPlataTableSQL);
            statement.execute(createMaterijalTableSQL);
            statement.execute(createStavkaTableSQL);
            statement.execute(createRezervacijaTableSQL);

            // Perform other database operations...

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void dropTables() {
        try (Connection connection = DB.getConnection();
                Statement statement = connection.createStatement()) {

            // Drop tables
            String dropPlataTableSQL = "DROP TABLE IF EXISTS Plata";
            String dropMaterijalTableSQL = "DROP TABLE IF EXISTS Materijal";
            String dropStavkaTableSQL = "DROP TABLE IF EXISTS Stavka";
            String dropReportTableSQL = "DROP TABLE IF EXISTS Report";
            String dropRezervacijaTableSQL = "DROP TABLE IF EXISTS Rezervacija";

            statement.executeUpdate(dropPlataTableSQL);
            statement.executeUpdate(dropMaterijalTableSQL);
            statement.executeUpdate(dropStavkaTableSQL);
            statement.executeUpdate(dropReportTableSQL);
            statement.executeUpdate(dropRezervacijaTableSQL);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Rezervacija> readAllRezervacija() {
        List<Rezervacija> rezervacije = new ArrayList<>();

        try (Connection connection = DB.getConnection();
                Statement statement = connection.createStatement()) {

            String selectAllRezervacijaSQL = "SELECT * FROM Rezervacija";

            try (ResultSet resultSet = statement.executeQuery(selectAllRezervacijaSQL)) {
                while (resultSet.next()) {
                    Rezervacija rezervacija = new Rezervacija();
                    rezervacija.setId(resultSet.getInt("id"));
                    rezervacija.setClient(resultSet.getString("client"));
                    rezervacija.setDate(resultSet.getString("date"));
                    rezervacija.setCar(resultSet.getString("car"));
                    rezervacija.setExplain(resultSet.getString("explain"));
                    rezervacije.add(rezervacija);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rezervacije;
    }

    public static void insertRezervacija(Rezervacija rezervacija) {
        try (Connection connection = DB.getConnection()) {
            String insertRezervacijaSQL = "INSERT INTO Rezervacija (client, date, car, explain) VALUES (?, ?, ?, ?)";
            try (PreparedStatement insertRezervacijaStatement = connection.prepareStatement(insertRezervacijaSQL)) {
                insertRezervacijaStatement.setString(1, rezervacija.getClient());
                insertRezervacijaStatement.setString(2, rezervacija.getDate());
                insertRezervacijaStatement.setString(3, rezervacija.getCar());
                insertRezervacijaStatement.setString(4, rezervacija.getExplain());

                insertRezervacijaStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteRezervacija(int rezervacijaId) {
        try (Connection connection = DB.getConnection()) {
            String deleteRezervacijaSQL = "DELETE FROM Rezervacija WHERE id = ?";
            try (PreparedStatement deleteRezervacijaStatement = connection.prepareStatement(deleteRezervacijaSQL)) {
                deleteRezervacijaStatement.setInt(1, rezervacijaId);
                deleteRezervacijaStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Plata readPlata(String mesec) {
        Plata plata = null;

        try (Connection connection = DB.getConnection()) {
            String selectPlataSQL = "SELECT * FROM Plata WHERE mesec = ?";
            try (PreparedStatement selectPlataStatement = connection.prepareStatement(selectPlataSQL)) {
                selectPlataStatement.setString(1, mesec);

                try (ResultSet resultSet = selectPlataStatement.executeQuery()) {
                    if (resultSet.next()) {
                        plata = new Plata();
                        plata.setId(resultSet.getInt("id"));
                        plata.setMesec(resultSet.getString("mesec"));
                        plata.setPlata(resultSet.getInt("plata"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return plata;
    }

    public static void insertPlata(Plata plata) {
        try (Connection connection = DB.getConnection()) {
            String insertPlataSQL = "INSERT INTO Plata (mesec, plata) VALUES (?, ?)";
            try (PreparedStatement plataStatement = connection.prepareStatement(insertPlataSQL)) {
                // System.out.println("INSERT PLATA" + plata.getMesec());
                plataStatement.setString(1, plata.getMesec());
                plataStatement.setInt(2, plata.getPlata());
                plataStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updatePlata(String mesec, int newPlata) {
        try (Connection connection = DB.getConnection()) {
            String updatePlataSQL = "UPDATE Plata SET plata = ? WHERE mesec = ?";
            try (PreparedStatement updatePlataStatement = connection.prepareStatement(updatePlataSQL)) {
                updatePlataStatement.setInt(1, newPlata);
                updatePlataStatement.setString(2, mesec);
                updatePlataStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int insertReport(Report report) {
        try (Connection connection = DB.getConnection()) {
            connection.setAutoCommit(false);

            // System.out.println("INSERT" + report);

            int cenaDelova = 0;
            for (Materijal materijal : report.getMaterijali()) {
                cenaDelova += materijal.getCenaDela() * materijal.getKolicina();
            }

            // Insert Report
            String insertReportSQL = "INSERT INTO Report "
                    + "(model, vlasnik, registarskiBroj, telefon, brojSasije, brojMotora, boja, godiste, serviser, datum, ruke, cenaDelova) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement reportStatement = connection.prepareStatement(insertReportSQL)) {
                reportStatement.setString(1, report.getModel());
                reportStatement.setString(2, report.getVlasnik());
                reportStatement.setString(3, report.getRegistarskiBroj());
                reportStatement.setString(4, report.getTelefon());
                reportStatement.setString(5, report.getBrojSasije());
                reportStatement.setString(6, report.getBrojMotora());
                reportStatement.setString(7, report.getBoja());
                reportStatement.setString(8, report.getGodiste());
                reportStatement.setString(9, report.getServiser());
                reportStatement.setDate(10, new java.sql.Date(report.getDatum().getTime()));
                reportStatement.setInt(11, report.getRuke());
                reportStatement.setInt(12, cenaDelova);

                reportStatement.executeUpdate();
            }

            // Get the generated ID of the inserted report
            int reportId;
            try (PreparedStatement idStatement = connection.prepareStatement("SELECT IDENTITY()")) {
                ResultSet resultSet = idStatement.executeQuery();
                resultSet.next();
                reportId = resultSet.getInt(1);
            }

            String insertMaterijalSQL = "INSERT INTO Materijal (imeDela, kataloskiBroj, kolicina, cenaDela, report_id) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement materijalStatement = connection.prepareStatement(insertMaterijalSQL)) {
                for (Materijal materijal : report.getMaterijali()) {
                    // System.out.println("INSERT" + materijal.getCenaDela());
                    materijalStatement.setString(1, materijal.getImeDela());
                    materijalStatement.setString(2, materijal.getKataloskiBroj());
                    materijalStatement.setInt(3, materijal.getKolicina());
                    materijalStatement.setInt(4, materijal.getCenaDela());
                    materijalStatement.setInt(5, reportId);
                    materijalStatement.executeUpdate();
                }
            }

            // Insert Stavke
            String insertStavkaSQL = "INSERT INTO Stavka (imeStavke, brojSati, report_id) VALUES (?, ?, ?)";
            try (PreparedStatement stavkaStatement = connection.prepareStatement(insertStavkaSQL)) {
                for (Stavka stavka : report.getStavke()) {
                    stavkaStatement.setString(1, stavka.getImeStavke());
                    stavkaStatement.setInt(2, stavka.getBrojSati());
                    stavkaStatement.setInt(3, reportId);
                    stavkaStatement.executeUpdate();
                }
            }

            connection.commit();
            return reportId;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static List<Report> readAllReports() {
        List<Report> reports = new ArrayList<>();

        try (Connection connection = DB.getConnection()) {
            String selectAllReportsSQL = "SELECT * FROM Report";

            try (PreparedStatement allReportsStatement = connection.prepareStatement(selectAllReportsSQL);
                    ResultSet resultSet = allReportsStatement.executeQuery()) {

                while (resultSet.next()) {
                    Report report = new Report();
                    report.setId(resultSet.getInt("id"));
                    report.setModel(resultSet.getString("model"));
                    report.setVlasnik(resultSet.getString("vlasnik"));
                    report.setRegistarskiBroj(resultSet.getString("registarskiBroj"));
                    report.setTelefon(resultSet.getString("telefon"));
                    report.setBrojSasije(resultSet.getString("brojSasije"));
                    report.setBrojMotora(resultSet.getString("brojMotora"));
                    report.setBoja(resultSet.getString("boja"));
                    report.setGodiste(resultSet.getString("godiste"));
                    report.setServiser(resultSet.getString("serviser"));
                    report.setDatum(resultSet.getDate("datum"));
                    report.setRuke(resultSet.getInt("ruke"));
                    report.setCenaDelova(resultSet.getInt("cenaDelova"));

                    int reportId = report.getId();

                    // Read associated Materijal
                    List<Materijal> materijali = readMaterijalForReport(connection, reportId);
                    report.setMaterijali(materijali);

                    // Read associated Stavka
                    List<Stavka> stavke = readStavkaForReport(connection, reportId);
                    report.setStavke(stavke);

                    reports.add(report);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reports;
    }

    public static List<Report> readReport(String vlasnik, String firstWordModel, String secondWordModel,
            String registarskiBroj, String brojSasije) {
        List<Report> reports = new ArrayList<>();

        try (Connection connection = DB.getConnection()) {
            StringBuilder selectReportSQL = new StringBuilder("SELECT * FROM Report WHERE 1=1");

            if (vlasnik != null && !vlasnik.isEmpty()) {
                selectReportSQL.append(" AND vlasnik ilike ?");
            }

            if (firstWordModel != null && !firstWordModel.isEmpty()) {
                selectReportSQL.append(" AND model ilike ?");
            }

            if (secondWordModel != null && !secondWordModel.isEmpty()) {
                selectReportSQL.append(" AND model ilike ?");
            }

            if (registarskiBroj != null && !registarskiBroj.isEmpty()) {
                selectReportSQL.append(" AND registarskiBroj ilike ?");
            }

            if (brojSasije != null && !brojSasije.isEmpty()) {
                selectReportSQL.append(" AND brojSasije ilike ?");
            }
            // System.out.println(selectReportSQL.toString());
            try (PreparedStatement reportStatement = connection.prepareStatement(selectReportSQL.toString())) {
                int parameterIndex = 1;

                if (vlasnik != null && !vlasnik.isEmpty()) {
                    reportStatement.setString(parameterIndex++, "%" + vlasnik + "%");
                }

                if (firstWordModel != null && !firstWordModel.isEmpty()) {
                    reportStatement.setString(parameterIndex++, firstWordModel + "%");
                }

                if (secondWordModel != null && !secondWordModel.isEmpty()) {
                    reportStatement.setString(parameterIndex++, "%" + secondWordModel + "%");
                }

                if (registarskiBroj != null && !registarskiBroj.isEmpty()) {
                    reportStatement.setString(parameterIndex++, "%" + registarskiBroj + "%");
                }

                if (brojSasije != null && !brojSasije.isEmpty()) {
                    reportStatement.setString(parameterIndex, "%" + brojSasije + "%");
                }

                try (ResultSet resultSet = reportStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Report report = new Report();
                        report.setId(resultSet.getInt("id"));
                        report.setModel(resultSet.getString("model"));
                        report.setVlasnik(resultSet.getString("vlasnik"));
                        report.setRegistarskiBroj(resultSet.getString("registarskiBroj"));
                        report.setTelefon(resultSet.getString("telefon"));
                        report.setBrojSasije(resultSet.getString("brojSasije"));
                        report.setBrojMotora(resultSet.getString("brojMotora"));
                        report.setBoja(resultSet.getString("boja"));
                        report.setGodiste(resultSet.getString("godiste"));
                        report.setServiser(resultSet.getString("serviser"));
                        report.setDatum(resultSet.getDate("datum"));
                        report.setRuke(resultSet.getInt("ruke"));
                        report.setCenaDelova(resultSet.getInt("cenaDelova"));

                        int reportId = report.getId();

                        // Read associated Materijal
                        List<Materijal> materijali = readMaterijalForReport(connection, reportId);
                        report.setMaterijali(materijali);

                        // Read associated Stavka
                        List<Stavka> stavke = readStavkaForReport(connection, reportId);
                        report.setStavke(stavke);

                        reports.add(report);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reports;
    }

    private static List<Materijal> readMaterijalForReport(Connection connection, int reportId) throws SQLException {
        List<Materijal> materijali = new ArrayList<>();

        String selectMaterijalSQL = "SELECT * FROM Materijal WHERE report_id = ?";
        try (PreparedStatement materijalStatement = connection.prepareStatement(selectMaterijalSQL)) {
            materijalStatement.setInt(1, reportId);

            try (ResultSet resultSet = materijalStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Create Materijal object
                    Materijal materijal = new Materijal();
                    materijal.setId(resultSet.getInt("id"));
                    materijal.setImeDela(resultSet.getString("imeDela"));
                    materijal.setKataloskiBroj(resultSet.getString("kataloskiBroj"));
                    materijal.setKolicina(Integer.parseInt(resultSet.getString("kolicina")));
                    materijal.setCenaDela(Integer.parseInt(resultSet.getString("cenaDela")));
                    materijali.add(materijal);
                }
            }
        }

        return materijali;
    }

    private static List<Stavka> readStavkaForReport(Connection connection, int reportId) throws SQLException {
        List<Stavka> stavke = new ArrayList<>();

        String selectStavkaSQL = "SELECT * FROM Stavka WHERE report_id = ?";
        try (PreparedStatement stavkaStatement = connection.prepareStatement(selectStavkaSQL)) {
            stavkaStatement.setInt(1, reportId);

            try (ResultSet resultSet = stavkaStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Create Stavka object
                    Stavka stavka = new Stavka();
                    stavka.setId(resultSet.getInt("id"));
                    stavka.setImeStavke(resultSet.getString("imeStavke"));
                    stavka.setBrojSati(Integer.parseInt(resultSet.getString("brojSati")));
                    stavke.add(stavka);
                }
            }
        }

        return stavke;
    }

    public static void deleteReport(int reportId) {
        try (Connection connection = DB.getConnection()) {
            connection.setAutoCommit(false);

            // Delete associated Materijal records
            String deleteMaterijalSQL = "DELETE FROM Materijal WHERE report_id = ?";
            try (PreparedStatement deleteMaterijalStatement = connection.prepareStatement(deleteMaterijalSQL)) {
                deleteMaterijalStatement.setInt(1, reportId);
                deleteMaterijalStatement.executeUpdate();
            }

            // Delete associated Stavka records
            String deleteStavkaSQL = "DELETE FROM Stavka WHERE report_id = ?";
            try (PreparedStatement deleteStavkaStatement = connection.prepareStatement(deleteStavkaSQL)) {
                deleteStavkaStatement.setInt(1, reportId);
                deleteStavkaStatement.executeUpdate();
            }

            // Delete the Report
            String deleteReportSQL = "DELETE FROM Report WHERE id = ?";
            try (PreparedStatement deleteReportStatement = connection.prepareStatement(deleteReportSQL)) {
                deleteReportStatement.setInt(1, reportId);
                deleteReportStatement.executeUpdate();
            }

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Plata> readAllPlata() {
        List<Plata> plataList = new ArrayList<>();

        try (Connection connection = DB.getConnection();
                Statement statement = connection.createStatement()) {

            String selectAllPlataSQL = "SELECT * FROM Plata";

            try (ResultSet resultSet = statement.executeQuery(selectAllPlataSQL)) {
                while (resultSet.next()) {
                    Plata plata = new Plata();
                    plata.setId(resultSet.getInt("id"));
                    plata.setMesec(resultSet.getString("mesec"));
                    plata.setPlata(resultSet.getInt("plata"));
                    // System.out.println("GETALL PLATA" + plata.getMesec());
                    plataList.add(plata);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return plataList;
    }

    public static void deletePlata(int plataId) {
        try (Connection connection = DB.getConnection()) {
            String deletePlataSQL = "DELETE FROM Plata WHERE id = ?";
            try (PreparedStatement deletePlataStatement = connection.prepareStatement(deletePlataSQL)) {
                deletePlataStatement.setInt(1, plataId);
                deletePlataStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // dropTables();
        // initTables();

        // int id = insertReport(ServisApp.createTestReport());
        // System.out.println("Unet " + id);
        // List<Report> reports = readAllReports();
        // // List<Report> reports = readReport("Zoki", "", "", "ABC", "S12341");

        // for (Report r : reports) {
        //     System.out.println(r);
        // }

        // deleteReport(2);

        // List<Report> reports1 = readAllReports();

        // for (Report r : reports1) {
        // System.out.println(r);
        // }
        // List<Plata> plate = SimpleJavaFXApp.generatePlataList();
        // for(Plata p: plate) {
        // insertPlata(p);
        // }
    }
}