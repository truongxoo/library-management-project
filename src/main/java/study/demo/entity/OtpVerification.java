package study.demo.entity;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
    @Column(name = "opt", columnDefinition = "varchar(255)")
    private String otp;

    @Column(name = "otp_create_time", columnDefinition = "datetime")
    private Instant otpCreateTime = Instant.now();
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
}
