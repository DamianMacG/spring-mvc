package com.spring.mvc.entities;

import com.spring.mvc.model.Beerstyle;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.*;

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
    private UUID id;

    @Version // Needed in Entities if using version
    private Integer version;
    private String beerName;
    private Beerstyle beerStyle;
    private String upc;
    private Integer quantityOnHand;
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
