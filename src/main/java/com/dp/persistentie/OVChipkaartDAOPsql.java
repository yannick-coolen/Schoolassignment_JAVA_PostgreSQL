package com.dp.persistentie;

import com.dp.domein.OVChipkaart;
import com.dp.domein.Product;
import com.dp.domein.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO{
    private Connection conn;
    private ReizigerDAO rdao;
    private ProductDAO pdao;

    public OVChipkaartDAOPsql(Connection conn) {
        this.conn = conn;
        this.rdao = new ReizigerDAOPsql(conn);
        this.pdao = new ProductDAOPsql(conn);
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) {
        String query = "INSERT INTO ov_chipkaart VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, ovChipkaart.getKaartNummer());
            pst.setDate(2, ovChipkaart.getGeldigTot());
            pst.setInt(3, ovChipkaart.getKlasse());
            pst.setDouble(4, ovChipkaart.getSaldo());
            pst.setInt(5, ovChipkaart.getReiziger().getId());
            pst.executeUpdate();
            pst.close();

            if (this.pdao != null) {
                for (Product product : ovChipkaart.getProducten()) {
                    pdao.save(product);
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) {
        String query = """
                UPDATE
                    ov_chipkaart
                SET
                    geldig_tot = ?,
                    klasse = ?,
                    saldo = ?,
                    reiziger_id = ?
                WHERE
                    kaart_nummer = ?""";

        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setDate(1, ovChipkaart.getGeldigTot());
            pst.setInt(2, ovChipkaart.getKlasse());
            pst.setDouble(3, ovChipkaart.getSaldo());
            pst.setInt(4, ovChipkaart.getReiziger().getId());
            pst.setInt(5, ovChipkaart.getKaartNummer());
            pst.executeUpdate();
            pst.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.printf("Update van adres ID %d is mislukt.\n",
                    ovChipkaart.getKaartNummer());
        }
        return false;
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        String query = "DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?";

        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, ovChipkaart.getKaartNummer());

            System.out.printf("OV-Chipkaart met de kaartnummer: %d, is uit de database verwijderd.\n",
                    ovChipkaart.getKaartNummer());

            pst.executeUpdate();

            String query2 = "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?";
            pst = conn.prepareStatement(query2);
            pst.setInt(1, ovChipkaart.getKaartNummer());
            pst.executeUpdate();
            pst.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        List<OVChipkaart> ovChipkaartList = new ArrayList<>();
        String query = """
                SELECT
                    kaart_nummer,
                    geldig_tot,
                    klasse,
                    saldo,
                    reiziger_id
                FROM
                    ov_chipkaart
                WHERE
                    reiziger_id = ?""";

        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, reiziger.getId());
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                OVChipkaart ovChipkaart = new OVChipkaart();
                ovChipkaart.setKaartNummer(rs.getInt(1));
                ovChipkaart.setGeldigTot(rs.getDate(2));
                ovChipkaart.setKlasse(3);
                ovChipkaart.setSaldo(rs.getDouble(4));
                ovChipkaart.setReizigerId(5);
                ovChipkaart.setReiziger(reiziger);
                ovChipkaartList.add(ovChipkaart);
            }
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ovChipkaartList;
    }

    @Override
    public List<OVChipkaart> findAll() {
        List<OVChipkaart> ovChipkaartList = new ArrayList<>();
        String query = "SELECT * FROM ov_chipkaart";

        try {
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while(rs.next()) {
                OVChipkaart ovChipkaart = new OVChipkaart();
                ovChipkaart.setKaartNummer(rs.getInt(1));
                ovChipkaart.setGeldigTot(rs.getDate(2));
                ovChipkaart.setKlasse(rs.getInt(3));
                ovChipkaart.setSaldo(rs.getDouble(4));
                ovChipkaart.setReizigerId(rs.getInt(5));
                ovChipkaart.setReiziger(rdao.findById(ovChipkaart.getReizigerId()));
                ovChipkaartList.add(ovChipkaart);
            }
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ovChipkaartList;
    }
}
