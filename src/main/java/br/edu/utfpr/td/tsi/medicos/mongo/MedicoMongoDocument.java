package br.edu.utfpr.td.tsi.medicos.mongo;

import org.bson.Document;
import org.springframework.data.annotation.Id;

import java.util.Map;

@org.springframework.data.mongodb.core.mapping.Document(collection = "medicos")
public class MedicoMongoDocument extends Document {

    @Id
    private String id;

    private Map<String, Object> dados;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Map<String, Object> getDados() { return dados; }
    public void setDados(Map<String, Object> dados) { this.dados = dados; }
}
