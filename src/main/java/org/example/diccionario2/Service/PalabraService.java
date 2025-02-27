package org.example.diccionario2.Service;

import org.example.diccionario2.Exception.RecordNotFoundException;
import org.example.diccionario2.Model.Palabra;
import org.example.diccionario2.Repository.PalabraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PalabraService {

    @Autowired
    private PalabraRepository palabraRepository;

    public List<Palabra> getAllPalabras() {
        List<Palabra> palabras = palabraRepository.findAll();
        if (!palabras.isEmpty()) {
            return palabras;
        } else {
            return new ArrayList<>();
        }
    }

    public Palabra getPalabra(int id) throws RecordNotFoundException {
        Optional<Palabra> palabra = palabraRepository.findById(id);
        if (palabra.isPresent()) {
            return palabra.get();
        } else {
            throw new RecordNotFoundException("No se ha encontrado la palabra", id);
        }
    }

    public Palabra crearPalabra(Palabra palabra) {
        if (existePalabra(palabra.getTermino())) {
            throw new IllegalArgumentException("La palabra ya existe: " + palabra.getTermino());
        }
        return palabraRepository.save(palabra);
    }

    public boolean existePalabra(String termino) {
        if (palabraRepository.existeTermino(termino) != null) return true;
        else return false;
    }


    public Palabra updatePalabra(int id, Palabra palabra) throws RecordNotFoundException {
        if (palabra == null) {
            throw new RecordNotFoundException("El objeto palabra no puede ser nulo.", 0l);
        }
            Optional<Palabra> palabraOptional = palabraRepository.findById(id);
            if (palabraOptional.isPresent()) {
                Palabra newPalabra = palabraOptional.get();
                newPalabra.setTermino(palabra.getTermino());
                newPalabra.setCategoriaGramatical(palabra.getCategoriaGramatical());
                newPalabra = palabraRepository.save(newPalabra);
                return newPalabra;
            } else {
                throw new RecordNotFoundException("No existe palabra para el id: ", palabra.getId());
            }

    }
}
