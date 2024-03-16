package load;

import domain.PollResult;

import java.util.List;

public class Result {
    final long processed;
    final List<PollResult> polls;
    final long read;

    public Result(long processed, List<PollResult> polls, long read) {
        this.processed = processed;
        this.polls = polls;
        this.read = read;
    }
}
