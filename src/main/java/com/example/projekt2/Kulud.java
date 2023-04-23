package com.example.projekt2;

public class Kulud {

    private double[] kategooria;
    private String nimetus;

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
        if (kategooria[2] > 75 && kategooria[2] < 100)
            System.out.println("Oled kulutanud üle 75% selle kategooria eelarvest. Piira oma edasisi kulutusi!");
        else if (kategooria[2] > 100) {
            System.out.println("Oled ületanud eelarve " + nimetus + " " + (kategooria[2] - kategooria[1]) + " euro võrra!");
        }
    }
}
