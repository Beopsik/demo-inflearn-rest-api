package com.example.demoinflearnrestapi.events;

import com.example.demoinflearnrestapi.accounts.Account;
import com.example.demoinflearnrestapi.accounts.AccountSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@EqualsAndHashCode(of = "id")
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Event {

    @Id @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;
    @JsonSerialize(using = AccountSerializer.class)
    @ManyToOne
    private Account manager;
    public void update() {
        // Update free
        this.free = this.basePrice == 0 && this.maxPrice == 0;

        // Update offline
        this.offline = this.location != null && !this.location.isBlank();
    }
}
