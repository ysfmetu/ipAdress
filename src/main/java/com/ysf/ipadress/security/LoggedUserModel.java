package com.ysf.ipadress.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoggedUserModel {
    private Long id;
    private String userName;
    private String displayName;


}
