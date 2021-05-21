package com.csse.restapi.restapireact.models;


import com.csse.restapi.restapireact.entities.Movies;
import com.csse.restapi.restapireact.entities.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDTO implements Serializable {

    private Long id;


    Long movie_id;


    private Date day;

    private int hall;

    private String time;

    private boolean finished;

    private int price_ch;

    private int price_st;

    private int price_ad;
    String places;
}
