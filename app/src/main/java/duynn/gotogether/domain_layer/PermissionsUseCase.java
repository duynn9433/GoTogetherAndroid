package duynn.gotogether.domain_layer;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;

import org.jetbrains.annotations.NotNull;

import duynn.gotogether.domain_layer.common.Constants;
import pub.devrel.easypermissions.EasyPermissions;

public class PermissionsUseCase {
    public static boolean hasLocationPermission(@NotNull Context context){
        return EasyPermissions.hasPermissions(context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION);
    }
    public static void requestLocationPermission(@NotNull Context context){
        if(!hasLocationPermission(context)){
            EasyPermissions.requestPermissions(
                    (Activity) context,
                    "Ứng dụng cần quyền truy cập vị trí để hoạt động.",
                    Constants.REQUEST_LOCATION_PERMISSIONS_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }

    public static boolean hasBackgroundLocationPermission(@NotNull Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            return EasyPermissions.hasPermissions(context,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        }
        return true;
    }
    public static void requestBackgroundLocationPermission(@NotNull Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            EasyPermissions.requestPermissions(
                    (Activity) context,
                    "Ứng dụng cần quyền truy cập vị trí dưới nền để hoạt động.",
                    Constants.REQUEST_BACKGROUND_LOCATION_PERMISSIONS_CODE,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        }
    }
}
