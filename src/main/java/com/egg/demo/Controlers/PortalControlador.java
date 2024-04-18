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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/panel")
    public String panel() {
        return "panel.html";
    }

    @GetMapping("/registro")
    public String registrar() {
        return "registro.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("Error", "Email o contraseña invalidos");
        }
        return "login.html";
    }

    @PostMapping("/panel")
    public String paneel() {
        return "panel.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String email, @RequestParam String password, @RequestParam String password2, ModelMap modelo, MultipartFile archivo) throws MiExceptions {
        try {
            usuarioServicio.registrar(archivo, nombre, email, password, password2);
            modelo.put("Exito", "El registro fue cargado correctamente");
        } catch (MiExceptions ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "registro.html";
        }
        return "preindex.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuarioSession");
        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        return "preindex.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioSession");
        modelo.put("usuario", usuario);
        return "usuarioModAdmin.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("/perfil")
    public String actualizar(MultipartFile archivo, @RequestParam String id, @RequestParam String nombre, @RequestParam String password, ModelMap modelo, @RequestParam String password2, HttpSession session, @RequestParam String email) throws MiExceptions {
        Usuario usuario = (Usuario) session.getAttribute("usuarioSession");
        try {
            usuarioServicio.actualizar(archivo, nombre, usuario.getId(), email, password, password2);
            modelo.put("Exito", "El registro fue cargado correctamente");
            return "perfil.html";

        } catch (MiExceptions ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("usuario", usuario);
            return "usuarioModAdmin.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/usuarios")
    public String listar(ModelMap modelo, HttpSession sesion) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        Usuario logueado = (Usuario) sesion.getAttribute("usuarioSession");
        modelo.addAttribute("usuarios", usuarios);
//        if (logueado.getRol().toString().equals("ADMIN")) {
//            return "usuario_list.html";
//        }
//        if (logueado.getRol().toString().equals("USER")) {
//            return "usuario_list.html";
//        }
        return "usuario_lista.html";
    }
}
