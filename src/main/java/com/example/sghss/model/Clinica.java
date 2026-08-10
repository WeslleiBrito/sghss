package com.example.sghss.model;


import com.example.sghss.model.base.UnidadeSaude;
import com.example.sghss.model.enums.TipoUnidade;
import com.example.sghss.model.interfaces.DadosPessoa;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_clinica")
@PrimaryKeyJoinColumn(name = "unidade_saude_id")
@NoArgsConstructor
@DiscriminatorValue("HOSPITAL")
public class Clinica extends UnidadeSaude {

    @ElementCollection
    @CollectionTable(name = "tb_clinica_especialidade", joinColumns = @JoinColumn(name = "clinica_id"))
    @Column(name = "especialidade")
    private List<String> especialidadesAtendidas;

    @Override
    public boolean consultarDisponibilidade() {
        return this.getStatusOperacao();
    }

    @Override
    public TipoUnidade getTipoUnidade() {
        return TipoUnidade.HOSPITAL;
    }

    @Override
    public DadosPessoa getRepresentacaoDados() {
        return null;
    }
}