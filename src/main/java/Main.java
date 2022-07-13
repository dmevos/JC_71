import java.util.HashMap;
import java.util.Map;

import ru.osipov.geo.GeoService;
import ru.osipov.geo.GeoServiceImpl;
import ru.osipov.i18n.LocalizationService;
import ru.osipov.i18n.LocalizationServiceImpl;
import ru.osipov.sender.MessageSender;
import ru.osipov.sender.MessageSenderImpl;

public class Main {

    //Тестовый пример
    public static void main(String[] args) {
        GeoService geoService = new GeoServiceImpl();
        LocalizationService localizationService = new LocalizationServiceImpl();
        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.123.12.19");
        messageSender.send(headers);
    }
}