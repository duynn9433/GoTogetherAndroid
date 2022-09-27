package duynn.gotogether.domain_layer;

public class TimeUseCase {
    public static long calculateTime(long startTime, long endTime) {
        return endTime - startTime;
    }
    public static String stringElapsedTime(long startTime, long endTime) {
        long elapsedTime = calculateTime(startTime, endTime);
        long seconds = (elapsedTime / 1000) % 60;
        long minutes = (elapsedTime / (1000 * 60)) % 60;
        long hours = (elapsedTime / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
