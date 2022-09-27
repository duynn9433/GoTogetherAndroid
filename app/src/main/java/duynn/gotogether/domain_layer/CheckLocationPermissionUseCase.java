package duynn.gotogether.domain_layer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import duynn.gotogether.domain_layer.common.Constants;

public class CheckLocationPermissionUseCase {
    public static boolean checkLocationPermission(@NonNull Context context) {
        return (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }
    public static void requestLocationPermission(@NonNull Context context) {
        if(!checkLocationPermission(context)){
            ActivityCompat.requestPermissions(
                    (Activity) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.REQUEST_LOCATION_PERMISSIONS_CODE);
        }
    }
}
