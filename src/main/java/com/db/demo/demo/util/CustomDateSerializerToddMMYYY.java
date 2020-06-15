package com.db.demo.demo.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.SneakyThrows;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.db.demo.demo.util.TradeConstants.DATE_FORMAT;

/**
 * @author Savitha
 */
public class CustomDateSerializerToddMMYYY extends StdSerializer<Date> {

    private SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

    public CustomDateSerializerToddMMYYY() {
        this(null);
    }

    public CustomDateSerializerToddMMYYY(Class t) {
        super(t);
    }

    @SneakyThrows
    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider arg2)
            throws IOException {
        gen.writeString(String.valueOf(formatter.parse(formatter.format(value))));
    }
}