package io.github.azizie13.pong.gamelogic;

public class Event {
    public enum Type{
        WALL_COLLIDE,
        PADDLE_COLLIDE,
        COLLIDE,
    }

    private Type type;

    protected Event(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
