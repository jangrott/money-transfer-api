package pl.jangrot.mtransfer.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "client")
@ToString
@EqualsAndHashCode
public class Client {

    @Id
    @Column
    @GeneratedValue
    @Setter
    @Getter
    private UUID id;

    @Column
    @Setter
    @Getter
    private String firstName;

    @Column
    @Setter
    @Getter
    private String lastName;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<Account> accounts = new ArrayList<>();

    public void addAccount(Account account) {
        account.set_client(this);
        accounts.add(account);
    }

}
