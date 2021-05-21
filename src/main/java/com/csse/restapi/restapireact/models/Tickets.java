package com.csse.restapi.restapireact.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tickets implements Serializable {
    private Integer row;

    private Integer col;
    private Integer type;
    private String typeStr;
    private Integer price;

}
