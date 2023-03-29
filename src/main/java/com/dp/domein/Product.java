package com.dp.domein;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private int productNummer;
    private String naam;
    private String beschrijving;
    private double prijs;
    private List<OVChipkaart> ovChipkaarten;

    public Product() { }

    public Product(int productNummer, String naam, String beschrijving, double prijs) {
        this.productNummer = productNummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
        this.ovChipkaarten = new ArrayList<>();
    }

    public int getProductNummer() {
        return productNummer;
    }

    public void setProductNummer(int productNummer) {
        this.productNummer = productNummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        if (naam != null) {
            this.naam = naam;
        }
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        if (beschrijving != null) {
            this.beschrijving = beschrijving;
        }
    }

    public double getPrijs() {
        return prijs;
    }

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    public List<OVChipkaart> getOvChipkaarten() {
        return ovChipkaarten;
    }

    public void setOvChipkaart(List<OVChipkaart> ovChipkaarten) {
        this.ovChipkaarten = ovChipkaarten;
    }

    public void addOVChipkaart(OVChipkaart ovChipkaart) {
        if (ovChipkaart != null) {
            if (!this.ovChipkaarten.contains(ovChipkaart)) {
                ovChipkaarten.add(ovChipkaart);
                ovChipkaart.addProduct(this);
            }
        }
    }

    public void removeOVChipkaart(OVChipkaart ovChipkaart) {
        if (ovChipkaart != null) {
            if (this.getOvChipkaarten().contains(ovChipkaart)) {
                ovChipkaarten.remove(ovChipkaart);
                ovChipkaart.removeProduct(this);
            }
        }
    }

    @Override
    public String toString() {
        return String.format("" +
                        "Productnummer: %d, " +
                        "Naam: %s, " +
                        "Beschrijving: %s, " +
                        "Prijs: %.2f " + (getPrijs() != 0.0 ? "euro" : ""),
                getProductNummer(),
                getNaam(),
                getBeschrijving(),
                getPrijs());
    }
}
