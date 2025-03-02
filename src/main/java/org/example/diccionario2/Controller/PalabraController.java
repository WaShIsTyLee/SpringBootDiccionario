package org.example.diccionario2.Controller;

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
public class PalabraController {

    @Autowired
    private PalabraService palabraService;
    @Autowired
    private DefinicionService definicionService;


    @CrossOrigin
    @GetMapping
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
    public ResponseEntity<Palabra> getPalabra(@PathVariable int id){
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
    public ResponseEntity<Palabra> crearPalabra(@RequestBody Palabra palabra) {
        if (palabraService.existePalabra(palabra.getTermino())) {
            throw new RecordNotFoundException("La palabra ya existe: ", palabra.getTermino());
        }
        Palabra createdPalabra = palabraService.crearPalabra(palabra);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPalabra);
    }


    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<Palabra> updatePalabra(@PathVariable int id, @RequestBody Palabra palabra) throws RecordNotFoundException {
        Palabra updatedPalabra = palabraService.updatePalabra(id, palabra);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPalabra);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public HttpStatus deletePalabra(@PathVariable int id) throws RecordNotFoundException {
        palabraService.deletePalabra(id);
        return HttpStatus.ACCEPTED;
    }


}

