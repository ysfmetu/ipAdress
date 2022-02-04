package com.ysf.ipadress.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.crypto.tink.subtle.AesGcmJce;
import com.ysf.ipadress.model.Category;
import com.ysf.ipadress.model.Response;
import com.ysf.ipadress.model.Server;
import com.ysf.ipadress.model.dto.ServerSaveDTO;
import com.ysf.ipadress.model.dto.UserDTO;
import com.ysf.ipadress.repo.CategoryRepo;
import com.ysf.ipadress.repo.ServerRepo;
import com.ysf.ipadress.security.EResult;
import com.ysf.ipadress.security.JwtUtil;
import com.ysf.ipadress.security.LoggedUserModel;
import com.ysf.ipadress.security.Result;
import com.ysf.ipadress.service.CategoryService;
import com.ysf.ipadress.service.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import static com.ysf.ipadress.enumeration.Status.SERVER_DOWN;
import static com.ysf.ipadress.enumeration.Status.SERVER_UP;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ServerController {

    private final ServerService serverService;
    private final CategoryService categoryService;
    private final CategoryRepo categoryRepo;
    private final ServerRepo serverRepo;


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
    @GetMapping("/listcat")
    public ResponseEntity<Response> getCategories() throws InterruptedException {
        /*TimeUnit.SECONDS.sleep(5);*/
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("servers",categoryService.categoryList()))
                        .message("server listesi çekilmiştir.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
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
    @GetMapping("/list/{id}")
    public ResponseEntity<Response> getServersByCategory(@PathVariable("id") Long id) throws InterruptedException {
        /*TimeUnit.SECONDS.sleep(5);*/
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("servers",serverService.servers(id)))
                        .message("server listesi çekilmiştir.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
    @GetMapping("/ping/{ipAddress}")
    public ResponseEntity<Response> pingServers(@PathVariable("ipAddress") String ipAddress) throws IOException {
        Server server =serverService.ping(ipAddress);
        String key128Bit="14slduGFdlFJn3w3";
        String  associatedData="ysf";
        try {
        byte[] cipherBytes = server.getPassword().getBytes(StandardCharsets.ISO_8859_1);
        AesGcmJce aesGcmJce = new AesGcmJce(key128Bit.getBytes());
        byte[] decryptedBytes = aesGcmJce.decrypt(cipherBytes, associatedData.getBytes());
        String convertedString = new String(decryptedBytes);
        server.setPassword(convertedString);
        } catch (Exception ex) {
            System.err.println("encrypt Error : " + ex);
        }
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
    public ResponseEntity<Response> saveServer(@RequestBody ServerSaveDTO serverSaveDTO){
        Server server = serverRepo.findById(serverSaveDTO.getId()).orElse(new Server());
        String key128Bit="14slduGFdlFJn3w3";
        String plainText=serverSaveDTO.getPassword();
        String  associatedData="ysf";
        if(serverSaveDTO.getStatus() =="SERVER_DOWN"){
            server.setStatus(SERVER_DOWN);
        }
        else if(serverSaveDTO.getStatus() =="SERVER_UP")
        {
            server.setStatus(SERVER_UP);
        }
        /*//encrypt*/
        try {
            AesGcmJce aesGcmJce = new AesGcmJce(key128Bit.getBytes());
            byte[] encrypted = aesGcmJce.encrypt(plainText.getBytes(), associatedData.getBytes());
            String stDataEnc = new String(encrypted, StandardCharsets.ISO_8859_1);
            server.setPassword(stDataEnc);
        }catch (Exception ex) {
            System.err.println("encrypt Error : " + ex);
        }


        server.setServerName(serverSaveDTO.getServerName());
        server.setDetail(serverSaveDTO.getDetail());
        server.setKullanici_adi(serverSaveDTO.getKullanici_adi());
        /*server.setPassword(serverSaveDTO.getPassword());*/
        server.setIpAddress(serverSaveDTO.getIpAddress());

        Category category = categoryRepo.findById(serverSaveDTO.getCategoryId()).get();

        server.setCategory(category);

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
    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateServer(@RequestBody ServerSaveDTO serverSaveDTO,@PathVariable Long id) {
        Server server = serverService.get(id);

        server.setServerName(serverSaveDTO.getServerName());
        server.setDetail(serverSaveDTO.getDetail());
        server.setKullanici_adi(serverSaveDTO.getKullanici_adi());
        server.setPassword(serverSaveDTO.getPassword());
        server.setIpAddress(serverSaveDTO.getIpAddress());

       /* Category category = new Category();
        category.setId(serverSaveDTO.getCategoryName());
        server.setCategory(category);*/

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("server",serverService.update(server)))
                        .message("güncelleme işlemi başarılı")
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
