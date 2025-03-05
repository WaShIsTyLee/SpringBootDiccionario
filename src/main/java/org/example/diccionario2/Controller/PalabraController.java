package org.example.diccionario2.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;

import org.example.diccionario2.Exception.RecordNotFoundException;
import org.example.diccionario2.Model.Definicion;
import org.example.diccionario2.Model.Palabra;
import org.example.diccionario2.Service.DefinicionService;
import org.example.diccionario2.Service.PalabraService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/palabras")
@Tag(name = "Palabra Resource", description = "Operaciones relacionadas con las palabras y sus definiciones")
public class PalabraController {

    @Autowired
    private PalabraService palabraService;

    @Autowired
    private DefinicionService definicionService;

    @CrossOrigin
    @GetMapping
    @Operation(
            summary = "Obtener todas las palabras",
            description = "Este endpoint obtiene una lista de todas las palabras disponibles en la base de datos sin incluir sus definiciones.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de palabras", content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "[{\"id\": 1, \"termino\": \"java\", \"categoriaGramatical\": \"sustantivo\"}, {\"id\": 2, \"termino\": \"python\", \"categoriaGramatical\": \"sustantivo\"}]"))
                    )
            }
    )
    public ResponseEntity<List<Map<String, Object>>> getAllPalabras() {
        List<Palabra> palabras = palabraService.getAllPalabras();
        List<Map<String, Object>> palabrasSinDefiniciones = new ArrayList<>();

        for (Palabra palabra : palabras) {
            Map<String, Object> palabraData = new HashMap<>();
            palabraData.put("id", palabra.getId());
            palabraData.put("termino", palabra.getTermino());
            palabraData.put("categoriaGramatical", palabra.getCategoriaGramatical());
            palabrasSinDefiniciones.add(palabraData);
        }
        return new ResponseEntity<>(palabrasSinDefiniciones, new HttpHeaders(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener palabra por ID",
            description = "Este endpoint obtiene una palabra específica junto con sus definiciones asociadas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Palabra con definiciones", content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"id\": 1, \"termino\": \"java\", \"categoriaGramatical\": \"sustantivo\", \"definiciones\": [{\"id\": 1, \"definicion\": \"Lenguaje de programación orientado a objetos\"}]}"))
                    ),
                    @ApiResponse(responseCode = "404", description = "Palabra no encontrada", content = @Content)
            }
    )
    public ResponseEntity<Palabra> getPalabra(@Parameter(description = "ID de la palabra a obtener") @PathVariable int id) {
        Palabra palabra = palabraService.getPalabra(id);
        List<Definicion> definiciones = definicionService.getDefinicionesByPalabraID(id);

        if (palabra == null) {
            throw new RecordNotFoundException("No se ha encontrado la palabra", id);
        }

        palabra.setDefiniciones(definiciones);
        return new ResponseEntity<>(palabra, new HttpHeaders(), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping
    @Operation(
            summary = "Crear nueva palabra",
            description = "Este endpoint crea una nueva palabra en la base de datos. Si la palabra ya existe, se generará un error.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Palabra creada", content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"id\": 1, \"termino\": \"java\", \"categoriaGramatical\": \"sustantivo\"}"))
                    ),
                    @ApiResponse(responseCode = "400", description = "La palabra ya existe", content = @Content)
            }
    )
    public ResponseEntity<Palabra> crearPalabra(@RequestBody Palabra palabra) {
        if (palabraService.existePalabra(palabra.getTermino())) {
            throw new RecordNotFoundException("La palabra ya existe: ", palabra.getTermino());
        }
        Palabra createdPalabra = palabraService.crearPalabra(palabra);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPalabra);
    }

    @CrossOrigin
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar palabra",
            description = "Este endpoint permite actualizar una palabra existente.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Palabra actualizada", content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"id\": 1, \"termino\": \"java\", \"categoriaGramatical\": \"sustantivo\"}"))
                    ),
                    @ApiResponse(responseCode = "404", description = "Palabra no encontrada", content = @Content)
            }
    )
    public ResponseEntity<Palabra> updatePalabra(@PathVariable int id, @RequestBody Palabra palabra) throws RecordNotFoundException {
        Palabra updatedPalabra = palabraService.updatePalabra(id, palabra);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPalabra);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar palabra",
            description = "Este endpoint permite eliminar una palabra por su ID.",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Palabra eliminada correctamente", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Palabra no encontrada", content = @Content)
            }
    )
    public HttpStatus deletePalabra(@Parameter(description = "ID de la palabra a eliminar") @PathVariable int id) throws RecordNotFoundException {
        palabraService.deletePalabra(id);
        return HttpStatus.ACCEPTED;
    }

    @CrossOrigin
    @PostMapping("/con-definiciones")
    public ResponseEntity<Palabra> crearPalabraConDefiniciones(@RequestBody Palabra palabra) {
        if (palabraService.existePalabra(palabra.getTermino())) {
            throw new RecordNotFoundException("La palabra ya existe: ", palabra.getTermino());
        }
        Palabra createdPalabra = palabraService.crearPalabra(palabra);
        if (palabra.getDefiniciones() != null && !palabra.getDefiniciones().isEmpty()) {
            for (Definicion definicion : palabra.getDefiniciones()) {
                definicion.setPalabra(createdPalabra);
                definicionService.saveDefinicion(definicion);
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPalabra);
    }

    @CrossOrigin
    @GetMapping("/categoria/{categoriaGramatical}")
    public ResponseEntity<List<Palabra>> obtenerPalabrasPorCategoria(@PathVariable String categoriaGramatical) {
        List<Palabra> palabras = palabraService.obtenerPalabrasPorCategoria(categoriaGramatical);
        if (palabras.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(palabras);
    }

    @CrossOrigin
    @GetMapping("/inicial/{letra}")
    public ResponseEntity<List<Palabra>> obtenerPalabrasPorInicial(@PathVariable String letra) {
        List<Palabra> palabras = palabraService.obtenerPalabrasPorInicial(letra.toUpperCase());
        if (palabras.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(palabras);
    }
}