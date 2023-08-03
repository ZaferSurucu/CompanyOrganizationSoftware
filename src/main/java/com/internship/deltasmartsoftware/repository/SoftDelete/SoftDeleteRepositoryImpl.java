package com.internship.deltasmartsoftware.repository.SoftDelete;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;


public class SoftDeleteRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
        implements SoftDeleteRepository<T, ID> {


    private final JpaEntityInformation<T, ?> entityInformation;
    private final EntityManager em;
    private final Class<T> domainClass;
    private static final String DELETED_FIELD = "deletedAt";

    private static final Logger LOGGER = LoggerFactory.getLogger(SoftDeleteRepositoryImpl.class);

    public SoftDeleteRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.em = em;
        this.domainClass = domainClass;
        this.entityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, em);
    }


    @Override
    public Iterable<T> findAllActive() {
        if (isFieldDeletedAtExists()){
            return super.findAll(notDeleted());
        } else {
            return super.findAll();
        }
    }

    @Override
    public Iterable<T> findAllActive(Sort sort) {
        if (isFieldDeletedAtExists()){
            return super.findAll(notDeleted(), sort);
        } else {
            return super.findAll(sort);
        }
    }

    @Override
    public Page<T> findAllActive(Pageable page){
        if (isFieldDeletedAtExists()){
            return super.findAll(notDeleted(), page);
        } else {
            return super.findAll(page);
        }
    }

    @Override
    public Page<T> findAllActive(Specification<T> spec, Pageable pageable){
        if (isFieldDeletedAtExists()){
            return super.findAll(spec.and(notDeleted()), pageable);
        } else {
            return super.findAll(spec, pageable);
        }
    }

    @Override
    public Optional<T> findOneActive(ID id) {
        if (isFieldDeletedAtExists()){
            return super.findOne(Specification.where(new ByIdSpecification<>(entityInformation, id)).and(notDeleted()));
        } else {
            return super.findOne(Specification.where(new ByIdSpecification<>(entityInformation, id)));
        }
    }

    @Override
    public Optional<T> findOneActive(Specification<T> spec) {
        if (isFieldDeletedAtExists()){
            return super.findOne(spec.and(notDeleted()));
        } else {
            return super.findOne(spec);
        }
    }

    @Override
    public Iterable<T> findAllActive(Specification<T> spec) {
        if (isFieldDeletedAtExists()){
            return super.findAll(spec.and(notDeleted()));
        } else {
            return super.findAll(spec);
        }
    }

    private String localeDateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return localDateTime.format(formatter);
    }


    @Override
    public Optional<T> delete(ID id) {
        return softDelete(id, LocalDateTime.now());
    }

    @Override
    public void delete(T entity) {
        softDelete(entity, LocalDateTime.now());
    }

    @Override
    public Optional<T> hardDelete(T entity) {
        super.delete(entity);
        return Optional.of(entity);
    }

    private boolean isFieldDeletedAtExists() {
        try{
            domainClass.getSuperclass().getDeclaredField(DELETED_FIELD);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    private Optional<T> softDelete(ID id, LocalDateTime localDateTime) {
        Assert.notNull(id, "The given id must not be null!");

        Optional<T> entity = findOneActive(id);

        if (entity.isEmpty())
            throw new EmptyResultDataAccessException(
                    String.format("No %s entity with id %s exists!", entityInformation.getJavaType(), id), 1);

        return softDelete(entity.get(), localDateTime);
    }

    private Optional<T> softDelete(T entity, LocalDateTime localDateTime) {
        Assert.notNull(entity, "The entity must not be null!");


        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<T> update = cb.createCriteriaUpdate(domainClass);

        Root<T> root = update.from(domainClass);

        update.set(DELETED_FIELD, localeDateTimeToString(localDateTime));

        update.where(
                cb.equal(
                        root.<ID>get(Objects.requireNonNull(entityInformation.getIdAttribute()).getName()),
                        entityInformation.getId(entity)
                )
        );
        em.createQuery(update).executeUpdate();
        return Optional.of(entity);
    }


    private record ByIdSpecification<T, ID>(JpaEntityInformation<T, ?> entityInformation,
                                            ID id) implements Specification<T> {

        @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.<ID>get(Objects.requireNonNull(entityInformation.getIdAttribute()).getName()), id);
            }
        }

    private static final class DeletedIsNull<T> implements Specification<T> {
        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return cb.isNull(root.get(DELETED_FIELD));
        }
    }

    private static final class DeletedTimeGreaterThanNow<T> implements Specification<T> {
        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            return cb.greaterThan(root.get(DELETED_FIELD), LocalDateTime.now().format(formatter));
        }
    }

    private static <T> Specification<T> notDeleted() {
        return Specification.where(new DeletedIsNull<T>()).or(new DeletedTimeGreaterThanNow<T>());
    }
}
