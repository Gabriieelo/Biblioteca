 package com.egg.demo.Controlers;

import com.egg.demo.Servicio.UsuarioServicio;
import com.egg.demo.entidad.Usuario;
import com.egg.demo.exceptions.MiExceptions;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/dashboard")
    public String panelAdministrativo() {
        return "panel.html";
    }
    @GetMapping("/usuarios")
    public String listar(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
        return "usuario_lista";
    }

    @GetMapping("/modificarRol/{id}")
    public String cambiarRol(@PathVariable String id) {
        usuarioServicio.cambiarRol(id);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/modificar/{id}")
    public String perfil(ModelMap modelo, HttpSession session, @PathVariable String id) {
        Usuario usuario = usuarioServicio.getOne(id);
        modelo.put("usuario", usuario);
        return "usuarioModAdmin.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("/modificar/{id}")
    public String actualizar(MultipartFile archivo, @PathVariable String id, @RequestParam String nombre, String password, ModelMap modelo, String password2, String email) {
        Usuario usuario = usuarioServicio.getOne(id);
        try {
            usuarioServicio.actualizar(archivo, id, nombre, email, password, password2);
            modelo.put("Exito", "El registro fue cargado correctamente");
            return "panel.html";
        } catch (MiExceptions ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("usuario", usuario);
            return "usuarioModAdmin.html";
        }
    }
}
