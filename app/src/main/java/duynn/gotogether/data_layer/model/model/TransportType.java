package duynn.gotogether.data_layer.model.model;

public enum TransportType {
    CAR ("car"),
    BIKE ("bike"),
    TAXI ("taxi"),
    TRUCK ("truck"),
    BUS ("bus"),

    WALKING ("walking"),
    OTHER ("other"),
    MOTORCYCLE ("motorcycle");

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
