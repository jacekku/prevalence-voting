package domain;

import api.VotingService;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VotingSystem implements VotingService, Serializable {

    Map<String, Poll> polls = new ConcurrentHashMap<>();

    @Override
    public void applyVoteAdded(Events.VoteAdded event)  {
        var poll = polls.get(event.poll());
        if (event.vote().equals(Vote.YES)) {
            poll.result = new PollResult(poll.id, poll.result.yes() + 1, poll.result.no());
        } else {
            poll.result = new PollResult(poll.id, poll.result.yes(), poll.result.no() + 1);
        }
    }

    @Override
    public void applyPollCreated(Events.PollCreated event) {
        Poll poll = new Poll();
        poll.result = new PollResult(event.poll(), 0, 0);
        poll.creator = event.user();
        poll.id = event.poll();
        polls.put(event.poll(), poll);
    }

    @Override
    public void applyPollClosed(Events.PollClosed event) {
        polls.remove(event.poll());
    }


    @Override
    public boolean validateAddVote(Commands.AddVote command) {
        return polls.containsKey(command.poll().toString());
    }

    @Override
    public boolean validateCreatePoll(Commands.CreatePoll command) {
        return true;
    }

    @Override
    public boolean validateClosePoll(Commands.ClosePoll command) {
        if (!polls.containsKey(command.poll().toString())) return false;
        if (!polls.get(command.poll().toString()).creator.equals(command.user().toString())) return false;
        return true;
    }

    @Override
    public Optional<PollResult> getResults(UUID poll) {
        if (!polls.containsKey(poll.toString())) return Optional.empty();
        return Optional.of(polls.get(poll.toString()).result);
    }


}
