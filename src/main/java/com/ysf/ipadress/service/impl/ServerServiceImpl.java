package com.ysf.ipadress.service.impl;

import com.google.crypto.tink.subtle.AesGcmJce;
import com.ysf.ipadress.enumeration.Status;
import com.ysf.ipadress.model.Server;
import com.ysf.ipadress.repo.ServerRepo;
import com.ysf.ipadress.service.ServerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class ServerServiceImpl implements ServerService {

    private final ServerRepo serverRepo;

    public ServerServiceImpl(ServerRepo serverRepo) {
        this.serverRepo = serverRepo;
    }


    @Override
    public Server create(Server server) {
        return serverRepo.save(server);
    }

    @Override
    public Collection<Server> list(int limit) {
        log.info("tüm kayıtlı bilgiler taranıyor ");
        return serverRepo.findAll(PageRequest.of(0,limit)).toList();
    }

    @Override
    public Server get(long id) {
        log.info("ping atılan id adresi : ",id);
        return serverRepo.findById(id).get();
    }

    @Override
    public Server update(Server server) {
       Server updatedServer= serverRepo.save(server);
        return updatedServer;
    }

    @Override
    public Boolean delete(Long id) {
        log.info("silinen id adresi : ",id);
        serverRepo.deleteById(id);
        return true;
    }
    @Override
    public Server ping(String ipAddress) throws IOException {
        log.info("ping atılan server adresi : ", ipAddress);
        Server server =serverRepo.findByIpAddress(ipAddress);
        InetAddress address=InetAddress.getByName(ipAddress);
        server.setStatus(address.isReachable(1000)? Status.SERVER_UP:Status.SERVER_DOWN);
        serverRepo.save(server);
        return server;
    }

    @Override
    public Server pasif(long id) {
        Server server =serverRepo.findById(id).get();
        server.setDurum("PASİF");
        Server updatedServer=serverRepo.save(server);
        return updatedServer;
    }

    @Override
    public Server[] servers(Long id) {
        String key128Bit="14slduGFdlFJn3w3";
        String  associatedData="ysf";
        Server[] servers= serverRepo.findAllByCategoryId(id).toArray(new Server[0]);
        for(int i = 0; i< servers.length; i++) {

            try {
                byte[] cipherBytes = servers[i].getPassword().getBytes(StandardCharsets.ISO_8859_1);
                AesGcmJce aesGcmJce = new AesGcmJce(key128Bit.getBytes());
                byte[] decryptedBytes = aesGcmJce.decrypt(cipherBytes, associatedData.getBytes());
                String convertedString = new String(decryptedBytes);
                servers[i].setPassword(convertedString);
            } catch (Exception ex) {
                System.err.println("encrypt Error : " + ex);
            }
        }
        return servers;
    }
}
