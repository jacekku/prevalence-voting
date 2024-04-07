package api;

import domain.Commands;
import domain.Events;
import domain.Vote;

import java.util.UUID;

public interface VotingService extends Voting, Polling {


    boolean validateCommand(Commands command);
    void applyEvent(Events event);

    @Override
    default Events.VoteAdded addVote(String user, String poll, Vote vote) {
        var command = new Commands.AddVote(user, poll, vote);
        if (!validateCommand(command)) {
            return null;
        }
        var events = new Events.VoteAdded(user, poll, vote);
        applyEvent(events);
        return events;
    }

    @Override
    default Events.PollCreated createPoll(UUID creator, String pollName) {
        var command = new Commands.CreatePoll(creator);
        if (!validateCommand(command)) {
            return null;
        }
        var events = new Events.PollCreated(creator.toString(), UUID.randomUUID().toString(), pollName);
        applyEvent(events);
        return events;
    }

    @Override
    default Events.PollClosed closePoll(UUID user, UUID poll) {
        var command = new Commands.ClosePoll(user, poll);
        if (!validateCommand(command)) {
            return null;
        }
        var events = new Events.PollClosed(poll.toString());
        applyEvent(events);
        return events;
    }

}
