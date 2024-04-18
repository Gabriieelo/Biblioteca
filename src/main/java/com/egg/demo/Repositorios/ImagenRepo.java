package com.egg.demo.Repositorios;
import com.egg.demo.entidad.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagenRepo extends JpaRepository <Imagen,String>{
    
}
