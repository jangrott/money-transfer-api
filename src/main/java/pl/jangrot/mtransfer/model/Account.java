package pl.jangrot.mtransfer.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "account")
@Getter
@Setter
@ToString(exclude = "_client")
@EqualsAndHashCode(exclude = "_client")
public class Account {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq-gen")
    @SequenceGenerator(name = "seq-gen",
            sequenceName = "user_sequence", initialValue = 236476251)
    private Long id;

    @Column
    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "fk_client", updatable = false, nullable = false)
    private Client _client;
}
