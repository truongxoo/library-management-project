package study.demo.entity;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Otp_verification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpVerification  implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "opt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer optId;
    
    @Column(name = "opt_code", columnDefinition = "varchar(255)")
    private String otpCode;

    @CreatedDate
    @Column(name = "otp_create_time", columnDefinition = "datetime")
    private Instant createTime = Instant.now();
   
    @Column(name = "isExpired", columnDefinition = "datetime")
    private Instant expiryDate;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
}
