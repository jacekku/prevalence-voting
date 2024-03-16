package domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Poll implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    String creator;
    String id;
    PollResult result;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Poll poll = (Poll) o;
        return Objects.equals(creator, poll.creator) && Objects.equals(id, poll.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creator, id);
    }
}
