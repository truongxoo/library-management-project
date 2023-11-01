package study.demo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import study.demo.entity.Category;
import study.demo.repository.CategoryRepository;
import study.demo.service.CategoryService;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Page<Category> findAllBookCategories(Pageable pageable) {
        pageable = PageRequest.of(0, 10, Sort.by("categoryId"));
        return categoryRepository.findAll(pageable);
    }

}
