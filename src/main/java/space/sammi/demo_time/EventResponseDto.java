package space.sammi.demo_time;

import lombok.Data;
import java.time.OffsetDateTime; // <- RULE 2

/**
 * DTO untuk OUTPUT API.
 * Best practice adalah selalu mengembalikan timestamp
 * dalam format UTC (Z) agar konsisten.
 * misal: "2025-11-13T03:00:00Z"
 */
@Data
public class EventResponseDto {
    private Long id;
    private String name;
    private OffsetDateTime eventTime;
    private OffsetDateTime createdAt;
}
