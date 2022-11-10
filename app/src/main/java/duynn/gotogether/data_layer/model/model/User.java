package duynn.gotogether.data_layer.model.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 4L;

    private Long id;

    private String role;

    private String avatar;

    private String description;

    private Account account;

    private Fullname fullname;

    @SerializedName("contact_information")
    private ContactInfomation contactInfomation;

    private Address address;

    private boolean isActive;

    public User() {
        role = "Client";
        isActive = true;
        account = new Account();
        fullname = new Fullname();
        contactInfomation = new ContactInfomation();
        address = new Address();
    }
    public User(Client client) {
        this.id = client.getId();
        this.role = client.getRole();
        this.avatar = client.getAvatar();
        this.description = client.getDescription();
        this.account = client.getAccount();
        this.fullname = client.getFullname();
        this.contactInfomation = client.getContactInfomation();
        this.address = client.getAddress();
        this.isActive = client.isActive();
    }


}
