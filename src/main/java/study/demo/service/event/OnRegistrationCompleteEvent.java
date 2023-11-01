package study.demo.service.event;

import org.springframework.context.ApplicationEvent;

import lombok.Builder;
import lombok.Data;
import study.demo.entity.User;

@Data   
@Builder
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private String appUrl;

    private User user;

    public OnRegistrationCompleteEvent( String appUrl, User user) {
        super(user);
        
        this.appUrl = appUrl;
        this.user = user;
    }
    
}
