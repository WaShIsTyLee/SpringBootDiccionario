package org.example.diccionario2.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.diccionario2.Exception.RecordNotFoundException;
import org.example.diccionario2.Model.Definicion;
import org.example.diccionario2.Model.Palabra;
import org.example.diccionario2.Service.DefinicionService;
import org.example.diccionario2.Service.PalabraService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/palabras")
@Tag(name = "Definicion resource", description = "Operaciones relacionadas con las definiciones de las palabras")
public class DefinicionController {

    @Autowired
    private PalabraService palabraService;

    @Autowired
    private DefinicionService definicionService;

    /**
     * Agrega una nueva definición a una palabra existente.
     * @param id ID de la palabra a la que se añadirá la definición.
     * @param definicion Definición a agregar.
     * @return La palabra con la nueva definición añadida.
     * @throws RecordNotFoundException Si la palabra no existe.
     */
    @CrossOrigin
    @PostMapping("/{id}/definiciones")
    @Operation(
            summary = "Agregar una definición a una palabra",
            description = "Este endpoint permite agregar una nueva definición a una palabra existente. Se asume que la palabra ya existe en la base de datos.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Palabra con la nueva definición añadida",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(value = "{ \"id\": 1, \"termino\": \"java\", \"categoriaGramatical\": \"sustantivo\", \"definiciones\": [{\"id\": 1, \"definicion\": \"Lenguaje de programación orientado a objetos\"}]}"))
                    ),
                    @ApiResponse(responseCode = "404", description = "Palabra no encontrada", content = @Content)
            }
    )
    public ResponseEntity<Palabra> agregarDefinicion(@PathVariable int id, @RequestBody Definicion definicion) throws RecordNotFoundException {

        Palabra palabra = palabraService.agregarDefinicion(id, definicion);
        return ResponseEntity.status(HttpStatus.OK).body(palabra);
    }

    /**
     * Elimina una definición específica por su ID.
     * @param id ID de la definición a eliminar.
     * @return Estado HTTP 202 Accepted si se elimina correctamente.
     * @throws RecordNotFoundException Si la definición no existe.
     */
    @CrossOrigin
    @DeleteMapping("/definiciones/{id}")
    @Operation(
            summary = "Eliminar una definición",
            description = "Este endpoint permite eliminar una definición específica por su ID.",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Definición eliminada correctamente", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Definición no encontrada", content = @Content)
            }
    )
    public HttpStatus deleteDefinicion(@PathVariable int id) throws RecordNotFoundException {
        definicionService.deleteDefinicion(id);
        return HttpStatus.ACCEPTED;
    }
}
