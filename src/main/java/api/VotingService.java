package api;

import domain.Commands;
import domain.Events;
import domain.PollResult;
import domain.Vote;

import java.util.UUID;

public interface VotingService extends Voting, VotingValidator, VotingApplier, Polling {

    @Override
    default Events.VoteAdded addVote(UUID user, UUID poll, Vote vote) {
        var command = new Commands.AddVote(user, poll, vote);
        if (!validateAddVote(command)) {
            return null;
        }
        var events = new Events.VoteAdded(user.toString(), poll.toString(), vote);
        applyVoteAdded(events);
        return events;
    }

    @Override
    default Events.PollCreated createPoll(UUID creator) {
        var command = new Commands.CreatePoll(creator);
        if (!validateCreatePoll(command)) {
            return null;
        }
        var events = new Events.PollCreated(creator.toString(), UUID.randomUUID().toString());
        applyPollCreated(events);
        return events;
    }

    @Override
    default Events.PollClosed closePoll(UUID user, UUID poll) {
        var command = new Commands.ClosePoll(user, poll);
        if (!validateClosePoll(command)) {
            return null;
        }
        var events = new Events.PollClosed(poll.toString());
        applyPollClosed(events);
        return events;
    }

}
