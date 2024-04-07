package server;

import com.fasterxml.jackson.annotation.JsonProperty;
import domain.Vote;

import java.util.UUID;

public record AddVoteDto(
        @JsonProperty("user") String user,
        @JsonProperty("poll") String poll,
        @JsonProperty("vote") Vote vote
) {
}
