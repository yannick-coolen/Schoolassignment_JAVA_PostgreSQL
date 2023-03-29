package com.dp.persistentie;

import com.dp.domein.Adres;
import com.dp.domein.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {
    private Connection conn;
    private ReizigerDAO rdao;

    public AdresDAOPsql(Connection conn) {
        this.conn = conn;
        this.rdao = new ReizigerDAOPsql(conn);
    }

    @Override
    public boolean save(Adres adres) {
        String query = """
                INSERT INTO adres (
                    adres_id, 
                    postcode, 
                    huisnummer, 
                    straat, 
                    woonplaats, 
                    reiziger_id) 
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, adres.getId());
            pst.setString(2, adres.getPostcode());
            pst.setString(3, adres.getHuisnummer());
            pst.setString(4, adres.getStraat());
            pst.setString(5, adres.getWoonplaats());
            pst.setInt(6, adres.getReiziger().getId());
            pst.executeUpdate();
            pst.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("(Toevoeging adres mislukt)");
        }
        return false;
    }

    @Override
    public boolean update(Adres adres) {
        String query = """
            UPDATE adres
            SET 
                adres_id = ?, 
                postcode = ?, 
                huisnummer = ?, 
                straat = ?, 
                woonplaats = ?
            WHERE 
                reiziger_id = ?
            """;

        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, adres.getId());
            pst.setString(2, adres.getPostcode());
            pst.setString(3, adres.getHuisnummer());
            pst.setString(4, adres.getStraat());
            pst.setString(5, adres.getWoonplaats());
            pst.setInt(6, adres.getReizigerId());
            pst.executeUpdate();
            pst.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.printf("Update van adres ID %d is mislukt", adres.getId());
        }
        return false;
    }

    @Override
    public boolean delete(Adres adres) {
        String query = "DELETE FROM adres WHERE adres_id = ?";

        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, adres.getId());

            System.out.printf(
                    "Adres met ID %d succesvol uit de DB verwijderd\n",
                    adres.getId());

            pst.executeUpdate();
            pst.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Verwijdering van adres mislukt");
        }
        return false;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        Adres adres = new Adres();
        String query = """
                SELECT 
                    adres_id,
                    postcode,
                    huisnummer,
                    straat,
                    woonplaats,
                    reiziger_id
                FROM 
                    adres 
                WHERE 
                reiziger_id = ?""";

        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, reiziger.getId());
            ResultSet rs = pst.executeQuery();

            while(rs.next()) {
                adres.setId(rs.getInt(1));
                adres.setPostcode(rs.getString(2));
                adres.setHuisnummer(rs.getString(3));
                adres.setStraat(rs.getString(4));
                adres.setWoonplaats(rs.getString(5));
                adres.setReizigerId(rs.getInt(6));
                adres.setReiziger(rdao.findById(adres.getReizigerId()));
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adres;
    }

    @Override
    public List<Adres> findAll() {
        List<Adres> adresList = new ArrayList<>();
        String query = """
            SELECT 
                adres_id,
                postcode,
                huisnummer,
                straat,
                woonplaats,
                reiziger_id
            FROM 
            adres""";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Adres adres = new Adres();
                adres.setId(rs.getInt(1));
                adres.setPostcode(rs.getString(2));
                adres.setHuisnummer(rs.getString(3));
                adres.setStraat(rs.getString(4));
                adres.setWoonplaats(rs.getString(5));
                adres.setReizigerId(rs.getInt(6));
                adres.setReiziger(rdao.findById(adres.getReizigerId()));
                adresList.add(adres);
            }
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adresList;
    }
}
