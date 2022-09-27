package duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceAutocomple;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Prediction implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("description")
    private String description;
    @SerializedName("matched_substrings")
    private List<Object> matchedSubstrings;
    @SerializedName("place_id")
    private String placeID;
    @SerializedName("reference")
    private String reference;
    @SerializedName("structured_formatting")
    private StructuredFormatting structuredFormatting;
    @SerializedName("terms")
    private List<Object> terms;
    @SerializedName("has_children")
    private boolean hasChildren;
    @SerializedName("display_type")
    private String displayType;
    @SerializedName("score")
    private double score;
    @SerializedName("plus_code")
    private PlusCode plusCode;


    public String getDescription() { return description; }
    public void setDescription(String value) { this.description = value; }


    public List<Object> getMatchedSubstrings() { return matchedSubstrings; }
    public void setMatchedSubstrings(List<Object> value) { this.matchedSubstrings = value; }


    public String getPlaceID() { return placeID; }
    public void setPlaceID(String value) { this.placeID = value; }


    public String getReference() { return reference; }
    public void setReference(String value) { this.reference = value; }


    public StructuredFormatting getStructuredFormatting() { return structuredFormatting; }
    public void setStructuredFormatting(StructuredFormatting value) { this.structuredFormatting = value; }


    public List<Object> getTerms() { return terms; }
    public void setTerms(List<Object> value) { this.terms = value; }


    public boolean getHasChildren() { return hasChildren; }
    public void setHasChildren(boolean value) { this.hasChildren = value; }


    public String getDisplayType() { return displayType; }
    public void setDisplayType(String value) { this.displayType = value; }


    public double getScore() { return score; }
    public void setScore(double value) { this.score = value; }


    public PlusCode getPlusCode() { return plusCode; }
    public void setPlusCode(PlusCode value) { this.plusCode = value; }

    @Override
    public String toString() {
        return "Prediction{" +
                "description='" + description + '\'' +
                ", matchedSubstrings=" + matchedSubstrings +
                ", placeID='" + placeID + '\'' +
                ", reference='" + reference + '\'' +
                ", structuredFormatting=" + structuredFormatting +
                ", terms=" + terms +
                ", hasChildren=" + hasChildren +
                ", displayType='" + displayType + '\'' +
                ", score=" + score +
                ", plusCode=" + plusCode +
                '}';
    }
}