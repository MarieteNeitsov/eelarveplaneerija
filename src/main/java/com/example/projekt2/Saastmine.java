package com.example.projekt2;

import java.util.Random;

public class Saastmine {
    private double tulu;

    public Saastmine(double tulu) {
        this.tulu = tulu;
    }

    public int säästa() {
        Random suvaline = new Random();
        int protsent;
        if (tulu > 1000) {
            protsent = suvaline.nextInt(20) + 1;
        } else
            protsent = suvaline.nextInt(10) + 1;
        double sääst = protsent / 100.0 * tulu;
        int tulemus = (int) Math.ceil(sääst);
        return tulemus;
    }


}
