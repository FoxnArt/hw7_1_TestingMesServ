package ru.netology.geo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;

import java.util.Locale;
import java.util.stream.Stream;

public class GeoServiceImplTests {

    GeoServiceImpl sut;

    @BeforeEach
    public void beforeEach() {
        sut = new GeoServiceImpl();
    }

    @AfterEach
    public void afterEach() {
        sut = null;
    }

    @ParameterizedTest
    @MethodSource("byIpSource")
    public void testByIp(String ip, String expected) {
        // when:
        var result = sut.byIp(ip);

        // then:
        Assertions.assertEquals(expected, result.getCity());
    }

    public static Stream<Arguments> byIpSource() {
        // given
        return Stream.of(
                Arguments.of("127.0.0.1", null),
                Arguments.of("172.0.32.11", "Moscow"),
                Arguments.of("96.44.183.149", "New York"),
                Arguments.of("172.", "Moscow"),
                Arguments.of("96.", "New York"),
                Arguments.of("125", null)
        );
    }

    @Test
    public void testByCoordinates() {
        // given:
        double a = 57.64911, b = 10.40744;
        Class<RuntimeException> expected = RuntimeException.class;

        // when:
        Executable executable = () -> sut.byCoordinates(a, b);

        // then:
        Assertions.assertThrowsExactly(expected, executable);
    }
}
