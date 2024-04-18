package com.egg.demo.Repositorios;

import com.egg.demo.entidad.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Gabriel
 */
@Repository
public interface RepositorioLibro extends JpaRepository<Libro, Long>{
     @Query("SELECT l FROM Libro l WHERE l.titulo = :titulo")
    public Libro buscarPorTitulo(@Param("titulo") String titulo);

    @Query("SELECT l FROM Libro l WHERE l.autor.name = :nombre")
    public List<Libro> buscarPorAutor(@Param("nombre") String name);
}

