package pl.xsware.domain.model.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionRange {

    TODAY("today"),
    LAST_MONTH("month"),
    LAST_WEEK("lastWeek"),
    LAST_QUARTER("lastQuarter"),
    LAST_YEAR("lastYear");

    private final String value;

    public static TransactionRange fromValue(String value) {
        for (TransactionRange range : TransactionRange.values()) {
            if (range.value.equalsIgnoreCase(value)) {
                return range;
            }
        }
        throw new IllegalArgumentException("Nieznany zakres: " + value);
    }
}
