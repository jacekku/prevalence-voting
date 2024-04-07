package server;

import api.VotingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import pl.setblack.badass.Politician;
import ratpack.func.Action;
import ratpack.handling.Chain;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfig;
import system.VotingSystemPrevayler;

import java.net.URI;
import java.util.UUID;

import static ratpack.jackson.Jackson.json;

public class VotingServer {
    private final VotingService service;

    public VotingServer() {
        this.service = new VotingSystemPrevayler("prod_events");
    }

    private void init() {
        Politician.beatAroundTheBush(() -> {
            RatpackServer.start(server -> {
                server
                        .registryOf(r -> r
                                .add(ObjectMapper.class, new ObjectMapper().registerModule(new Jdk8Module())))
                        .serverConfig(
                                ServerConfig
                                        .embedded()
                                        .publicAddress(new URI(("http://localhost")))
                                        .port(8080)
                                        .threads(1))
                        .handlers(chain -> {
                            chain.prefix("api", apiChain -> {
                                apiChain
                                        .prefix("votes/close", closePollAction())
                                        .prefix("votes/create", createPollAction())
                                        .prefix("votes/vote", addVoteAction())
                                        .prefix("votes", getPollResult());
                            });
                        });
            });
        });
    }

    private Action<Chain> getPollResult() {
        return chain -> chain.get(":uuid", ctx -> {
            var uuid = ctx.getPathTokens().get("uuid");
            var resultOptional = service.getResults(uuid);
            if (resultOptional.isPresent()) {
                var result = resultOptional.get();
                ctx.render(json(new PollResultDto(result.poll(), result.yes(), result.no())));
            }

        });
    }

    private Action<Chain> addVoteAction() {
        return chain -> chain.patch(ctx -> {
            ctx.parse(AddVoteDto.class)
                    .onError(error -> {
                        System.out.println(error);
                        ctx.render(error);
                    })
                    .then(command -> {
                        var event = service.addVote(command.user(), command.poll(), command.vote());
                        if (event != null) {
                            ctx.render(json(new VoteAddedDto(event)));
                        }
                    });
        });
    }

    private Action<Chain> closePollAction() {
        return chain -> chain.delete(ctx -> {
            ctx.parse(ClosePollDto.class)
                    .onError(error -> {
                        System.out.println(error);
                        ctx.render(error);
                    })
                    .then(command -> {
                        var event = service.closePoll(command.user(), command.poll());
                        if (event != null) {
                            ctx.render(json(new PollClosedDto(event)));
                        }
                    });
        });
    }

    private Action<Chain> createPollAction() {
        return chain -> chain.post(ctx -> {
            ctx.parse(CreatePollDto.class)
                    .onError(error -> {
                        System.out.println(error);
                        ctx.render(error);
                    })
                    .then(command -> {
                        var event = service.createPoll(command.user(), "poll name");
                        if (event != null) {
                            ctx.render(json(new PollCreatedDto(event)));
                        }

                    });
        });

    }

    public static void main(final String... args) throws Exception {
        final VotingServer server = new VotingServer();
        server.init();
    }
}
