package com.letscook.menu.repository;

import com.letscook.menu.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findMealsByMenu_Cook_Id(Long id);
}
