package com.ysf.ipadress.service.impl;

import com.ysf.ipadress.model.Category;
import com.ysf.ipadress.repo.CategoryRepo;
import com.ysf.ipadress.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;

    public CategoryServiceImpl(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public List<Category> categoryList() {
        return categoryRepo.findAll();
    }
}
