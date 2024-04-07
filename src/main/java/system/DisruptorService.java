package system;

import api.VotingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import server.AddVoteDto;
import server.RequestType;
import server.VotingServer;

public class DisruptorService {

    private final int bufferSize = 65_536;
    private final Disruptor<DisruptorEvent> disruptor;
    private final ObjectMapper mapper;
    private final VotingService service;


    public DisruptorService(VotingService service) {
        this.service = service;
        disruptor = new Disruptor<>(DisruptorEvent::new, bufferSize, DaemonThreadFactory.INSTANCE, ProducerType.SINGLE, new SleepingWaitStrategy());
        disruptor.handleEventsWith(this::handle);
        disruptor.start();

        mapper = new ObjectMapper();
    }

    public void acceptRequest(RequestType type, String body) {
        RingBuffer<DisruptorEvent> ringBuffer = disruptor.getRingBuffer();
        long next = ringBuffer.next();
        try {
            DisruptorEvent event = ringBuffer.get(next);
            event.set(body);
        } finally {
            ringBuffer.publish(next);
        }

    }

    private void handle(DisruptorEvent event, long sequence, boolean endOfBatch) {
        try {
            var dto = mapper.readValue(event.rawData, AddVoteDto.class);
            this.service.addVote(dto.user(), dto.poll(), dto.vote());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
