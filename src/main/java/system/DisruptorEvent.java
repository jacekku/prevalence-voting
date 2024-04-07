package system;

public class DisruptorEvent {
    public String rawData;

    public void set(String rawData) {
        this.rawData = rawData;
    }

    @Override
    public String toString() {
        return "DisruptorEvent{value=" + this.rawData + "}";
    }
}
