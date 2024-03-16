package api;

import domain.PollResult;

import java.util.Optional;
import java.util.UUID;

interface Polling {
    Optional<PollResult> getResults(UUID poll);
}
