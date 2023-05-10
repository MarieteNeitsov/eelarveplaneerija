package com.example.projekt2;

import javafx.scene.control.Alert;

public class Kulud {

    private double[] kategooria;
    private String nimetus;

    public Kulud(double[] kategooria, String nimetus) {
        this.kategooria = kategooria;
        this.nimetus = nimetus;
    }

    public Kulud(String nimetus) {
        kategooria = new double[3];
        this.nimetus = nimetus;
    }

    public double[] getKategooria() {
        return kategooria;
    }

    public String getNimetus() {
        return nimetus;
    }

    public double lisaEelarve(double summa) {
        // vb oleks parem kui olekd mingi üldisem meetod kasutaja sisestuse kontrollimiseks, sest kulutustel tuleks see sama läbi teha
        if (summa == 0) {
            kategooria[0] = 0;
        } else {
            try {
                if(summa < 0.0) throw new NumberFormatException();
                kategooria[0] = summa;
            } catch (NumberFormatException e) {
                // vaja midagi välja mõelda
            }
        }
        return kategooria[0];
    }

    public double lisaKokkuEelarve(double summa,double tulu) throws EelarvedÜletavadTuluErind{
        if(summa > tulu) throw new EelarvedÜletavadTuluErind("Sinu planeeritud eelarved ületavad tulu " + (summa - tulu) +" euro võrra");
        kategooria[0] = summa;
        return kategooria[0];
    }

    public double lisaKulu(double kulu) {
        kategooria[1] = kategooria[1] + kulu;
        return kategooria[1];
    }

    public int protsent() {
        kategooria[2] = (int) Math.round(kategooria[1] * 100 / kategooria[0]);
        return (int) kategooria[2];
    }

    public void ülePiiri() {
        if (kategooria[2] > 75 && kategooria[2] < 100) {
            Alert info = new Alert(Alert.AlertType.WARNING);
            info.setHeaderText(null);
            info.setTitle("");
            info.setContentText("Oled kulutanud üle 75% selle kategooria eelarvest. Piira oma edasisi kulutusi!");
            info.showAndWait();
        }
        else if (kategooria[2] > 100) {
            Alert info = new Alert(Alert.AlertType.WARNING);
            info.setHeaderText(null);
            info.setTitle("");
            info.setContentText("Oled ületanud kategooria \"" + nimetus + "\" eelarve " + (kategooria[1] - kategooria[0]) + " euro võrra!");
            info.showAndWait();
        }
    }
}
