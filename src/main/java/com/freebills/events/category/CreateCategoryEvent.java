package com.freebills.events.category;

import com.freebills.domain.Category;
import lombok.Getter;

import java.util.concurrent.CompletableFuture;

public class CreateCategoryEvent {

    private final Object source;

    @Getter
    private final Category category;

    @Getter
    private final CompletableFuture<Category> future;

    public CreateCategoryEvent(Object source, Category category, CompletableFuture<Category> future) {
        this.source = source;
        this.category = category;
        this.future = future;
    }
}
