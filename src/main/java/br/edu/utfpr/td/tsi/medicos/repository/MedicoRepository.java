package br.edu.utfpr.td.tsi.medicos.repository;

import br.edu.utfpr.td.tsi.medicos.model.Medico;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MedicoRepository {

    private final List<Medico> medicos = new ArrayList<>();

    private final AtomicLong sequence = new AtomicLong(0);

    public List<Medico> findAll() {
        return new ArrayList<>(medicos);
    }

    public Optional<Medico> findById(Long id) {

        return medicos.stream()
                .filter(medico -> medico.getId().equals(id))
                .findFirst();
    }

    public Optional<Medico> findByCrmAndUf(String crm, String uf) {

        return medicos.stream()
                .filter(medico ->
                        medico.getCrm().equalsIgnoreCase(crm)
                                && medico.getUf().equalsIgnoreCase(uf))
                .findFirst();
    }

    public Medico save(Medico medico) {

        if (medico.getId() == null) {
            medico.setId(sequence.incrementAndGet());
            medicos.add(medico);
        } else {

            for (int i = 0; i < medicos.size(); i++) {

                if (medicos.get(i).getId().equals(medico.getId())) {
                    medicos.set(i, medico);
                    break;
                }
            }
        }

        return medico;
    }

    public void deleteById(Long id) {

        medicos.removeIf(medico ->
                medico.getId().equals(id));
    }

    public boolean existsById(Long id) {

        return medicos.stream()
                .anyMatch(medico ->
                        medico.getId().equals(id));
    }

    public boolean existsByCrmAndUf(
            String crm,
            String uf,
            Long idExcluir) {

        return medicos.stream()
                .anyMatch(medico ->
                        medico.getCrm().equalsIgnoreCase(crm)
                                && medico.getUf().equalsIgnoreCase(uf)
                                && !medico.getId().equals(idExcluir));
    }
}
