package com.ysf.ipadress.repo;

import com.ysf.ipadress.model.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KullaniciRepostories extends JpaRepository<Kullanici,Long> {

    Kullanici findByIdEquals(int id);


    Optional<Kullanici> findByUserName(String username);
}