package com.csse.restapi.restapireact.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoviesSearchDTO implements Serializable {


    private String title;


    private int page;

    private int size;


}