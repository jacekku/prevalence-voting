package system;

import api.VotingService;
import domain.Commands;
import domain.Events;
import domain.PollResult;
import domain.VotingSystem;
import io.fury.Fury;
import io.fury.ThreadSafeFury;
import org.prevayler.foundation.serialization.JavaSerializer;
import pl.setblack.airomem.core.PersistenceController;
import pl.setblack.airomem.core.Query;
import pl.setblack.airomem.core.VoidCommand;
import pl.setblack.airomem.core.builders.PrevaylerBuilder;

import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

public class VotingSystemPrevayler implements VotingService {
    final PersistenceController<VotingSystem> controller;

    public VotingSystemPrevayler(String repositoryFolder) {
        controller = PrevaylerBuilder
                .<VotingSystem>newBuilder()
                .withFolder(Paths.get(repositoryFolder))
                .useSupplier(VotingSystem::new)
                .disableRoyalFoodTester()
                .build();
    }

    public void destroy() {
        controller.erase();
    }

    @Override
    public boolean validateCommand(Commands command) {
        return controller.query(votingSystem -> votingSystem.validateCommand(command));
    }

    @Override
    public void applyEvent(Events event) {
        controller.executeAndQuery(new ApplyEvent(event));
    }


    @Override
    public Optional<PollResult> getResults(String poll) {
        return controller.query(new Query<>() {
            @Override
            public Optional<PollResult> evaluate(VotingSystem votingSystem) {
                return votingSystem.getResults(poll);
            }
        });
    }

    private record ApplyEvent(Events event) implements VoidCommand<VotingSystem> {
        @Override
        public void executeVoid(VotingSystem votingSystem) {
            votingSystem.applyEvent(event);
        }
    }


}
