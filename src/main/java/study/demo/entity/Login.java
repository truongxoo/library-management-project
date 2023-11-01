
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
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "login")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Login implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "user_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userLogId;

    @Column(name = "token", columnDefinition = "datetime")
    private Instant loginTime;

    @Column(name = "refresh_token", columnDefinition = "varchar(255)")
    private String refreshToken;

    @Column(name = "event", columnDefinition = "varchar(255)")
    private String event;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
