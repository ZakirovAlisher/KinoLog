package com.csse.restapi.restapireact.repositories;

import com.csse.restapi.restapireact.entities.Categories;
import com.csse.restapi.restapireact.entities.Movies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CatsRepository extends JpaRepository<Categories, Long> {


}
