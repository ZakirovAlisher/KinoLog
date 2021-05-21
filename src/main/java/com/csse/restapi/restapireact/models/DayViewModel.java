package com.csse.restapi.restapireact.models;


import com.csse.restapi.restapireact.entities.Movies;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayViewModel {


    private Date day;
    private String dayStr;
    List<MovieViewModel> movies;

}
