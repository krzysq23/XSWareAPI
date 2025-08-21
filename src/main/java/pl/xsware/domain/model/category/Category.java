package pl.xsware.domain.model.category;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class Category {

    private Long id;
    private Long userId;
    private String name;
    private CategoryType type;
    private String color;
    private String icon;
    private Instant createdAt;
}
