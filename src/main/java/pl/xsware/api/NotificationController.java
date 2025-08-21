package pl.xsware.api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.xsware.domain.model.Response;
import pl.xsware.domain.model.notification.Notification;
import pl.xsware.domain.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<Notification>> getAll(@PathVariable Long userId) {
        return ResponseEntity.ok(null);
    }

    @PostMapping("/remove")
    public ResponseEntity<Response> removeNotifications(@RequestBody @Valid List<Notification> data) {
        return ResponseEntity.ok(Response.create("OK"));
    }

    @PostMapping("/changeAsRead")
    public ResponseEntity<Response> changeAsRead(@RequestBody @Valid List<Notification> data) {
        return ResponseEntity.ok(Response.create("OK"));
    }
}
