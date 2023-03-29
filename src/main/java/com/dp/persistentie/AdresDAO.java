package com.dp.persistentie;

import com.dp.domein.Adres;
import com.dp.domein.Reiziger;

import java.util.List;

public interface AdresDAO {
    boolean save(Adres adres);
    boolean update(Adres adres);
    boolean delete(Adres adres);
    Adres findByReiziger(Reiziger reiziger);
    List<Adres> findAll();
}
