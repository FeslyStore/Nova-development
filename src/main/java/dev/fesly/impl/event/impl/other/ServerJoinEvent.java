package dev.fesly.impl.event.impl.other;

import dev.fesly.impl.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class ServerJoinEvent implements Event {

    public String ip;
    public int port;
}