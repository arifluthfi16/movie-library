package com.movielibrary.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMovieRequestDTO {
    @NotNull
    @Size(min = 1, max = 255)
    private String title;

    @NotNull
    private int releaseYear;

    @NotNull
    private String genre;

    @Min(value = 1, message = "The rating must be at least 1")
    @Max(value = 5, message = "The rating must be at most 5")
    private int rating;

    @Size(max = 1000)
    private String description;
}
