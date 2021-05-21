package com.csse.restapi.restapireact.services.impl;


import com.csse.restapi.restapireact.entities.Categories;
import com.csse.restapi.restapireact.entities.Halls;
import com.csse.restapi.restapireact.entities.Movies;
import com.csse.restapi.restapireact.entities.Schedules;
import com.csse.restapi.restapireact.repositories.CatsRepository;
import com.csse.restapi.restapireact.repositories.HallRepository;
import com.csse.restapi.restapireact.repositories.MovieRepository;
import com.csse.restapi.restapireact.repositories.ScheduleRepository;
import com.csse.restapi.restapireact.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    public MovieRepository movieRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private CatsRepository catsRepository;
    @Autowired
    private HallRepository hallRepository;
    @Override
    public List<Movies> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Movies addMovie(Movies item) {
        return movieRepository.save(item);
    }

    @Override
    public Movies getMovie(Long id) {
        Optional<Movies> opt = movieRepository.findById(id);
        return opt.isPresent()?opt.get():null;
    }

    @Override
    public Movies saveMovie(Movies item) {
        return movieRepository.save(item);
    }

    @Override
    public void deleteMovie(Movies item) {
        movieRepository.delete(item);
    }

    @Override
    public List<Schedules> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    @Override
    public Schedules addSchedule(Schedules item) {
        return scheduleRepository.save(item);
    }

    @Override
    public Schedules getSchedule(Long id) {
        Optional<Schedules> opt = scheduleRepository.findById(id);
        return opt.isPresent()?opt.get():null;
    }

    @Override
    public Schedules saveSchedule(Schedules item) {
        return scheduleRepository.save(item);
    }

    @Override
    public void deleteSchedule(Schedules item) {
        scheduleRepository.delete(item);
    }

    @Override
    public List<Categories> getAllCategories() {
        return catsRepository.findAll ();
    }

    @Override
    public Categories addCategory(Categories item) {
        return catsRepository.save(item);
    }

    @Override
    public Categories getCategory(Long id) {
        Optional<Categories> opt = catsRepository.findById(id);
        return opt.isPresent()?opt.get():null;
    }

    @Override
    public Categories saveCategory(Categories item) {
        return catsRepository.save(item);
    }

    @Override
    public void deleteCategory(Categories item) {
         catsRepository.delete(item);
    }

    @Override
    public List<Halls> getAllHalls() {
        return hallRepository.findAll ();
    }

    @Override
    public Halls addHall(Halls item) {
        return hallRepository.save (item);
    }

    @Override
    public Halls getHall(Long id) {
        Optional<Halls> opt = hallRepository.findById(id);
        return opt.isPresent()?opt.get():null;
    }

    @Override
    public Halls saveHall(Halls item) {
        return hallRepository.save (item);
    }

    @Override
    public void deleteHall(Halls item) {
        hallRepository.delete (item);
    }


}
