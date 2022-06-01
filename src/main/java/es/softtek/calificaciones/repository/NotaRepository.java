package es.softtek.calificaciones.repository;

import es.softtek.calificaciones.dto.Nota;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface NotaRepository extends JpaRepository<Nota, Long> {
    List<Nota> findByUsername(String username);
}
