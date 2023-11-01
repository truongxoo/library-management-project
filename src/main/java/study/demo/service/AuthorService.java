package study.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import study.demo.entity.Author;

@Service
public interface AuthorService {

    List<Author> findAllAuthors();

}
