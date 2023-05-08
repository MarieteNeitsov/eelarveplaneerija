package com.example.projekt2;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Peaklass extends Application {
    static double tulu;
    static List<Kulud> kulutused;
    static double säästud;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
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
                //äkki mingi muu erind,veel ei tea:/

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
            säästmineTekst.setText(String.valueOf(säästmine.säästa()));
            säästud = Double.parseDouble(säästmineTekst.getText());
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
        kulutused = new ArrayList<>(Arrays.asList(üür, kommunaalkulud, söök, transport, meelelahutus, riided_ja_jalatsid, ilu_ja_tervis, muu));

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
            int i = 0;
            double eelarvedKokku = 0;
            try {
                for (Kulud kulu : kulutused) {
                    double eelarveSumma = kulu.lisaEelarve(tekstid.get(i));
                    eelarvedKokku += eelarveSumma;
                    i++;
                }
                kokku.lisaKokkuEelarve(eelarvedKokku, tulu - säästud);
                double vahe = tulu - säästud - eelarvedKokku;
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
            } catch (EelarvetestJäiÜleErind | KõikJagatudErind e) {
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setHeaderText(null);
                info.setTitle("");
                info.setContentText(e.getMessage());
                info.showAndWait();
                valiTegevus(primaryStage);
            }
            // uus meetod mis viib järgmisesse aknasse
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
        ülevaade.setOnAction(event -> vaataÜlevaadet(primaryStage));
//        lõpeta.setOnAction(event -> lõpetaTöö(primaryStage));
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

        HBox nupud = new HBox(üür,kommunaalkulud,söök,transport, meelelahutus, riided_ja_jalatsid, ilu_ja_tervis, muu);
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
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setHeaderText(null);
            info.setTitle("");
            info.setContentText("Summa valdkonna " + "\"" + valdkond.getNimetus() + "\"" + " kuludesse lisatud");
            info.showAndWait();
            valiTegevus(primaryStage);
        });
    }

    private void vaataÜlevaadet(Stage primaryStage) {
        VBox juur = new VBox();
        TableView<Kulud> tableView = new TableView<>();

        TableColumn<Kulud, String> valdkonnad = new TableColumn<>();
        TableColumn<Kulud, double[]> planeeritud = new TableColumn<>("Planeeritud");
        TableColumn<Kulud, double[]> tegelik = new TableColumn<>("Tegelik kulu");
        TableColumn<Kulud, double[]> protsent = new TableColumn<>("Protsent eelarvest");

//        valdkonnad.setCellValueFactory(new PropertyValueFactory<>("nimetus"));
//        planeeritud.setCellValueFactory(new PropertyValueFactory<>("kategooria[0]"));
//        tegelik.setCellValueFactory(new PropertyValueFactory<>("kategooria[1]"));
//        protsent.setCellValueFactory(new PropertyValueFactory<>("kategooria[2]"));

        tableView.getColumns().add(valdkonnad);
        tableView.getColumns().add(planeeritud);
        tableView.getColumns().add(tegelik);
        tableView.getColumns().add(protsent);

//        tableView.getItems().addAll(kulutused.get(0), kulutused.get(1), kulutused.get(2), kulutused.get(3), kulutused.get(4), kulutused.get(5), kulutused.get(6), kulutused.get(7));

        juur.getChildren().add(tableView);

        Scene scene = new Scene(juur, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}