package com.intimetec.newsaggregation.classifier;

import com.intimetec.newsaggregation.dto.request.NewsCategoryClassifierLLMRequest;
import com.intimetec.newsaggregation.dto.response.NewsCategoryClassifierLLMResponse;
import com.intimetec.newsaggregation.entity.NewsCategory;
import com.intimetec.newsaggregation.repository.NewsCategoryRepository;
import com.intimetec.newsaggregation.util.ApiClient;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewsCategoryClassifier {

    private final ApiClient apiClient;
    private final NewsCategoryRepository newsCategoryRepository;

    @Value("${llm.url}")
    private String llmApiUrl;

    private static final String AI_PROMPT = "%s what should be the best category for this news headline and description ? Dont the category with name test";

    @NotBlank
    public String classifyNewsCategory(
            @NotBlank String headline,
            @NotBlank String description
    ) {
        final List<String> existingCategories = this.newsCategoryRepository
                .findAll()
                .stream()
                .map(NewsCategory::getCategoryName)
                .toList();
        final String prompt = buildPrompt(headline, description);
        final NewsCategoryClassifierLLMRequest classifierRequest = new NewsCategoryClassifierLLMRequest();
        classifierRequest.setText(prompt);
        classifierRequest.setCategories(existingCategories);

        final NewsCategoryClassifierLLMResponse classifierResponse = this.apiClient.post(llmApiUrl, classifierRequest, NewsCategoryClassifierLLMResponse.class);
        return classifierResponse.getTopic();
    }

    private String buildPrompt(String headline, String description) {
        return String.format(AI_PROMPT, headline + " " + description);
    }

}
