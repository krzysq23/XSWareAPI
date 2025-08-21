package pl.xsware.domain.model.notification;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class Notification {

    private Long id;
    private Long userId;
    private String message;
    private NotificationType type;
    private boolean isRead;
    private Instant createdAt;
}
