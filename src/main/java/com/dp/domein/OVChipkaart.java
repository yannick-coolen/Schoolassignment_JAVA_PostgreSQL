package com.dp.domein;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaart {
    private int kaartNummer;
    private Date geldigTot;
    private int klasse;
    private double saldo;
    private int reizigerId;
    private Reiziger reiziger;
    private List<Product> producten;

    public OVChipkaart() {}

    public OVChipkaart(
            int kaartNummer,
            Date geldigTot,
            int klasse,
            double saldo,
            Reiziger reiziger) {
        this.kaartNummer = kaartNummer;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
        this.producten = new ArrayList<>();
    }

    public int getKaartNummer() {
        return kaartNummer;
    }

    public void setKaartNummer(int kaartNummer) {
        this.kaartNummer = kaartNummer;
    }

    public Date getGeldigTot() {
        return geldigTot;
    }

    public void setGeldigTot(Date geldigTot) {
        if (geldigTot != null) {
            this.geldigTot = geldigTot;
        }
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public int getReizigerId() {
        return reizigerId;
    }

    public void setReizigerId(int reizigerId) {
        this.reizigerId = reizigerId;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        if (reiziger != null) {
            this.reiziger = reiziger;
        }
    }

    public List<Product> getProducten() {
        return producten;
    }

    public void setProducten(List<Product> producten) {
        this.producten = producten;
    }

    public void addProduct(Product product) {
        if (product != null) {
            if (!this.producten.contains(product)) {
                producten.add(product);
                product.addOVChipkaart(this);
            }
        }
    }

    public void removeProduct(Product product) {
        if (product != null) {
            if (this.producten.contains(product)) {
                producten.remove(product);
                product.removeOVChipkaart(this);
            }
        }
    }

    @Override
    public String toString() {
        return String.format(
                "#%d, Datum: %s, Klasse: %d, Saldo: %.2f, ReizigerID: %s",
                getKaartNummer(),
                getGeldigTot(),
                getKlasse(),
                getSaldo(),
                getReiziger().getId());
    }
}
