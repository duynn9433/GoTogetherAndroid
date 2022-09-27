package duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceAutocomple;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GoongPlaceAutocompleteResult implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("predictions")
    private List<Prediction> predictions;
    @SerializedName("executed_time")
    private long executedTime;
    @SerializedName("executed_time_all")
    private long executedTimeAll;
    @SerializedName("status")
    private String status;


    public List<Prediction> getPredictions() { return predictions; }
    public void setPredictions(List<Prediction> value) { this.predictions = value; }

    public long getExecutedTime() { return executedTime; }
    public void setExecutedTime(long value) { this.executedTime = value; }

    public long getExecutedTimeAll() { return executedTimeAll; }
    public void setExecutedTimeAll(long value) { this.executedTimeAll = value; }

    public String getStatus() { return status; }
    public void setStatus(String value) { this.status = value; }

    @Override
    public String toString() {
        return "GoongPlaceAutocompleteResult{" +
                "predictions=" + predictions +
                ", executedTime=" + executedTime +
                ", executedTimeAll=" + executedTimeAll +
                ", status='" + status + '\'' +
                '}';
    }
}