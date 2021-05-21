package com.csse.restapi.restapireact.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignCatDTO implements Serializable {

    private Long movie_id;
    private Long cat_id;


}
