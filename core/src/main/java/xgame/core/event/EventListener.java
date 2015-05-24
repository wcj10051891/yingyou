package xgame.core.event;


public abstract class EventListener<E extends Event> {
	public void onEvent(E event) {
	}
}
