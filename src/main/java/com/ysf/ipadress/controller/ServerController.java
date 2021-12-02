package com.ysf.ipadress.controller;

import com.ysf.ipadress.enumeration.Status;
import com.ysf.ipadress.model.Response;
import com.ysf.ipadress.model.Server;
import com.ysf.ipadress.service.impl.ServerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.ysf.ipadress.enumeration.Status.SERVER_UP;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerController {

    private final ServerServiceImpl serverService;
    @GetMapping("/list")
    public ResponseEntity<Response> getServers() throws InterruptedException {
        /*TimeUnit.SECONDS.sleep(5);*/
        return ResponseEntity.ok(
            Response.builder()
                    .timeStamp(now())
                    .data(Map.of("servers",serverService.list(30)))
                    .message("server listesi çekilmiştir.")
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .build()
        );
    }
    @GetMapping("/ping/{ipAddress}")
    public ResponseEntity<Response> pingServers(@PathVariable("ipAddress") String ipAddress) throws IOException {
        Server server =serverService.ping(ipAddress);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("server",server))
                        .message(server.getStatus()== SERVER_UP? "ping işlemi başarılı" : "ping başarısız")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
    @PostMapping("/save")
    public ResponseEntity<Response> saveServer(@RequestBody @Valid Server server) {

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("server",serverService.create(server)))
                        .message("kayıt işlemi başarılı")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<Response> getServer(@PathVariable("id") Long id) throws IOException {

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("server",serverService.get(id)))
                        .message("server tetiklendi")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteServer(@PathVariable("id") Long id) throws IOException {

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("server",serverService.delete(id)))
                        .message("server kaydı silindi")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}
