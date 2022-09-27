package duynn.gotogether.data_layer.model.model;

import java.io.Serializable;

public class Comment implements Serializable {
    private static final long serialVersionUID = 11L;

    private Long id;

    private String content;

    private Integer rating;

    private Client Author;

    private Client Receiver;

    private Trip trip;
}
