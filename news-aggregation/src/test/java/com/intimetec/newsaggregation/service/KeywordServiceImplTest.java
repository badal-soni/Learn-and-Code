package com.intimetec.newsaggregation.service;

import com.intimetec.newsaggregation.dto.request.CreateKeywordRequest;
import com.intimetec.newsaggregation.entity.BlockedKeyword;
import com.intimetec.newsaggregation.entity.Keyword;
import com.intimetec.newsaggregation.entity.NewsCategory;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.exception.BadRequestException;
import com.intimetec.newsaggregation.repository.BlockedKeywordRepository;
import com.intimetec.newsaggregation.repository.KeywordsRepository;
import com.intimetec.newsaggregation.repository.NewsCategoryRepository;
import com.intimetec.newsaggregation.repository.NewsRepository;
import com.intimetec.newsaggregation.service.impl.KeywordServiceImpl;
import com.intimetec.newsaggregation.util.MockDataCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KeywordServiceImplTest {

    @Mock
    private KeywordsRepository keywordsRepository;

    @Mock
    private NewsCategoryRepository newsCategoryRepository;

    @Mock
    private BlockedKeywordRepository blockedKeywordRepository;

    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    private KeywordServiceImpl keywordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createKeyword_shouldThrowExceptionIfKeywordExists() {
        CreateKeywordRequest request = MockDataCreator.createMockCreateKeywordRequest();
        User user = MockDataCreator.createMockUser();
        when(keywordsRepository.existsByKeywordAndUserIdAndCategoryName(request.getKeyword(), user.getId(), request.getParentCategory())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> keywordService.createKeyword(request, user));
    }

    @Test
    void createKeyword_shouldSaveKeywordIfCategoryExists() {
        CreateKeywordRequest request = MockDataCreator.createMockCreateKeywordRequest();
        User user = MockDataCreator.createMockUser();
        NewsCategory newsCategory = MockDataCreator.createMockNewsCategory();
        when(keywordsRepository.existsByKeywordAndUserIdAndCategoryName(request.getKeyword(), user.getId(), request.getParentCategory())).thenReturn(false);
        when(newsCategoryRepository.findByCategoryName(request.getParentCategory())).thenReturn(Optional.of(newsCategory));
        keywordService.createKeyword(request, user);
        verify(keywordsRepository, times(1)).save(any(Keyword.class));
    }

    @Test
    void toggleKeywordActiveStatus_shouldToggleStatus() {
        User user = MockDataCreator.createMockUser();
        Keyword keyword = MockDataCreator.createMockKeyword();
        when(keywordsRepository.findByIdAndUserId(keyword.getId(), user.getId())).thenReturn(Optional.of(keyword));
        keywordService.toggleKeywordActiveStatus(keyword.getId(), user);
        assertFalse(keyword.isActive());
        verify(keywordsRepository, times(1)).save(keyword);
    }

    @Test
    void getAllKeywordsOfUser_shouldReturnKeywordResponses() {
        User user = MockDataCreator.createMockUser();
        Keyword keyword = MockDataCreator.createMockKeyword();
        user.setKeywords(new HashSet<>());
        user.getKeywords().add(keyword);
        List<?> responses = keywordService.getAllKeywordsOfUser(user);
        assertEquals(1, responses.size());
    }

    @Test
    void addBlockedKeyword_shouldThrowExceptionIfKeywordExists() {
        String keyword = "mockBlockedKeyword";
        when(blockedKeywordRepository.existsByBlockedKeyword(keyword.toLowerCase())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> keywordService.addBlockedKeyword(keyword));
    }

    @Test
    void addBlockedKeyword_shouldSaveBlockedKeyword() {
        String keyword = "mockBlockedKeyword";
        when(blockedKeywordRepository.existsByBlockedKeyword(keyword.toLowerCase())).thenReturn(false);
        keywordService.addBlockedKeyword(keyword);
        verify(blockedKeywordRepository, times(1)).save(any(BlockedKeyword.class));
    }

    @Test
    void getAllBlockedKeywords_shouldReturnBlockedKeywords() {
        BlockedKeyword blockedKeyword = MockDataCreator.createMockBlockedKeyword();
        when(blockedKeywordRepository.findAll()).thenReturn(List.of(blockedKeyword));
        List<String> blockedKeywords = keywordService.getAllBlockedKeywords();
        assertEquals(1, blockedKeywords.size());
    }

    @Test
    void deleteBlockedKeyword_shouldThrowExceptionIfKeywordNotFound() {
        String keyword = "mockBlockedKeyword";
        when(blockedKeywordRepository.findByBlockedKeyword(keyword.toLowerCase())).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> keywordService.deleteBlockedKeyword(keyword));
    }

    @Test
    void deleteBlockedKeyword_shouldDeleteBlockedKeywordIfExists() {
        String keyword = "mockBlockedKeyword";
        BlockedKeyword blockedKeyword = MockDataCreator.createMockBlockedKeyword();
        when(blockedKeywordRepository.findByBlockedKeyword(keyword.toLowerCase())).thenReturn(Optional.of(blockedKeyword));
        keywordService.deleteBlockedKeyword(keyword);
        verify(blockedKeywordRepository, times(1)).deleteByBlockedKeyword(blockedKeyword.getBlockedKeyword());
    }
}