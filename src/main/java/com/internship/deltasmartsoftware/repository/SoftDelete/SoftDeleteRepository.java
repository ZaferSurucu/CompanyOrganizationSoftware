package com.internship.deltasmartsoftware.repository.SoftDelete;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;
import java.util.Optional;


@Transactional
@NoRepositoryBean
public interface SoftDeleteRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {

    Iterable<T> findAllActive();

    Iterable<T> findAllActive(Specification<T> spec);

    Iterable<T> findAllActive(Sort sort);

    Page<T> findAllActive(Specification<T> spec, Pageable pageable);

    Page<T> findAllActive(Pageable page);

    Optional<T> findOneActive(ID id);

    Optional<T> findOneActive(Specification<T> spec);


    Optional<T> delete(ID id);

    void delete(T entity);

    Optional<T> hardDelete(T entity);
}
