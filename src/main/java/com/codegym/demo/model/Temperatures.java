package com.codegym.demo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Temperatures {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String temperature;

    private Date createdTime;

    @ManyToOne
    private Cities cities;
}
