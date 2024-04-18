package com.egg.demo.Servicio;

import com.egg.demo.Repositorios.RepositorioAutor;
import com.egg.demo.Repositorios.RepositorioEditorial;
import com.egg.demo.Repositorios.RepositorioLibro;
import com.egg.demo.entidad.Autor;
import com.egg.demo.exceptions.MiExceptions;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorServicio {

    @Autowired
    private RepositorioAutor repositorioAutor;
    @Autowired
    private RepositorioLibro repositorioLibro;
    @Autowired
    private RepositorioEditorial repositorioEditorial;

    @Transactional
    public void crearAutor(String nombre, String edad) throws MiExceptions {
        validar(nombre, edad);
        Autor autor = new Autor();
        autor.setName(nombre);
        autor.setEdad(edad);
        repositorioAutor.save(autor);
    }

    public List<Autor> listarAutores() {
        List<Autor> autores = new ArrayList();
        autores = repositorioAutor.findAll();
        return autores;
    }

    @Transactional
    public void modificarAutor(String nombre, String Id, String edad) throws MiExceptions {
        validar(nombre, edad);
        Optional<Autor> respuestaAutor = repositorioAutor.findById(Id);
        if (respuestaAutor.isPresent()) {
            Autor autor = respuestaAutor.get();
            autor.setName(nombre);
            autor.setEdad(edad);
            repositorioAutor.save(autor);
        }
    }

    public Autor getOne(String id) {
        return repositorioAutor.getOne(id);
    }

    private void validar(String nombre, String edad) throws MiExceptions {
        if (nombre == null || nombre.isEmpty()) {
            throw new MiExceptions(" El nombre no puede ser nulo o estar vacio");
        }
        if (edad == null || edad.isEmpty()) {
            throw new MiExceptions(" La edad no puede ser nula o estar vacio");
        }
    }
}
