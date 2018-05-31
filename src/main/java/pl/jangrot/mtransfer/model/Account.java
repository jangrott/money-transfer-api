package pl.jangrot.mtransfer.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "account")
@Data
public class Account {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq-gen")
    @SequenceGenerator(name = "seq-gen",
            sequenceName = "user_sequence", initialValue = 236476251)
    private Long id;

    @Column
    private BigDecimal balance = BigDecimal.ZERO;
}
