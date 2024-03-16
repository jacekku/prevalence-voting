package domain;

import api.VotingService;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public abstract class VotingSystemTestBase<Service extends VotingService> {
    protected Service service;

    @Test
    public void shouldCreatePoll() {
        UUID user = UUID.randomUUID();
        var event = service.createPoll(user);
        assertThat(event.user(), equalTo(user.toString()));
    }

    @Test
    public void shouldClosePoll() {
        UUID user = UUID.randomUUID();
        var created = service.createPoll(user);
        var closed = service.closePoll(user, UUID.fromString(created.poll()));
        assertThat(closed.poll(), equalTo(created.poll()));
    }

    @Test
    public void shouldAddVoteToPoll() {
        UUID user = UUID.randomUUID();
        var created = service.createPoll(user);
        var voted = service.addVote(user, UUID.fromString(created.poll()), Vote.YES);
        assertThat(voted.vote(), equalTo(Vote.YES));
    }

    @Test
    public void shouldNotAddVoteToNoPoll() {
        var voted = service.addVote(UUID.randomUUID(), UUID.randomUUID(), Vote.YES);
        assertThat(voted, equalTo(null));
    }

    @Test
    public void onlyCreatorCanClosePoll() {
        UUID user = UUID.randomUUID();
        var created = service.createPoll(user);
        var closed = service.closePoll(UUID.randomUUID(), UUID.fromString(created.poll()));
        assertThat(closed, equalTo(null));
    }

    @Test
    public void canGetPollResults() {
        UUID user = UUID.randomUUID();
        var created = service.createPoll(user);
        var voted = service.addVote(user, UUID.fromString(created.poll()), Vote.YES);
        var result = service.getResults(UUID.fromString(created.poll())).get();
        assertThat(result, equalTo(new PollResult(created.poll(), 1,0)));
    }
}