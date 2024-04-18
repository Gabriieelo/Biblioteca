package com.egg.demo.Repositorios;
import com.egg.demo.entidad.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioAutor extends JpaRepository<Autor, String> {
     
}
