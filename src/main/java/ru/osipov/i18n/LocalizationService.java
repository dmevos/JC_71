package ru.osipov.i18n;

import ru.osipov.entity.Country;

public interface LocalizationService {

    String locale(Country country);
}
