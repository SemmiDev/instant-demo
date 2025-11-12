package space.sammi.demo_time;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant; // <- RULE 1

@Data // Lombok untuk getters, setters, etc.
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /**
     * RULE 1 (DATABASE):
     * Disimpan sebagai 'Instant' (UTC murni).
     * Di PostgreSQL, ini akan menjadi 'timestamp with time zone'.
     */
    @Column(nullable = false)
    private Instant eventTime;

    /**
     * RULE 1 (INTERNAL):
     * 'CreationTimestamp' akan otomatis mengisi 'Instant.now()' (UTC)
     * saat data pertama kali dibuat.
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
