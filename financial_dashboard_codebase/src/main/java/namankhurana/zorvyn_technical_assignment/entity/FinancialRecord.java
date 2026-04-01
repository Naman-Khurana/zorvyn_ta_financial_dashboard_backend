package namankhurana.zorvyn_technical_assignment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import namankhurana.zorvyn_technical_assignment.enums.RecordTypeEnum;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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
    private String Category;

    @Column(name = "description")
    private String description;

    @Column(name = "record_date")
    private LocalDate recordDate;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private LocalDateTime created_at;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
