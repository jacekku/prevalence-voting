package load;

import api.VotingService;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class ReaderSimulator implements Runnable {
    final VotingService votingService;

    final Random rnd = new Random(137);

    final AtomicBoolean stopped = new AtomicBoolean(false);

    final AtomicLong counter = new AtomicLong(0);
    private final List<String> polls;

    public ReaderSimulator(VotingService votingService, List<String> polls) {
        this.votingService = votingService;
        this.polls = polls;
    }

    @Override
    public void run() {
        while (!stopped.get()) {
            getRandomPoll();
        }
    }

    public void getRandomPoll() {
        var number = rnd.nextInt(this.polls.size());
        var poll = this.polls.get(number);
        var result = this.votingService.getResults(poll);
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
