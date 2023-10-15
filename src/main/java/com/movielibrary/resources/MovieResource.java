package com.movielibrary.resources;

import com.movielibrary.api.Movie;
import com.movielibrary.db.dao.MovieDAO;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieResource {
    private final MovieDAO movieDAO;

    public MovieResource(MovieDAO movieDAO) {
        this.movieDAO = movieDAO;
    }


    @GET
    public List<Movie> getAllMovies() {
        return movieDAO.getAll();
    }

    @GET
    @Path("/{id}")
    public Movie getMovieById(@PathParam("id") int id) {
        return movieDAO.getMovieById(id);
    }

    @POST
    public Movie createMovie(@Valid Movie movie) {
        int movieId = movieDAO.insertMovie(movie);
        movie.setId(movieId);
        return movie;
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Movie updateMovie(@PathParam("id") int id, @Valid Movie movie) {
        int updatedRows = movieDAO.updateMovie(id, movie);
        if (updatedRows > 0) {
            Movie updatedMovie = movieDAO.getMovieById(id);
            if (updatedMovie != null) {
                return updatedMovie;
            }
        }
        throw new NotFoundException("Movie not found or not updated.");
    }

    @DELETE
    @Path("/{id}")
    public void deleteMovie(@PathParam("id") int id) {
        movieDAO.deleteMovie(id);
    }
}
