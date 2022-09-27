package duynn.gotogether.domain_layer;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import duynn.gotogether.domain_layer.common.Constants;

public class MoveCameraUseCase {
    public static CameraPosition setCameraPosition(LatLng location) {
        return new CameraPosition.Builder()
                .target(location)
                .zoom(Constants.DEFAULT_ZOOM_FOR_TRACKER)
                .build();
    }
}
