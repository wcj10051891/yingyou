package xgame.core.event;


import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class BaseObservable {
	public Map<Class<? extends Event>, Set<EventListener<? extends Event>>> eventListeners = new HashMap<Class<? extends Event>, Set<EventListener<? extends Event>>>(0);

	public void addListener(Class<? extends Event> eventType, EventListener... listeners) {
		if(listeners.length == 0)
			return;
		
		Set<EventListener<? extends Event>> _listeners = eventListeners.get(eventType);
		if (_listeners == null) {
			_listeners = new LinkedHashSet<EventListener<? extends Event>>();
			eventListeners.put(eventType, _listeners);
		}
		for (EventListener<? extends Event> eventListener : listeners)
			_listeners.add(eventListener);
	}

	public void removeListener(Class<? extends Event> eventType, EventListener... listeners) {
		if(listeners.length == 0) {
			eventListeners.remove(eventType);
			return;
		}

		Set<EventListener<? extends Event>> _listeners = eventListeners.get(eventType);
		if (_listeners == null || _listeners.isEmpty())
			return;
		
		for (EventListener<? extends Event> eventListener : listeners)
			_listeners.remove(eventListener);
	}

	public <E extends Event> void fireEvent(E event) {
		Set<EventListener<? extends Event>> _listeners = eventListeners.get(event.getClass());
		if (_listeners == null || _listeners.isEmpty())
			return;

		for (EventListener<? extends Event> eventListener : _listeners)
			((EventListener<Event>)eventListener).onEvent(event);
	}
}
