package load;

import api.VotingService;
import system.VotingSystemPrevayler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Simulator {
    public static final int SECONDS_TOTAL = 10;
    private final VotingService votingService;

    public Simulator(VotingService votingService) {
        this.votingService = votingService;
    }

    public static void main(String... args) {
        runSimulation(5000);
        System.gc();
        sleep();
        Result result = runSimulation(SECONDS_TOTAL * 1000);
        System.out.println("Orders processed:" + result.processed);
        System.out.println("p/s:" + (double) result.processed / (double) SECONDS_TOTAL);
        System.out.println("Read processed:" + result.read);
        System.out.println("p/s:" + (double) result.read / (double) SECONDS_TOTAL);
        for (var i = 0; i < result.polls.size(); i++) {
//            System.out.println(result.polls.get(i));
        }
    }

    private static Result runSimulation(long time) {
        final VotingSystemPrevayler gp = new VotingSystemPrevayler("./load_test");
        final Simulator simulator = new Simulator(gp);
        Result result = simulator.runSimulator(time, 3, 1);
        gp.destroy();
        return result;
    }

    public Result runSimulator(long time, int clientThreads, int readingThreads) {
        final List<UserSimulator> clients = new ArrayList<>();
        final List<ReaderSimulator> readers = new ArrayList<>();
        final List<String> polls = new ArrayList<>();
        for (int i = 0; i < 10_000; i++) {
            var created = this.votingService.createPoll(UUID.randomUUID(), "poll name");
            polls.add(created.poll());
        }

        for (int i = 0; i < clientThreads; i++) {
            clients.add(new UserSimulator(this.votingService, polls));
        }
        for (int i = 0; i < clientThreads; i++) {
            readers.add(new ReaderSimulator(this.votingService, polls));
        }

        clients.forEach(c -> new Thread(c).start());
        readers.forEach(c -> new Thread(c).start());

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        clients.forEach(cl -> cl.stopSimulation());
        readers.forEach(cl -> cl.stopSimulation());


        sleep();

        final long orders = clients.stream().map(c -> c.getFinalState()).reduce(0L, Long::sum);
        final long read = readers.stream().map(c -> c.getFinalState()).reduce(0L, Long::sum);

        var results = polls.stream().map(p -> this.votingService.getResults(p).get()).toList();
        var allVotes = results.stream().map(poll -> poll.no() + poll.yes()).reduce(0L, Long::sum);
        System.out.println(orders + " " + allVotes);
        if (orders != allVotes) {
            System.out.println(orders + " " + allVotes);
            throw new RuntimeException();
        }
        return new Result(orders, results, read);
    }

    private static void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
