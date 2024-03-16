package domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import system.VotingSystemPrevayler;

public class PrevaylerVotingSystemTest extends VotingSystemTestBase<VotingSystemPrevayler> {
    @BeforeEach
    public void init() {
        service = new VotingSystemPrevayler("./test_voting");
    }

    @AfterEach
    public void cleanUp() {
        service.destroy();
    }
}
