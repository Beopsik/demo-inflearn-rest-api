package com.example.demoinflearnrestapi.events;

import com.example.demoinflearnrestapi.common.ErrorsResource;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
@Controller
public class EventController {
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    @PostMapping
    public ResponseEntity<? extends RepresentationModel<?>> createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        Event newEvent = this.eventRepository.save(event);

        WebMvcLinkBuilder webMvcLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        EventResource eventResource = new EventResource(newEvent);
        eventResource.add(webMvcLinkBuilder.withRel("update-event"));
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(Link.of("/docs/index.html#resources-events-create").withRel("profile"));

        URI createdUri = webMvcLinkBuilder.toUri();

        return ResponseEntity.created(createdUri).body(eventResource);
    }
    private ResponseEntity<? extends RepresentationModel<?>> badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Event>>> queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
        Page<Event> page = this.eventRepository.findAll(pageable);
        PagedModel<EntityModel<Event>> pagedResources = assembler.toModel(page, EventResource::new);
        pagedResources.add(Link.of("/docs/index.html#resources-events-list").withRel("profile"));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable Integer id) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        try {
            Event event = optionalEvent.orElseThrow();
            EventResource eventResource = new EventResource(event);
            eventResource.add(Link.of("/docs/index.html#resources-events-get").withRel("profile"));
            return ResponseEntity.ok(eventResource);
        } catch (NoSuchElementException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Integer id, @RequestBody @Valid EventDto eventDto, Errors errors) {
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        try {
            Event existingEvent = optionalEvent.orElseThrow();
            this.modelMapper.map(eventDto, existingEvent);
            Event savedEvent = this.eventRepository.save(existingEvent);

            EventResource eventResource = new EventResource(savedEvent);
            eventResource.add(Link.of("/docs/index.html#resources-events-update").withRel("profile"));

            return ResponseEntity.ok(eventResource);
        } catch (NoSuchElementException exception) {
            return ResponseEntity.notFound().build();
        }
    }

}
