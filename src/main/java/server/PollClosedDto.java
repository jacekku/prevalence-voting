package server;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import domain.Events;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize
public record PollClosedDto(String name, String poll) {
    PollClosedDto(Events.PollClosed event) {
        this("PollClosed", event.poll());
    }
}
