package com.ysf.ipadress.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ysf.ipadress.model.Response;
import com.ysf.ipadress.model.Server;
import com.ysf.ipadress.model.dto.UserDTO;
import com.ysf.ipadress.security.EResult;
import com.ysf.ipadress.security.JwtUtil;
import com.ysf.ipadress.security.LoggedUserModel;
import com.ysf.ipadress.security.Result;
import com.ysf.ipadress.service.impl.ServerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.ysf.ipadress.enumeration.Status.SERVER_UP;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerController {

    private final ServerServiceImpl serverService;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public Result<Object> saveServer(@RequestBody UserDTO dto) throws JsonProcessingException {

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUserName(), dto.getPassword()));
        String token = jwtUtil.generateToken(authenticate);
        LoggedUserModel user = (LoggedUserModel) authenticate.getPrincipal();
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("info", user);

        return new Result<Object>(EResult.success,"Başarılı",result);
    }



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
