package com.movielibrary.db.dao;

import com.movielibrary.api.Movie;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.List;

@RegisterBeanMapper(Movie.class)
public interface MovieDAO {
    @SqlUpdate("""
        INSERT INTO movies (title, release_year, genre, rating, description)
        VALUES (:movie.title, :movie.releaseYear, :movie.genre, :movie.rating, :movie.description)
    """)
    @Transaction
    @GetGeneratedKeys
    int insertMovie(@BindBean Movie movie);

    @SqlQuery("SELECT * FROM movies WHERE id = :id")
    Movie getMovieById(
            @Bind("id") int id
    );

    @SqlQuery("SELECT * FROM movies")
    List<Movie> getAll();

    @SqlUpdate("""
        UPDATE movies
        SET
            title = :movie.title,
            release_year = :movie.releaseYear,
            genre = :movie.genre,
            rating = :movie.rating,
            description = :movie.description
        WHERE id = :id
    """)
    @Transaction
    int updateMovie(@Bind("id") int id, @BindBean Movie movie);

    @SqlUpdate("DELETE FROM movies WHERE id = :id")
    @Transaction
    void deleteMovie(@Bind("id") int id);
}

