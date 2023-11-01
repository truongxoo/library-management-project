
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
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.demo.enums.EGender;

@Entity
@Table(name = "member")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "first_name", columnDefinition = "varchar(50)")
    private String firstName;

    @Column(name = "last_name", columnDefinition = "varchar(50)")
    private String lastName;

    @Column(name = "birthday", columnDefinition = "datetime")
    private Instant birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "varchar(10)")
    private EGender gender;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<BorrowerRecord> borrowerRecord;

}
