package api;

import domain.Events.PollClosed;
import domain.Events.PollCreated;
import domain.Events.VoteAdded;
import domain.Vote;

import java.util.UUID;

interface Voting {
    VoteAdded addVote(UUID user, UUID poll, Vote vote);

    PollCreated createPoll(UUID creator);

    PollClosed closePoll(UUID user, UUID poll);
}
