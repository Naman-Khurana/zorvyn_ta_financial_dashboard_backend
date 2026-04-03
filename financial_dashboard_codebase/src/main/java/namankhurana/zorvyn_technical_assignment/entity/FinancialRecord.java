package namankhurana.zorvyn_technical_assignment.entity;

import jakarta.persistence.*;
import lombok.*;
import namankhurana.zorvyn_technical_assignment.enums.RecordTypeEnum;
import org.hibernate.annotations.CreationTimestamp;

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

    @Column(name = "category")
    private String category;

    @Column(name = "description")
    private String description;

    @Column(name = "record_date")
    private LocalDate recordDate;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
