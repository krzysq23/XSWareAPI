package pl.xsware.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.xsware.domain.model.Response;
import pl.xsware.domain.model.data.ContactForm;
import pl.xsware.domain.service.MailService;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private MailService mailService;

    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getApplicationStatusInfo() {
        return Response.builder().message("Application is started").build();
    }

    @PostMapping(value = "/contact", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response contact(@RequestBody @Valid ContactForm data) {
        mailService.sendContactMail(data);
        return Response.builder().message("Formularz został wysłany!").build();
    }

}
