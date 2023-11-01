package study.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.entity.Author;
import study.demo.repository.AuthorRepository;
import study.demo.service.AuthorService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
	
	private final AuthorRepository authorRepository;
	
	@Override
	public List<Author> findAllAuthors() {
	    log.info("Find all authors");
		return authorRepository.findAll();
	}

}
