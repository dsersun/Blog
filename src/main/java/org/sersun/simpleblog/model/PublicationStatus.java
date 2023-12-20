package org.sersun.simpleblog.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PublicationStatus {
    DRAFT("Draft"),
    UNDER_REVIEW("Under Review"),
    PUBLISHED("Published"),
    REJECTED("Rejected");

    private final String name;
}
