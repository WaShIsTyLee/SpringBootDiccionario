package org.example.diccionario2.Service;

import org.example.diccionario2.Exception.RecordNotFoundException;
import org.example.diccionario2.Model.Definicion;
import org.example.diccionario2.Model.Palabra;
import org.example.diccionario2.Repository.DefinicionRepository;
import org.example.diccionario2.Repository.PalabraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DefinicionService {

    @Autowired
    private DefinicionRepository definicionRepository;
    @Autowired
    private PalabraRepository palabraRepository;

    // Guarda la definición en la base de datos.
    public Definicion saveDefinicion(Definicion definicion) {
        return definicionRepository.save(definicion);
    }

    // Obtener todas las definiciones de una palabra específica
    public List<Definicion> getDefinicionesByPalabraID(int palabraID) {
        return definicionRepository.getDefinicionesByPalabraID(palabraID);

    }

    // Eliminar una definición específica (DELETE /definiciones/{id})
    public void deleteDefinicion(int id) throws RecordNotFoundException {
        Optional<Definicion> definicionOptional = definicionRepository.findById(id);
        if (definicionOptional.isPresent()) {
            Definicion definicion = definicionOptional.get();
            Palabra palabra = definicion.getPalabra();
            palabra.getDefiniciones().remove(definicion);
            palabraRepository.save(palabra);
            definicionRepository.delete(definicion);
        }
    }

}
