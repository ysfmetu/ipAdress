package com.ysf.ipadress.repo;

import com.ysf.ipadress.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category,Long> {
    @Override
    Optional<Category> findById(Long aLong);
}
