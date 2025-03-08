package com.spring.mvc.entities;

import com.spring.mvc.model.BeerStyle;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter // Use @Getter and @Setter annotations as you shouldn't use @Data in Entities
@Setter //
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Beer {
    @Id // Needed in Entities
    @GeneratedValue(generator = "UUID") // Automatically generates a unique identifier using a UUID strategy
    @UuidGenerator // Specifies that this field should be automatically populated with a unique UUID value
    // @Column defines a database column with a fixed length of 36 characters, stored as a VARCHAR type, and ensures the value cannot be updated or left null
    @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID id;

    @Version // Needed in Entities if using version
    private Integer version;
    private String beerName;
    private BeerStyle beerStyle;
    private String upc;
    private Integer quantityOnHand;
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
