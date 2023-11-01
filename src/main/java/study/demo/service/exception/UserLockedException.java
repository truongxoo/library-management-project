package study.demo.service.exception;

import org.springframework.security.authentication.LockedException;

public class UserLockedException extends LockedException {

    private static final long serialVersionUID = 1L;

    public UserLockedException(String msg) {
        super(msg);
    }

}
