package duynn.gotogether.data_layer.model.model;
import java.io.Serializable;


public class Address implements Serializable {
    private static final long serialVersionUID = 3L;

    private Long id;

    private String city;

    private String district;

    private String province;

    private String detail;
}
