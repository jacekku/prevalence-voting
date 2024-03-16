package system;

import api.VotingService;
import domain.Commands;
import domain.Events;
import domain.PollResult;
import domain.VotingSystem;
import pl.setblack.airomem.core.PersistenceController;
import pl.setblack.airomem.core.Query;
import pl.setblack.airomem.core.VoidCommand;
import pl.setblack.airomem.core.builders.PrevaylerBuilder;

import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

public class VotingSystemPrevayler implements VotingService {
    final PersistenceController<VotingSystem> controller;
    final VotingSystem system;

    public VotingSystemPrevayler(String repositoryFolder) {
        system = new VotingSystem();
        controller = PrevaylerBuilder
                .<VotingSystem>newBuilder()
                .withFolder(Paths.get(repositoryFolder))
                .useSupplier(() -> system)
                .disableRoyalFoodTester()
                .build();
    }

    public void destroy() {
        controller.erase();
    }

    @Override
    public void applyVoteAdded(Events.VoteAdded event) {
        controller.execute(new VoteAdded(event));
    }

    @Override
    public void applyPollCreated(Events.PollCreated event) {
        controller.execute(new PollCreated(event));
    }

    @Override
    public void applyPollClosed(Events.PollClosed event) {
        controller.execute(new PollClosed(event));
    }

    @Override
    public boolean validateAddVote(Commands.AddVote command) {
        return controller.query(new Query<>() {
            @Override
            public Boolean evaluate(VotingSystem votingSystem) {
                return votingSystem.validateAddVote(command);
            }
        });
    }

    @Override
    public boolean validateCreatePoll(Commands.CreatePoll command) {
        return controller.query(new Query<>() {
            @Override
            public Boolean evaluate(VotingSystem votingSystem) {
                return votingSystem.validateCreatePoll(command);

            }
        });
    }

    @Override
    public boolean validateClosePoll(Commands.ClosePoll command) {
        return controller.query(new Query<>() {
            @Override
            public Boolean evaluate(VotingSystem votingSystem) {
                return votingSystem.validateClosePoll(command);
            }
        });
    }

    @Override
    public Optional<PollResult> getResults(UUID poll) {
        return controller.query(new Query<>() {
            @Override
            public Optional<PollResult> evaluate(VotingSystem votingSystem) {
                return votingSystem.getResults(poll);
            }
        });
    }


    private record VoteAdded(Events.VoteAdded event) implements VoidCommand<VotingSystem> {
        @Override
        public void executeVoid(VotingSystem votingSystem) {
            votingSystem.applyVoteAdded(event);

        }
    }

    private record PollCreated(Events.PollCreated event) implements VoidCommand<VotingSystem> {
        @Override
        public void executeVoid(VotingSystem votingSystem) {
            votingSystem.applyPollCreated(event);
        }
    }

    private record PollClosed(Events.PollClosed event) implements VoidCommand<VotingSystem> {
        @Override
        public void executeVoid(VotingSystem votingSystem) {
            votingSystem.applyPollClosed(event);
        }
    }

}
