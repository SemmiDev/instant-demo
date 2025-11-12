package space.sammi.demo_time;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Lombok untuk auto-inject 'final' fields
public class EventService {

    private final EventRepository eventRepository;

    /**
     * Membuat Event baru
     */
    public EventResponseDto createEvent(EventRequestDto dto) {
        Event event = new Event();
        event.setName(dto.getName());

        // KONVERSI (API -> DB):
        // Ubah OffsetDateTime (Rule 2) dari DTO
        // ke Instant (Rule 1) untuk Entity.
        // .toInstant() otomatis mengkonversi ke UTC.
        event.setEventTime(dto.getEventTime().toInstant());

        Event savedEvent = eventRepository.save(event);

        // Konversi kembali ke DTO untuk response
        return mapEntityToDto(savedEvent);
    }

    /**
     * Mendapatkan satu Event
     */
    public EventResponseDto getEventById(Long id) {
        Event event = eventRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Event not found"));

        return mapEntityToDto(event);
    }

    /**
     * Mencari Event dalam range waktu
     * Menerima 'Instant' (Rule 1) dari Controller.
     */
    public List<EventResponseDto> findEventsInRange(Instant start, Instant end) {
        // 1. Panggil query murni (Rule 1)
        List<Event> events = eventRepository.findByEventTimeBetween(start, end);

        // 2. Konversi hasilnya ke DTO (Rule 2)
        return events.stream()
            .map(this::mapEntityToDto)
            .collect(Collectors.toList());
    }

    /**
     * Helper private untuk konversi Entity -> DTO
     */
    private EventResponseDto mapEntityToDto(Event event) {
        EventResponseDto dto = new EventResponseDto();
        dto.setId(event.getId());
        dto.setName(event.getName());

        // KONVERSI (DB -> API):
        // Ubah Instant (Rule 1) dari DB
        // ke OffsetDateTime (Rule 2) untuk API.
        // Kita paksakan .atOffset(ZoneOffset.UTC) agar API
        // selalu mengembalikan UTC ("...Z")
        dto.setEventTime(event.getEventTime().atOffset(ZoneOffset.UTC));
        dto.setCreatedAt(event.getCreatedAt().atOffset(ZoneOffset.UTC));

        return dto;
    }
}
