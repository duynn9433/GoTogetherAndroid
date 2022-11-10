package duynn.gotogether.ui_layer.activity;

import static duynn.gotogether.domain_layer.PermissionsUseCase.requestLocationPermission;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import duynn.gotogether.databinding.ActivityPermissionBinding;
import duynn.gotogether.domain_layer.PermissionsUseCase;
import duynn.gotogether.ui_layer.activity.execute_route.TrackingMapsActivity;
import duynn.gotogether.ui_layer.activity.home.HomeActivity;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class PermissionActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private final String TAG = PermissionActivity.class.getSimpleName() ;
    private ActivityPermissionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_permission);

        binding = ActivityPermissionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(PermissionsUseCase.hasLocationPermission(this)){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        binding.btnAgreePermission.setOnClickListener(v -> {
            if(PermissionsUseCase.hasLocationPermission(this)){
                //TODO: custom tracking && get direction
//                Intent intent = new Intent(this, TrackingMapsActivity.class);
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            }else{
                PermissionsUseCase.requestLocationPermission(this);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //TODO: custom tracking && get direction
//        Intent intent = new Intent(this, TrackingMapsActivity.class);
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }else{
            requestLocationPermission(this);
        }
    }
}