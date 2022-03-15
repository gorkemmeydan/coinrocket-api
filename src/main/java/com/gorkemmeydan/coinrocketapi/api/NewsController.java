package com.gorkemmeydan.coinrocketapi.api;

import com.gorkemmeydan.coinrocketapi.service.NewsService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class NewsController {
    private final NewsService newsService;

    // cache previous news for fallback
    Object cachedNews;

    @GetMapping("/news")
    @HystrixCommand(fallbackMethod = "getCachedNews", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
    })
    public ResponseEntity<?> getMarketData() {
        Object news = newsService.getNews();
        cachedNews = news;
        return ResponseEntity.ok(news);
    }

    @SuppressWarnings("unused")
    public ResponseEntity<?> getCachedNews() {
        log.info("Fallback for news");
        return ResponseEntity.ok(cachedNews);
    }
}
