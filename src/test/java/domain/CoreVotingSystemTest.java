package domain;

import org.junit.jupiter.api.BeforeEach;

public class CoreVotingSystemTest extends VotingSystemTestBase<VotingSystem> {

    @BeforeEach
    public void init() {
        service = new VotingSystem();
    }
}
