package com.movielibrary;

import com.movielibrary.client.UserRPC;
import com.movielibrary.db.dao.MovieDAO;
import com.movielibrary.resources.MovieResource;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayBundle;
import io.dropwizard.flyway.FlywayFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;

public class MovieLibraryServiceApplication extends Application<MovieLibraryServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new MovieLibraryServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "MovieLibraryService";
    }

    @Override
    public void initialize(final Bootstrap<MovieLibraryServiceConfiguration> bootstrap) {
        bootstrap.addBundle(new FlywayBundle<MovieLibraryServiceConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(MovieLibraryServiceConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }

            @Override
            public FlywayFactory getFlywayFactory(MovieLibraryServiceConfiguration configuration) {
                return configuration.getFlywayFactory();
            }
        });
    }

    @Override
    public void run(final MovieLibraryServiceConfiguration config,
                    final Environment environment) throws Exception {
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, config.getDataSourceFactory(), "postgresql");

        MovieDAO movieDAO = jdbi.onDemand(MovieDAO.class);

        runFlyway(config.getDataSourceFactory());
        UserRPC userRPC = new UserRPC();

        environment.jersey().register(new MovieResource(movieDAO, userRPC));
    }

    private void runFlyway(DataSourceFactory dataSourceFactory) {
        Flyway flyway = Flyway.configure()
                            .driver("com.microsoft.sqlserver.jdbc.SQLServerDriver")
                            .dataSource(dataSourceFactory.getUrl(), dataSourceFactory.getUser(), dataSourceFactory.getPassword())
                            .load();
        flyway.migrate();
    }
}
