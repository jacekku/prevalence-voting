package server;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record CreatePollDto(@JsonProperty("user") UUID user) {
}
