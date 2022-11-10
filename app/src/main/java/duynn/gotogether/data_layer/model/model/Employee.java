package duynn.gotogether.data_layer.model.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    private String email;
}
