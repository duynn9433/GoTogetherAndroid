package duynn.gotogether.data_layer.model.model;

import java.io.Serializable;
import java.util.*;



public class ClientTrip implements Serializable {
    private static final long serialVersionUID = 10L;

    private Long id;

    private Position pickUpPosition;

    private Position dropOffPosition;

    private Date pickUpTime;

    private Integer numOfPeople;

    private Double pricePerKmForOnePeople;

    private boolean isAccepted;

    private boolean isCanceled;

    private Client client;

    private Trip trip;

}
