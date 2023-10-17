package com.movielibrary.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMovieRequestDTO {
    @Size(min = 1, max = 255)
    private String title;

    private Integer releaseYear;

    private String genre;

    @Min(value = 1, message = "The rating must be at least 1")
    @Max(value = 5, message = "The rating must be at most 5")
    private Integer rating;

    @Size(max = 1000)
    private String description;

}
