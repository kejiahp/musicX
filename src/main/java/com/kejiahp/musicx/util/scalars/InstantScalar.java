package com.kejiahp.musicx.util.scalars;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public class InstantScalar {

    public static final GraphQLScalarType GRAPHQL_INSTANT = GraphQLScalarType.newScalar()
            .name("Instant")
            .description("A custom scalar that handles java.time.Instant in ISO-8601 format")
            .coercing(new Coercing<Instant, String>() {

                @Override
                public String serialize(Object dataFetcherResult) {
                    if (dataFetcherResult instanceof Instant) {
                        return ((Instant) dataFetcherResult).toString(); // ISO-8601
                    }
                    throw new CoercingSerializeException("Expected an Instant object.");
                }

                @Override
                public Instant parseValue(Object input) {
                    try {
                        if (input instanceof String) {
                            return Instant.parse((String) input);
                        }
                        throw new CoercingParseValueException("Expected a String for Instant.");
                    } catch (DateTimeParseException e) {
                        throw new CoercingParseValueException("Invalid ISO-8601 date format: " + input);
                    }
                }

                @Override
                public Instant parseLiteral(Object input) {
                    if (input instanceof StringValue) {
                        try {
                            return Instant.parse(((StringValue) input).getValue());
                        } catch (DateTimeParseException e) {
                            throw new CoercingParseLiteralException("Invalid ISO-8601 date format: " + input);
                        }
                    }
                    throw new CoercingParseLiteralException("Expected a StringValue for Instant.");
                }
            })
            .build();
}
