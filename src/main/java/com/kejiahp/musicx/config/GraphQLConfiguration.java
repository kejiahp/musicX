package com.kejiahp.musicx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import com.kejiahp.musicx.util.scalars.InstantScalar;

import graphql.scalars.ExtendedScalars;

@Configuration
public class GraphQLConfiguration {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder.scalar(ExtendedScalars.Date).scalar(ExtendedScalars.Url)
                .scalar(InstantScalar.GRAPHQL_INSTANT);
    }
}
