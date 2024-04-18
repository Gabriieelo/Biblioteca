package com.egg.demo.Controlers;

import com.egg.demo.Servicio.AutorServicio;
import com.egg.demo.entidad.Autor;
import com.egg.demo.exceptions.MiExceptions;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/autor")
public class AutorControlador {

    @Autowired
    private AutorServicio autorServicio;

    @GetMapping("/registrar")
    public String registrar() {
        return "autor_form.html";
    }
    @PostMapping("/registrar")
    public String registro(@RequestParam(required = false) String nombre, @RequestParam(required = false) String edad, ModelMap modelo) {
        try {
            autorServicio.crearAutor(nombre, edad);
            modelo.put("Exito", "El libro fue cargado correctamente");
        } catch (MiExceptions ex) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            return "autor_form.html";
        }
        return "autor_form.html";
    }
    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Autor> autores = autorServicio.listarAutores();
        modelo.addAttribute("autores", autores);
        return "autor_lista.html";
    }
    @GetMapping("/modoficar/{id}")
    public String modificar(@PathVariable String id,ModelMap modelo){
        modelo.put("autor",autorServicio.getOne(id));
        return "autor_modoficar.html";
    }
    @PostMapping("/modoficar/{id}")
    public String modificar(@PathVariable String id,String name,String edad,ModelMap modelo) throws MiExceptions{
        try{
        autorServicio.modificarAutor(name, id, edad);
        modelo.put("Exito","Fue modoficado correctamente");
        return "redirect:../lista";
        }catch(MiExceptions ex){
            ex.printStackTrace();
            modelo.put("error",ex.getMessage());
            return "autor_modoficar.html";
        }
    }
}
