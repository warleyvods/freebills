package com.freebills.events.category;

import com.freebills.domain.Category;

import java.util.concurrent.CompletableFuture;

public record CreateCategoryEvent(Object source, Category category, CompletableFuture<Category> future) { }
