package study.demo.entity;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.demo.enums.ERecordStatus;

@Entity
@Table(name = "borrower_record")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BorrowerRecord extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "borrower_record_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer borrowerRecordId;

    @Column(name = "issue_on", columnDefinition = "datetime")
    private Instant issueOn;

    @Column(name = "due_date", columnDefinition = "datetime")
    private Instant dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "record_status", columnDefinition = "varchar(10)")
    private ERecordStatus recordStatus;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

}
