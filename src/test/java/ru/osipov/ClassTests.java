package ru.osipov;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import ru.osipov.entity.Country;
import ru.osipov.entity.Location;
import ru.osipov.geo.GeoService;
import ru.osipov.geo.GeoServiceImpl;
import ru.osipov.i18n.LocalizationService;
import ru.osipov.i18n.LocalizationServiceImpl;
import ru.osipov.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.JMock1Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ClassTests {

    @BeforeAll
    static void startTests() {
        System.out.println("Начинаем гонять тесты:");
    }

    @AfterAll
    static void stopTests() {
        System.out.println("Закончили гонять тесты!");
    }

    @BeforeEach
    public void startTest() {
        System.out.println("Гоним очередной тест:");
    }

    @AfterEach
    public void stopTest() {
        System.out.println("Прогнали очередной тест!");
    }


    @ParameterizedTest
    @MethodSource("sourceForLocationByIp")
    public void LocationByIpTest(String ip, Location expected) {
        assertThat(expected, samePropertyValuesAs(new GeoServiceImpl().byIp(ip)));
    }

    private static Stream<Arguments> sourceForLocationByIp() {
        return Stream.of(
                Arguments.of("172.0.32.11", new Location("Moscow", Country.RUSSIA, "Lenina", 15)),
                Arguments.of("96.44.183.149", new Location("New York", Country.USA, " 10th Avenue", 32)),
                Arguments.of("127.0.0.1", new Location(null, null, null, 0)),
                Arguments.of("172.", new Location("Moscow", Country.RUSSIA, null, 0)),
                Arguments.of("96.", new Location("New York", Country.USA, null, 0))
        );
    }

    @Test
    public void LocationByCoordinatesTest() {
        assertThrows(RuntimeException.class, () ->
        {
            throw new RuntimeException("Not implemented");
        });
    }

    @ParameterizedTest
    @MethodSource("sourceForLocale")
    public void localeTest(Country country, String expected) {
        assertThat(expected, Matchers.equalTo(new LocalizationServiceImpl().locale(country)));
    }

    private static Stream<Arguments> sourceForLocale() {
        return Stream.of(
                Arguments.of(Country.RUSSIA, "Добро пожаловать"),
                Arguments.of(Country.USA, "Welcome")
        );
    }

    @ParameterizedTest
    @MethodSource("sourceForSend")
    public void MessageSenderImplTest(String ip, Location location, String str){
        GeoServiceImpl geoServiceM = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoServiceM.byIp(ip)).thenReturn(location);

        LocalizationServiceImpl localizationServiceM = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationServiceM.locale(location.getCountry()))
                .thenReturn(str);

        MessageSenderImpl messageSenderM = new MessageSenderImpl(geoServiceM, localizationServiceM);
        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);
        String result = messageSenderM.send(headers);

        GeoServiceImpl geoService  = new GeoServiceImpl();
        LocalizationServiceImpl localizationService = new LocalizationServiceImpl();
        MessageSenderImpl messageSender = new MessageSenderImpl(geoService, localizationService);
        String exp = messageSender.send(headers);

        assertEquals(exp,result);

    }
    private static Stream<Arguments> sourceForSend() {
        return Stream.of(
                Arguments.of("172.123.12.19", new Location("Moscow", Country.RUSSIA, "Lenina", 15), "Добро пожаловать"),
                Arguments.of("96.44.183.149", new Location("Moscow", Country.RUSSIA, "Lenina", 15), "Welcome"),
                Arguments.of("172.0.32.11", new Location("New York", Country.USA, " 10th Avenue", 32), "Добро пожаловать")



        );
    }

}
