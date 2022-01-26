package org.example.scalar;

import graphql.schema.GraphQLScalarType;

public interface ExtendedScalars {
    GraphQLScalarType DATETIME = GraphQLScalarType.newScalar()
            .name("DateTime")
            .coercing(new InstantCoercing())
            .build();
}
