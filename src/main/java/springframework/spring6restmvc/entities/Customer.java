package springframework.spring6restmvc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;
    private String customerName;

    @Column()
    private String email;

    @Version
    private Integer version;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    @Builder.Default //builds an empty HashSet if it's not set already
    @OneToMany(mappedBy = "customer")
    private Set<BeerOrder> beerOrders = new HashSet<>(); //to make sure its initialized

}
