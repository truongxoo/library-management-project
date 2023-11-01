package study.demo.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.demo.enums.EUserStatus;

@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "email", columnDefinition = "varchar(50) NOT NULL")
    private String email;

    @Column(name = "password", columnDefinition = "varchar(255) NOT NULL")
    private String password;

    @Column(name = "phone", columnDefinition = "varchar(10)")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", columnDefinition = "varchar(10)")
    private EUserStatus userStatus;

    @JsonIgnore
    @Column(name = "failed_attempt", columnDefinition = "int default 0")
    private Integer failedAttempt = 0;

    @Column(name = "locked", columnDefinition = "boolean default false")
    private boolean locked;

    @JsonIgnore
    @Column(name = "lock_time", columnDefinition = "datetime default null")
    private Instant lockTime;
    
    @Column(name = "verification_code", columnDefinition = "varchar(255)")
    private String verificationCode;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<Login> login;

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private UserSession userSession;
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<OtpVerification> otpVerification;
    
    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private LinkVerification linkVerification;
    

}
