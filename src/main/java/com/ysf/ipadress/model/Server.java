package com.ysf.ipadress.model;

import com.ysf.ipadress.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Server {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    @NotEmpty(message = "ip adresi unique değer içermelidir ayrıca boş geçilemez ")
    private String ipAddress;
    private String serverName;
    private String detail;
    private String kullanici_adi;
    private String password;
    private Status status;
}
