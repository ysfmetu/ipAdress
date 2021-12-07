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
  /*  @Bean
    CommandLineRunner run(ServerRepo serverRepo) {
        return args -> {
            serverRepo.save(new Server(null, "10.0.0.211", "uygulama sunucusu", "java projelerinin çalıştığı sunucu", "admin", "deneme123", Status.SERVER_UP));
            serverRepo.save(new Server(null, "10.0.0.142", "Veritabanı sunucusu", "Postgre çalışıyor", "admin", "deneme123", Status.SERVER_UP));
        };
    }*/



}
