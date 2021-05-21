package com.csse.restapi.restapireact.models;


import com.csse.restapi.restapireact.entities.Movies;
import com.csse.restapi.restapireact.entities.Schedules;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieViewModel {


    private Movies movie;
    private double rating;
    List<HallViewModel> halls;


}
