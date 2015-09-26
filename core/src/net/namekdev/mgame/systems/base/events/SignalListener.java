package net.namekdev.mgame.systems.base.events;

import com.artemis.utils.reflect.Method;

import net.mostlyoriginal.api.event.common.EventListener;

class SignalListener extends EventListener {
	public final int signalCode;

	public SignalListener(Object object, Method method, int code) {
		super(object, method);
		signalCode = code;
	}
}
