package com.csse.restapi.restapireact.repositories;

import com.csse.restapi.restapireact.entities.Movies;
import com.csse.restapi.restapireact.entities.Schedules;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ScheduleRepository extends JpaRepository<Schedules, Long> {

    @Query(value = "select v from Schedules v group by v.day")
    public List<?> findSurveyCount();

}
