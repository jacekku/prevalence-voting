package domain;

import api.VotingService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class VotingSystem implements VotingService, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    List<Poll> pollArrayList = new ArrayList<>();
    Map<String, Poll> polls = new HashMap<>();


    private void voteAdded(Events.VoteAdded event) {
        var poll = pollArrayList.stream().filter(ev -> ev.id.equals(event.poll())).findFirst().orElse(null);
        if (poll == null) return;
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
        pollArrayList.add(poll);
    }

    private void pollClosed(Events.PollClosed event) {
        pollArrayList.remove(event.poll());
    }


    private boolean validateAddVote(Commands.AddVote command) {
        return findPoll(command.poll()) != null;
    }

    private boolean validateCreatePoll(Commands.CreatePoll command) {
        return true;
    }


    private boolean validateClosePoll(Commands.ClosePoll command) {
        var p = this.findPoll(command.poll().toString());
        if (p == null) return false;
        return p.creator.equals(command.user().toString());
    }

    public Optional<PollResult> getResults(String poll) {
        var p = this.findPoll(poll);
        if (p == null) return Optional.empty();
        return Optional.of(p.result);
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

    private Poll findPoll(String pollId) {
        for (int i = 0; i < pollArrayList.size(); i++) {
            if (pollArrayList.get(i).id.equals(pollId)) return pollArrayList.get(i);
        }
        return null;
    }

    @Serial
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        pollArrayList = new ArrayList<>();
        polls.forEach((key, value) -> pollArrayList.add(value));
    }
}
