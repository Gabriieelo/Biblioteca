package com.egg.demo.Servicio;
import com.egg.demo.Repositorios.RepositorioAutor;
import com.egg.demo.Repositorios.RepositorioEditorial;
import com.egg.demo.Repositorios.RepositorioLibro;
import com.egg.demo.entidad.Autor;
import com.egg.demo.entidad.Editorial;
import com.egg.demo.entidad.Libro;
import com.egg.demo.exceptions.MiExceptions;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LibroServicio {

    @Autowired
    private RepositorioLibro repositorioLibro;

    @Autowired
    private RepositorioAutor repositorioAutor;

    @Autowired
    RepositorioEditorial repositorioEditorial;

    @Transactional
    public void crearLibro(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiExceptions {
        validar( isbn,  titulo,  ejemplares,  idAutor, idEditorial);
        Autor autor = repositorioAutor.findById(idAutor).get();
        Editorial editorial = repositorioEditorial.findById(idEditorial).get();
        Libro libro = new Libro();
        
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setEjemplares(ejemplares);
        libro.setEditorial(editorial);
        libro.setAutor(autor);
        libro.setAlta(new Date());
        repositorioLibro.save(libro);

    }

    public List<Libro> listarLibros() {
        List<Libro> libros = new ArrayList();
        libros = repositorioLibro.findAll();
        return libros;
    }
    
    @Transactional
    public void modificarLibro(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiExceptions  {
        validar( isbn,  titulo,  ejemplares,  idAutor, idEditorial);
        Optional<Libro> respuestaLibro = repositorioLibro.findById(isbn);
        Optional<Autor> respuestaAutor = repositorioAutor.findById(idAutor);
        Optional<Editorial> respuestaEditorial = repositorioEditorial.findById(idEditorial);
        

        Autor autor = new Autor();
        Editorial editorial = new Editorial();
        if (respuestaAutor.isPresent()) {
            autor = respuestaAutor.get();
        }
        if (respuestaEditorial.isPresent()) {
            editorial = respuestaEditorial.get();
        }
        if (respuestaLibro.isPresent()) {
            Libro libro = respuestaLibro.get();
            libro.setTitulo(titulo);
            libro.setEjemplares(ejemplares);
            libro.setAutor(autor);
            libro.setEditorial(editorial);
            repositorioLibro.save(libro);
        }
    }
    private void validar(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiExceptions{
        if(isbn == null){
            throw new MiExceptions("El isbn no puede ser nulo");
        }
        if(titulo == null || titulo.isEmpty()){
            throw new MiExceptions("El titulo no puede ser nulo");
        }
        if(ejemplares == null){
            throw new MiExceptions("Los ejemplares no pueden ser nulo");
        }
        if(idAutor == null || idAutor.isEmpty()){
            throw new MiExceptions("El nombre del autor no puede ser nulo");
        }
        if(idEditorial == null || idEditorial.isEmpty()){
            throw new MiExceptions("La editorial no puede ser nula");
        }
        
    }
}
