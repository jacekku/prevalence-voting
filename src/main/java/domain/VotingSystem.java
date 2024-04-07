package domain;

import api.VotingService;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VotingSystem implements VotingService, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    Map<String, Poll> polls = new ConcurrentHashMap<>();

    private void voteAdded(Events.VoteAdded event) {
        var poll = polls.get(event.poll());
        if (event.vote().equals(Vote.YES)) {
            poll.result = new PollResult(poll.id, poll.result.yes() + 1, poll.result.no());
        } else {
            poll.result = new PollResult(poll.id, poll.result.yes(), poll.result.no() + 1);
        }
    }

    private void pollCreated(Events.PollCreated event) {
        Poll poll = new Poll();
        poll.result = new PollResult(event.poll(), 0, 0);
        poll.creator = event.user();
        poll.id = event.poll();
        poll.name = event.pollName();
        polls.put(event.poll(), poll);
    }

    private void pollClosed(Events.PollClosed event) {
        polls.remove(event.poll());
    }


    private boolean validateAddVote(Commands.AddVote command) {
        return polls.containsKey(command.poll());
    }

    private boolean validateCreatePoll(Commands.CreatePoll command) {
        return true;
    }


    private boolean validateClosePoll(Commands.ClosePoll command) {
        if (!polls.containsKey(command.poll().toString())) return false;
        return polls.get(command.poll().toString()).creator.equals(command.user().toString());
    }

    public Optional<PollResult> getResults(String poll) {
        if (!polls.containsKey(poll)) return Optional.empty();
        return Optional.of(polls.get(poll).result);
    }


    @Override
    public boolean validateCommand(Commands command) {
        if (command instanceof Commands.CreatePoll e) {
            return validateCreatePoll(e);
        } else if (command instanceof Commands.ClosePoll e) {
            return validateClosePoll(e);
        } else if (command instanceof Commands.AddVote e) {
            return validateAddVote(e);
        }
        return false;
    }

    @Override
    public void applyEvent(Events event) {
        if (event instanceof Events.PollCreated e) {
            pollCreated(e);
        } else if (event instanceof Events.PollClosed e) {
            pollClosed(e);
        } else if (event instanceof Events.VoteAdded e) {
            voteAdded(e);
        }
    }
}
