package duynn.gotogether.data_layer.model.model;


import java.io.Serializable;
import java.util.List;



public class Client extends User implements Serializable {
    private static final long serialVersionUID = 5L;

    private Long id;

    private Position position;

    private Double rate;

    private boolean isInTrip;

    private List<Transport> transports;
}
