package com.dp.persistentie;

import com.dp.domein.OVChipkaart;
import com.dp.domein.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPsql implements ProductDAO {
    private Connection conn;

    public ProductDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Product product) {
        String query = """
                INSERT INTO product (
                    product_nummer, 
                    naam, 
                    beschrijving, 
                    prijs) 
                VALUES (?, ?, ?, ?)""";

        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, product.getProductNummer());
            pst.setString(2, product.getNaam());
            pst.setString(3, product.getBeschrijving());
            pst.setDouble(4, product.getPrijs());
            pst.executeUpdate();

            // Insert `kaart_nummer` from table `ov_chipkaart`
            // and `productnummer` from table `product`
            // into the `ov_chipkaart_product`
            for (OVChipkaart ovChipkaart : product.getOvChipkaarten()) {
                String query2 = """
                        INSERT INTO ov_chipkaart_product (
                            kaart_nummer, 
                            product_nummer)
                        VALUES (?, ?)""";
                pst = conn.prepareStatement(query2);
                pst.setInt(1, ovChipkaart.getKaartNummer());
                pst.setInt(2, product.getProductNummer());
                product.setProductNummer(product.getProductNummer());
                pst.executeUpdate();
            }
            pst.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Product product) {
        String query = """
                UPDATE 
                    product
                SET 
                    naam = ?, 
                    beschrijving = ?, 
                    prijs = ?
                WHERE product_nummer = ?""";

        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, product.getNaam());
            pst.setString(2, product.getBeschrijving());
            pst.setDouble(3, product.getPrijs());
            pst.setInt(4, product.getProductNummer());
            pst.executeUpdate();
            pst.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Product product) {
        String query = """
                DELETE FROM 
                    ov_chipkaart_product 
                WHERE 
                    product_nummer = ?""";

        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, product.getProductNummer());
            pst.executeUpdate();

            String query2 = """
                    DELETE FROM product WHERE product_nummer = ?
                    """;

            pst = conn.prepareStatement(query2);
            pst.setInt(1, product.getProductNummer());
            pst.executeUpdate();
            pst.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        List<Product> productList = new ArrayList<>();
        String query = """
                SELECT p.product_nummer, p.naam, p.beschrijving, p.prijs FROM product p
                    JOIN ov_chipkaart_product ocp 
                        ON p.product_nummer = ocp.product_nummer
                    JOIN ov_chipkaart oc 
                        ON oc.kaart_nummer = ocp.kaart_nummer                   
                WHERE ocp.kaart_nummer = ?""";

        try{
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, ovChipkaart.getKaartNummer());
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setProductNummer(rs.getInt(1));
                product.setNaam(rs.getString(2));
                product.setBeschrijving(rs.getString(3));
                product.setPrijs(rs.getDouble(4));
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    @Override
    public List<Product> findAll() {
        List<Product> productList = new ArrayList<>();
        String query = """
                SELECT
                    product_nummer,
                    naam,
                    beschrijving,
                    prijs
                FROM product;
                """;
        try {
            PreparedStatement preparedState = conn.prepareStatement(query);
            ResultSet rs = preparedState.executeQuery();

            while(rs.next()) {
                Product product = new Product();
                product.setProductNummer(rs.getInt(1));
                product.setNaam(rs.getString(2));
                product.setBeschrijving(rs.getString(3));
                product.setPrijs(rs.getDouble(4));
                productList.add(product);
            }
            preparedState.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }
}
