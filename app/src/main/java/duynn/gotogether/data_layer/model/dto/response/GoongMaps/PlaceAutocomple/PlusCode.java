// PlusCode.java

package duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceAutocomple;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlusCode implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("compound_code")
    private String compoundCode;
    @SerializedName("global_code")
    private String globalCode;

    public String getCompoundCode() { return compoundCode; }
    public void setCompoundCode(String value) { this.compoundCode = value; }

    public String getGlobalCode() { return globalCode; }
    public void setGlobalCode(String value) { this.globalCode = value; }

    @Override
    public String toString() {
        return "PlusCode{" +
                "compoundCode='" + compoundCode + '\'' +
                ", globalCode='" + globalCode + '\'' +
                '}';
    }
}