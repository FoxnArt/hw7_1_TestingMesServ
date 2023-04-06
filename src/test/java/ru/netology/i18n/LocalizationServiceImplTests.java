package ru.netology.i18n;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;

import java.util.stream.Stream;

import static ru.netology.entity.Country.*;

public class LocalizationServiceImplTests {
    LocalizationServiceImpl sut;

    @BeforeEach
    public void beforeEach() {
        sut = new LocalizationServiceImpl();
    }

    @AfterEach
    public void afterEach() {
        sut = null;
    }

    @ParameterizedTest
    @MethodSource("countrySource")
    public void testByIp(String expected, Country country) {
        // when:
        var result = sut.locale(country);

        // then:
        Assertions.assertEquals(expected, result);
    }

    public static Stream<Arguments> countrySource() {
        // given
        return Stream.of(
                Arguments.of("Добро пожаловать", RUSSIA),
                Arguments.of("Welcome", GERMANY),
                Arguments.of("Welcome", USA),
                Arguments.of("Welcome", BRAZIL)
        );
    }
}
