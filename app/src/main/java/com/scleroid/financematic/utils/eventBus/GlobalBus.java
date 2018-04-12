package com.scleroid.financematic.utils.eventBus;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * @author Ganesh Kaple
 * @since 22-11-2017
 */

public class GlobalBus {
	private static EventBus sBus;

	@Inject
	public GlobalBus() {
	}

	//TODO remove this, we need this inside Dagger
	public static EventBus getBus() {
		if (sBus == null) { sBus = EventBus.getDefault(); }
		return sBus;
	}
}