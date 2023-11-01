package study.demo.entity;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "userSession")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "user_session_id", columnDefinition = "varchar(255)")
    private String userSessionId;
    
    @Column(name = "is_refresh_token", columnDefinition = "boolean default true")
    private boolean isRefreshToken;
    
    @Column(name = "is_expired", columnDefinition = "boolean default false")
    private boolean isExpired;
    
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    @JsonIgnore
    private Instant createdDate = Instant.now();

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
