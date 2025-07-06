package com.intimetec.newsaggregation.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JpaCriteria<T> {

    private CriteriaBuilder builder;
    private CriteriaQuery<T> query;
    private Root<T> root;

}
