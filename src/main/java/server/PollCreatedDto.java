package server;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import domain.Events;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize
public record PollCreatedDto(String name, String creator, String poll, String pollName) {

    PollCreatedDto(Events.PollCreated event) {
        this("PollCreated", event.user(), event.poll(), event.pollName());
    }
}
