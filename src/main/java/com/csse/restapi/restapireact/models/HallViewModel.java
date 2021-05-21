package com.csse.restapi.restapireact.models;


import com.csse.restapi.restapireact.entities.Movies;
import com.csse.restapi.restapireact.entities.Schedules;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HallViewModel {


    private Integer hallNumber;

    List<Schedules> schedules;

}
