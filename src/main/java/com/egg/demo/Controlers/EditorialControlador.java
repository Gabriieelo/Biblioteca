package com.egg.demo.Controlers;
import com.egg.demo.Servicio.AutorServicio;
import com.egg.demo.Servicio.EditorialServicio;
import com.egg.demo.Servicio.LibroServicio;
import com.egg.demo.entidad.Editorial;
import com.egg.demo.exceptions.MiExceptions;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/editorial")
public class EditorialControlador {

    @Autowired
    private AutorServicio autorServicio;

    @Autowired
    private EditorialServicio editorialServicio;

    @Autowired
    private LibroServicio libroServicio;

    @GetMapping("/registrar")
    public String registrar() {
        return "editorial_form.html";
    }

    @PostMapping("/registrar")
    public String registro(@RequestParam(required = false) String name, ModelMap modelo) {
        try {
            editorialServicio.crearEditorial(name);
            modelo.put("Exito", "El libro fue cargado correctamente");

        } catch (MiExceptions ex) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());

            return "registrar.html";
        }
        return "editorial_form.html";
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        modelo.addAttribute("editoriales", editoriales);
        return "editorialLista.html";
    }
}
