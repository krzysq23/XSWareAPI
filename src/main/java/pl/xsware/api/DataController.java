package pl.xsware.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.xsware.domain.model.dto.Response;

@RestController
public class DataController {

    @GetMapping(value = "/public/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getApplicationStatusInfo() {
        return Response.builder().message("Application is started").build();
    }

    @GetMapping(value = "/api/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getAuthenticationInfo() {
        return Response.builder().message("You are authorized").build();
    }

}
