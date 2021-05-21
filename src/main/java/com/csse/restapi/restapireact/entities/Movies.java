package com.csse.restapi.restapireact.entities;

import com.csse.restapi.restapireact.models.MovieViewModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "movies")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;



    @Column(name = "pic")
    private String pic;

    @Column(name = "rating")
    private double rating;

    @Column(name = "year")
    private int year;

    @Column(name = "description", columnDefinition="TEXT")
    private String desc;


    @ManyToMany(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Categories> categories;


}
