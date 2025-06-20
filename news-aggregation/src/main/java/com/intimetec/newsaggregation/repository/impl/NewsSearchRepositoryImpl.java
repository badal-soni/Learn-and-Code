package com.intimetec.newsaggregation.repository.impl;

import com.intimetec.newsaggregation.dto.JpaCriteria;
import com.intimetec.newsaggregation.dto.request.DateRangeFilterCriteria;
import com.intimetec.newsaggregation.entity.News;
import com.intimetec.newsaggregation.entity.NewsLikes;
import com.intimetec.newsaggregation.exception.BadRequestException;
import com.intimetec.newsaggregation.repository.NewsSearchRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewsSearchRepositoryImpl implements NewsSearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<News> searchNewsByCriteria(DateRangeFilterCriteria dateRangeFilterCriteria, Pageable page) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<News> query = criteriaBuilder.createQuery(News.class);
        Root<News> root = query.from(News.class);
        Join<News, NewsLikes> joinLikes = root.join("userInteractions", JoinType.LEFT);
        query.select(root).distinct(true);

        JpaCriteria<News> jpaCriteria = new JpaCriteria<>(
                criteriaBuilder,
                query,
                root
        );

        List<Predicate> predicates = buildSearchPredicates(jpaCriteria, dateRangeFilterCriteria);
        predicates.addAll(buildDateRangePredicates(jpaCriteria, dateRangeFilterCriteria));

        query.where(predicates.toArray(new Predicate[0]));
        query.groupBy(root);

        Expression<Long> likeCount = criteriaBuilder.sum(
                criteriaBuilder.<Long>selectCase()
                        .when(criteriaBuilder.equal(joinLikes.get("interactionType"), "LIKE"), 1L)
                        .otherwise(0L)
        );
        Expression<Long> dislikeCount = criteriaBuilder.sum(
                criteriaBuilder.<Long>selectCase()
                        .when(criteriaBuilder.equal(joinLikes.get("interactionType"), "DISLIKE"), 1L)
                        .otherwise(0L)
        );

        if ("likes".equalsIgnoreCase(dateRangeFilterCriteria.getSortBy())) {
            query.orderBy(criteriaBuilder.desc(likeCount));
        } else if ("dislikes".equalsIgnoreCase(dateRangeFilterCriteria.getSortBy())) {
            query.orderBy(criteriaBuilder.asc(dislikeCount));
        }
        // todo: paginate

        return entityManager
                .createQuery(query)
                .getResultList();
    }

    private ArrayList<Predicate> buildSearchPredicates(
            JpaCriteria<News> jpaCriteria,
            DateRangeFilterCriteria dateRangeFilterCriteria
    ) {
        Predicate headlinePredicate = jpaCriteria.getCriteriaBuilder().like(
                jpaCriteria.getCriteriaBuilder().lower(jpaCriteria.getRoot().get("headline")),
                "%" + dateRangeFilterCriteria.getSearchQuery() + "%"
        );
        Predicate descriptionPredicate = jpaCriteria.getCriteriaBuilder().like(
                jpaCriteria.getCriteriaBuilder().lower(jpaCriteria.getRoot().get("description")),
                "%" + dateRangeFilterCriteria.getSearchQuery() + "%"
        );
        return new ArrayList<>(List.of(headlinePredicate, descriptionPredicate));
    }

    private ArrayList<Predicate> buildDateRangePredicates(
            JpaCriteria<News> jpaCriteria,
            DateRangeFilterCriteria dateRangeFilterCriteria
    ) {
        if (!shouldFilterByDateRange(dateRangeFilterCriteria)) {
            throw new BadRequestException("Invalid date range filter criteria provided.");
        }
        Predicate fromDatePredicate = jpaCriteria.getCriteriaBuilder().greaterThanOrEqualTo(
                jpaCriteria.getRoot().get("publishedAt"),
                dateRangeFilterCriteria.getFromDate()
        );
        Predicate toDatePredicate = jpaCriteria.getCriteriaBuilder().lessThanOrEqualTo(
                jpaCriteria.getRoot().get("publishedAt"),
                dateRangeFilterCriteria.getToDate()
        );
        return new ArrayList<>(List.of(fromDatePredicate, toDatePredicate));
    }

    private boolean shouldFilterByDateRange(DateRangeFilterCriteria dateRangeFilterCriteria) {
        if (Objects.nonNull(dateRangeFilterCriteria.getToDate()) && Objects.isNull(dateRangeFilterCriteria.getFromDate())) {
            return false;
        }
        return !Objects.nonNull(dateRangeFilterCriteria.getFromDate()) || !Objects.isNull(dateRangeFilterCriteria.getToDate());
    }

}
