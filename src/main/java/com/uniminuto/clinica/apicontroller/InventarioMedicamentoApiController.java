package com.uniminuto.clinica.apicontroller;

import com.uniminuto.clinica.api.InventarioMedicamentoApi;
import com.uniminuto.clinica.entity.InventarioMedicamento;
import com.uniminuto.clinica.model.InventarioMedicamentoRq;
import com.uniminuto.clinica.model.RespuestaRs;
import com.uniminuto.clinica.service.InventarioMedicamentoService;
import java.util.List;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lmora
 */
@RestController
public class InventarioMedicamentoApiController implements InventarioMedicamentoApi {

    @Autowired
    private InventarioMedicamentoService inventarioService;

    @Override
    public ResponseEntity<List<InventarioMedicamento>> listarInventario() {
        return ResponseEntity.ok(this.inventarioService.listarInventario());
    }

    @Override
    public ResponseEntity<List<InventarioMedicamento>> listarPorMedicamento(Long idMedicamento) throws BadRequestException {
        return ResponseEntity.ok(this.inventarioService.listarPorMedicamento(idMedicamento));
    }

    @Override
    public ResponseEntity<RespuestaRs> guardarInventario(InventarioMedicamentoRq rq) throws BadRequestException {
        return ResponseEntity.ok(this.inventarioService.guardarInventario(rq));
    }

    @Override
    public ResponseEntity<RespuestaRs> actualizarInventario(Long id, InventarioMedicamentoRq rq) throws BadRequestException {
        return ResponseEntity.ok(this.inventarioService.actualizarInventario(id, rq));
    }

}
