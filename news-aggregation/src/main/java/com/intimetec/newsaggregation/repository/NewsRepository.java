package com.intimetec.newsaggregation.repository;

import com.intimetec.newsaggregation.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    @Query(value = "SELECT n FROM News n JOIN FETCH n.categories c WHERE n.publishedAt = :date AND c.isHidden = FALSE")
    List<News> findAllByPublishedAtAndIsHiddenFalse(LocalDate date);

    @Query(value = "SELECT n FROM News n JOIN FETCH n.categories c WHERE n.publishedAt >= :from AND n.publishedAt <= :to AND c.isHidden = FALSE")
    List<News> findAllByPublishedAtBetweenAndIsHiddenFalse(LocalDate from, LocalDate to);

    @Query(value = "SELECT n FROM News n JOIN FETCH n.categories c WHERE c.categoryName = :categoryName AND c.isHidden = FALSE AND n.isHidden = FALSE")
    List<News> findAllByCategory(String categoryName);

    @Query(value = "SELECT n FROM News n JOIN FETCH n.categories c WHERE c.categoryName = :categoryName AND n.publishedAt >= :from AND c.isHidden = FALSE AND n.isHidden = FALSE")
    List<News> findAllByCategoryFromPublishedAt(String categoryName, LocalDate from);

    @Query(value = "SELECT n FROM News n JOIN FETCH n.categories c WHERE c.categoryName = :categoryName AND n.publishedAt <= :to AND c.isHidden = FALSE AND n.isHidden = FALSE")
    List<News> findAllByCategoryTillPublishedAt(String categoryName, LocalDate to);

    @Query(value = "SELECT n FROM News n JOIN FETCH n.categories c WHERE c.categoryName = :categoryName AND n.publishedAt >= :from AND n.publishedAt <= :to AND c.isHidden = FALSE AND n.isHidden = FALSE")
    List<News> findAllByCategoriesCategoryNameAndPublishedAtBetweenAndIsHiddenFalse(String categoryName, LocalDate from, LocalDate to);

    List<News> findAllByIsHiddenTrue();

    List<News> findAllByIsHiddenFalse();

    @Query(value = "SELECT n.id FROM News n JOIN n.categories c WHERE n.isHidden = FALSE AND c.isHidden = FALSE")
    List<Long> findAllIds();

    @Query(
            value = "SELECT * FROM news " +
                    "WHERE (" +
                    "LOWER(news.headline) REGEXP :pattern OR LOWER(news.description) REGEXP :pattern)",
            nativeQuery = true
    )
    List<News> findAllHavingKeywords(@Param("pattern") String pattern);

    @Query(value = "SELECT news FROM News news WHERE LOWER(news.headline) LIKE LOWER(CONCAT('%', :textToSearch, '%')) OR LOWER(news.description) LIKE LOWER(CONCAT('%', :textToSearch, '%')) AND news.isHidden = false")
    List<News> findAllNewsMatchesHeadlineOrDescriptionAndHiddenFalse(@Param("textToSearch") String textToSearch);

    @Query(value = "SELECT news FROM News news WHERE LOWER(news.headline) LIKE LOWER(CONCAT('%', :textToSearch, '%')) OR LOWER(news.description) LIKE LOWER(CONCAT('%', :textToSearch, '%')) AND news.publishedAt >= :publishedDate AND news.isHidden = false")
    List<News> findAllNewsMatchesHeadlineOrDescriptionFromPublishedDate(
            @Param("textToSearch") String textToSearch,
            @Param("publishedDate") LocalDate publishedDate
    );

    @Query(value = "SELECT news FROM News news WHERE LOWER(news.headline) LIKE LOWER(CONCAT('%', :textToSearch, '%')) OR LOWER(news.description) LIKE LOWER(CONCAT('%', :textToSearch, '%')) AND news.publishedAt <= :publishedDate AND news.isHidden = false")
    List<News> findAllNewsMatchesHeadlineOrDescriptionTillPublishedDate(
            @Param("textToSearch") String textToSearch,
            @Param("publishedDate") LocalDate publishedDate
    );

    @Query(value = "SELECT news FROM News news WHERE LOWER(news.headline) LIKE LOWER(CONCAT('%', :textToSearch, '%')) OR LOWER(news.description) LIKE LOWER(CONCAT('%', :textToSearch, '%')) AND news.publishedAt >= :from AND news.publishedAt <= :to AND news.isHidden = false")
    List<News> findAllNewsMatchesHeadlineOrDescriptionAndPublishedAtInBetween(
            @Param("textToSearch") String textToSearch,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE news " +
                    "SET is_hidden = TRUE " +
                    "WHERE is_hidden = false AND (" +
                    "LOWER(headline) REGEXP :pattern OR LOWER(description) REGEXP :pattern)",
            nativeQuery = true
    )
    int setIsHiddenTrue(@Param("pattern") String pattern);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
            value = "UPDATE news " +
                    "SET is_hidden = FALSE " +
                    "WHERE is_hidden = TRUE AND (" +
                    "LOWER(headline) REGEXP :pattern OR LOWER(description) REGEXP :pattern)",
            nativeQuery = true
    )
    int setIsHiddenFalse(@Param("pattern") String pattern);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE News n SET n.isHidden = :isHidden WHERE n.id IN :newsIds")
    int updateHiddenStatusByIds(@Param("isHidden") boolean isHidden, @Param("newsIds") List<Long> newsIds);

}
