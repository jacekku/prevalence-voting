package server;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record ClosePollDto(
        @JsonProperty("user") UUID user,
        @JsonProperty("poll") UUID poll
) {
}
