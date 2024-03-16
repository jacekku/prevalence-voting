package domain;

import java.io.Serializable;
import java.util.UUID;

public record PollResult(String poll, long yes, long no) implements Serializable {
}

