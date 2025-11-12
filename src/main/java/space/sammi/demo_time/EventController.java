package space.sammi.demo_time;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime; // <- RULE 2
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    /**
     * POST /api/events
     * Body:
     * {
     * "name": "Acara di Jakarta",
     * "eventTime": "2025-11-13T10:00:00+07:00"
     * }
     */
    @PostMapping
    public ResponseEntity<EventResponseDto> createEvent(@RequestBody EventRequestDto requestDto) {
        EventResponseDto responseDto = eventService.createEvent(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * GET /api/events/1
     * Response:
     * {
     * "id": 1,
     * "name": "Acara di Jakarta",
     * "eventTime": "2025-11-13T03:00:00Z",  <-- Dikembalikan sebagai UTC
     * "createdAt": "..."
     * }
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getEvent(@PathVariable Long id) {
        EventResponseDto responseDto = eventService.getEventById(id);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * GET /api/events/search?start=...&end=...
     * Client WAJIB mengirim parameter dalam format UTC (Z)
     * e.g., ?start=2025-11-12T17:00:00Z&end=2025-11-13T16:59:59Z
     */
    @GetMapping("/search")
    public ResponseEntity<List<EventResponseDto>> getEventsInRange(
        @RequestParam OffsetDateTime start, // <- RULE 2 (API)
        @RequestParam OffsetDateTime end) {

        // KONVERSI (API -> SERVICE):
        // Ubah OffsetDateTime (Rule 2) ke Instant (Rule 1)
        // sebelum dilempar ke service layer.
        List<EventResponseDto> dtos = eventService.findEventsInRange(
            start.toInstant(),
            end.toInstant()
        );

        return ResponseEntity.ok(dtos);
    }
}
