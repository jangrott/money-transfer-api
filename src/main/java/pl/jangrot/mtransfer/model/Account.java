package pl.jangrot.mtransfer.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.jangrot.mtransfer.exception.InsufficientFundsException;

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

    public void withdraw(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException(String.format("Insufficient funds to perform withdraw from account: %d", id));
        }
        balance = balance.subtract(amount);
    }

    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }
}
