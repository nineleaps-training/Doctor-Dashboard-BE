package com.sagar.Dashboard.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Login {

    private Long id;
    private String username;
    private String email;
    //private String password;
}
