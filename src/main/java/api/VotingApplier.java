package api;

import domain.Events;

public interface VotingApplier {
    void applyVoteAdded(Events.VoteAdded event);

    void applyPollCreated(Events.PollCreated event);

    void applyPollClosed(Events.PollClosed event);
}
