package pl.xsware.domain.model.category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryType {

    INCOME("Przychody"),
    EXPENSE("Wydatki");

    private final String label;
}
