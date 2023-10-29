package com.movielibrary.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Movie {
    private long id;
    private String title;
    private int releaseYear;
    private String genre;
    private int rating;
    private String description;
    private String thumbnailUrl;
}
