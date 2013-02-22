package de.switchbetweensrcandtest.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import de.switchbetweensrcandtest.Activator;


public class PreferenceInitializer extends 	AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstance.JAVA_PATH, "src/main/java");
		store.setDefault(PreferenceConstance.TEST_PATH, "src/test/java");
		store.setDefault(PreferenceConstance.TEST_PREFIX, "true");
	}

}
