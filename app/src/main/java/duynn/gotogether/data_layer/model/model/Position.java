package duynn.gotogether.data_layer.model.model;


import java.io.Serializable;


public class Position implements Serializable {

    private static final long serialVersionUID = 6L;

    private Long id;

    private Double latitude;

    private Double longitude;

    private String primaryAddress;
    private String fullAddress;
    private String formattedAddress;

    public Position() {
    }

    public Position(Double latitude, Double longitude, String primaryAddress, String fullAddress, String formattedAddress) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.primaryAddress = primaryAddress;
        this.fullAddress = fullAddress;
        this.formattedAddress = formattedAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(String primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }
}