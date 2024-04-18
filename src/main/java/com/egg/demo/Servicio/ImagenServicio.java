package com.egg.demo.Servicio;

import com.egg.demo.Repositorios.ImagenRepo;
import com.egg.demo.entidad.Imagen;
import com.egg.demo.exceptions.MiExceptions;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImagenServicio {

    @Autowired
    private ImagenRepo imagenRepo;

    public Imagen guardar(MultipartFile archivo) throws MiExceptions{
        if (archivo != null) {
            try {
                Imagen imagen = new Imagen();
                imagen.setMime(archivo.getContentType());
                imagen.setContenido(archivo.getBytes());
                imagen.setName(archivo.getName());
                return imagenRepo.save(imagen);
            } catch (Exception e) {
                System.out.println(e.getMessage());

            }
        }
        return null;
    }
    public Imagen actualizar(MultipartFile archivo,String idImagen)throws MiExceptions{
         if (archivo != null) {
            try {
                Imagen imagen = new Imagen();
                if (idImagen !=null) {
                    Optional<Imagen>respuesta=imagenRepo.findById(idImagen);
                    if (respuesta.isPresent()) {
                        imagen=respuesta.get();
                    }
                }
                imagen.setMime(archivo.getContentType());
                imagen.setContenido(archivo.getBytes());
                imagen.setName(archivo.getName());
                return imagenRepo.save(imagen);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }
}
