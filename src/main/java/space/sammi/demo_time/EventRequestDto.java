package space.sammi.demo_time;

import lombok.Data;
import java.time.OffsetDateTime; // <- RULE 2

/**
 * DTO untuk INPUT API.
 * Menerima format string ISO 8601 / RFC3339, misal:
 * "2025-11-13T10:00:00+07:00" (dari user Jakarta)
 * "2025-11-13T03:00:00Z" (dari sistem lain)
 */
@Data
public class EventRequestDto {
    private String name;
    private OffsetDateTime eventTime;
}
