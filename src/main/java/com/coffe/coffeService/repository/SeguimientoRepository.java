package com.coffe.coffeService.repository;

import com.coffe.coffeService.dto.SeguimientoPlanificacionDTO;
import com.coffe.coffeService.models.Seguimiento;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeguimientoRepository extends JpaRepository<Seguimiento, Long> {

    @Query("SELECT new com.coffe.coffeService.dto.SeguimientoPlanificacionDTO(s.actividadRealizada, s.fecha, s.observaciones, p.planificacionId) " +
            "FROM Seguimiento s INNER JOIN s.planificacion p " +
            "WHERE p.finca.fincaId = :fincaId")
    List<SeguimientoPlanificacionDTO> findSeguimientosByFincaId(@Param("fincaId") Long fincaId);

    @Query("SELECT s FROM Seguimiento s WHERE s.planificacion.id = :planificacionId")
    List<Seguimiento> findSeguimientoWithPlanificacion(@Param("planificacionId") Long planificacionId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Seguimiento s WHERE s.planificacion.planificacionId = :planificacionId")
    void deleteByPlanificacionId(@Param("planificacionId") Long planificacionId);
}
