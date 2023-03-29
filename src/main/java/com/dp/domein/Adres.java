package com.dp.domein;

public class Adres {
    private int id;
    private String postcode;
    private String huisnummer;
    private String straat;
    private String woonplaats;
    private int reizigerId;
    private Reiziger reiziger;

    public Adres() {}

    public Adres(
            int id,
            String postcode,
            String huisnummer,
            String straat,
            String woonplaats,
            Reiziger reiziger) {
        this.id = id;
        this.postcode = postcode;
        this.huisnummer = huisnummer;
        this.straat = straat;
        this.woonplaats = woonplaats;
        this.reiziger = reiziger;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        if (postcode != null && !postcode.equals("")) {
            this.postcode = postcode;
        }
    }

    public String getHuisnummer() {
        return huisnummer;
    }

    public void setHuisnummer(String huisnummer) {
        if (huisnummer != null && !huisnummer.equals("")) {
            this.huisnummer = huisnummer;
        }
    }

    public String getStraat() {
        return straat;
    }

    public void setStraat(String straat) {
        if (straat != null && !straat.equals("")) {
            this.straat = straat;
        }
    }

    public String getWoonplaats() {
        return woonplaats;
    }

    public void setWoonplaats(String woonplaats) {
        if (woonplaats != null && !woonplaats.equals("")) {
            this.woonplaats = woonplaats;
        }
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
        this.reiziger = reiziger;
    }


    @Override
    public String toString() {
        return String.format("%d: %s %s %s %s %s",
                getId(),
                getPostcode().equals("") ? "\b" : getPostcode(),
                getHuisnummer().equals("") ? "\b" : getHuisnummer(),
                getStraat().equals("") ? "\b" : getStraat() + ",",
                getWoonplaats().equals("") ? "\b" : getWoonplaats() + ",",
                getReiziger().getId());
    }
}
