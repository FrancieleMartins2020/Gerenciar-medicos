package br.edu.utfpr.td.tsi.medicos.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicoMongoRepository extends MongoRepository<MedicoMongoDocument, String> {
}