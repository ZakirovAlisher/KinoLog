package com.csse.restapi.restapireact.models;


import com.csse.restapi.restapireact.entities.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignRoleDTO implements Serializable {

    private Long user_id;
    private Long role_id;


}
