package com.ysf.ipadress.model.dto;

import lombok.Data;

@Data
public class ServerSaveDTO {
    private long id;
    private String serverName;
    private String status;
    private String password;
    private String kullanici_adi;
    private String ipAddress;
    private String detail;
    private int categoryName;
    private Long categoryId;




}
