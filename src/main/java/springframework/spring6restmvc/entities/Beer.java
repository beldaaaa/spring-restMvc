package springframework.spring6restmvc.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
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
    @Column(length = 99, columnDefinition = "varchar(36)", updatable = false, nullable = false)//some hints for Hibernate
    // to know how to create the DB table when generating the SQL inside H2 in-memory DB
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;
    @Version
//mandatory, locking strategy used by Hibernate if version in DB is the same, if not - it will throw an exception
    //for ex if data has been hanged by another process and my process has stale data
    private Integer version;
    @NotNull
    @NotBlank
    //default Hibernate String VARCHAR length is 255, if I want to restrict max length, I will use JPA annotation
    @Size(max = 50)//it is good practice to validate it here before it hits the DB (it would catch this violation too,
    //but it is better to validate before I even try to persist to the DB, because this DB exception would bubble up
    // to my service layer and ultimately to the web layer, so it is much more preferable to handle it through a
    // bean validation constraint)
    //generally it is good to have here validation constraints matching DB validation constraints
    private String beerName;
    @NotNull
    private BeerStyle beerStyle;
    @NotNull
    @NotBlank
    private String upc;
    private Integer quantityOnHand;
    @NotNull
    private BigDecimal price;
    @CreationTimestamp//those 2 annotations are Hibernate specific, not JPA
    private LocalDateTime createdData;
    @UpdateTimestamp
    private LocalDateTime updateData;
}
