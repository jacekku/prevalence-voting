package api;

import domain.Events;
import domain.Events.PollClosed;
import domain.Events.PollCreated;
import domain.Events.VoteAdded;
import domain.Vote;

import java.util.UUID;

interface Voting {
    VoteAdded addVote(String user, String poll, Vote vote);
    

    PollCreated createPoll(UUID creator, String pollName);

    PollClosed closePoll(UUID user, UUID poll);
}
