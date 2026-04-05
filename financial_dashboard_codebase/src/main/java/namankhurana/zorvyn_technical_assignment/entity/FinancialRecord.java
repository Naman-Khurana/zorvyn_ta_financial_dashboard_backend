package namankhurana.zorvyn_technical_assignment.entity;

import jakarta.persistence.*;
import lombok.*;
import namankhurana.zorvyn_technical_assignment.enums.CategoryEnum;
import namankhurana.zorvyn_technical_assignment.enums.RecordTypeEnum;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "financial_records")
@SQLDelete(sql = "UPDATE financial_records SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class FinancialRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount",nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type",nullable = false)
    private RecordTypeEnum type;

    @Enumerated(EnumType.STRING)
    @Column(name = "category",nullable = false)
    private CategoryEnum category;

    @Column(name = "description")
    private String description;

    @Column(name = "record_date",nullable = false)
    private LocalDate recordDate;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted",nullable = false)
    @Builder.Default
    private Boolean deleted=false;

}
