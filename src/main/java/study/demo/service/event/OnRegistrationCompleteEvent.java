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

    private String appUrl;

    private OtpVerification otp;

    private LinkVerification link;

    private User user;

    public OnRegistrationCompleteEvent(String appUrl, OtpVerification otp, LinkVerification link,
            User user) {
        super(user);
        this.appUrl = appUrl;
        this.otp = otp;
        this.link = link;
        this.user = user;
    }

    public OnRegistrationCompleteEvent(String appUrl, LinkVerification link, User user) {
        super(user);
        this.appUrl = appUrl;
        this.link = link;
        this.user = user;
    }

    public OnRegistrationCompleteEvent(String appUrl, OtpVerification otp, User user) {
        super(user);
        this.appUrl = appUrl;
        this.otp = otp;
        this.user = user;
    }

    
}
