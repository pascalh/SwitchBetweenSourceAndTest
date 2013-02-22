package de.switchbetweensrcandtest.preference;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.switchbetweensrcandtest.Activator;


public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * Create the preference page.
	 */
	public PreferencePage() {
		super(FLAT);
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		setPreferenceStore(preferenceStore);
		setTitle("Java Test Switcher");
	}

	/**
	 * Create contents of the preference page.
	 */
	@Override
	protected void createFieldEditors() {
		StringFieldEditor stringFieldEditor = new StringFieldEditor(PreferenceConstance.JAVA_PATH, "Java Path", -1, StringFieldEditor.VALIDATE_ON_KEY_STROKE, getFieldEditorParent());
		addField(stringFieldEditor);
		stringFieldEditor = new StringFieldEditor(PreferenceConstance.TEST_PATH, "Test Path", -1, StringFieldEditor.VALIDATE_ON_KEY_STROKE, getFieldEditorParent());
		addField(stringFieldEditor);
		addField(new RadioGroupFieldEditor(PreferenceConstance.TEST_PREFIX, "Test Filename", 1, 
				new String[][]{{"TestXYZ.java", "true"}, {"XYZTest.java", "false"}}, getFieldEditorParent(), false));
	}

	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
	
	}

}
