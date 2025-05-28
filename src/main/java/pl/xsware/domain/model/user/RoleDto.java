package pl.xsware.domain.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDto {

    private Long id;
    private String name;
    private String fullName;
}
