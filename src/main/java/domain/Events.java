package domain;

import java.io.Serializable;

public sealed interface Events {
    record VoteAdded(String user, String poll, Vote vote) implements Events, Serializable {
    }

    record PollCreated(String user, String poll, String pollName) implements Events, Serializable {
    }

    record PollClosed(String poll) implements Events, Serializable {
    }
}
