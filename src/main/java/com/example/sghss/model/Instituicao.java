package com.example.sghss.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_instituicao")
@DiscriminatorValue("INSTITUICAO")
public class Instituicao extends PessoaJuridica {

}