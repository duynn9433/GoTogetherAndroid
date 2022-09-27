package duynn.gotogether.data_layer.model.model;

public enum TransportType {
    CAR ("Car"),
    BUS ("Bus"),
    BIKE ("Bike"),
    WALKING ("Walking"),
    OTHER ("Other"),
    MOTORCYCLE ("Motorcycle");

    private String code;

    private TransportType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "TransportType{" +
                "code='" + code + '\'' +
                '}';
    }
}
