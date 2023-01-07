package duynn.gotogether.domain_layer;

import android.util.Log;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class CalendarDeserializer implements JsonDeserializer<Calendar> {
    @Override
    public Calendar deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String dateAsString = json.getAsString();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
//            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(formatter.parse(dateAsString));
//            Log.d("CalendarDeserializer", calendar.getTime().toString());
            return calendar;
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }
}