package org.pms.sammenu.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FlexibleDoubleSerializer extends JsonSerializer<Double> {

    @Override
    public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        if (value.equals(Double.MAX_VALUE)) {
            gen.writeString("INF");
        } else {
            boolean negativeFlag = value < 0d;
            value = Math.abs(value);
            String integer = String.valueOf(value.intValue());
            String decimal = "0";
            DecimalFormat df;
            df = new DecimalFormat("#.##");
            df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
            try {
                decimal = (df.format
                        (value - value.intValue())
                ).replace("0.", "");
            } catch (Exception e) {
                decimal = "0";
            } finally {
                Integer integerPartLength = integer.length();
                String integerString = integer.substring(0, integer.length() % 3);
                for (Integer i = (integerPartLength % 3); i < integer.length() - 1; i = i + 3) {
                    integerString = integerString.concat(".").concat(integer.substring(i, i + 3));
                }
                if (integerString.startsWith(".")) {
                    integerString = integerString.substring(1);
                }

                integerString = integerString.concat(",").concat(decimal);
                if (negativeFlag) {
                    integerString = "-".concat(integerString);
                }

                gen.writeString(integerString);
            }
        }
    }
}
