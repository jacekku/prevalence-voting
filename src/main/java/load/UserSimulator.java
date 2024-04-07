package load;

import api.VotingService;
import domain.Vote;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;


public class UserSimulator implements Runnable {
    final VotingService votingService;

    final Random rnd = new Random(137);

    final AtomicBoolean stopped = new AtomicBoolean(false);

    final AtomicLong counter = new AtomicLong(0);
    private final List<String> polls;
    private final String id;

    public UserSimulator(VotingService votingService, List<String> polls) {
        this.votingService = votingService;
        this.polls = polls;
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public void run() {
        while (!stopped.get()) {
            voteOnRandomPoll();
        }
    }

    public void voteOnRandomPoll() {
        var number = rnd.nextInt(this.polls.size());
        var poll = this.polls.get(number);
        var vote = rnd.nextInt(2) > 0 ? Vote.YES : Vote.NO;
        this.votingService.addVote(this.id, poll, vote);
        this.counter.getAndIncrement();
    }

    public long stopSimulation() {
        this.stopped.lazySet(true);
        return this.counter.get();
    }

    public long getFinalState() {
        return this.counter.get();
    }
}