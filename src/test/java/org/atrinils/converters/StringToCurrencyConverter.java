package org.atrinils.converters;

import com.creditdatamw.zerocell.converter.Converter;
import org.atrinils.enums.Currency;

import java.lang.reflect.Method;
import java.util.Map;

public class StringToCurrencyConverter implements Converter<Currency> {
    /*@Override
    public Currency convert(Method method, String currencyValue) {
        Map<String, Currency> stringCurrencyMap =
                Map.of("INR", Currency.INR,
                        "DIR", Currency.DIR,
                        "YEN", Currency.YEN,
                        "USD", Currency.USD,
                        "GBP", Currency.GBP
                );
        return stringCurrencyMap.getOrDefault(currencyValue.toUpperCase(), Currency.INR);
    }*/

    @Override
    public Currency convert(String currencyValue, String s1, int i) {
        Map<String, Currency> stringCurrencyMap =
                Map.of("INR", Currency.INR,
                        "DIR", Currency.DIR,
                        "YEN", Currency.YEN,
                        "USD", Currency.USD,
                        "GBP", Currency.GBP
                );
        return stringCurrencyMap.getOrDefault(currencyValue.toUpperCase(), Currency.INR);
    }
}
