package duynn.gotogether.data_layer.model.model;


import java.io.Serializable;



public class Transport implements Serializable {
    private static final long serialVersionUID = 7L;

    private Long id;

    private String name;

    private String licensePlate;

    private String description;

    private String image;

//    private TransportType transportType;

    private Client owner;

}
