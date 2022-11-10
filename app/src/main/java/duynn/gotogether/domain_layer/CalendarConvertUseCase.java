package duynn.gotogether.domain_layer;

import java.text.SimpleDateFormat;

public class CalendarConvertUseCase {
    public static String fromCalendarToString(java.util.Calendar calendar) {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm")
                .format(calendar.getTime());
    }
}
