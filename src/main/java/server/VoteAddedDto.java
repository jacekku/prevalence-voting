package server;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import domain.Events;
import domain.Vote;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize
public record VoteAddedDto(String name, String creator, String poll, Vote vote) {

    VoteAddedDto(Events.VoteAdded event) {
        this("VoteAdded", event.user(), event.poll(), event.vote());
    }

}
