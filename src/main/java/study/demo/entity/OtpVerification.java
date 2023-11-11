package study.demo.entity;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.NumberFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.demo.enums.EOtpType;

@Entity
@Table(name = "Otp_verification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpVerification extends AbstractAuditingEntity  implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "opt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer optId;
    
    @Column(name = "opt_code", columnDefinition = "varchar(255)")
    private String otpCode;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "opt_type", columnDefinition = "varchar(50)")
    private EOtpType otpType;

    @Column(name = "isExpired", columnDefinition = "datetime")
    private Instant expiryDate;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
}
