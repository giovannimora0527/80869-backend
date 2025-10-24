package com.uniminuto.clinica.api;

import com.uniminuto.clinica.entity.InventarioMedicamento;
import com.uniminuto.clinica.model.InventarioMedicamentoRq;
import com.uniminuto.clinica.model.RespuestaRs;
import java.util.List;
import javax.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author lmora
 */
@CrossOrigin(origins = "*")
@RequestMapping("/inventario-medicamento")
public interface InventarioMedicamentoApi {

    @RequestMapping(value = "/listar",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<InventarioMedicamento>> listarInventario();

    @RequestMapping(value = "/listar-x-medicamento",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<InventarioMedicamento>> listarPorMedicamento(@RequestParam Long idMedicamento) throws BadRequestException;

    @RequestMapping(value = "/guardar",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<RespuestaRs> guardarInventario(@RequestBody @Valid InventarioMedicamentoRq rq) throws BadRequestException;

    @RequestMapping(value = "/actualizar",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.PUT)
    ResponseEntity<RespuestaRs> actualizarInventario(@RequestParam Long id, @RequestBody @Valid InventarioMedicamentoRq rq) throws BadRequestException;
}