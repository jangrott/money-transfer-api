package pl.jangrot.mtransfer.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "client")
@Data
public class Client {

    @Id
    @Column
    @GeneratedValue
    private UUID id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @OneToMany(mappedBy = "client")
    private Set<Account> accounts = new HashSet<>();
}
