package org.example.scalar;

import graphql.GraphqlErrorException;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.function.Function;

public class InstantCoercing implements Coercing<Instant, String> {

    private static final Logger log = LoggerFactory.getLogger(InstantCoercing.class);

    private static final String PARSING_EXCEPTION_MESSAGE = "Invalid RFC3339 value : '%s'. " +
            "Unable to convert provided input into 'java.time.Instant'";

    private static final Map<Class<?>, Function<Object, String>> SERIALIZERS = Map.of(
            OffsetDateTime.class, (Object input) ->
                    ((OffsetDateTime) input).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            ZonedDateTime.class, (Object input) ->
                    ((ZonedDateTime) input).format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
            LocalDateTime.class, (Object input) ->
                    ((LocalDateTime) input).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    );

    @Override
    public String serialize(Object input) {
        Function<Object, String> serializer = SERIALIZERS.get(input.getClass());
        if (serializer == null) {
            throw new CoercingSerializeException("Unable to convert '" + typeName(input) + "' into String");
        }
        return serializer.apply(input);
    }

    @NonNull
    @Override
    public Instant parseValue(Object input) {
        return parseStringDate(input.toString(), CoercingParseValueException::new);
    }

    @NonNull
    @Override
    public Instant parseLiteral(@NonNull Object input) {
        if (input instanceof StringValue) {
            String value = ((StringValue) input).getValue();
            return parseStringDate(value, CoercingParseLiteralException::new);
        }
        throw new CoercingParseLiteralException("Expected AST type 'StringValue' but was '" + typeName(input) + "'.");
    }

    private static Instant parseStringDate(String s, Function<String, GraphqlErrorException> exceptionMaker) {
        try {
            return OffsetDateTime.parse(s, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant();
        } catch (DateTimeParseException ex) {
            log.debug("'java.time.OffsetDateTime' type mismatch for input string '{}'", s);
        }
        try {
            return ZonedDateTime.parse(s, DateTimeFormatter.ISO_ZONED_DATE_TIME).toInstant();
        } catch (DateTimeParseException ex) {
            log.debug("'java.time.ZonedDateTime' type mismatch for input string '{}'", s);
        }
        try {
            return LocalDateTime.parse(s, DateTimeFormatter.ISO_LOCAL_DATE_TIME).atZone(ZoneId.systemDefault())
                    .toInstant();
        } catch (DateTimeParseException ex) {
            log.debug("'java.time.LocalDateTime' type mismatch for input string '{}'", s);
        }
        throw exceptionMaker.apply(String.format(PARSING_EXCEPTION_MESSAGE, s));
    }

    private static String typeName(Object input) {
        if (input == null) {
            return "null";
        }
        return input.getClass().getSimpleName();
    }
}
