package com.letscook.menu.repository;

import com.letscook.menu.model.Dish;
import com.letscook.menu.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Long>  {
}
