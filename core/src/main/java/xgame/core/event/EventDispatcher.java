package xgame.core.event;


import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class EventDispatcher {
	public Map<Class<? extends Event>, Set<EventListener<? extends Event>>> eventListeners = new ConcurrentHashMap<Class<? extends Event>, Set<EventListener<? extends Event>>>(0);

	public void addListener(EventListener listener) {
		Class eventType = listener.getEventType();
		Set<EventListener<? extends Event>> _listeners = eventListeners.get(eventType);
		if (_listeners == null) {
			_listeners = new LinkedHashSet<EventListener<? extends Event>>();
			eventListeners.put(eventType, _listeners);
		}
		_listeners.add(listener);
	}

	public void removeListener(EventListener listener) {
		Class eventType = listener.getEventType();
		Set<EventListener<? extends Event>> _listeners = eventListeners.get(eventType);
		if (_listeners == null || _listeners.isEmpty())
			return;
		_listeners.remove(listener);
	}

	public <E extends Event> void fireEvent(E event) {
		Set<EventListener<? extends Event>> _listeners = eventListeners.get(event.getClass());
		if (_listeners == null || _listeners.isEmpty())
			return;

		for (EventListener<? extends Event> eventListener : _listeners)
			((EventListener<Event>)eventListener).onEvent(event);
	}
}
