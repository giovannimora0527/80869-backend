package com.uniminuto.clinica.repository;

import com.uniminuto.clinica.entity.InventarioMedicamento;
import com.uniminuto.clinica.entity.Medicamento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author lmora
 */
@Repository
public interface InventarioMedicamentoRepository extends JpaRepository<InventarioMedicamento, Long> {
    List<InventarioMedicamento> findByMedicamento(Medicamento medicamento);
}
