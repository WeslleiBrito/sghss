package com.example.sghss.model;


import com.example.sghss.model.base.UnidadeSaude;
import com.example.sghss.model.enums.TipoUnidade;
import com.example.sghss.model.interfaces.DadosPessoa;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_hospital")
@PrimaryKeyJoinColumn(name = "unidade_saude_id")
@DiscriminatorValue("JURIDICA")
public class Hospital extends UnidadeSaude {

    @Column(name = "quantidade_leitos")
    private Integer quantidadeLeitos;

    @Column(name = "possui_uti")
    private Boolean possuiUTI;

    @Override
    public boolean consultarDisponibilidade() {
        // Exemplo: Um hospital só está disponível se operar e tiver leitos
        return this.getStatusOperacao() && (this.quantidadeLeitos != null && this.quantidadeLeitos > 0);
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