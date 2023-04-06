package ru.netology.sender;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static ru.netology.entity.Country.*;
import static ru.netology.entity.Country.BRAZIL;

public class MessageSenderImplTests {

    @ParameterizedTest
    @MethodSource("ipAddressSource")
    public void test_checks_language_sent_message(String testMessage, String testIpAddress, Location testLocation) {
        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp(testIpAddress))
                .thenReturn(testLocation);

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(testLocation.getCountry()))
                .thenReturn(testMessage);

        MessageSenderImpl messageSenderImpl = new MessageSenderImpl(geoService, localizationService);
        Map<String, String> ipAddress = new HashMap<String, String>();
        ipAddress.put(MessageSenderImpl.IP_ADDRESS_HEADER, testIpAddress);
        String message = messageSenderImpl.send(ipAddress);
        String expected = testMessage;

        Assertions.assertEquals(expected, message);
    }

    public static Stream<Arguments> ipAddressSource() {
        // given
        return Stream.of(
                Arguments.of("Добро пожаловать", "172.0.32.11", new Location("Moscow", Country.RUSSIA, "Lenina", 15)),
                Arguments.of("Welcome", "96.44.183.149", new Location("New York", Country.USA, " 10th Avenue", 32)),
                Arguments.of("Добро пожаловать", "172.", new Location("Moscow", Country.RUSSIA, null, 0)),
                Arguments.of("Welcome", "96.", new Location("New York", Country.USA, null,  0))
        );
    }

    @Test
    public void test_send_message_when_ip_null() {
        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp(null))
                .thenReturn(null);

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Country.USA))
                .thenReturn("Welcome");

        MessageSenderImpl messageSenderImpl = new MessageSenderImpl(geoService, localizationService);
        Map<String, String> ipAddress = new HashMap<String, String>();
        ipAddress.put(MessageSenderImpl.IP_ADDRESS_HEADER, null);
        String message = messageSenderImpl.send(ipAddress);
        String expected = "Welcome";

        Assertions.assertEquals(expected, message);
    }
}
