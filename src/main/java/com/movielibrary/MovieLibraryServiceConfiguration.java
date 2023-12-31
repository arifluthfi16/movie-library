package com.movielibrary;

import com.movielibrary.dto.RabbitMQConfigurationDTO;
import io.dropwizard.core.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayFactory;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.*;
import jakarta.validation.constraints.*;

public class MovieLibraryServiceConfiguration extends Configuration {
    @JsonProperty("database")
    @NotNull
    public DataSourceFactory database;

    @JsonProperty("flyway")
    public FlywayFactory flywayFactory;

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory factory) {
        this.database = factory;
    }

    @NotNull
    @JsonProperty("rabbitmq")
    private RabbitMQConfigurationDTO rabbitMQConfiguration;

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    public FlywayFactory getFlywayFactory() {
        return flywayFactory;
    }

    @JsonProperty("rabbitmq")
    public RabbitMQConfigurationDTO getRabbitMQConfiguration() {
        return rabbitMQConfiguration;
    }

    @JsonProperty("rabbitmq")
    public void setRabbitMQConfiguration(RabbitMQConfigurationDTO rabbitMQConfiguration) {
        this.rabbitMQConfiguration = rabbitMQConfiguration;
    }
}
