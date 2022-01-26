package org.example.contoller;

import org.example.dto.TestInput;
import org.example.dto.TestOutput;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Controller
public class TestController {

    /**
     * Initially contains 3 predefined values to test query methods:
     * 1) with offsetDateTime = "2021-10-03T00:31:33.130+03:00" (other null)
     * 2) with zonedDateTime = "2021-06-09T06:44:53.130+03:00[Europe/Moscow]" (other null)
     * 3) with localDateTime = "2021-02-13T12:58:13.130" (other null)
     */
    private static final List<TestOutput> STORAGE = new LinkedList<>();

    @PostConstruct
    public void init() {
        initData();
    }

    @QueryMapping(name = "byZonedDateTime")
    public TestOutput findByZonedDateTime(@Argument ZonedDateTime zonedDateTime) {
        return STORAGE.stream()
                .filter(o -> zonedDateTime.equals(o.getZonedDateTime()))
                .findAny()
                .orElse(null);
    }

    @QueryMapping(name = "byOffsetDateTime")
    public TestOutput findByOffsetDateTime(@Argument OffsetDateTime offsetDateTime) {
        return STORAGE.stream()
                .filter(o -> offsetDateTime.equals(o.getOffsetDateTime()))
                .findAny()
                .orElse(null);
    }

    @QueryMapping(name = "byLocalDateTime")
    public TestOutput findByLocalDateTime(@Argument LocalDateTime localDateTime) {
        return STORAGE.stream()
                .filter(o -> localDateTime.equals(o.getLocalDateTime()))
                .findAny()
                .orElse(null);
    }

    @MutationMapping(name = "upsertTest")
    public TestOutput upsert(@Argument TestInput input) {
        TestOutput testOutput = new TestOutput();
        testOutput.setOffsetDateTime(input.getOffsetDateTime());
        testOutput.setLocalDateTime(input.getLocalDateTime());
        testOutput.setZonedDateTime(input.getZonedDateTime());
        STORAGE.add(testOutput);
        return testOutput;
    }

    private static void initData() {
        TestOutput to1 = new TestOutput();
        ZoneId zoneId = ZoneId.of("Europe/Moscow");
        to1.setOffsetDateTime(OffsetDateTime.ofInstant(Instant.ofEpochMilli(1633210293130L), zoneId));
        TestOutput to2 = new TestOutput();
        to2.setZonedDateTime(ZonedDateTime.ofInstant(Instant.ofEpochMilli(1623210293130L), zoneId));
        TestOutput to3 = new TestOutput();
        to3.setLocalDateTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(1613210293130L), zoneId));
        Collections.addAll(STORAGE, to1, to2, to3);
    }
}
