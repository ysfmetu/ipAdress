package com.ysf.ipadress;

import com.ysf.ipadress.enumeration.Status;
import com.ysf.ipadress.model.Server;
import com.ysf.ipadress.repo.ServerRepo;
import com.ysf.ipadress.service.ServerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@SpringBootApplication
public class IpAdressApplication {

    public static void main(String[] args) {
        SpringApplication.run(IpAdressApplication.class, args);
    }




}
