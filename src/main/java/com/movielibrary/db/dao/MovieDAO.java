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
        INSERT INTO movies (title, release_year, genre, rating, description, thumbnail_url)
        VALUES (:movie.title, :movie.releaseYear, :movie.genre, :movie.rating, :movie.description, :movie.thumbnailUrl)
    """)
    @Transaction
    @GetGeneratedKeys
    int insertMovie(@BindBean("movie") Movie movie);

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
        description = :movie.description,
        thumbnail_url = :movie.thumbnailUrl
    WHERE id = :id
    """)
    @Transaction
    int updateMovie(@Bind("id") int id, @BindBean("movie") Movie movie);

    @SqlUpdate("DELETE FROM movies WHERE id = :id")
    @Transaction
    void deleteMovie(@Bind("id") int id);

    @SqlQuery("""
        SELECT *
        FROM movies
        WHERE LOWER(SUBSTR(title, 1, 1)) = LOWER(:firstLetter)
        LIMIT :limit
    """)
    List<Movie> getMoviesByFirstLetterOfTitle(
            @Bind("firstLetter") String firstLetter,
            @Bind("limit") int limit
    );
}

