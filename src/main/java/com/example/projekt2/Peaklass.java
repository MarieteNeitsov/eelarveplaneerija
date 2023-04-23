package com.example.projekt2;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Peaklass extends Application {
    static double tulu;
    static List<Kulud> kulutused;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
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
        kulutused = new ArrayList<>(Arrays.asList(üür, kommunaalkulud, söök, transport, meelelahutus, riided_ja_jalatsid, ilu_ja_tervis, muu));

        BorderPane juur = new BorderPane();
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);

        Label tuluSilt = new Label("Sisesta oma selle kuu tulu:");
        TextField tekst = new TextField();
        Button edasiNupp = new Button("Edasi");
        vbox.getChildren().addAll(tuluSilt,tekst,edasiNupp);

       edasiNupp.setOnAction(event -> {
            String tuluTekst = tekst.getText();
            try {
                tulu = Double.parseDouble(tuluTekst);
                planeeriEelarved(primaryStage);
            } catch (NumberFormatException e) { //äkki mingi muu erind,veel ei tea:/

            }
        });

        juur.setCenter(vbox);

        Scene stseen = new Scene(juur, 400, 400);
        primaryStage.setScene(stseen);
        primaryStage.show();
    }
    private void planeeriEelarved(Stage primaryStage) {

        GridPane juur = new GridPane();
        juur.setAlignment(Pos.CENTER);
        juur.setHgap(10);
        juur.setVgap(10);
        juur.setPadding(new Insets(10, 10, 10, 10));

        Label üürSilt = new Label("Üür:");
        TextField üürTekst = new TextField();
        Label  kommunaalkuludSilt = new Label("Kommunaalkulud:");
        TextField kommunaalkuludTekst = new TextField();
        Label söökSilt = new Label("Söök:");
        TextField söökTekst = new TextField();
        Label transportSilt = new Label("Transport");
        TextField transportTekst= new TextField();
        Label meelelahtusSilt = new Label("Meelelahutus:");
        TextField meelelahutusTekst = new TextField();
        Label riided_jalatsidSilt = new Label("Riided/jalatsid:");
        TextField riided_jalatsidTekst = new TextField();
        Label ilu_tervisSilt = new Label("ilu/tervis:");
        TextField ilu_tervisTekst = new TextField();
        Label muuSilt = new Label("ilu/tervis:");
        TextField muuTekst = new TextField();
        Button kinnitaNupp = new Button("Kinnita");

        kinnitaNupp.setOnAction(event -> { /*mingi meetod vms, mis võtab tekstist summa*/ });

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
        juur.add(kinnitaNupp, 0, 8,2,1);
        GridPane.setHalignment(kinnitaNupp, HPos.CENTER);

        Scene scene = new Scene(juur, 400, 400);
        primaryStage.setScene(scene);
    }
}
