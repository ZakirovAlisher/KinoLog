package com.csse.restapi.restapireact.services;


import com.csse.restapi.restapireact.entities.Categories;
import com.csse.restapi.restapireact.entities.Halls;
import com.csse.restapi.restapireact.entities.Movies;
import com.csse.restapi.restapireact.entities.Schedules;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {

    List<Movies> getAllMovies();
    Movies addMovie(Movies item);
    Movies getMovie(Long id);
    Movies saveMovie(Movies item);
    void deleteMovie(Movies item);

    List<Schedules> getAllSchedules();
    Schedules addSchedule(Schedules item);
    Schedules getSchedule(Long id);
    Schedules saveSchedule(Schedules item);
    void deleteSchedule(Schedules item);

    List<Categories> getAllCategories();
    Categories addCategory(Categories item);
    Categories getCategory(Long id);
    Categories saveCategory(Categories item);
    void deleteCategory(Categories item);


    List<Halls> getAllHalls();
    Halls addHall(Halls item);
    Halls getHall(Long id);
    Halls saveHall(Halls item);
    void deleteHall(Halls item);
}
