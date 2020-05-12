package com.codegym.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    private Long id;

    private boolean enable;
}
