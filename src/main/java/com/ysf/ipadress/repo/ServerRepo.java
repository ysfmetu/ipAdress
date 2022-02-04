package com.ysf.ipadress.repo;

import com.ysf.ipadress.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServerRepo extends JpaRepository<Server,Long> {
    Server findByIpAddress(String ipAddress);
    Server findByServerName(String name);

    List<Server> findAllByCategoryId(Long id);



}
