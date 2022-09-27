// StructuredFormatting.java

package duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceAutocomple;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StructuredFormatting implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("main_text")
    private String mainText;
    @SerializedName("secondary_text")
    private String secondaryText;

    public String getMainText() { return mainText; }
    public void setMainText(String value) { this.mainText = value; }

    public String getSecondaryText() { return secondaryText; }
    public void setSecondaryText(String value) { this.secondaryText = value; }

    @Override
    public String toString() {
        return "StructuredFormatting{" +
                "mainText='" + mainText + '\'' +
                ", secondaryText='" + secondaryText + '\'' +
                '}';
    }
}
