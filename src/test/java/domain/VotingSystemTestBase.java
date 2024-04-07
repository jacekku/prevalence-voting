package domain;

import api.VotingService;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public abstract class VotingSystemTestBase<Service extends VotingService> {
    protected Service service;

    private Events.PollCreated createPoll(UUID user) {
        return service.createPoll(user, "poll name");
    }

    @Test
    public void shouldCreatePoll() {
        UUID user = UUID.randomUUID();
        var event = createPoll(user);
        assertThat(event.user(), equalTo(user.toString()));
    }

    @Test
    public void shouldCreatePollWithName() {
        UUID user = UUID.randomUUID();
        var event = service.createPoll(user, "Poll Name");
        assertThat(event.pollName(), equalTo("Poll Name"));
    }

    @Test
    public void shouldAddOptionsToPoll() {

    }

    @Test
    public void shouldClosePoll() {
        UUID user = UUID.randomUUID();
        var created = createPoll(user);
        var closed = service.closePoll(user, UUID.fromString(created.poll()));
        assertThat(closed.poll(), equalTo(created.poll()));
    }

    @Test
    public void shouldAddVoteToPoll() {
        UUID user = UUID.randomUUID();
        var created = createPoll(user);
        var voted = service.addVote(user.toString(), UUID.fromString(created.poll()).toString(), Vote.YES);
        assertThat(voted.vote(), equalTo(Vote.YES));
    }

    @Test
    public void shouldNotAddVoteToNoPoll() {
        var voted = service.addVote(UUID.randomUUID().toString(), UUID.randomUUID().toString(), Vote.YES);
        assertThat(voted, equalTo(null));
    }

    @Test
    public void onlyCreatorCanClosePoll() {
        UUID user = UUID.randomUUID();
        var created = createPoll(user);
        var closed = service.closePoll(UUID.randomUUID(), UUID.fromString(created.poll()));
        assertThat(closed, equalTo(null));
    }

    @Test
    public void canGetPollResults() {
        UUID user = UUID.randomUUID();
        var created = createPoll(user);
        var voted = service.addVote(user.toString(), created.poll(), Vote.YES);
        var result = service.getResults(created.poll()).get();
        assertThat(result, equalTo(new PollResult(created.poll(), 1, 0)));
    }
}