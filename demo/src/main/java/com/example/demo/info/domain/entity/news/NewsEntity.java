package com.example.demo.info.domain.entity.news;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsEntity {

    @Id
    private Long id;

    private String url;

    private String title;

    private String image_url;

}
