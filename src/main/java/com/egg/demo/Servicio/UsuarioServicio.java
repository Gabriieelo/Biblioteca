package com.egg.demo.Servicio;

import com.egg.demo.Repositorios.UsuarioRepositorio;
import com.egg.demo.entidad.Imagen;
import com.egg.demo.entidad.Usuario;
import com.egg.demo.enumeracion.Rol;
import com.egg.demo.exceptions.MiExceptions;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void actualizar(MultipartFile archivo, String id, String nombre, String email, String password, String password2) throws MiExceptions {
        validar(nombre, email, password, password2);
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            if (!usuario.getEmail().equals(email)) {
                if (usuarioRepositorio.buscarPorEmail(email) != null) {
                    throw new MiExceptions("el email ya esta en uso");
                }
            }
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
            usuario.setRol(usuario.getRol());
            String idImagen = null;
            if (usuario.getImagen() != null) {
                idImagen = usuario.getImagen().getId();
            }
            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            usuario.setImagen(imagen);
            usuarioRepositorio.save(usuario);
        }
    }

    private void validar(String nombre, String email, String password, String password2) throws MiExceptions {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiExceptions("el nombre no puede ser nulo o estar vacio");
        }
        if (email.isEmpty() || email == null) {
            throw new MiExceptions("el ejemplares no puede ser nulo");
        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiExceptions("el id del autor no puede ser nulo ni estar vacio");
        }
        if (!password.equals(password2)) {
            throw new MiExceptions("el id de la editorial no puede ser nulo ni estar vacio");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);
        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            permisos.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuarioSession", usuario);
            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }
    }

    @Transactional
    public void registrar(MultipartFile archivo, String nombre, String email, String password, String password2) throws MiExceptions {
        if (usuarioRepositorio.buscarPorEmail(email) != null) {
            throw new MiExceptions("Ese email ya se encuentra registrado");
        }
        Usuario usuario = new Usuario();
        validar(nombre, email, password, password2);
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setRol(Rol.USER);
        Imagen imagen = imagenServicio.guardar(archivo);
        usuario.setImagen(imagen);
        usuarioRepositorio.save(usuario);
    }

    public Usuario getOne(String id) {
        return usuarioRepositorio.getOne(id);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        List<Usuario> usuarioLista = new ArrayList();
        usuarioLista = usuarioRepositorio.findAll();
        return usuarioLista;
    }

    @Transactional
    public void cambiarRol(String id) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            if (usuario.getRol().equals(Rol.USER)) {
                usuario.setRol(Rol.ADMIN);

            } else if (usuario.getRol().equals(Rol.ADMIN)) { //si el rol que trae es adin lo cambia a user
                usuario.setRol(Rol.USER);
            }
        }
    }
}
