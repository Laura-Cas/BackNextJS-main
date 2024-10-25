package com.coffe.coffeService.service;

import com.coffe.coffeService.dto.*;
import com.coffe.coffeService.models.*;
import com.coffe.coffeService.repository.*;
import com.coffe.coffeService.utilerias.ExcelCoffe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FincaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FincaService.class);

    @Autowired
    private FincaRepository fincaRepository;

    @Autowired
    private ProductoresRepository productoresRepository;

    @Autowired
    private PlanificacionRepository planificacionRepository;

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private CultivoRepository cultivoRepository;

    @Autowired
    private SeguimientoRepository seguimientoRepository;

    @Autowired
    private ExcelCoffe excelCoffe;

    public Finca crearFinca(FincaDTO fincaDTO) {
        // Validar datos del DTO
        if (fincaDTO.getNombre() == null || fincaDTO.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la finca no puede estar vacío");
        }

        // Buscar el productor por ID
        Productor productor = productoresRepository.findById(fincaDTO.getProductorId())
                .orElseThrow(() -> new RuntimeException("Productor no encontrado"));

        // Crear una nueva instancia de Finca
        Finca nuevaFinca = new Finca();

        nuevaFinca.setNombre(fincaDTO.getNombre());
        nuevaFinca.setUbicacion(fincaDTO.getUbicacion());
        nuevaFinca.setTamanioHectareas(fincaDTO.getTamanioHectareas());
        nuevaFinca.setFechaRegistro(LocalDate.now()); // Fecha actual como fecha de registro
        nuevaFinca.setProductor(productor);

        try {
            // Guardar la finca en la base de datos
            return fincaRepository.save(nuevaFinca);
        } catch (Exception e) {
            // Manejar el caso en que no se pueda crear la finca
            throw new RuntimeException("Error al crear la finca: " + e.getMessage());
        }
    }

    public Planificacion crearPlanificacion(PlanificacionDTO planificacionDTO) {

        Finca finca = fincaRepository.findById(planificacionDTO.getFinca_id())
                .orElseThrow(() -> new RuntimeException("Finca NO ENCONTRADA "));

        Planificacion planificacion = new Planificacion();
        planificacion.setFinca(finca);
        planificacion.setActividad(planificacionDTO.getActividad());
        planificacion.setEstado(planificacionDTO.getEstado());
        planificacion.setFechaInicio(planificacionDTO.getFecha_inicio());
        planificacion.setFechaFin(planificacionDTO.getFecha_fin());
        try {
            return planificacionRepository.save(planificacion);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la planificacion: " + e.getMessage());
        }
    }

    public Cultivo crearCultivo(CultivoDto cultivoDto) {
        Finca finca = fincaRepository.findById(cultivoDto.getFinca_id())
                .orElseThrow(() -> new RuntimeException("Finca NO ENCONTRADA "));

        Cultivo cultivo = new Cultivo();
        cultivo.setFinca(finca);
        cultivo.setVariedad(cultivoDto.getVariedad());
        cultivo.setFechaSiembra(cultivoDto.getFecha());
        cultivo.setEstado(cultivoDto.getEstado());
        try {
            return cultivoRepository.save(cultivo);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el cultivo: " + e.getMessage());
        }
    }

    public Inventario crearInventario(InventarioDTO inventarioDTO) {
        Finca finca = fincaRepository.findById(inventarioDTO.getFinca_id())
                .orElseThrow(() -> new RuntimeException("Finca NO ENCONTRADA "));

        Inventario inventario = new Inventario();
        inventario.setFinca(finca);
        inventario.setCantidad(inventarioDTO.getCantidad());
        inventario.setProducto(inventarioDTO.getProducto());
        inventario.setUnidad(inventarioDTO.getUnidad());
        inventario.setFechaRegistro(LocalDate.now());

        try {
            return inventarioRepository.save(inventario);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el inventario: " + e.getMessage());
        }
    }

    public Seguimiento crearSeguimiento(SeguimientoDTO seguimientoDTO) {

        Planificacion planificacion = planificacionRepository.findById(seguimientoDTO.getPlanificacion_id())
                .orElseThrow(() -> new RuntimeException("Planificacion NO ENCONTRADA "));

        Seguimiento seguimiento = new Seguimiento();
        seguimiento.setPlanificacion(planificacion);
        seguimiento.setFecha(seguimientoDTO.getFecha());
        seguimiento.setObservaciones(seguimientoDTO.getObservaciones());
        seguimiento.setActividadRealizada(seguimientoDTO.getActividad_realizada());

        try {
            return seguimientoRepository.save(seguimiento);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el seguimiento: " + e.getMessage());
        }
    }

    public Finca getFincaById(Long id) {
        if (fincaRepository.existsById(id)) {
            return fincaRepository.findById(id).get();
        } else {
            return null;
        }

    }

    public boolean eliminarFinca(Long id) {
        Optional<Finca> fincaOptional = fincaRepository.findById(id);
        if (fincaOptional.isPresent()) {
            fincaRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<Finca> getFincasProductor(Long productorID) {
        // Buscar el productor por ID
        Productor productor = productoresRepository.findById(productorID)
                .orElseThrow(() -> new RuntimeException("Productor no encontrado"));

        return fincaRepository.findByProductor(productor);
    }

    public List<Finca> getAllFincas() {
        return fincaRepository.findAll();
    }

    public boolean editarFinca(Map _fincaActualizada) {
        try {
            Productor productor = productoresRepository.findById(Long.parseLong(""+_fincaActualizada.get("productorid")))
                    .orElseThrow(() -> new RuntimeException("Productor no encontrado"));
            Finca nuevaFinca = new Finca();

            nuevaFinca.setFincaId(Long.parseLong(""+_fincaActualizada.get("fincaId")));
            nuevaFinca.setNombre(""+_fincaActualizada.get("nombre"));
            nuevaFinca.setUbicacion(""+_fincaActualizada.get("ubicacion"));
            nuevaFinca.setTamanioHectareas(new BigDecimal(""+_fincaActualizada.get("tamanioHectareas")));
            nuevaFinca.setFechaRegistro(LocalDate.now());
            nuevaFinca.setProductor(productor);

            fincaRepository.save(nuevaFinca);
            return true; // Guardar
        } catch (Exception e) {
            return false;
        }
    }

    public List<Cultivo> obtenerCultivosPorFinca(Long fincaId) {
        return cultivoRepository.findCultivosWithFincas(fincaId);
    }

    public List<Inventario> obtenerInventarioPorFinca(Long fincaId) {
        return inventarioRepository.findInventarioWithFincas(fincaId);
    }

    public List<Planificacion> obtenerPlanificacionPorFinca(Long fincaId) {
        return planificacionRepository.findPlanificacionWithFincas(fincaId);
    }

    public boolean eliminarCultivo(Long id) {
        Optional<Cultivo> cultivoOptional = cultivoRepository.findById(id);
        if (cultivoOptional.isPresent()) {
            cultivoRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean eliminarInventario(Long id) {
        Optional<Inventario> inventarioOptional = inventarioRepository.findById(id);
        if (inventarioOptional.isPresent()) {
            inventarioRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean eliminarPlanificacion(Long id) {
        Optional<Planificacion> planificacionOptional = planificacionRepository.findById(id);
        if (planificacionOptional.isPresent()) {
            // Primero, elimina los seguimientos asociados
            seguimientoRepository.deleteByPlanificacionId(id);
            planificacionRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<SeguimientoPlanificacionDTO> obtenerSeguimientosPorFinca(Long fincaId) {

        Finca finca = fincaRepository.findById(fincaId)
                .orElseThrow(() -> new RuntimeException("Finca NO ENCONTRADA "));

        return seguimientoRepository.findSeguimientosByFincaId(fincaId);
    }

    public byte[] generarExcelTest(Map<String, Object> parametros) throws IOException {
        Integer idFincaInt = (Integer) parametros.get("idFinca");
        Long idFinca = idFincaInt != null ? idFincaInt.longValue() : null;
        Integer idProductorInt = (Integer) parametros.get("idProductor");
        Long idProductor = idProductorInt != null ? idProductorInt.longValue() : null;
        List<SeguimientoPlanificacionDTO> seguimientos = seguimientoRepository.findSeguimientosByFincaId(idFinca);
        if (seguimientos.isEmpty()){
            LOGGER.info("vacio");
            return null;
        }
        String nombrePlanificacion = planificacionRepository.findActividadByPlanificacionId(seguimientos.get(0).getPlanificacionId());
        String nombreFinca = fincaRepository.findFincaByFincaId(idFinca);
        String nombreProductor = productoresRepository.findProductorById(idProductor);
        LOGGER.info("nombre planificacion {}", nombrePlanificacion);
        LOGGER.info("nombre finca {}", nombreFinca);
        LOGGER.info("nombre Productor {}", nombreProductor);
        byte [] excelBytes = excelCoffe.generarExcel(seguimientos, nombrePlanificacion, nombreFinca, nombreProductor);
        return excelBytes;
    }

    public List<Seguimiento> getSeguimientoXPlanificacion(Long planificacionID) {
        return seguimientoRepository.findSeguimientoWithPlanificacion(planificacionID);
    }

    public boolean deleteSeguimiento(Long id) {
        Optional<Seguimiento> seguimientoOptional = seguimientoRepository.findById(id);
        if (seguimientoOptional.isPresent()) {
            seguimientoRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
