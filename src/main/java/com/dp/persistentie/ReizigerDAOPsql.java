package com.dp.persistentie;

import com.dp.domein.OVChipkaart;
import com.dp.domein.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;
    private AdresDAO adao;
    private OVChipkaartDAO ovdao;

    public ReizigerDAOPsql(Connection conn) {
        this.conn = conn;
        this.adao = null;
        this.ovdao = null;
    }

    @Override
    public boolean save(Reiziger reiziger) {
        String query = "INSERT INTO reiziger VALUES (?, ?, ?, ?, ? )";

        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, reiziger.getId());
            pst.setString(2, reiziger.getVoorletters());
            pst.setString(3, reiziger.getTussenvoegsel());
            pst.setString(4, reiziger.getAchternaam());
            pst.setDate(5, reiziger.getGeboortedatum());
            pst.executeUpdate();
            pst.close();

            if (adao != null) {
                this.adao.save(reiziger.getAdres());
            }

            if (ovdao != null) {
                for (OVChipkaart ovChipkaart : reiziger.getOvChipkaart()) {
                    this.ovdao.save(ovChipkaart);
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("(Toevoeging mislukt)");
        }
        return false;
    }

    @Override
    public boolean update(Reiziger reiziger) {
        String query = """
                UPDATE reiziger
                SET
                    voorletters = ?,
                    tussenvoegsel = ?,
                    achternaam = ?,
                    geboortedatum = ?
                WHERE
                reiziger_id = ?""";

        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, reiziger.getVoorletters());
            pst.setString(2, reiziger.getTussenvoegsel());
            pst.setString(3, reiziger.getAchternaam());
            pst.setDate(4, reiziger.getGeboortedatum());
            pst.setInt(5, reiziger.getId());

            System.out.printf("Update van ID gebruiker %d succesvol voltooid.\n",
                    reiziger.getId());

            pst.executeUpdate();
            pst.close();

            if (adao != null) {
                this.adao.save(reiziger.getAdres());
            }

            if (ovdao != null) {
                for (OVChipkaart ovChipkaart : reiziger.getOvChipkaart()) {
                    this.ovdao.save(ovChipkaart);
                }
            }

            return true;
        } catch(SQLException e) {
            e.printStackTrace();
            System.err.println("Update mislukt");
        }
        return false;
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        String query = "DELETE FROM reiziger WHERE reiziger_id = ?";

        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, reiziger.getId());
            pst.executeUpdate();
            pst.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.printf("Verwijdering van reiziger %d mislukt\n",
                    reiziger.getId());
        }
        return false;
    }

    @Override
    public Reiziger findById(int id) {
        Reiziger reiziger = new Reiziger();
        String query = """
            SELECT
                reiziger_id,
                voorletters,
                tussenvoegsel,
                achternaam,
                geboortedatum
            FROM reiziger
            WHERE
                reiziger_id = ?""";

        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                reiziger.setId(rs.getInt(1));
                reiziger.setVoorletters(rs.getString(2));
                reiziger.setTussenvoegsel(rs.getString(3));
                reiziger.setAchternaam(rs.getString(4));
                reiziger.setGeboortedatum(rs.getDate(5));
            }
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reiziger;
    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) {
        Reiziger reiziger = new Reiziger();
        List<Reiziger> reizigerList = new ArrayList<>();
        String query = """
            SELECT
                reiziger_id,
                voorletters,
                tussenvoegsel,
                achternaam,
                geboortedatum
            FROM reiziger
            WHERE
                geboortedatum = ?""";

        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setDate(1, Date.valueOf(datum));
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                reiziger.setId(rs.getInt(1));
                reiziger.setVoorletters(rs.getString(2));
                reiziger.setTussenvoegsel(rs.getString(3));
                reiziger.setAchternaam(rs.getString(4));
                reiziger.setGeboortedatum(rs.getDate(5));

                reizigerList.add(reiziger);
            }
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reizigerList;
    }

    @Override
    public List<Reiziger> findAll() {
        List<Reiziger> reizigerList = new ArrayList<>();
        String query = """
            SELECT
                reiziger_id,
                voorletters,
                tussenvoegsel,
                achternaam,
                geboortedatum
            FROM reiziger""";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Reiziger reiziger = new Reiziger();
                reiziger.setId(rs.getInt(1));
                reiziger.setVoorletters(rs.getString(2));
                reiziger.setTussenvoegsel(rs.getString(3));
                reiziger.setAchternaam(rs.getString(4));
                reiziger.setGeboortedatum(rs.getDate(5));
                reizigerList.add(reiziger);
            }
            pst.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return reizigerList;
    }
}
