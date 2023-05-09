package com.example.projekt2;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
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

import javafx.stage.FileChooser;
import javafx.util.Callback;

public class Peaklass extends Application {
    static double tulu;
    static List<Kulud> kulutused;
    static double säästusumma;

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
            // tuleb sisse lugeda
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

            } catch (NumberFormatException e) {
                tekst.clear();
                //äkki mingi muu erind,veel ei tea:/

            }
            planeeriSäästmine(primaryStage);
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
            säästmineTekst.setText(String.valueOf(säästmine.säästa()));
            säästusumma = Double.parseDouble(säästmineTekst.getText());
        });

        edasiNupp.setOnAction(event -> planeeriEelarved(primaryStage));

        juur.setCenter(hBox);
        Scene stseen = new Scene(juur, 400, 300);
        primaryStage.setScene(stseen);
    }

    private void planeeriEelarved(Stage primaryStage) {
        //kulude isendid iga valdkonna eelarve jaoks
        Kulud üür = new Kulud("üür");
        Kulud kommunaalkulud = new Kulud("kommunaalkulud");
        Kulud söök = new Kulud("söök");
        Kulud transport = new Kulud("transport");
        Kulud meelelahutus = new Kulud("meelelahutus");
        Kulud riided_ja_jalatsid = new Kulud("riided/jalatsid");
        Kulud ilu_ja_tervis = new Kulud("ilu/tervis");
        Kulud muu = new Kulud("muu");
        Kulud kokku = new Kulud("kokku");
        Kulud säästud = new Kulud("säästud");
        kulutused = new ArrayList<>(Arrays.asList(üür, kommunaalkulud, söök, transport, meelelahutus, riided_ja_jalatsid, ilu_ja_tervis, muu, kokku, säästud));

        GridPane juur = new GridPane();
        juur.setAlignment(Pos.CENTER);
        juur.setHgap(10);
        juur.setVgap(10);
        juur.setPadding(new Insets(10));

        Label üürSilt = new Label("Üür:");
        TextField üürTekst = new TextField();
        Label kommunaalkuludSilt = new Label("Kommunaalkulud:");
        TextField kommunaalkuludTekst = new TextField();
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
        List<TextField> tekstid = new ArrayList<>(Arrays.asList(üürTekst, kommunaalkuludTekst, söökTekst, transportTekst, meelelahutusTekst, riided_jalatsidTekst, ilu_tervisTekst, muuTekst));

        juur.add(üürSilt, 0, 0);
        juur.add(üürTekst, 1, 0);
        juur.add(kommunaalkuludSilt, 0, 1);
        juur.add(kommunaalkuludTekst, 1, 1);
        juur.add(söökSilt, 0, 2);
        juur.add(söökTekst, 1, 2);
        juur.add(transportSilt, 0, 3);
        juur.add(transportTekst, 1, 3);
        juur.add(meelelahtusSilt, 0, 4);
        juur.add(meelelahutusTekst, 1, 4);
        juur.add(riided_jalatsidSilt, 0, 5);
        juur.add(riided_jalatsidTekst, 1, 5);
        juur.add(ilu_tervisSilt, 0, 6);
        juur.add(ilu_tervisTekst, 1, 6);
        juur.add(muuSilt, 0, 7);
        juur.add(muuTekst, 1, 7);
        juur.add(kinnitaNupp, 0, 8, 2, 1);
        GridPane.setHalignment(kinnitaNupp, HPos.CENTER);

        kinnitaNupp.setOnAction(event -> {
            double eelarvedKokku = 0;
            double vahe = 0;
            try {
                for (int i = 0; i < kulutused.size() - 2; i++) {
                    double eelarveSumma = kulutused.get(i).lisaEelarve(tekstid.get(i));
                    eelarvedKokku += eelarveSumma;
                }
                kokku.lisaKokkuEelarve(eelarvedKokku, tulu - säästusumma);
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
                säästud.lisaKulu(vahe);
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
            valiTegevus(primaryStage);
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
        Button kommunaalkulud = new Button("kommunaalkulud");
        Button söök = new Button("söök");
        Button transport = new Button("transport");
        Button meelelahutus = new Button("meelelahutus");
        Button riided_ja_jalatsid = new Button("riided/jalatsid");
        Button ilu_ja_tervis = new Button("ilu/tervis");
        Button muu = new Button("muu");

        HBox nupud = new HBox(üür, kommunaalkulud, söök, transport, meelelahutus, riided_ja_jalatsid, ilu_ja_tervis, muu);
        nupud.setAlignment(Pos.CENTER);
        nupud.setSpacing(10);

        vBox.getChildren().addAll(valdkondSilt, nupud);

        juur.setCenter(vBox);
        Scene stseen = new Scene(juur, 700, 300);
        primaryStage.setScene(stseen);

        üür.setOnAction(event -> lisaKulu(primaryStage, kulutused.get(0)));
        kommunaalkulud.setOnAction(event -> lisaKulu(primaryStage, kulutused.get(1)));
        söök.setOnAction(event -> lisaKulu(primaryStage, kulutused.get(2)));
        transport.setOnAction(event -> lisaKulu(primaryStage, kulutused.get(3)));
        meelelahutus.setOnAction(event -> lisaKulu(primaryStage, kulutused.get(4)));
        riided_ja_jalatsid.setOnAction(event -> lisaKulu(primaryStage, kulutused.get(5)));
        ilu_ja_tervis.setOnAction(event -> lisaKulu(primaryStage, kulutused.get(6)));
        muu.setOnAction(event -> lisaKulu(primaryStage, kulutused.get(7)));
    }

    private void lisaKulu(Stage primaryStage, Kulud valdkond) {
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
            valdkond.lisaKulu(Double.parseDouble(kuluTekst.getText()));
            valdkond.protsent();
            kulutused.get(8).lisaKulu(Double.parseDouble(kuluTekst.getText()));
            kulutused.get(8).protsent();
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setHeaderText(null);
            info.setTitle("");
            info.setContentText("Summa valdkonna " + "\"" + valdkond.getNimetus() + "\"" + " kuludesse lisatud");
            info.showAndWait();
            valiTegevus(primaryStage);
        });
    }

    private void vaataÜlevaadet() {
        VBox juur = new VBox();
        Object[][] andmed = new Object[11][4];
        String[] pealkirjad = {"", "Planeeritud", "Tegelik kulu", "Protsent eelarvest"};
        int j = 1;
        for (int i = 0; i < kulutused.size(); i++) {
            andmed[i][0] = kulutused.get(i).getNimetus();
            for (double v : kulutused.get(i).getKategooria()) {
                andmed[i][j] = v;
                j++;
            }
            j = 1;
        }
        andmed[9] = new Object[]{"", "", "", ""};
        andmed[10][0] = kulutused.get(9).getNimetus();
        for (double v : kulutused.get(9).getKategooria()) {
            andmed[10][j] = v;
            j++;
        }

        ObservableList<Object[]> read = FXCollections.observableArrayList();
        read.addAll(Arrays.asList(andmed));
        TableView<Object[]> tabel = new TableView<>();
        for (int i = 0; i < pealkirjad.length; i++) {
            TableColumn<Object[], Object> veerg = new TableColumn<>(pealkirjad[i]);
            final int veerunr = i;
            veerg.setCellValueFactory(param -> new SimpleObjectProperty<>((param.getValue()[veerunr])));
            tabel.getColumns().add(veerg);
        }

        tabel.setItems(read);

        juur.getChildren().add(tabel);

        Stage stage = new Stage();
        Scene scene = new Scene(juur, 400, 300);
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
