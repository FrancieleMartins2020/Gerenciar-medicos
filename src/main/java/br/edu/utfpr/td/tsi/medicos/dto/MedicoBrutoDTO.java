package br.edu.utfpr.td.tsi.medicos.dto;

import org.springframework.data.mongodb.core.mapping.Field;

public record MedicoBrutoDTO(
        @Field("NM_MEDICO") String nomeMedico,
        @Field("NU_CRM") String nuCrm,
        @Field("SG_UF") String sgUf,
        @Field("ESPECIALIDADE") String especialidade,
        @Field("AREA_ATUACAO") String areaAtuacao,
        @Field("TIPO_INSCRICAO") String tipoInscricao,
        @Field("SITUACAO") String situacao,
        @Field("DT_INSCRICAO") String dtInscricao,
        @Field("PRIM_INSCRICAO_UF") String primInscricaoUf,
        @Field("NM_INSTITUICAO_GRADUACAO") String nmInstituicaoGraduacao,
        @Field("DT_GRADUACAO") Object dtGraduacao,
        @Field("MUNICIPIO") String municipio,
        @Field("TELEFONE") String telefone
) {}
