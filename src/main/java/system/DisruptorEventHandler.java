package system;

import com.lmax.disruptor.EventHandler;

public class DisruptorEventHandler implements EventHandler<DisruptorEvent> {
    @Override
    public void onEvent(DisruptorEvent disruptorEvent, long sequence, boolean endOfBatch) throws Exception {

    }
}
