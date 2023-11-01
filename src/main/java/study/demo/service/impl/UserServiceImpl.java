package study.demo.service.impl;

import java.time.Instant;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import study.demo.entity.LinkVerification;
import study.demo.entity.User;
import study.demo.repository.UserRepository;
import study.demo.service.UserService;
import study.demo.service.dto.request.UserFilter;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

	@Value("${app.maxFailedAttempts}")
	private long maxFailedAttempts;

	@Value("${app.lockTimeDuration}")
	private long lockTimeDuration; 

	private final UserRepository userRepository;

	@Override
	public Page<User> findAllUsers(Integer pageIndex) {
		Pageable pageable = null;
		if (pageIndex == 0) {
			pageable = PageRequest.of(0, 10);
		} else {
			pageable = PageRequest.of(pageIndex, 10);
		}
		return userRepository.findAll(pageable);
	}

	@Override
	public Page<User> findUsersByFilter(UserFilter userFilter, Integer pageIndex) {
		Pageable pageable = null;
		if (pageIndex == 0) {
			pageable = PageRequest.of(0, 10);
		} else {
			pageable = PageRequest.of(pageIndex, 10);
		}
		
		return null;
	}

	@Transactional
	public void increaseFailedAttempts(User user) {
		int newFailAttempts = user.getFailedAttempt() + 1;
		userRepository.updateFailedAttempts(newFailAttempts, user.getEmail());
	}
	

	public void lock(User user) {
		user.setLocked(true);
		user.setLockTime(Instant.now());
		userRepository.save(user);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		if(email==null) {
			return Optional.empty();
		}
		return userRepository.findByEmail(email);
	}

	@Override
	public void unlockUser(User user) {
		userRepository.unlockUser(user.getEmail());
	}


}
