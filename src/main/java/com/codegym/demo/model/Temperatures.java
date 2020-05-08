package com.codegym.demo.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Temperatures {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String temperature;

    @ManyToOne
    private Cities cities;
}
