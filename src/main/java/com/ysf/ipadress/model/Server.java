package com.ysf.ipadress.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ysf.ipadress.enumeration.Status;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Objects;

@Entity
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
@Getter
@Setter
public class Server implements Serializable {
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

    @ManyToOne()
    private Category category;


}
