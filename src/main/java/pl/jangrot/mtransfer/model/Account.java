package pl.jangrot.mtransfer.model;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "account")
@Data
public class Account {

    @Id
    @Column
    private Long number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Client client;
}
