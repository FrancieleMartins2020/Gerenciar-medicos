package br.edu.utfpr.td.tsi.medicos.repository;

import br.edu.utfpr.td.tsi.medicos.model.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Optional<Medico> findByCrmAndUf(String crm, String uf);

    boolean existsByCrmAndUf(String crm, String uf);

    @Query(value = "SELECT DISTINCT m FROM Medico m " +
            "LEFT JOIN FETCH m.especialidades esp " +
            "WHERE m.id IN (" +
            "  SELECT m2.id FROM Medico m2 " +
            "  LEFT JOIN m2.especialidades e " +
            "  WHERE (:nome IS NULL OR m2.nome LIKE CONCAT('%', :nome, '%')) AND " +
            "  (:crm IS NULL OR m2.crm = :crm) AND " +
            "  (:especialidade IS NULL OR e.nome LIKE CONCAT('%', :especialidade, '%'))" +
            ") ORDER BY m.nome ASC",
            countQuery = "SELECT COUNT(DISTINCT m2) FROM Medico m2 " +
                    "LEFT JOIN m2.especialidades e " +
                    "WHERE (:nome IS NULL OR m2.nome LIKE CONCAT('%', :nome, '%')) AND " +
                    "(:crm IS NULL OR m2.crm = :crm) AND " +
                    "(:especialidade IS NULL OR e.nome LIKE CONCAT('%', :especialidade, '%'))")
    Page<Medico> buscarComFiltros(
            @Param("nome") String nome,
            @Param("crm") String crm,
            @Param("especialidade") String especialidade,
            Pageable pageable
    );
}
