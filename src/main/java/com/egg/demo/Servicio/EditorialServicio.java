package com.egg.demo.Servicio;

import com.egg.demo.Repositorios.RepositorioEditorial;
import com.egg.demo.entidad.Editorial;
import com.egg.demo.exceptions.MiExceptions;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EditorialServicio {

    @Autowired
    RepositorioEditorial repositorioEditorial;

    @Transactional
    public void crearEditorial(String name) throws MiExceptions {
        validar(name);
        Editorial editorial = new Editorial();
        editorial.setName(name);
        repositorioEditorial.save(editorial);
    }

    public List<Editorial> listarEditoriales() {
        List<Editorial> editoriales = new ArrayList();
        editoriales = repositorioEditorial.findAll();
        return editoriales;
    }

    public void modificarEditorial(String name ,String id) throws MiExceptions {
        validar(name);
        Optional<Editorial> respuestaEditorial = repositorioEditorial.findById(id);
        if (respuestaEditorial.isPresent()) {
            Editorial editorial = respuestaEditorial.get();
            editorial.setName(name);
            repositorioEditorial.save(editorial);
        }
    }

    private void validar(String name) throws MiExceptions {
        if (name  == null || name.isEmpty()) {
            throw new MiExceptions(" El nombre no puede ser nulo o estar vacio");
        }
    }
    
}
