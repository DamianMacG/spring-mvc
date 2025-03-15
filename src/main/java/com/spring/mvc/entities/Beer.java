package com.spring.mvc.entities;

import com.spring.mvc.model.BeerStyle;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    
    // @Size(max = 50) applies validation at the application level, restricting the field's length before in reaches the database
    // @Column(length = 50) sets the maximum column size in the database but does not enforce validation - limits storage space
    @Column(length = 50)
    @Size(max = 50)
    @NotNull
    @NotBlank
    private String beerName;

    @NotNull
    private BeerStyle beerStyle;

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String upc;
    private Integer quantityOnHand;

    @NotNull
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
