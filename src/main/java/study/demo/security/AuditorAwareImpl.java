package study.demo.security;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if(securityContext.getAuthentication()==null || securityContext.getAuthentication().getName().equals("anonymousUser")) {
            return Optional.of("SYSTEM");
        }
        return  Optional.of(securityContext.getAuthentication().getName());
    }
}
