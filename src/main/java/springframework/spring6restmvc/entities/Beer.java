package springframework.spring6restmvc.entities;

import jakarta.persistence.*;
import lombok.*;
import springframework.spring6restmvc.model.BeerStyle;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Beer {
    @Id//mandatory
    @GeneratedValue(generator = "UUID")
    //@GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")//this is out of JPA, it's done by Hibernate
    @Column(length = 99,columnDefinition = "varchar", updatable = false,nullable = false)//some hints for Hibernate
    // to know how to create the DB table when generating the SQL inside H2 in-memory DB
    private UUID id;
    private String beerName;
    @Version//mandatory, locking strategy used by Hibernate if version in DB is the same, if not - it will throw an exception
    //for ex if data has been hanged by another process and my process has stale data
    private Integer version;
    private BeerStyle beerStyle;
    private String upc;
    private Integer quantityOnHand;
    private BigDecimal price;
    private LocalDateTime createdData;
    private LocalDateTime updateData;
}
