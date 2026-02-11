package com.example.veebilehekala.repository;

import com.example.veebilehekala.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
