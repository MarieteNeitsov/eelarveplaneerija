package com.example.projekt2;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javafx.stage.FileChooser;

public class Peaklass extends Application {
    static double tulu;
    static List<Kulud> kulutused;
    static double säästusumma;
    Kulud säästud = new Kulud("säästud");
    Object[][] andmed = {{"Üür", 0, 0, 0},
            {"Kommunaalid", 0, 0, 0},
            {"Söök", 0, 0, 0},
            {"Transport", 0, 0, 0},
            {"Meelelahutus", 0, 0, 0},
            {"Riided/jalatsid", 0, 0, 0},
            {"Ilu/tervis", 0, 0, 0},
            {"Muu", 0, 0, 0},
            {"Kokku", 0, 0, 0},
            {"", "", "", ""},
            {"Säästud", 0, 0, 0}};

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox juur = new VBox();
        juur.setAlignment(Pos.CENTER);
        juur.setSpacing(20);

        Label silt = new Label("Mingi programmi tutvustav tekst");
        Button valifailnupp = new Button("Vali DAT fail, et jätkata kulutuste lisamist");
        valifailnupp.setOnAction(event -> {
            //pole vist koige parem lahendus, aga teisiti vist ei saa praegu
            try {
                valiFail(primaryStage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        Button alustaUut = new Button("Alusta uut sessiooni");
        alustaUut.setOnAction(event -> alustus(primaryStage));


        juur.getChildren().addAll(silt, valifailnupp, alustaUut);

        Scene scene = new Scene(juur, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void valiFail(Stage primaryStage) throws IOException {
        List<Kulud> loetudKulud = new ArrayList<>();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("DAT Files", "*.dat"));
        File fail = fileChooser.showOpenDialog(primaryStage);

        if (fail != null) {
            try (DataInputStream dis = new DataInputStream(new FileInputStream(fail.getName()))) {
                tulu = dis.readDouble();
                säästusumma = dis.readDouble();
                for (int i = 0; i < 10; i++) {
                    String nimetus = dis.readUTF();
                    double[] kategooria = new double[3];
                    kategooria[0] = dis.readDouble();
                    kategooria[1] = dis.readDouble();
                    kategooria[2] = dis.readDouble();
                    Kulud kulu = new Kulud(kategooria, nimetus);
                    loetudKulud.add(kulu);
                }
                kulutused = loetudKulud;
            }
        }
        valiTegevus(primaryStage);
    }

    public void alustus(Stage primaryStage) {
        BorderPane juur = new BorderPane();
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(10));

        Label tuluSilt = new Label("Sisesta oma selle kuu tulu (€):");
        TextField tekst = new TextField();
        tekst.setPrefWidth(380);
        Button edasiNupp = new Button("Edasi");

        vBox.getChildren().addAll(tuluSilt, tekst, edasiNupp);
        hBox.getChildren().add(vBox);
        HBox.setHgrow(tekst, Priority.ALWAYS);

        edasiNupp.setOnAction(event -> {
            String tuluTekst = tekst.getText();
            try {
                tulu = Double.parseDouble(tuluTekst);
                if (tulu < 0.0) throw new NumberFormatException();
                planeeriSäästmine(primaryStage);
            } catch (NumberFormatException e) {
                tekst.clear();
            }

        });

        juur.setCenter(hBox);

        Scene stseen = new Scene(juur, 400, 300);
        primaryStage.setScene(stseen);
        primaryStage.show();
    }

    public void planeeriSäästmine(Stage primaryStage) {
        BorderPane juur = new BorderPane();
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(10));

        Label säästmineSilt = new Label("Kui soovid sellel kuul säästa, siis sisesta summa või lase genereerida juhuslik säästusumma");
        säästmineSilt.setWrapText(true);
        säästmineSilt.setTextAlignment(TextAlignment.CENTER);
        TextField säästmineTekst = new TextField();
        säästmineTekst.setPrefWidth(380);
        Button juhuslikSummaNupp = new Button("Genereeri juhuslik summa");
        Button edasiNupp = new Button("Edasi");

        HBox nupud = new HBox(juhuslikSummaNupp, edasiNupp);
        nupud.setAlignment(Pos.CENTER);
        nupud.setSpacing(10);

        vBox.getChildren().addAll(säästmineSilt, säästmineTekst, nupud);
        hBox.getChildren().addAll(vBox);
        HBox.setHgrow(säästmineTekst, Priority.ALWAYS);

        juhuslikSummaNupp.setOnAction(event -> {
            Saastmine säästmine = new Saastmine(tulu);
            säästusumma = säästmine.säästa();
            säästud.lisaEelarve(säästusumma);
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setHeaderText(null);
            info.setTitle("");
            info.setContentText("Säästusumma " + säästusumma + " eelarvetesse lisatud");
            info.showAndWait();
            planeeriEelarved(primaryStage);
        });

        edasiNupp.setOnAction(event -> {
            try {
                String sisestus = säästmineTekst.getText();
                if (sisestus.equals(""))
                    säästud.lisaEelarve(0);
                else {
                    säästusumma = Double.parseDouble(sisestus);
                    säästud.lisaEelarve(säästusumma);
                    if (säästusumma < 0) throw new NumberFormatException("negatiivne arv");
                }
                planeeriEelarved(primaryStage);
            } catch (NumberFormatException e) {
                säästmineTekst.clear();
            }
        });

        juur.setCenter(hBox);
        Scene stseen = new Scene(juur, 400, 300);
        primaryStage.setScene(stseen);
    }

    private void planeeriEelarved(Stage primaryStage) {
        //kulude isendid iga valdkonna eelarve jaoks
        Kulud üür = new Kulud("üür");
        Kulud kommunaalid = new Kulud("kommunaalid");
        Kulud söök = new Kulud("söök");
        Kulud transport = new Kulud("transport");
        Kulud meelelahutus = new Kulud("meelelahutus");
        Kulud riided_ja_jalatsid = new Kulud("riided/jalatsid");
        Kulud ilu_ja_tervis = new Kulud("ilu/tervis");
        Kulud muu = new Kulud("muu");
        Kulud kokku = new Kulud("kokku");
        kulutused = new ArrayList<>(Arrays.asList(üür, kommunaalid, söök, transport, meelelahutus, riided_ja_jalatsid, ilu_ja_tervis, muu, kokku, säästud));

        VBox juur = new VBox();
        juur.setAlignment(Pos.CENTER);
        juur.setSpacing(10);

        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(new Insets(10));

        Label tekst = new Label("Sisesta valdkondadele soovitud eelarved: ");
        Label üürSilt = new Label("Üür:");
        TextField üürTekst = new TextField();
        Label kommunaalidSilt = new Label("Kommunaalid:");
        TextField kommunaalidTekst = new TextField();
        Label söökSilt = new Label("Söök:");
        TextField söökTekst = new TextField();
        Label transportSilt = new Label("Transport");
        TextField transportTekst = new TextField();
        Label meelelahtusSilt = new Label("Meelelahutus:");
        TextField meelelahutusTekst = new TextField();
        Label riided_jalatsidSilt = new Label("Riided/jalatsid:");
        TextField riided_jalatsidTekst = new TextField();
        Label ilu_tervisSilt = new Label("Ilu/tervis:");
        TextField ilu_tervisTekst = new TextField();
        Label muuSilt = new Label("Muu:");
        TextField muuTekst = new TextField();
        Button kinnitaNupp = new Button("Kinnita");
        List<TextField> tekstid = new ArrayList<>(Arrays.asList(üürTekst, kommunaalidTekst, söökTekst, transportTekst, meelelahutusTekst, riided_jalatsidTekst, ilu_tervisTekst, muuTekst));

        gp.add(üürSilt, 0, 0);
        gp.add(üürTekst, 1, 0);
        gp.add(kommunaalidSilt, 0, 1);
        gp.add(kommunaalidTekst, 1, 1);
        gp.add(söökSilt, 0, 2);
        gp.add(söökTekst, 1, 2);
        gp.add(transportSilt, 0, 3);
        gp.add(transportTekst, 1, 3);
        gp.add(meelelahtusSilt, 0, 4);
        gp.add(meelelahutusTekst, 1, 4);
        gp.add(riided_jalatsidSilt, 0, 5);
        gp.add(riided_jalatsidTekst, 1, 5);
        gp.add(ilu_tervisSilt, 0, 6);
        gp.add(ilu_tervisTekst, 1, 6);
        gp.add(muuSilt, 0, 7);
        gp.add(muuTekst, 1, 7);
        gp.add(kinnitaNupp, 0, 8, 2, 1);
        GridPane.setHalignment(kinnitaNupp, HPos.CENTER);

        juur.getChildren().addAll(tekst, gp);

        kinnitaNupp.setOnAction(event -> {
            double eelarvedKokku = 0;
            double vahe = 0;
            try {
                for (int i = 0; i < kulutused.size() - 2; i++) {
                    String sisestus = tekstid.get(i).getText();
                    if (sisestus.equals(""))
                        kulutused.get(i).lisaEelarve(0);
                    else {
                        double eelarveSumma = kulutused.get(i).lisaEelarve(Double.parseDouble(sisestus));
                        andmed[i][1] = eelarveSumma;
                        eelarvedKokku += eelarveSumma;
                    }
                }
                andmed[8][1] = kokku.lisaKokkuEelarve(eelarvedKokku, tulu - säästusumma);
                vahe = tulu - säästusumma - eelarvedKokku;
                if (vahe > 0)
                    throw new EelarvetestJäiÜleErind("Sul jäi eelarvetest üle " + vahe + " eurot, summa lisatud säästudesse");
                else if (vahe == 0)
                    throw new KõikJagatudErind("Kogu sissetulek edukalt eelarvete vahel ära jaotatud");
            } catch (EelarvedÜletavadTuluErind e) {
                Alert veahoiatus = new Alert(Alert.AlertType.ERROR);
                veahoiatus.setHeaderText(null);
                veahoiatus.setTitle("");
                veahoiatus.setContentText(e.getMessage());
                veahoiatus.showAndWait();
            } catch (EelarvetestJäiÜleErind e) {
                andmed[10][2] = säästud.lisaKulu(vahe);
                if (säästud.protsent() < 0)
                    andmed[10][3] = "Eelarve puudub!";
                else
                    andmed[10][3] = säästud.protsent();
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setHeaderText(null);
                info.setTitle("");
                info.setContentText(e.getMessage());
                info.showAndWait();
                valiTegevus(primaryStage);
            } catch (KõikJagatudErind e) {
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setHeaderText(null);
                info.setTitle("");
                info.setContentText(e.getMessage());
                info.showAndWait();
                valiTegevus(primaryStage);
            }
        });

        Scene scene = new Scene(juur, 400, 400);
        primaryStage.setScene(scene);
    }

    private void valiTegevus(Stage primaryStage) {
        BorderPane juur = new BorderPane();
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);

        Label tegevusSilt = new Label("Vali tegevus:");
        Button kulutus = new Button("Lisa kulutus");
        Button ülevaade = new Button("Vaata ülevaadet");
        Button lõpeta = new Button("Lõpeta");

        HBox nupud = new HBox(kulutus, ülevaade, lõpeta);
        nupud.setAlignment(Pos.CENTER);
        nupud.setSpacing(10);

        vBox.getChildren().addAll(tegevusSilt, nupud);

        juur.setCenter(vBox);
        Scene stseen = new Scene(juur, 400, 300);
        primaryStage.setScene(stseen);

        kulutus.setOnAction(event -> valiValdkond(primaryStage));
        ülevaade.setOnAction(event -> vaataÜlevaadet());
        lõpeta.setOnAction(event -> {
            try {
                lõpetaTöö(primaryStage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void valiValdkond(Stage primaryStage) {
        BorderPane juur = new BorderPane();
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);

        Label valdkondSilt = new Label("Vali valdkond:");
        Button üür = new Button("üür");
        Button kommunaalid = new Button("kommunaalid");
        Button söök = new Button("söök");
        Button transport = new Button("transport");
        Button meelelahutus = new Button("meelelahutus");
        Button riided_ja_jalatsid = new Button("riided/jalatsid");
        Button ilu_ja_tervis = new Button("ilu/tervis");
        Button muu = new Button("muu");

        HBox nupud = new HBox(üür, kommunaalid, söök, transport, meelelahutus, riided_ja_jalatsid, ilu_ja_tervis, muu);
        nupud.setAlignment(Pos.CENTER);
        nupud.setSpacing(10);

        vBox.getChildren().addAll(valdkondSilt, nupud);

        juur.setCenter(vBox);
        Scene stseen = new Scene(juur, 700, 300);
        primaryStage.setScene(stseen);

        üür.setOnAction(event -> lisaKulu(primaryStage, kulutused.get(0), 0));
        kommunaalid.setOnAction(event -> lisaKulu(primaryStage, kulutused.get(1), 1));
        söök.setOnAction(event -> lisaKulu(primaryStage, kulutused.get(2), 2));
        transport.setOnAction(event -> lisaKulu(primaryStage, kulutused.get(3), 3));
        meelelahutus.setOnAction(event -> lisaKulu(primaryStage, kulutused.get(4), 4));
        riided_ja_jalatsid.setOnAction(event -> lisaKulu(primaryStage, kulutused.get(5), 5));
        ilu_ja_tervis.setOnAction(event -> lisaKulu(primaryStage, kulutused.get(6), 6));
        muu.setOnAction(event -> lisaKulu(primaryStage, kulutused.get(7), 7));
    }

    private void lisaKulu(Stage primaryStage, Kulud valdkond, int indeks) {
        BorderPane juur = new BorderPane();
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(10));

        Label kuluSilt = new Label("Sisesta summa, mis selles valdkonnas kulutasid:");
        TextField kuluTekst = new TextField();
        kuluTekst.setPrefWidth(380);
        Button edasiNupp = new Button("Edasi");

        vBox.getChildren().addAll(kuluSilt, kuluTekst, edasiNupp);
        hBox.getChildren().add(vBox);
        HBox.setHgrow(kuluTekst, Priority.ALWAYS);

        juur.setCenter(hBox);
        Scene stseen = new Scene(juur, 400, 300);
        primaryStage.setScene(stseen);

        edasiNupp.setOnAction(event -> {
            andmed[indeks][2] = valdkond.lisaKulu(Double.parseDouble(kuluTekst.getText()));
            if (valdkond.protsent() < 0)
                andmed[indeks][3] = "Eelarve puudub!";
            else
                andmed[indeks][3] = valdkond.protsent();
            if (!Objects.equals(valdkond.getNimetus(), "üür") || !Objects.equals(valdkond.getNimetus(), "kommunaalid") || !Objects.equals(valdkond.getNimetus(), "söök") || !Objects.equals(valdkond.getNimetus(), "transport"))
                valdkond.ülePiiri();
            else {
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setHeaderText(null);
                info.setTitle("");
                info.setContentText("Summa valdkonna " + "\"" + valdkond.getNimetus() + "\"" + " kuludesse lisatud");
                info.showAndWait();
            }
            andmed[8][2] = kulutused.get(8).lisaKulu(Double.parseDouble(kuluTekst.getText()));
            andmed[8][3] = kulutused.get(8).protsent();
            valiTegevus(primaryStage);
        });
    }

    private void vaataÜlevaadet() {
        VBox juur = new VBox();
        String[] pealkirjad = {"", "Planeeritud", "Tegelik", "% eelarvest"};

        ObservableList<Object[]> read = FXCollections.observableArrayList();
        read.addAll(Arrays.asList(andmed));
        TableView<Object[]> tabel = new TableView<>();
        for (int i = 0; i < pealkirjad.length; i++) {
            TableColumn<Object[], Object> veerg = new TableColumn<>(pealkirjad[i]);
            veerg.prefWidthProperty().bind(tabel.widthProperty().multiply(0.24));
            final int veerunr = i;
            veerg.setCellValueFactory(param -> new SimpleObjectProperty<>((param.getValue()[veerunr])));
            tabel.getColumns().add(veerg);
        }

        tabel.setItems(read);

        juur.getChildren().add(tabel);

        Stage stage = new Stage();
        Scene scene = new Scene(juur, 430, 300);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> stage.close());
        stage.show();
    }

    private void lõpetaTöö(Stage primaryStage) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream("eelarve.dat"))) {
            dos.writeDouble(tulu);
            dos.writeDouble(säästusumma);
            for (Kulud kulud : kulutused) {
                dos.writeUTF(kulud.getNimetus());
                for (double arv : kulud.getKategooria())
                    dos.writeDouble(arv);
            }
        }
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setHeaderText(null);
        info.setTitle("");
        info.setContentText("Andmed salvestatud faili eelarve.dat");
        info.showAndWait();
        primaryStage.close();
    }
}
