package pl.xsware.domain.model.data;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContactForm {

    @NotBlank
    private String name;
    @NotBlank
    private String email;
    private String phone;
    private String company;
    @NotBlank
    private String message;
}
