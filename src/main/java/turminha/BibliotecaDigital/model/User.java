package turminha.BibliotecaDigital.model;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@DiscriminatorValue("USER")
public class User extends Person {

    @Column(nullable = false)
    private String email;

    private String telephoneNumber;

    public String getInfo() {
        return "Id: " + super.getId() + " / " + "Name: " + super.getName() + " / "
                + "E-mail: " + this.email + " / " + "Telephone number: "
                + this.telephoneNumber;
    }

    public User(){

    }

    public User(Long id, String name, String email, String telephoneNumber) {
        super(id, name);
        this.email = email;
        this.telephoneNumber = telephoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
}


