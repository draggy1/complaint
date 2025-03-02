package com.complaint.service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "complainers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Complainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "complainer_name", nullable = false)
    private String name;

    @Column(name = "complainer_surname", nullable = false)
    private String surname;

}
