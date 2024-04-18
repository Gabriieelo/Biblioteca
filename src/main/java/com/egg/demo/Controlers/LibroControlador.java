package com.egg.demo.Controlers;

import com.egg.demo.Servicio.AutorServicio;
import com.egg.demo.Servicio.EditorialServicio;
import com.egg.demo.Servicio.LibroServicio;
import com.egg.demo.entidad.Autor;
import com.egg.demo.entidad.Editorial;
import com.egg.demo.entidad.Libro;
import com.egg.demo.exceptions.MiExceptions;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/libro")
public class LibroControlador {

    @Autowired
    private LibroServicio libroServicio;
    @Autowired
    private AutorServicio autorServicio;
    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/registrar") //localhost:8080/libro/registrar
    public String registrar(ModelMap modelo) {
        List<Autor> autores = autorServicio.listarAutores();
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);

        return "libro.html";
    }

    @PostMapping("/registrar")
    public String registro(@RequestParam(required = false) Long isbn,
            @RequestParam String titulo,
            @RequestParam(required = false) Integer ejemplares,
            @RequestParam String idAutor,
            @RequestParam String idEditorial, ModelMap modelo) {// con model map puedo insertar informacion en pantalla 
        try {
            libroServicio.crearLibro(isbn, titulo, ejemplares, idAutor, idEditorial);
            modelo.put("Exito", "El libro fue cargado correctamente");

        } catch (MiExceptions ex) {
            List<Autor> autores = autorServicio.listarAutores();
            List<Editorial> editoriales = editorialServicio.listarEditoriales();

            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);
            modelo.put("error", ex.getMessage());
            return "libro.html";
        }
        return "libro.html";
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Libro> libros = libroServicio.listarLibros();
        modelo.addAttribute("libros", libros);
        return "libroLista.html";
    }
}
