package com.ysf.ipadress.repo;

import com.ysf.ipadress.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerRepo extends JpaRepository<Server,Long> {
    Server findByIpAddress(String ipAddress);
    Server findByServerName(String name);


}
