package pl.jangrot.mtransfer.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.jangrot.mtransfer.exception.InsufficientFundsException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Entity(name = "account")
@Getter
@Setter
@ToString(exclude = {"_client", "_lock"})
@EqualsAndHashCode(exclude = {"_client", "_lock"})
public class Account {

    @Transient
    public Lock _lock = new ReentrantLock();

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
