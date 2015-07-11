package xgame.core.event;


public abstract class EventListener<E extends Event> {
	public abstract Class<E> getEventType();
	public abstract void onEvent(E event);
}
