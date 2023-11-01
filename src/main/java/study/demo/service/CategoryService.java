package study.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import study.demo.entity.Category;


@Service
public interface CategoryService {

    Page<Category> findAllBookCategories(Pageable pageable);
}
