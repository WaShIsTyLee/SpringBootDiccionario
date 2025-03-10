package org.example.diccionario2.Service;

import org.example.diccionario2.Exception.RecordNotFoundException;
import org.example.diccionario2.Model.Definicion;
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

    // Obtener todas las palabras del diccionario (GET /palabras)
    public List<Palabra> getAllPalabras() {
        List<Palabra> palabras = palabraRepository.findAll();
        if (!palabras.isEmpty()) {
            return palabras;
        } else {
            return new ArrayList<>();
        }
    }

    // Obtener una palabra específica con sus definiciones (GET /palabras/{id})
    public Palabra getPalabra(int id) throws RecordNotFoundException {
        Optional<Palabra> palabra = palabraRepository.findById(id);
        if (palabra.isPresent()) {
            return palabra.get();
        } else {
            throw new RecordNotFoundException("No se ha encontrado la palabra", id);
        }
    }

    // Agregar una nueva palabra sin definiciones (POST /palabras)
    public Palabra crearPalabra(Palabra palabra) {
        if (existePalabra(palabra.getTermino())) {
            throw new IllegalArgumentException("La palabra ya existe: " + palabra.getTermino());
        }
        return palabraRepository.save(palabra);
    }

    public List<Palabra> obtenerPalabrasPorCategoria(String categoriaGramatical) {
        return palabraRepository.findByCategoriaGramatical(categoriaGramatical);

    }

    // Verificar si una palabra ya existe en el diccionario
    public boolean existePalabra(String termino) {
        if (palabraRepository.existeTermino(termino) != null) return true;
        else return false;
    }

    public List<Palabra> obtenerPalabrasPorInicial(String letra) {
        if (letra.length() != 1 || !Character.isLetter(letra.charAt(0))) {
            throw new IllegalArgumentException("El parámetro debe ser una letra válida.");
        }
        return palabraRepository.findByInicial(letra.toUpperCase());
    }

    // Agregar una nueva definición a una palabra (POST /palabras/{id}/definiciones)
    public Palabra agregarDefinicion(int id, Definicion definicion) throws RecordNotFoundException {
        Optional<Palabra> optionalPalabra = palabraRepository.findById(id);
        if (!optionalPalabra.isPresent()) {
            throw new RecordNotFoundException("No se ha encontrado la palabra", id);
        }
        Palabra palabra = optionalPalabra.get();
        definicion.setPalabra(palabra);
        if (palabra.getDefiniciones() == null) {
            palabra.setDefiniciones(new ArrayList<>());
        }
        palabra.getDefiniciones().add(definicion);
        return palabraRepository.save(palabra);
    }

    // Actualizar una palabra existente (PUT /palabras/{id})
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

    // Eliminar una palabra y sus definiciones (DELETE /palabras/{id})
    public void deletePalabra(int id) throws RecordNotFoundException {
        Optional<Palabra> palabraOptional = palabraRepository.findById(id);
        if (palabraOptional.isPresent()) {
            palabraRepository.delete(palabraOptional.get());
        } else {
            throw new RecordNotFoundException("No existe Pelicula para el id: ", id);
        }
    }


}

