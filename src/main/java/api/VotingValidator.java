package api;

import domain.Commands;


public interface VotingValidator {
    default boolean validateAddVote(Commands.AddVote command) {
        return true;
    }

    default boolean validateCreatePoll(Commands.CreatePoll command) {
        return true;
    }

    default boolean validateClosePoll(Commands.ClosePoll command) {
        return true;
    }
}
