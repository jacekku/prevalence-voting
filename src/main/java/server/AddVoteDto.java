package server;

import com.fasterxml.jackson.annotation.JsonProperty;
import domain.Vote;

import java.util.UUID;

public record AddVoteDto(
        @JsonProperty("user") UUID user,
        @JsonProperty("poll") UUID poll,
        @JsonProperty("vote") Vote vote
) {
}
