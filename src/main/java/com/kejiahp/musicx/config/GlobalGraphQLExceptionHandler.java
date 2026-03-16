package com.kejiahp.musicx.config;

import java.util.Map;

import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Controller;

import com.kejiahp.musicx.util.DomainException;

import graphql.GraphQLError;
import graphql.GraphqlErrorException;

@Controller
public class GlobalGraphQLExceptionHandler {
    // Handles exceptions throw from the DomainException Class
    @GraphQlExceptionHandler(DomainException.class)
    public GraphQLError handleDomainException(DomainException ex) {
        return GraphqlErrorException.newErrorException().message(ex.getMessage())
                .extensions(Map.of("code", ex.getCode(), "field", ex.getField())).build();
    }

    // Handles the unexpected exceptions from resolvers
    @GraphQlExceptionHandler(Exception.class)
    public GraphQLError handleUnexpected(Exception ex) {
        return GraphqlErrorException.newErrorException().message("An unexpected error occurred")
                .errorClassification(ErrorType.INTERNAL_ERROR).build();
    }
}
