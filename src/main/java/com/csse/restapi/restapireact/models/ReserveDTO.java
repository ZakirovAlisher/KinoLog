package com.csse.restapi.restapireact.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveDTO implements Serializable {
    private String email;
    private Long itemId;
    private List<Tickets> tickets;





}
