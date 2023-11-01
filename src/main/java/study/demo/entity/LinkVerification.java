package study.demo.entity;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "link_verification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LinkVerification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "verification_code", columnDefinition = "varchar(255)")
    private String verificationCode;

    @Column(name = "link_create_time", columnDefinition = "datetime")
    private Instant linkCreateTime = Instant.now();
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
