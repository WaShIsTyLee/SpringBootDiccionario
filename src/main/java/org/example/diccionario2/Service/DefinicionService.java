package org.example.diccionario2.Service;

import org.example.diccionario2.Model.Definicion;
import org.example.diccionario2.Repository.DefinicionRepository;
import org.example.diccionario2.Repository.PalabraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DefinicionService {

    @Autowired
    private DefinicionRepository definicionRepository;

    public List<Definicion> getDefinicionesByPalabraID(int palabraID) {
        return definicionRepository.getDefinicionesByPalabraID(palabraID);

    }

}
