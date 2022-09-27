package duynn.gotogether.domain_layer;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class ToastUseCase {
    public static void showShortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static void showLongToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
