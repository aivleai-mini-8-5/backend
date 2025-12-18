package com.aivle.spring.domain;

import lombok.Getter;

@Getter
public enum Category {
    // 임의로 적어놓은 거라 수정 필요

    FICTION("소설"),
    NON_FICTION("비소설"),
    SCIENCE("과학"),
    HISTORY("역사"),
    ART("예술"),
    TECHNOLOGY("기술"),
    EDUCATION("교육"),
    TRAVEL("여행"),
    OTHER("기타");


    private final String label;

    Category(String label) {
        this.label = label;
    }
}
