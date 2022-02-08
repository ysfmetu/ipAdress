package com.ysf.ipadress.repo;

import com.ysf.ipadress.model.Server;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServerRepo extends JpaRepository<Server,Long> {
    Server findByIpAddress(String ipAddress);
    @Query(value = "select s from Server s where s.durum= 'AKTİF' and s.category.id=:id ")
    List<Server> findAllByCategoryId(Long id);



}


