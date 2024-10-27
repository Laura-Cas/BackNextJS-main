package com.coffe.coffeService.controller;

import com.coffe.coffeService.dto.*;
import com.coffe.coffeService.models.*;
import com.coffe.coffeService.service.FincaService;
import com.coffe.coffeService.service.ProductoreService;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api/v1")
public class RequestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestController.class);

    @Autowired
    private ProductoreService productoreService;

    @Autowired
    private FincaService fincaService;

    @GetMapping(value = "/getAllProductores", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Productor>> getAllProductores() {
        List<Productor> productores = productoreService.getAllProductores();
        return ResponseEntity.ok(productores);
    }

    @PostMapping(value = "/addProductor", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Productor> addProductor(@RequestBody Productor productor) {
        try {
            Productor newProductor = productoreService.saveProductor(productor);
            return new ResponseEntity<>(newProductor, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new Productor(), HttpStatus.ACCEPTED);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProductor(@PathVariable Long id) {
        productoreService.deleteProductor(id);
        return ResponseEntity.noContent().build(); // Responde con un 204 No Content si se elimina con éxito
    }

    @GetMapping("/getProductoresFincas")
    public ResponseEntity<List<ProductoresFincas>> getAllFincas() {
        List<ProductoresFincas> lista = productoreService.getAllProductoresFincas();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/getProductor/{id}")
    public ResponseEntity<Productor> getProductorById(@PathVariable Long id) {
        Productor productor = productoreService.getProductorById(id);
        return ResponseEntity.ok(productor);
    }

    @GetMapping("/getFinca/{id}")
    public ResponseEntity<Finca> getFincaById(@PathVariable Long id) {
        Finca fincaById = fincaService.getFincaById(id);
        if(fincaById!=null){
            return ResponseEntity.ok(fincaById);
        }else{
            return ResponseEntity.ok(new Finca());
        }

    }

    @PostMapping("/addFinca")
    public ResponseEntity<Object> agregarFinca(@RequestBody FincaDTO fincaDTO) {
        // Llamar al método del servicio para crear la finca
        Map<String, String> response = new HashMap<>();
        try {
            Finca nuevaFinca = fincaService.crearFinca(fincaDTO);
            response.put("estado", "exito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("estado", "error");
            return ResponseEntity.ok(response);
        }

    }

    @PostMapping(value = "/editFinca", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map> EditFincaById(@RequestBody Map fincaAActualizar) {
        Map<String, Object> response = new HashMap<>();
        boolean respuesta = fincaService.editarFinca(fincaAActualizar);
        if(respuesta){
            response.put("estado","exito");
            return ResponseEntity.ok(response);
        }else{
            response.put("estado","error");
            return ResponseEntity.ok(response);
        }

    }

    @DeleteMapping("/deleteFinca/{id}")
    public ResponseEntity<Object> eliminarFinca(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            boolean isDeleted = fincaService.eliminarFinca(id);
            if (isDeleted) {
                response.put("estado", "exito");
                response.put("mensaje", "finca eliminada exitosamente");
                return ResponseEntity.ok(response);
            } else {
                response.put("estado", "error");
                response.put("mensaje", "finca no encontrada");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.put("estado", "error");
            response.put("mensaje", "error al eliminar la finca");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/addPlanificacion")
    public ResponseEntity<?> agregarPlanificacion(@RequestBody PlanificacionDTO planificacionDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            Planificacion nuevaPlanificacion = fincaService.crearPlanificacion(planificacionDTO);
            response.put("estado", "exito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("estado", "error");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/addCultivo")
    public ResponseEntity<?> agregarCultivo(@RequestBody CultivoDto cultivoDto) {
        Map<String, String> response = new HashMap<>();
        try {
            Cultivo cultivo = fincaService.crearCultivo(cultivoDto);
            response.put("estado", "exito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("estado", "error");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/addInventario")
    public ResponseEntity<?> agregarInventario(@RequestBody InventarioDTO inventarioDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            Inventario inventario = fincaService.crearInventario(inventarioDTO);
            response.put("estado", "exito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("estado", "error");
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/getInventarioXFinca/{fincaId}")
    public ResponseEntity<List<Inventario>> getInventarioXFinca(@PathVariable Long fincaId) {
        try {
            List<Inventario> Inventario = fincaService.obtenerInventarioPorFinca(fincaId);
            return ResponseEntity.ok(Inventario);
        } catch (Exception e) {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @DeleteMapping("/deleteInventario/{id}")
    public ResponseEntity<Object> deleteInventario(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            boolean isDeleted = fincaService.eliminarInventario(id);
            if (isDeleted) {
                response.put("estado", "exito");
                response.put("mensaje", "Inventario eliminado exitosamente");
                return ResponseEntity.ok(response);
            } else {
                response.put("estado", "error");
                response.put("mensaje", "Inventario no encontrada");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.put("estado", "error");
            response.put("mensaje", "error al eliminar el inventario");
            return ResponseEntity.ok(response);
        }
    }


    @PostMapping("/addSeguimiento")
    public ResponseEntity<?> agregarSeguimiento(@RequestBody SeguimientoDTO seguimientoDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            Seguimiento seguimiento = fincaService.crearSeguimiento(seguimientoDTO);
            response.put("estado", "exito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("estado", "error");
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/getFincasDeProductor/{id}")
    public ResponseEntity<?> obtenerFincasDeProductor(@PathVariable Long id) {
        List<Finca> fincas = fincaService.getFincasProductor(id);
        return ResponseEntity.ok(fincas);
    }

    @PutMapping("/actualizarProductor/{id}")
    public ResponseEntity<Productor> actualizarProductor(
            @PathVariable Long id,
            @RequestBody ProductorDTO productorDTO) {

        Productor productorUpdate = productoreService.actualizarProductor(id, productorDTO);
        productorDTO.setPassword(null);
        return ResponseEntity.ok(productorUpdate);
    }

    @GetMapping(value = "/getAllFincas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Finca>> getAllFincasV2() {
        List<Finca> fincas = fincaService.getAllFincas();
        return ResponseEntity.ok(fincas);
    }

    @GetMapping("/getCultivosXFinca/{fincaId}")
    public ResponseEntity<List<Cultivo>> obtenerCultivosPorFinca(@PathVariable Long fincaId) {
        try {
            List<Cultivo> cultivos = fincaService.obtenerCultivosPorFinca(fincaId);
            return ResponseEntity.ok(cultivos);
        } catch (Exception e) {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @DeleteMapping("/deleteCultivo/{id}")
    public ResponseEntity<Object> eliminarCultivo(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            boolean isDeleted = fincaService.eliminarCultivo(id);
            if (isDeleted) {
                response.put("estado", "exito");
                response.put("mensaje", "Cultivo eliminado exitosamente");
                return ResponseEntity.ok(response);
            } else {
                response.put("estado", "error");
                response.put("mensaje", "Cultivo no encontrada");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.put("estado", "error");
            response.put("mensaje", "error al eliminar el cultivo");
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/obtenerSeguimientosPorFinca/{fincaId}")
    public ResponseEntity<?> obtenerSeguimientosPorFinca(@PathVariable Long fincaId) {
        List<SeguimientoPlanificacionDTO> seguimientos = fincaService.obtenerSeguimientosPorFinca(fincaId);
        return ResponseEntity.ok(seguimientos);
    }

    @PostMapping("/generarExcel")
    public ResponseEntity<?> downloadExcel(@RequestBody Map<String, Object> parametros) throws IOException {

        byte[] excelBytes = fincaService.generarExcelTest(parametros);
        if(excelBytes != null) {
            // Configurar los encabezados de la respuesta HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "data.xlsx");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            // Devolver la respuesta con el archivo Excel
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);
        } else {
            return new ResponseEntity<>("No existen planificaciones para la Finca", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getPlanificacionXFinca/{fincaId}")
    public ResponseEntity<List<Planificacion>> getPlanificacionXFinca(@PathVariable Long fincaId) {
        try {
            List<Planificacion> planificacion = fincaService.obtenerPlanificacionPorFinca(fincaId);
            return ResponseEntity.ok(planificacion);
        } catch (Exception e) {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @DeleteMapping("/deletePlanificacion/{id}")
    public ResponseEntity<Object> deletePlanificacion(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            boolean isDeleted = fincaService.eliminarPlanificacion(id);
            if (isDeleted) {
                response.put("estado", "exito");
                response.put("mensaje", "Planificacion eliminada exitosamente");
                return ResponseEntity.ok(response);
            } else {
                response.put("estado", "error");
                response.put("mensaje", "Planificacion no encontrada");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.put("estado", "error");
            response.put("mensaje", "error al eliminar la planificacion");
            return ResponseEntity.ok(response);
        }
    }


    @GetMapping("/getSeguimientoXPlanificacion/{idPlanificacion}")
    public ResponseEntity<List<Seguimiento>> getSeguimientoXPlanificacion(@PathVariable Long idPlanificacion) {
        try {
            List<Seguimiento> Seguimiento = fincaService.getSeguimientoXPlanificacion(idPlanificacion);
            return ResponseEntity.ok(Seguimiento);
        } catch (Exception e) {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }
    
    @GetMapping("/saludo")
    public ResponseEntity<?> saludo(){
        return ResponseEntity.ok("saludo desde el backend");
    }
  

    @DeleteMapping("/deleteSeguimiento/{id}")
    public ResponseEntity<Object> deleteSeguimiento(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            boolean isDeleted = fincaService.deleteSeguimiento(id);
            if (isDeleted) {
                response.put("estado", "exito");
                response.put("mensaje", "Seguimiento eliminado exitosamente");
                return ResponseEntity.ok(response);
            } else {
                response.put("estado", "error");
                response.put("mensaje", "Seguimiento no encontrado");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.put("estado", "error");
            response.put("mensaje", "error al eliminar el seguimiento");
            return ResponseEntity.ok(response);
        }
    }
}
