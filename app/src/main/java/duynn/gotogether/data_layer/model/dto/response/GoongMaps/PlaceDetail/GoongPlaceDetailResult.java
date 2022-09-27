package duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GoongPlaceDetailResult implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("result")
    private Place result;
    @SerializedName("status")
    private String status;

    public Place getResult() { return result; }
    public void setResult(Place value) { this.result = value; }

    public String getStatus() { return status; }
    public void setStatus(String value) { this.status = value; }

    @Override
    public String toString() {
        return "GoongPlaceDetailResult{" +
                "result=" + result +
                ", status='" + status + '\'' +
                '}';
    }
}