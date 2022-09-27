package duynn.gotogether.data_layer.model.model;

import java.io.Serializable;


public class User implements Serializable {
    private static final long serialVersionUID = 4L;

    private Long id;

    private String role;

    private String avatar;

    private String description;

    private Account account;

    private Fullname fullname;

    private ContactInfomation contactInfomation;

    private Address address;

    private boolean isActive;

    public String getFullname() {
//        return fullname.getFirstName() +" "+ fullname.getMiddleName() +" " + fullname.getLastName();
        return "";
    }

}
