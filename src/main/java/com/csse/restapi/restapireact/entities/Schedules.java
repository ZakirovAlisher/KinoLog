package com.csse.restapi.restapireact.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Table(name = "schedules")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Schedules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;



    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    Movies movie;

    @Column(name = "day")
    private Date day;

    @Column(name = "hall")
    private int hall;

    @Column(name = "time")
    private Time time;

    @Column(name = "finished")
    private boolean finished;

    @Column(name = "places", columnDefinition="TEXT")
    private String places;

    @Column(name = "price_ch")
    private int price_ch;

    @Column(name = "price_st")
    private int price_st;

    @Column(name = "price_ad")
    private int price_ad;
}
