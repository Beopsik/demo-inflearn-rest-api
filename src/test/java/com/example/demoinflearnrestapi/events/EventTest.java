package com.example.demoinflearnrestapi.events;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder().build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {
        // Given
        Event event = new Event();
        String name = "Event";
        String description = "Spring";

        // When
        event.setName(name);
        event.setDescription(description);

        // Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0, true",
            "100, 0, false",
            "0, 100, false"
    })
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        // Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    @Test
    public void testOffline() {
        // Given
        Event event = Event.builder()
                .location("중앙대학교 팀플실")
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isOffline()).isTrue();


        // Given
        event = Event.builder()
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isOffline()).isFalse();
    }
}