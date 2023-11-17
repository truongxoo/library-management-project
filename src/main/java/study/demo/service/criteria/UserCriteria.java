package study.demo.service.criteria;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.StringFilter;
@Data
@Builder
@AllArgsConstructor
public class UserCriteria implements Serializable, Criteria {
    
    private static final long serialVersionUID = 1L;

    private IntegerFilter userId;

    private StringFilter email;
    
    private StringFilter phone;
    
    private StringFilter userStatus;
    
    private StringFilter firstName;
    
    private InstantFilter birthday;
    
    private StringFilter lastName;
    
    private StringFilter gender;
    
    private IntegerFilter borrowerRecord;
    
    private BooleanFilter locked;
    
    @Override
    public UserCriteria copy() {
        return new UserCriteria(this);
    }
    
    public UserCriteria() {}
    
    public UserCriteria(UserCriteria other) {
        this.userId = other.userId == null ? null : other.userId.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.userStatus = other.userStatus == null ? null : other.userStatus.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.gender = other.gender == null ? null : other.gender.copy();
        this.borrowerRecord = other.borrowerRecord == null ? null : other.borrowerRecord.copy();
        this.locked = other.locked == null ? null : other.locked.copy();
        this.birthday = other.birthday == null ? null : other.birthday.copy();

    }

    public IntegerFilter userId() {
        if (userId == null) {
            userId = new IntegerFilter();
        }
        return userId;
    }
    
    public IntegerFilter borrowerRecord() {
        if (borrowerRecord == null) {
            borrowerRecord = new IntegerFilter();
        }
        return borrowerRecord;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }
    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }
    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }
    public StringFilter userStatus() {
        if (userStatus == null) {
            userStatus = new StringFilter();
        }
        return userStatus;
    }
    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }
    public StringFilter gender() {
        if (gender == null) {
            gender = new StringFilter();
        }
        return gender;
    }
    public BooleanFilter locked() {
        if (locked == null) {
            locked = new  BooleanFilter();
        }
        return locked;
    }
    public InstantFilter birthday() {
        if (birthday == null) {
            birthday = new  InstantFilter();
        }
        return birthday;
    }


}
