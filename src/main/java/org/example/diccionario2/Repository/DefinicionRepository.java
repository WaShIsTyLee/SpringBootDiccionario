package org.example.diccionario2.Repository;

import org.example.diccionario2.Model.Definicion;
import org.example.diccionario2.Model.Palabra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DefinicionRepository extends JpaRepository<Definicion, Integer> {

    @Query("SELECT d FROM Definicion d WHERE d.palabra.id = ?1")
    List<Definicion> getDefinicionesByPalabraID(int id);

}
