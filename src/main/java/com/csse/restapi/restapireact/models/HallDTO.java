package com.csse.restapi.restapireact.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HallDTO implements Serializable {

    private String[][] hallplaces;
    private int number;


}
