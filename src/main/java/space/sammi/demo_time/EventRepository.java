package space.sammi.demo_time;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Query untuk mencari event berdasarkan range waktu.
     * Menggunakan 'Instant' (Rule 1) karena ini berinteraksi
     * langsung dengan lapisan database.
     */
    List<Event> findByEventTimeBetween(Instant start, Instant end);
}
