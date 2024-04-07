package domain;

import java.util.UUID;

public sealed interface Commands {
    record AddVote(String user, String poll, Vote vote) implements Commands {
    }

    record CreatePoll(UUID creator) implements Commands {
    }

    record ClosePoll(UUID user, UUID poll) implements Commands {
    }
}
