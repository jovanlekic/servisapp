package servisapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ServisApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    Report currentReport;

    List<Report> reports;
    List<Plata> plate = new ArrayList<>();
    List<Rezervacija> rezervacije = new ArrayList<>();
    Plata currentPlata;

    Button unesiBtn = new Button("Unesi nalog");
    Button brisiBtn = new Button("Obrisi unos");
    Button searchButton;

    Image checkImage = new Image(getClass().getResourceAsStream("/check.png"));
    Image errorImage = new Image(getClass().getResourceAsStream("/delete.png"));
    ImageView imageView = new ImageView();

    ScrollPane scrollPane;

    Label formModelLabel = new Label("Model:");
    TextField modelField = new TextField();

    Label formVlasnikLabel = new Label("Vlasnik:");
    TextField vlasnikField = new TextField();

    Label formRegistrationNumberLabel = new Label("Registarski broj:");
    TextField registrationNumberField = new TextField();

    Label formPhoneNumberLabel = new Label("Telefon:");
    TextField phoneNumberField = new TextField();

    Label formChassisNumberLabel = new Label("Broj sasije:");
    TextField chassisNumberField = new TextField();

    Label formEngineNumberLabel = new Label("Broj motora:");
    TextField engineNumberField = new TextField();

    Label formColorLabel = new Label("Boja:");
    TextField colorField = new TextField();

    Label formProductionYearLabel = new Label("Godiste:");
    TextField productionYearField = new TextField();

    Label formServicePersonLabel = new Label("Serviser:");
    TextField servicePersonField = new TextField();

    Label formRukeLabel = new Label("Ruke:");
    TextField rukeField = new TextField();

    ScrollPane materijalPane;
    ScrollPane stavkaPane;
    ScrollPane rezPane;

    Label modelLabel = new Label();
    Label vlasnikLabel = new Label();
    Label registarskiBrojLabel = new Label();
    Label telefonLabel = new Label();
    Label brojSasijeLabel = new Label();
    Label brojMotoraLabel = new Label();
    Label bojaLabel = new Label();
    Label godisteLabel = new Label();
    Label serviserLabel = new Label();
    Label rukeLabel = new Label();
    Label cenaDelovaLabel = new Label();

    TableView<Materijal> materijalTableView;
    TableView<Stavka> stavkaTableView;
    TableView<Plata> platTableView;

    GridPane printContent;

    static int i = 0;

    /* ELEMENTS */

    private class Card extends BorderPane {
        Report cardReport;

        public Card(Report report) {
            cardReport = report;
            this.setPadding(new Insets(5));

            String[] words = report.getModel().split(" ", 2);
            // Labels for card text and description
            Label label = new Label(words.length >= 1 ? words[0] : "");
            label.setFont(new Font(14));
            Label descriptionLabel = new Label(words.length >= 2 ? words[1] : "");
            descriptionLabel.setFont(new Font(14));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Label subDescriptionLabel = new Label(dateFormat.format(report.getDatum()));
            subDescriptionLabel.setFont(new Font(14));

            // Button for the card
            Button button = new Button("Prikazi izvestaj");
            button.setFont(new Font(16));
            button.setOnAction(e -> displayReport(cardReport));

            // Add labels and button to the card
            VBox labelsContainer = new VBox(5);
            labelsContainer.getChildren().addAll(label, descriptionLabel, subDescriptionLabel);

            HBox empty = new HBox();
            empty.setPrefWidth(20);

            setLeft(labelsContainer);
            setCenter(empty);
            setRight(button);
        }
    }

    private static class StavkaCard extends HBox {
        public TextField imeStavkeTextField;
        public TextField brojSatiTextField;

        public StavkaCard() {
            // Initialize text fields
            imeStavkeTextField = createTextField("Ime stavke");
            brojSatiTextField = createTextField("Broj sati");

            // Add text fields to the HBox
            getChildren().addAll(imeStavkeTextField, brojSatiTextField);
        }

        public StavkaCard(String imeStavke, int brojSati) {
            // Initialize text fields
            imeStavkeTextField = createTextField("Ime stavke");
            imeStavkeTextField.setText(imeStavke);
            brojSatiTextField = createTextField("Broj sati");
            brojSatiTextField.setText(Integer.toString(brojSati));

            // Add text fields to the HBox
            getChildren().addAll(imeStavkeTextField, brojSatiTextField);
        }

        private TextField createTextField(String promptText) {
            TextField textField = new TextField();
            textField.setPromptText(promptText);
            textField.setFont(Font.font(14));
            return textField;
        }
    }

    private static class RezervacijeCard extends HBox {
        public Button deleteButton;

        public RezervacijeCard(Rezervacija rezervacija) {
            VBox containerBox = new VBox();
            Label clientLabel = new Label(rezervacija.getClient() + ", " + rezervacija.getCar());
            Label datLabel = new Label(rezervacija.getDate());
            Label expLabel = new Label(rezervacija.getExplain());
            deleteButton = new Button("Gotovo");
            deleteButton.setTranslateY(10);
            deleteButton.setTranslateX(10);
            containerBox.getChildren().addAll(clientLabel, datLabel, expLabel);
            getChildren().addAll(containerBox, deleteButton);
        }
    }

    private static class MaterialCard extends HBox {

        public TextField imeDelaTextField;
        public TextField katBrojTextField;
        public TextField kolicinaTextField;
        public TextField cenaPoDeluTextField;

        public MaterialCard() {
            // Initialize text fields
            imeDelaTextField = createTextField("Ime dela");
            katBrojTextField = createTextField("Kat. broj");
            kolicinaTextField = createTextField("Kolicina");
            kolicinaTextField.setPrefWidth(70);
            cenaPoDeluTextField = createTextField("Cena po delu");
            cenaPoDeluTextField.setPrefWidth(100);

            // Add text fields to the HBox
            getChildren().addAll(imeDelaTextField, katBrojTextField, kolicinaTextField, cenaPoDeluTextField);
        }

        public MaterialCard(String imeDela, String katBroj, int kolicina, int cenaPD) {
            // Initialize text fields
            imeDelaTextField = createTextField("Ime dela");
            imeDelaTextField.setText(imeDela);
            katBrojTextField = createTextField("Kat. broj");
            katBrojTextField.setText(katBroj);
            kolicinaTextField = createTextField("Kolicina");
            kolicinaTextField.setText(Integer.toString(kolicina));
            kolicinaTextField.setPrefWidth(70);
            cenaPoDeluTextField = createTextField("Cena po delu");
            cenaPoDeluTextField.setText(Integer.toString(cenaPD));
            cenaPoDeluTextField.setPrefWidth(100);

            // Add text fields to the HBox
            getChildren().addAll(imeDelaTextField, katBrojTextField, kolicinaTextField, cenaPoDeluTextField);
        }

        private TextField createTextField(String promptText) {
            TextField textField = new TextField();
            textField.setPromptText(promptText);
            textField.setFont(Font.font(14));
            return textField;
        }
    }

    /* UTILS */

    private ImageView scaleImage(Image image, double scale) {
        ImageView originalImageView = new ImageView(image);

        double newWidth = image.getWidth() * scale;
        double newHeight = image.getHeight() * scale;

        originalImageView.setFitWidth(newWidth);
        originalImageView.setFitHeight(newHeight);

        return originalImageView;
    }

    public <T, S> TableColumn<S, T> createColumn(String text, String property) {
        TableColumn<S, T> column = new TableColumn<>(text);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        return column;
    }

    public TableView<Materijal> createMaterijalTableView(List<Materijal> materijalList) {
        // Creating table columns
        TableColumn<Materijal, String> imeDelaColumn = createColumn("Ime dela", "imeDela");
        TableColumn<Materijal, String> kataloskiBrojColumn = createColumn("Kataloski broj", "kataloskiBroj");
        TableColumn<Materijal, Integer> kolicinaColumn = createColumn("Kolicina", "kolicina");
        TableColumn<Materijal, Integer> cenaDelaColumn = createColumn("Cena dela", "cenaDela");

        // Creating TableView and adding columns
        TableView<Materijal> tableView = new TableView<>();
        tableView.getColumns().addAll(imeDelaColumn, kataloskiBrojColumn, kolicinaColumn, cenaDelaColumn);

        // Adding data to the table
        ObservableList<Materijal> materijalObservableList = FXCollections.observableArrayList(materijalList);
        tableView.setItems(materijalObservableList);

        return tableView;
    }

    public TableView<Stavka> createStavkaTableView(List<Stavka> stavkaList) {
        // Creating table columns
        TableColumn<Stavka, String> imeStavkeColumn = createColumn("Ime stavke", "imeStavke");
        TableColumn<Stavka, Integer> brojSatiColumn = createColumn("Broj sati", "brojSati");

        // Creating TableView and adding columns
        TableView<Stavka> tableView = new TableView<>();
        tableView.getColumns().addAll(imeStavkeColumn, brojSatiColumn);

        // Adding data to the table
        ObservableList<Stavka> stavkaObservableList = FXCollections.observableArrayList(stavkaList);
        tableView.setItems(stavkaObservableList);

        return tableView;
    }

    public TableView<Plata> createPlataTableView(List<Plata> plataList) {
        // Creating table columns
        TableColumn<Plata, String> monthColumn = createColumn("Mesec", "mesec");
        TableColumn<Plata, Integer> plataColumn = createColumn("Plata", "plata");

        // Creating TableView and adding columns
        TableView<Plata> tableView = new TableView<>();
        tableView.getColumns().addAll(monthColumn, plataColumn);

        // Adding data to the table
        ObservableList<Plata> plataObservableList = FXCollections.observableArrayList(plataList);
        tableView.setItems(plataObservableList);

        return tableView;
    }

    private void initDB() {
        // DB.dropTables();
        DB.initTables();
    }

    private String makeTargetDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
        return sdf.format(date);
    }

    private void refreshPlate() {
        plate = DB.readAllPlata();
        String targetMesec = makeTargetDate(new Date());
        // System.out.println("TM" + targetMesec);
        boolean isMesecPresent = false;
        for (Plata plata : plate) {
            if (plata.getMesec().equals(targetMesec)) {
                isMesecPresent = true;
                currentPlata = plata;
                break; // No need to continue checking once found
            }
        }
        if (!isMesecPresent) {
            currentPlata = new Plata(targetMesec, 0);
            DB.insertPlata(currentPlata);
            plate.add(currentPlata);
        }
        platTableView.getItems().clear();
        platTableView.getItems().addAll(plate);
    }

    private void performSearch(String vlasnik, String marka, String model, String regBroj, String brSasije) {
        reports = DB.readReport(vlasnik, marka, model, regBroj, brSasije);
        populateScrollPane(reports);
    }

    private void populateScrollPane(List<Report> reports) {
        VBox cardContainer = (VBox) scrollPane.getContent();
        cardContainer.getChildren().clear();
        // System.out.println(reports.size());
        // if(reports.size() > 0) System.out.println(reports.get(0));
        for (Report r : reports) {
            performAdd(cardContainer, new Card(r));
        }
    }

    private void displayReport(Report report) {
        currentReport = report;
        // System.out.println(report);
        modelLabel.setText("Model: " + report.getModel());
        vlasnikLabel.setText("Vlasnik: " + report.getVlasnik());
        registarskiBrojLabel.setText("Registarski broj: " + report.getRegistarskiBroj());
        telefonLabel.setText("Telefon: " + report.getTelefon());
        brojSasijeLabel.setText("Broj sasije: " + report.getBrojSasije());
        brojMotoraLabel.setText("Broj motora: " + report.getBrojMotora());
        bojaLabel.setText("Boja: " + report.getBoja());
        godisteLabel.setText("Godiste: " + report.getGodiste());
        serviserLabel.setText("Serviser: " + report.getServiser());
        rukeLabel.setText("Ruke: " + report.getRuke());
        cenaDelovaLabel.setText("Cena delova: " + report.getCenaDelova());
        // System.out.println(report.getMaterijali());
        materijalTableView.getItems().clear();
        for (Materijal m : report.getMaterijali()) {
            materijalTableView.getItems().add(m);
        }
        stavkaTableView.getItems().clear();
        for (Stavka s : report.getStavke()) {
            stavkaTableView.getItems().add(s);
        }
    }

    private void copyReport() {
        modelField.setText(currentReport.getModel());
        vlasnikField.setText(currentReport.getVlasnik());
        registrationNumberField.setText(currentReport.getRegistarskiBroj());
        phoneNumberField.setText(currentReport.getTelefon());
        chassisNumberField.setText(currentReport.getBrojSasije());
        engineNumberField.setText(currentReport.getBrojMotora());
        colorField.setText(currentReport.getBoja());
        productionYearField.setText(currentReport.getGodiste());
        servicePersonField.setText(currentReport.getServiser());
        rukeField.setText(Integer.toString(currentReport.getRuke()));

        VBox cardMContainer = (VBox) materijalPane.getContent();
        cardMContainer.getChildren().removeIf(node -> !(node instanceof Button));
        for (Materijal m : currentReport.getMaterijali()) {
            performAdd(cardMContainer,
                    new MaterialCard(m.getImeDela(), m.getKataloskiBroj(), m.getKolicina(), m.getCenaDela()));
        }

        VBox cardSContainer = (VBox) stavkaPane.getContent();
        cardSContainer.getChildren().removeIf(node -> !(node instanceof Button));
        for (Stavka s : currentReport.getStavke()) {
            performAdd(cardSContainer, new StavkaCard(s.getImeStavke(), s.getBrojSati()));
        }

    }

    private void deleteInputReport() {
        modelField.setText("");
        vlasnikField.setText("");
        registrationNumberField.setText("");
        phoneNumberField.setText("");
        chassisNumberField.setText("");
        engineNumberField.setText("");
        colorField.setText("");
        productionYearField.setText("");
        servicePersonField.setText("");
        rukeField.setText("");
        VBox cardMContainer = (VBox) materijalPane.getContent();
        cardMContainer.getChildren().removeIf(node -> !(node instanceof Button));
        VBox cardSContainer = (VBox) stavkaPane.getContent();
        cardSContainer.getChildren().removeIf(node -> !(node instanceof Button));
    }

    private void deleteReport() {
        DB.deleteReport(currentReport.getId());
        String targetDate = makeTargetDate(currentReport.getDatum());
        Plata targetPlata = DB.readPlata(targetDate);
        if (targetPlata.getPlata() - currentReport.getRuke() >= 0) {
            DB.updatePlata(targetDate, targetPlata.getPlata() - currentReport.getRuke());
        }
        refreshPlate();
        reports.removeIf(report -> report.getId() == currentReport.getId());
        populateScrollPane(reports);
        if (!reports.isEmpty()) {
            currentReport = reports.get(0);
            displayReport(currentReport);
        } else {
            currentReport = createTestReport();
            displayReport(currentReport);
        }
    }

    private void performAdd(VBox cardContainer, Node elem) {
        cardContainer.getChildren().add(elem);
    }

    private Button generateImageButton(Image image, double scale, String txtString) {
        ImageView originalImageView = scaleImage(image, scale);

        // Create a Button with the text and image
        Button button = new Button(txtString, originalImageView);
        button.setFont(new Font(14));
        return button;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        return label;
    }

    private VBox createTableVBox(String title, TableView<?> tableView) {
        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(new Label(title), tableView);
        return vBox;
    }

    public static List<Plata> generatePlataList() {
        List<Plata> plataList = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            String month = "April";
            int plata = random.nextInt(10000); // Adjust the range as needed

            Plata newPlata = new Plata(month, plata);
            plataList.add(newPlata);
        }

        return plataList;
    }

    public static Report createTestReport() {
        Random r1 = new Random();
        Report testReport = new Report();
        testReport.setModel("Test Model");
        testReport.setVlasnik("Zoki");
        testReport.setRegistarskiBroj("ABC1" + r1.nextInt(100));
        testReport.setTelefon("123-456-78" + r1.nextInt(100));
        testReport.setBrojSasije("S1234" + r1.nextInt(100));
        testReport.setBrojMotora("M7890" + r1.nextInt(100));
        testReport.setBoja("Red");
        testReport.setGodiste("202" + r1.nextInt(10));
        testReport.setServiser("Test Serviser" + r1.nextInt(100));
        testReport.setRuke(2);
        testReport.setCenaDelova(1000);
        testReport.setDatum(new Date());

        Random r = new Random();

        // Create a list of Materijal objects
        List<Materijal> materijali = new ArrayList<>();
        Materijal materijal1 = new Materijal("Deli" + r.nextInt(50), "K123", 5, 200);
        Materijal materijal2 = new Materijal("Deli2", "K456", 3, 150);
        materijali.add(materijal1);
        materijali.add(materijal2);
        testReport.setMaterijali(materijali);

        // Create a list of Stavka objects
        List<Stavka> stavke = new ArrayList<>();
        Stavka stavka1 = new Stavka("Stavka" + r.nextInt(50), 300, 8);
        Stavka stavka2 = new Stavka("Stavka2", 150, 5);
        stavke.add(stavka1);
        stavke.add(stavka2);
        testReport.setStavke(stavke);
        return testReport;
    }

    public static List<Report> generateReports(int numReports) {
        List<Report> reports = new ArrayList<>();

        for (int i = 1; i <= numReports; i++) {
            // Customize the values as needed
            Report r = createTestReport();

            // Add the report to the list
            reports.add(r);
        }

        return reports;
    }

    private void printReport() {
        PrinterJob printerJob = PrinterJob.createPrinterJob();

        if (printerJob != null && printerJob.showPrintDialog(null)) {
            boolean success = printerJob.printPage(printContent);

            if (success) {
                printerJob.endJob();
            }
        }
    }

    /* INIT */

    private BorderPane initHeader() {
        unesiBtn.setFont(new Font(20));
        unesiBtn.setPrefSize(200, 50);

        brisiBtn.setFont(new Font(20));
        brisiBtn.setOnAction(e -> deleteInputReport());
        brisiBtn.setPrefSize(200, 50);

        TextField clientSearchBox = new TextField();
        clientSearchBox.setPromptText("Ime klijenta");
        clientSearchBox.setFont(new Font(20));
        clientSearchBox.setPrefSize(200, 50);
        clientSearchBox.setFocusTraversable(false);

        TextField carSearchBox = new TextField();
        carSearchBox.setPromptText("Marka auta");
        carSearchBox.setFont(new Font(20));
        carSearchBox.setPrefSize(200, 50);
        carSearchBox.setFocusTraversable(false);

        TextField modelSearchBox = new TextField();
        modelSearchBox.setPromptText("Model");
        modelSearchBox.setFont(new Font(20));
        modelSearchBox.setPrefSize(200, 50);
        modelSearchBox.setFocusTraversable(false);

        TextField regBrojSearchBox = new TextField();
        regBrojSearchBox.setPromptText("Registracija");
        regBrojSearchBox.setFont(new Font(20));
        regBrojSearchBox.setPrefSize(200, 50);
        regBrojSearchBox.setFocusTraversable(false);

        TextField brSasijeSearchBox = new TextField();
        brSasijeSearchBox.setPromptText("Broj sasije");
        brSasijeSearchBox.setFont(new Font(20));
        brSasijeSearchBox.setPrefSize(200, 50);
        brSasijeSearchBox.setFocusTraversable(false);

        searchButton = new Button("Pretrazi");
        searchButton.setFont(new Font(20));
        searchButton.setOnAction(e -> performSearch(clientSearchBox.getText(), carSearchBox.getText(),
                modelSearchBox.getText(), regBrojSearchBox.getText(), brSasijeSearchBox.getText()));
        searchButton.setPrefSize(140, 50);

        VBox leftSearchV = new VBox(10);
        leftSearchV.getChildren().addAll(clientSearchBox, carSearchBox, modelSearchBox, regBrojSearchBox,
                brSasijeSearchBox);

        platTableView = createPlataTableView(plate);
        platTableView.setPrefWidth(400);
        refreshPlate();

        Label plataLabel = new Label("Plata:");
        plataLabel.setFont(new Font(20));

        HBox leftHBox = new HBox(10);
        leftHBox.getChildren().addAll(leftSearchV, searchButton, plataLabel, platTableView);
        leftHBox.setPadding(new Insets(10));

        HBox rightSide = new HBox(10);

        VBox rightHBox = new VBox(10);
        rightHBox.getChildren().addAll(unesiBtn, brisiBtn, imageView);
        rightHBox.setPadding(new Insets(10));

        VBox rezForm = new VBox(10);
        rezForm.setPadding(new Insets(10));
        TextField clientTextField = new TextField();
        clientTextField.setPromptText("Klijent");
        clientTextField.setFont(new Font(20));
        clientTextField.setPrefSize(200, 50);
        clientTextField.setFocusTraversable(false);

        TextField carTextField = new TextField();
        carTextField.setPromptText("Model auta");
        carTextField.setFont(new Font(20));
        carTextField.setPrefSize(200, 50);
        carTextField.setFocusTraversable(false);

        TextField datumTextField = new TextField();
        datumTextField.setPromptText("Datum");
        datumTextField.setFont(new Font(20));
        datumTextField.setPrefSize(200, 50);
        datumTextField.setFocusTraversable(false);

        TextField explainTextField = new TextField();
        explainTextField.setPromptText("Razlog popravke");
        explainTextField.setFont(new Font(20));
        explainTextField.setPrefSize(200, 50);
        explainTextField.setFocusTraversable(false);

        Button rezButton = new Button("Rezervisi");
        rezButton.setPrefSize(200, 50);
        rezButton.setFont(new Font(20));
        rezButton.setOnAction(e -> {
            DB.insertRezervacija(new Rezervacija(clientTextField.getText(), datumTextField.getText(), carTextField.getText(), explainTextField.getText()));
            populateRezervacije((VBox)rezPane.getContent());
        });

        rezForm.getChildren().addAll(clientTextField, carTextField, datumTextField, explainTextField, rezButton);

        rightSide.getChildren().addAll(initRezPane(), rezForm, rightHBox);

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(leftHBox);
        borderPane.setRight(rightSide);
        borderPane.setMaxHeight(100);
        borderPane.setStyle("-fx-background-color: lightblue;");

        return borderPane;
    }

    private ScrollPane initStavkaPane(boolean stavkaCheck) {
        VBox cardContainer = new VBox(10); // 10 is the spacing between cards
        cardContainer.setPadding(new Insets(10));
        Button dodajBtn;

        if (stavkaCheck) {
            dodajBtn = new Button("Dodaj stavku");
            dodajBtn.setOnAction(e -> performAdd(cardContainer, new StavkaCard()));
        } else {
            dodajBtn = new Button("Dodaj materijal");
            dodajBtn.setOnAction(e -> performAdd(cardContainer, new MaterialCard()));
        }

        // Add cards to the VBox
        cardContainer.getChildren().add(dodajBtn);

        ScrollPane scrollPane = new ScrollPane(cardContainer);
        scrollPane.setPrefWidth(580);
        scrollPane.setStyle("-fx-background-color: blue;");

        return scrollPane;
    }

    private void populateRezervacije(VBox cardContainer) {
        rezervacije = DB.readAllRezervacija();
        cardContainer.getChildren().clear();
        for(Rezervacija r : rezervacije) {
            RezervacijeCard card = new RezervacijeCard(r);
            card.deleteButton.setOnAction(e-> {
                DB.deleteRezervacija(r.getId());
                populateRezervacije((VBox)rezPane.getContent());
                modelField.setText(r.getCar());
                vlasnikField.setText(r.getClient());
            });
            cardContainer.getChildren().add(card);
        }
    }

    private ScrollPane initRezPane() {
        VBox cardContainer = new VBox(10); // 10 is the spacing between cards
        cardContainer.setPadding(new Insets(10));

        populateRezervacije(cardContainer);

        rezPane = new ScrollPane(cardContainer);
        rezPane.setPrefWidth(170);
        // scrollPane.setStyle("-fx-background-color: blue;");

        return rezPane;
    }

    public static List<Rezervacija> generateTestRezervacije(int numberOfRezervacije) {
        List<Rezervacija> rezervacije = new ArrayList<>();

        for (int i = 1; i <= numberOfRezervacije; i++) {
            Rezervacija rezervacija = new Rezervacija();
            rezervacija.setClient("Client" + i);
            rezervacija.setDate("2023-12-" + i);
            rezervacija.setCar("Car" + i);
            rezervacija.setExplain("Explanation" + i);

            rezervacije.add(rezervacija);
        }

        return rezervacije;
    }

    private ScrollPane initScrollablePane() {

        VBox cardContainer = new VBox(10); // 10 is the spacing between cards
        cardContainer.setPadding(new Insets(10));

        // Add cards to the VBox
        scrollPane = new ScrollPane(cardContainer);
        scrollPane.setPrefWidth(300);

        reports = DB.readAllReports();
        populateScrollPane(reports);
        if (!reports.isEmpty()) {
            currentReport = reports.get(0);
        } else {
            currentReport = createTestReport();
        }
        return scrollPane;
    }

    private GridPane initReportInputForm() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPrefWidth(600);

        // Add event handling for the submit button
        unesiBtn.setOnAction(event -> {

            Report report = new Report();
            report.setModel(modelField.getText());
            report.setVlasnik(vlasnikField.getText());
            report.setRegistarskiBroj(registrationNumberField.getText());
            report.setTelefon(phoneNumberField.getText());
            report.setBrojSasije(chassisNumberField.getText());
            report.setBrojMotora(engineNumberField.getText());
            report.setBoja(colorField.getText());
            report.setGodiste(productionYearField.getText());
            report.setServiser(servicePersonField.getText());
            report.setRuke(Integer.parseInt(rukeField.getText().equals("")?"0":rukeField.getText()));
            report.setDatum(new Date());

            List<Materijal> materijaliList = new ArrayList<>();
            // add materijali and stavke
            VBox cardMBox = (VBox) materijalPane.getContent();
            for (Node node : cardMBox.getChildren()) {
                if (node instanceof MaterialCard) {
                    MaterialCard currentMaterialCard = (MaterialCard) node;
                    Materijal m = new Materijal();
                    m.setImeDela(currentMaterialCard.imeDelaTextField.getText());
                    m.setKataloskiBroj(currentMaterialCard.katBrojTextField.getText());
                    m.setKolicina(Integer.parseInt(currentMaterialCard.kolicinaTextField.getText().equals("")?"0":currentMaterialCard.kolicinaTextField.getText()));
                    m.setCenaDela(Integer.parseInt(currentMaterialCard.cenaPoDeluTextField.getText().equals("")?"0":currentMaterialCard.cenaPoDeluTextField.getText()));
                    materijaliList.add(m);
                }
            }

            List<Stavka> stavkaList = new ArrayList<>();

            // add materijali and stavke
            VBox cardSBox = (VBox) stavkaPane.getContent();
            for (Node node : cardSBox.getChildren()) {
                if (node instanceof StavkaCard) {
                    StavkaCard currentStavkaCard = (StavkaCard) node;
                    Stavka s = new Stavka();
                    s.setImeStavke(currentStavkaCard.imeStavkeTextField.getText());
                    s.setBrojSati(Integer.parseInt(currentStavkaCard.brojSatiTextField.getText().equals("")?"0":currentStavkaCard.brojSatiTextField.getText()));
                    stavkaList.add(s);
                }
            }

            report.setMaterijali(materijaliList);
            report.setStavke(stavkaList);
            //System.out.println(report);
            int rid = DB.insertReport(report);
            if (rid > 0) {
                DB.updatePlata(currentPlata.getMesec(), currentPlata.getPlata() + report.getRuke());
                refreshPlate();
                imageView.setImage(checkImage);
                imageView.setFitWidth(30);
                imageView.setFitHeight(30);
            } else {
                imageView.setImage(errorImage);
                imageView.setFitWidth(30);
                imageView.setFitHeight(30);
            }
        });

        materijalPane = initStavkaPane(false);
        stavkaPane = initStavkaPane(true);

        // Add components to the grid
        gridPane.add(formModelLabel, 0, 0);
        gridPane.add(modelField, 1, 0);
        gridPane.add(formVlasnikLabel, 0, 1);
        gridPane.add(vlasnikField, 1, 1);
        gridPane.add(formRegistrationNumberLabel, 0, 2);
        gridPane.add(registrationNumberField, 1, 2);
        gridPane.add(formPhoneNumberLabel, 0, 3);
        gridPane.add(phoneNumberField, 1, 3);
        gridPane.add(formChassisNumberLabel, 0, 4);
        gridPane.add(chassisNumberField, 1, 4);
        gridPane.add(formEngineNumberLabel, 0, 5);
        gridPane.add(engineNumberField, 1, 5);
        gridPane.add(formColorLabel, 0, 6);
        gridPane.add(colorField, 1, 6);
        gridPane.add(formProductionYearLabel, 0, 7);
        gridPane.add(productionYearField, 1, 7);
        gridPane.add(formServicePersonLabel, 0, 8);
        gridPane.add(servicePersonField, 1, 8);
        gridPane.add(formRukeLabel, 0, 9);
        gridPane.add(rukeField, 1, 9);
        gridPane.add(materijalPane, 0, 10, 2, 3);
        gridPane.add(stavkaPane, 0, 13, 2, 3);

        return gridPane;
    }

    private GridPane initReport(Report report) {
        modelLabel = createLabel("Model: " + report.getModel());
        vlasnikLabel = createLabel("Vlasnik: " + report.getVlasnik());
        registarskiBrojLabel = createLabel("Registarski broj: " + report.getRegistarskiBroj());
        telefonLabel = createLabel("Telefon: " + report.getTelefon());
        brojSasijeLabel = createLabel("Broj sasije: " + report.getBrojSasije());
        brojMotoraLabel = createLabel("Broj motora: " + report.getBrojMotora());
        bojaLabel = createLabel("Boja: " + report.getBoja());
        godisteLabel = createLabel("Godiste: " + report.getGodiste());
        serviserLabel = createLabel("Serviser: " + report.getServiser());
        rukeLabel = createLabel("Ruke: " + report.getRuke());
        cenaDelovaLabel = createLabel("Cena delova: " + report.getCenaDelova());

        // Create buttons
        Button printButton = generateImageButton(
                new Image(getClass().getResourceAsStream("/printer.png")), 0.3, "Stampaj");
        Button deleteButton = generateImageButton(
                new Image(getClass().getResourceAsStream("/delete.png")), 0.05, "Obrisi");
        Button copyButton = generateImageButton(new Image(getClass().getResourceAsStream("/copy.png")),
                0.09, "Kopiraj");

        copyButton.setOnAction(e -> copyReport());
        deleteButton.setOnAction(e -> deleteReport());
        printButton.setOnAction(e -> printReport());

        // Create layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        // Add labels to the left column
        gridPane.add(modelLabel, 0, 0);
        gridPane.add(vlasnikLabel, 0, 1); // Inserted line
        gridPane.add(registarskiBrojLabel, 0, 2);
        gridPane.add(telefonLabel, 0, 3);
        gridPane.add(brojSasijeLabel, 0, 4);
        gridPane.add(brojMotoraLabel, 0, 5);
        gridPane.add(bojaLabel, 0, 6);
        gridPane.add(godisteLabel, 0, 7);
        gridPane.add(serviserLabel, 0, 8);
        gridPane.add(rukeLabel, 0, 9);
        gridPane.add(cenaDelovaLabel, 0, 10);

        materijalTableView = createMaterijalTableView(Arrays.asList(
                new Materijal("D", "K1", 10, 100),
                new Materijal("D", "K2", 20, 200)));
        materijalTableView.setPrefWidth(600);

        stavkaTableView = createStavkaTableView(Arrays.asList(
                new Stavka("Stavka1", 300, 8),
                new Stavka("Stavka2", 150, 5)));
        stavkaTableView.setPrefWidth(400);

        gridPane.add(createTableVBox("Materijal Table", materijalTableView), 0, 11);
        gridPane.add(createTableVBox("StavkaCard Table", stavkaTableView), 0, 12);

        // Add buttons to the right column
        VBox buttonBox = new VBox(10, printButton, deleteButton, copyButton);
        gridPane.add(buttonBox, 1, 0, 1, 10);

        printContent = gridPane;

        return gridPane;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Vodjenje servisa");

        initDB();

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/service.png")));

        Report report = createTestReport();
        currentReport = report;

        BorderPane centrumPane = new BorderPane();
        centrumPane.setTop(initHeader());
        centrumPane.setCenter(initReport(report));
        centrumPane.setLeft(initScrollablePane());
        centrumPane.setRight(initReportInputForm());

        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            Platform.exit();
        });

        primaryStage.setScene(new Scene(centrumPane, 1700, 1100));
        primaryStage.show();
    }
}
