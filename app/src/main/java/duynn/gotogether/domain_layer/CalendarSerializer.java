package duynn.gotogether.domain_layer;

import android.util.Log;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class CalendarSerializer implements JsonSerializer<Calendar> {
    @Override
    public JsonElement serialize(Calendar src, Type typeOfSrc, JsonSerializationContext context) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatter.setTimeZone(TimeZone.getDefault());
        String dateAsString = formatter.format(src.getTime());
        Log.d("CalendarSerializer", dateAsString);
        return new JsonPrimitive(dateAsString);
    }
}