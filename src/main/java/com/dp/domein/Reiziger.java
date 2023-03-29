package com.dp.domein;

import java.sql.Date;
import java.util.List;

public class Reiziger {
    private int id;
    private String voorletters, tussenvoegsel, achternaam;
    private Date geboortedatum;
    private Adres adres;
    private List<OVChipkaart> ovChipkaart;

    public Reiziger() { }

    public Reiziger(
            int id,
            String voorletters,
            String tussenvoegsel,
            String achternaam,
            Date geboortedatum) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        if (voorletters != null && !voorletters.equals("")) {
            this.voorletters = voorletters;
        }
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        if (tussenvoegsel != null && !tussenvoegsel.equals("")) {
            this.tussenvoegsel = tussenvoegsel;
        }
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        if (achternaam != null && !achternaam.equals("")) {
            this.achternaam = achternaam;
        }
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(Date geboortedatum) {
        if (geboortedatum != null) {
            this.geboortedatum = geboortedatum;
        }
    }

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public List<OVChipkaart> getOvChipkaart() {
        return ovChipkaart;
    }

    public void setOvChipkaart(List<OVChipkaart> ovChipkaart) {
        this.ovChipkaart = ovChipkaart;
    }

    @Override
    public String toString() {
        return String.format("%d: %s.%s %s",
                getId(),
                getVoorletters(),
                getTussenvoegsel() == null || getTussenvoegsel().equals("")
                        ? "" : " " + getTussenvoegsel(),
                getAchternaam()) + getGeboortedatum();
    }
}
