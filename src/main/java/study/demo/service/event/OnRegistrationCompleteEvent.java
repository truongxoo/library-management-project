package study.demo.service.event;

import org.springframework.context.ApplicationEvent;

import lombok.Builder;
import lombok.Data;
import study.demo.entity.LinkVerification;
import study.demo.entity.OtpVerification;
import study.demo.entity.User;

@Data
@Builder
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;


    private OtpVerification otp;

    private LinkVerification link;

    private User user;

    public OnRegistrationCompleteEvent(OtpVerification otp, LinkVerification link,
            User user) {
        super(user);
        this.otp = otp;
        this.link = link;
        this.user = user;
    }

    public OnRegistrationCompleteEvent(LinkVerification link, User user) {
        super(user);
        this.link = link;
        this.user = user;
    }

    public OnRegistrationCompleteEvent( OtpVerification otp, User user) {
        super(user);
        this.otp = otp;
        this.user = user;
    }

    
}
