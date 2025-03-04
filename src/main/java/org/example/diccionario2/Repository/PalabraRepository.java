package org.example.diccionario2.Repository;

import org.example.diccionario2.Model.Palabra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PalabraRepository extends JpaRepository<Palabra, Integer> {
    @Query("SELECT p FROM Palabra p WHERE p.termino = ?1")
    Palabra existeTermino(String termino);
    @Query("SELECT p FROM Palabra p WHERE p.termino LIKE :letra%")
    List<Palabra> findByInicial(@Param("letra") String letra);

    List<Palabra> findByCategoriaGramatical(String categoriaGramatical);

}
