package com.dp;

import com.dp.domein.Adres;
import com.dp.domein.OVChipkaart;
import com.dp.domein.Product;
import com.dp.domein.Reiziger;
import com.dp.persistentie.*;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {
    private static Connection connection;

    public static void main(String[] args) {
        getConnection();
        closeConnection();
    }

    private static Connection getConnection() {
        Dotenv dotenv = Dotenv.load();

        String jdbcurl = "jdbc:postgresql://localhost:5432/ovchip";
        String username = "postgres";
        String password = dotenv.get("POSTGRES_SECRET");

        try {
            if (connection == null) {
                connection = DriverManager.getConnection(jdbcurl,username,password);

                System.out.println("Connected with the database");
                System.out.println("=======================");

                ReizigerDAOPsql reizigerDAOsql = new ReizigerDAOPsql(connection);
//                testReizigerDAO(reizigerDAOsql);

                AdresDAOPsql adresDAOsql = new AdresDAOPsql(connection);
                // testAdresDAO(adresDAOsql, reizigerDAOsql);

                OVChipkaartDAOPsql ovChipkaartDAOPsql = new OVChipkaartDAOPsql(connection);
//                testOVChipkaartDAO(ovChipkaartDAOPsql, reizigerDAOsql);

                ProductDAOPsql productDAOPsql = new ProductDAOPsql(connection);
                testOVChipkaartProductDAO(ovChipkaartDAOPsql, productDAOPsql, reizigerDAOsql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");
        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();

        System.out.println("[Test] AdresDAO.findAll() geeft de volgende reizigers:");

        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        // initiate new object of Reiziger
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        // save
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        for (int i = 1; i <= 5 ; i++) {
            System.out.println("[Test] " + rdao.findById(i));
        }

        System.out.println();
        System.out.println("==== findById ====");
        System.out.println("[Test] " + rdao.findById(77));
        System.out.println();

        System.out.println("==== findByGbdatum ====");
        System.out.println("[Test] " + rdao.findByGbdatum("2002-09-17"));
        System.out.println("[Test] " + rdao.findByGbdatum("2002-10-22"));
        System.out.println("[Test] " + rdao.findByGbdatum("1998-08-11"));
        System.out.println();

        // update
        System.out.println("=== update reiziger ====");
        Reiziger updateReiziger = new Reiziger(
                77, "A", "", "Steen", Date.valueOf("1982-03-14"));
        rdao.update(updateReiziger);

        // delete (ALLEEN TOEPASSEN ALS ER EEN REIZIGER MET ID 77 OF ANDERE GELIJK ID AAN DE DB IS TOEGEVOEGD)
        System.out.println("==== verwijder reiziger ====");
        var verwijderReiziger = new Reiziger();
        verwijderReiziger.setId(77);
        rdao.delete(verwijderReiziger);
    }

    public static void testAdresDAO(AdresDAO adao, ReizigerDAO rdao) throws SQLException {
        Reiziger reiziger1 = new Reiziger(
                77, "A", "", "Steen", Date.valueOf("1982-03-14"));

        Adres adres1 = new Adres(
                6, "2367FG", "54", "Teststraat", "Den Haag", reiziger1);

        // Start Create
        rdao.save(reiziger1);

        adao.save(adres1);
        // End Create

        // Start Read
        System.out.println("\n---------- Test AdresDAO -------------\n");
        System.out.println("Read");
        System.out.println("------------------------------");
        // Haal alle adressen op uit de database
        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende reizigers:");
        for (Adres a : adressen) {
            System.out.println(a);
        }

        System.out.println();
        System.out.println("[Test] AdresDAO.findByReiziger() geeft de volgende adressen:");
        System.out.println(adao.findByReiziger(reiziger1));
        System.out.println();
        // End Read

        // Start Read Reiziger and adres
        System.out.println("---- Read all reizigers and Adressen");
        List<Reiziger> reizigers = rdao.findAll();

        for (int i = 1; i <= 5 ; i++) {
            System.out.printf("[Test] Reiziger {#%s}" +
                     ", Adres {# %s}\n", rdao.findById(i),
                    adao.findByReiziger(reizigers.get(i - 1)));
        }
        // Read the new added reiziger and adres
        System.out.println();

        System.out.println("#" + reiziger1 +  ", #" + adao.findByReiziger(reiziger1));
        // End Read Reiziger and adres
        System.out.println("------------------------------\n");

        // Start Update
        System.out.println("Update");
        System.out.println("------------------------------");
        adres1 = new Adres(
                6, "2877WD", "47", "Testlaan", "Delft", reiziger1);

        adao.update(adres1);

        System.out.println(adres1);
        System.out.println("------------------------------\n");
        // Start Update

        // Start Delete
        System.out.println("Delete");
        System.out.println("------------------------------");
        var verwijderAdres = new Adres();
        verwijderAdres.setId(adres1.getId());
        adao.delete(verwijderAdres);

        var verwijderReiziger = new Reiziger();
        verwijderReiziger.setId(reiziger1.getId());
        rdao.delete(verwijderReiziger);
        // End Delete
    }

    public static void testOVChipkaartDAO (OVChipkaartDAO ovdao, ReizigerDAO rdao) throws SQLException {
        // Start Create
        Reiziger reiziger1 = new Reiziger(
                77, "A", "", "Steen", Date.valueOf("1982-03-14"));

        rdao.save(reiziger1);

        OVChipkaart ovChipkaart1 = new OVChipkaart(
                12345, Date.valueOf("2025-09-30"), 1, 40.00, reiziger1);
        OVChipkaart ovChipkaart2 = new OVChipkaart(
                12346, Date.valueOf("2028-09-30"), 2, 5.00, reiziger1);

        ovdao.save(ovChipkaart1);
        ovdao.save(ovChipkaart2);
        // End Create

        System.out.println("\n---------- Test AdresDAO -------------\n");

        // Start Read
        System.out.println("Read");
        System.out.println("------------------------------");
        List<OVChipkaart> ovChipkaarten = ovdao.findAll();

        for (OVChipkaart ovChipkaart : ovChipkaarten) {
            System.out.println(ovChipkaart);
        }
        System.out.println("------------------------------\n");
        // End Read

        // Start update
        ovChipkaart1 = new OVChipkaart(
                12345, Date.valueOf("2026-09-30"), 3, 10.00, reiziger1);
        // End update
        ovdao.update(ovChipkaart1);

        // Start read findByReiziger
        System.out.println("Read findByReiziger");
        for (int i = 0; i < ovdao.findByReiziger(reiziger1).size(); i++) {
            System.out.println(ovdao.findByReiziger(reiziger1).get(i));
        }
        System.out.println("------------------------------\n");
        // End read findByReiziger

        // Start Delete
        System.out.println("Delete");
        System.out.println("------------------------------");
        ovdao.delete(ovChipkaart1);
        ovdao.delete(ovChipkaart2);

        rdao.delete(reiziger1);
        // End Delete
    }

    public static void testOVChipkaartProductDAO(OVChipkaartDAO ovdao, ProductDAO pdao, ReizigerDAO rdao) throws SQLException {
        // Start Create
        // Reiziger
        Reiziger reiziger1 = new Reiziger(
                77, "A", "", "Steen", Date.valueOf("1982-03-14"));
        // Save Reiziger
        rdao.save(reiziger1);

        // OVChipkaart
        OVChipkaart ovChipkaart1 = new OVChipkaart(
                12345, Date.valueOf("2026-09-30"), 2, 10.00, reiziger1);

        OVChipkaart ovChipkaart2 = new OVChipkaart(
                12346, Date.valueOf("2026-09-30"), 2, 10.00, reiziger1);

        // Save OVChipkaarten
        ovdao.save(ovChipkaart1);
        ovdao.save(ovChipkaart2);

        // Product
        Product product1 = new Product(
                7, "Seniorenproduct", "20% korting voor 65+", 30.00);

        Product product2 = new Product(
                8, "Retour Amsterdam - Antwerpen", "Retourskorting 10%", 25.00);

        // Save producten
        ovChipkaart1.addProduct(product1);
        ovChipkaart2.addProduct(product1);
        ovChipkaart2.addProduct(product2);

        pdao.save(product1);
        pdao.save(product2);
        // End Create

        // Start Read
        // FindByOVChipkaart
        List<Product> findByOV = pdao.findByOVChipkaart(ovChipkaart2);
        System.out.println("FindByOVChipkaart -----------------------");
        for (Product productMetOv : findByOV) {
            System.out.println(productMetOv);
        }
        System.out.println("-----------------------\n");
        // FindAll
        List<Product> producten = pdao.findAll();
        System.out.println("Alle Product -----------------------");
        for (Product product : producten) {
            System.out.println(product);
        }
        System.out.println("-----------------------\n");

        System.out.println("OVChip -----------------------");
        List<OVChipkaart> ovChipkaarten = ovdao.findAll();
        for (OVChipkaart ovChipkaart : ovChipkaarten) {
            System.out.println(ovChipkaart);
        }
        System.out.println("-----------------------\n");
        // End Read

        // Start Update
        product1 = new Product(
                7, "Seniorenproduct", "20% korting voor 65+", 35.00);

        pdao.update(product1);

        System.out.println("After update -----------------------");
        System.out.println(ovChipkaart1);
        System.out.println("-----------------------\n");
        // End Update

        // Start Delete
        pdao.delete(product1);
        pdao.delete(product2);
        ovdao.delete(ovChipkaart1);
        ovdao.delete(ovChipkaart2);
        rdao.delete(reiziger1);
        // Start Delete
    }
}
