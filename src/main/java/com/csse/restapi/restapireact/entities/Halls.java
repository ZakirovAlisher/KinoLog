package com.csse.restapi.restapireact.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "halls")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Halls {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private Long number;


    @Column(name = "places", columnDefinition="TEXT")
    private String places;



}
