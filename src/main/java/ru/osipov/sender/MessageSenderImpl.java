package ru.osipov.sender;

import java.util.Map;

import ru.osipov.entity.Country;
import ru.osipov.entity.Location;
import ru.osipov.geo.GeoService;
import ru.osipov.i18n.LocalizationService;

public class MessageSenderImpl implements MessageSender {

    public static final String IP_ADDRESS_HEADER = "x-real-ip";
    private final GeoService geoService; //определяет местоположение (byIp). Результат: Location
    private final LocalizationService localizationService; //locale - Возвращает строку приветствия (String) в зависимости от страны

    public MessageSenderImpl(GeoService geoService, LocalizationService localizationService) {
        this.geoService = geoService;
        this.localizationService = localizationService;
    }

    public String send(Map<String, String> headers) {
        String ipAddress = String.valueOf(headers.get(IP_ADDRESS_HEADER));
        if (ipAddress != null && !ipAddress.isEmpty()) {
            Location location = geoService.byIp(ipAddress);
            System.out.printf("Отправлено сообщение: %s", localizationService.locale(location.getCountry()));
            return localizationService.locale(location.getCountry());
        }
        return localizationService.locale(Country.USA);
    }
}
