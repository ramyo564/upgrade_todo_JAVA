package com.example.todo.domain.model;

/**
 * 사용자 성별을 나타내는 enum
 */
public enum Gender {
    MALE("남"),
    FEMALE("여");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 