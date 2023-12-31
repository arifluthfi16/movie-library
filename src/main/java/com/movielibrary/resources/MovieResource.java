package com.movielibrary.resources;

import com.movielibrary.api.Movie;
import com.movielibrary.client.UserRPC;
import com.movielibrary.db.dao.MovieDAO;
import com.movielibrary.dto.CreateMovieRequestDTO;
import com.movielibrary.dto.UpdateMovieRequestDTO;
import com.movielibrary.dto.UserResponseDTO;
import com.movielibrary.response.ResponseWrapper;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieResource {
    private final MovieDAO movieDAO;
    private final UserRPC userRPC;

    public MovieResource(MovieDAO movieDAO, UserRPC userRPC) {
        this.movieDAO = movieDAO;
        this.userRPC = userRPC;
    }

    @GET
    public Response getAllMovies() {
        List<Movie> movieList = movieDAO.getAll();
        ResponseWrapper<List<Movie>> responseWrapper = new ResponseWrapper<>();
        responseWrapper.setMessage("Sucess retrieving all movies");
        responseWrapper.setData(movieList);
        return responseWrapper.build();
    }

    @GET
    @Path("/{id}")
    public Response getMovieById(@PathParam("id") int id) {
        Movie movie = movieDAO.getMovieById(id);
        ResponseWrapper<Movie> responseWrapper = new ResponseWrapper<>();
        responseWrapper.setMessage("Sucess retrieving all movies");
        responseWrapper.setData(movie);
        return responseWrapper.build();
    }

    @POST
    public Response createMovie(@Valid CreateMovieRequestDTO createMovieRequestDTO) {
        String errorString = null;

        if (createMovieRequestDTO.getReleaseYear() < 1950) errorString = "Minimum release year is 1950";
        if (
            createMovieRequestDTO.getThumbnailUrl() != null &&
            !Objects.equals(createMovieRequestDTO.getThumbnailUrl(), "") &&
            !isValidImageUrl(createMovieRequestDTO.getThumbnailUrl())
        ) {
            errorString = "Invalid Image URL";
        }

        if (errorString != null) {
            return new ResponseWrapper<>(Response.Status.BAD_REQUEST, errorString, null).build();
        }

        Movie movie = new Movie();

        movie.setDescription(createMovieRequestDTO.getDescription());
        movie.setGenre(createMovieRequestDTO.getGenre());
        movie.setTitle(createMovieRequestDTO.getTitle());
        movie.setRating(createMovieRequestDTO.getRating());
        movie.setReleaseYear(createMovieRequestDTO.getReleaseYear());
        movie.setThumbnailUrl(createMovieRequestDTO.getThumbnailUrl());

        int movieId = movieDAO.insertMovie(movie);

        movie.setId(movieId);

        ResponseWrapper<Movie> responseWrapper = new ResponseWrapper<>();
        responseWrapper.setMessage("Successfully created new movie");
        responseWrapper.setData(movie);

        return responseWrapper.build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMovie(@PathParam("id") int id, @Valid UpdateMovieRequestDTO updateRequest) {
        String errorString = null;
        Movie existingMovie = movieDAO.getMovieById(id);
        if (existingMovie == null) {
            return new ResponseWrapper<>(Response.Status.BAD_REQUEST, "Movie not found", null).build();
        }

        if (updateRequest.getTitle() != null) {
            existingMovie.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getReleaseYear() != null) {
            if (updateRequest.getReleaseYear() < 1950) errorString = "Minimum release year is 1950";
            existingMovie.setReleaseYear(updateRequest.getReleaseYear());
        }
        if (updateRequest.getGenre() != null) {
            existingMovie.setGenre(updateRequest.getGenre());
        }
        if (updateRequest.getRating() != null) {
            existingMovie.setRating(updateRequest.getRating());
        }
        if (updateRequest.getDescription() != null) {
            existingMovie.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getThumbnailUrl() != null) {
            if (
                    updateRequest.getThumbnailUrl() != null &&
                    !Objects.equals(updateRequest.getThumbnailUrl(), "") &&
                    !isValidImageUrl(updateRequest.getThumbnailUrl())
            ) {
                errorString = "Invalid Image URL";
            }
            existingMovie.setThumbnailUrl(updateRequest.getThumbnailUrl());
        }

        if (errorString != null) {
            return new ResponseWrapper<>(Response.Status.BAD_REQUEST, errorString, null).build();
        }

        int updatedRows = movieDAO.updateMovie(id, existingMovie);
        if (updatedRows > 0) {
            Movie updatedMovie = movieDAO.getMovieById(id);
            if (updatedMovie != null) {
                ResponseWrapper<Movie> responseWrapper = new ResponseWrapper<Movie>(Response.Status.OK,"Movie updated successfully", updatedMovie);
                return responseWrapper.build();
            }
        }

        throw new NotFoundException("Movie not found or not updated.");
    }

    @DELETE
    @Path("/{id}")
    public Response deleteMovie(@PathParam("id") int id) {
        movieDAO.deleteMovie(id);
        return new ResponseWrapper<>(Response.Status.OK,"Movie deleted successfully", null).build();
    }

    @GET
    @Path("/suggestion")
    public Response getMovieByUserCountry(@HeaderParam("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring("Bearer ".length()).trim();
                UserResponseDTO user = userRPC.getUserByToken(token);

                if (user != null) {
                    String country = user.getCountry();
                    char firstLetter = country != null && !country.isEmpty() ? country.charAt(0) : 'a';
                    List<Movie> recommendedMovies = movieDAO.getMoviesByFirstLetterOfTitle(String.valueOf(firstLetter), 20);

                    return new ResponseWrapper<>(Response.Status.OK, "User recommendation retrieved", recommendedMovies).build();
                } else {
                    return new ResponseWrapper<>(Response.Status.UNAUTHORIZED, "User not found", null).build();
                }
            } else {
                return new ResponseWrapper<>(Response.Status.OK,"Authorization Header is invalid", null).build();
            }
        } catch (Exception e) {
            return new ResponseWrapper<>(Response.Status.OK,"Something went wrong", null).build();
        }
    }

    private static boolean isValidImageUrl(String url) {
        String imageUrlPattern = "^(http|https)://.*\\.(jpg|jpeg|png|gif|bmp)$";
        Pattern pattern = Pattern.compile(imageUrlPattern);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }
}
