package org.example.config;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.stereotype.Controller;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.scalar.ExtendedScalars.DATETIME;

@Configuration
public class GraphQLConfiguration {

    @Bean
    public DataFetcherExceptionResolver exceptionResolver() {
        return new DataFetcherExceptionResolverAdapter() {
            @Override
            protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
                if (ex instanceof ConstraintViolationException) {
                    return GraphqlErrorBuilder.newError()
                            .errorType(graphql.ErrorType.ValidationError)
                            .extensions(getExtensions(((ConstraintViolationException) ex).getConstraintViolations()))
                            .message("Bean validation error")
                            .build();
                } else {
                    return super.resolveToSingleError(ex, env);
                }
            }
        };
    }

    private Map<String, Object> getExtensions(Set<ConstraintViolation<?>> constraintViolations) {
        return Map.of("constraintViolations", constraintViolations.stream()
                .map(this::composeErrorExtension)
                .collect(Collectors.toList()));
    }

    private Map<String, Object> composeErrorExtension(ConstraintViolation<?> constraintViolation) {
        Map<String, Object> errorMap = new HashMap<>();

        errorMap.put("messageTemplate", constraintViolation.getMessageTemplate());
        errorMap.put("message", constraintViolation.getMessage());
        errorMap.put("path", composePath(constraintViolation));
        errorMap.put("invalidValue", constraintViolation.getInvalidValue().toString());

        return errorMap;
    }

    private String composePath(ConstraintViolation<?> constraintViolation) {
        Controller annotation = AnnotationUtils.findAnnotation(constraintViolation.getRootBeanClass(), Controller.class);
        if (annotation != null) {
            String propertyPath = constraintViolation.getPropertyPath().toString();
            return propertyPath.substring(propertyPath.indexOf(".") + 1);
        } else {
            return constraintViolation.getPropertyPath().toString();
        }
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return builder -> builder.scalar(DATETIME);
    }
}
