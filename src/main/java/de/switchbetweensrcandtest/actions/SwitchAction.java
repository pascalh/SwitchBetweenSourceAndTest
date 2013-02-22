package de.switchbetweensrcandtest.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;

import de.switchbetweensrcandtest.Activator;
import de.switchbetweensrcandtest.preference.PreferenceConstance;


/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class SwitchAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	private IPreferenceStore preferenceStore;
	private String javaPath;
	private String testPath;
	/**
	 * The constructor.
	 */
	public SwitchAction() {
		
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		IPath relativePathOfCurrentClass = getRelativePathOfCurrentClass();
		if (relativePathOfCurrentClass == null){
			return;
		}
		String associatedFile = getAssociatedFile(relativePathOfCurrentClass);
		try {
			IPath location= Path.fromOSString(associatedFile);
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IWorkspaceRoot root = workspace.getRoot();
			IFile file = root.getFile(location);
			if(file.exists()){
				openInEditor(file);
			}
		} catch (Exception we){
			MessageDialog.openInformation(
					window.getShell(),
					"SwitchBetween",
					associatedFile);
		}
	}

	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
		preferenceStore = Activator.getDefault().getPreferenceStore();
		initialzeOptions();
	}
	
	private String getAssociatedFile(IPath fullPath) {
		initialzeOptions();
		String uriString = fullPath.toString();
		String name = fullPath.lastSegment();
		if (isJavaFileName(name)){
			if (isTestFile(name)) {
				return testToSourceFilename(uriString);
			} else {
				return sourceToTestFilename(uriString,name);
			}
		} else {
			throw new IllegalArgumentException(fullPath + " is not a java source file.");
		}
	}
	
	private void initialzeOptions() {
		javaPath = preferenceStore.getString(PreferenceConstance.JAVA_PATH);
		testPath = preferenceStore.getString(PreferenceConstance.TEST_PATH);
	}

	private String testToSourceFilename(String uriString) {
		String replaceFirst = uriString.replaceFirst(testPath, javaPath);
		return replaceFirst.replace("Test", "");
	}

	private String sourceToTestFilename(String uri,String name) {
		String replaceFirst = uri.replaceFirst(javaPath, testPath);
		if (isPrefixMode()){
			return replaceFirst.replace(name, "Test" + name);				
		} else {
			return replaceFirst.replace(".java", "Test.java");
		}
	}

	private boolean isPrefixMode() {
		String prefix = preferenceStore.getString(PreferenceConstance.TEST_PREFIX);
		return Boolean.parseBoolean(prefix);
	}
	
	private boolean isJavaFileName(String fileName) {
		return fileName.endsWith(".java");
	}
	
	private boolean isTestFile(String file) {
		return (file.contains("Test"));
	}
	
	private void openInEditor(IFile file) throws PartInitException {
		IWorkbenchPage page = window.getActivePage();
		IDE.openEditor(page, file);
	}
	
	private IPath getRelativePathOfCurrentClass() {
		IEditorPart activeEditor = window.getActivePage().getActiveEditor();
		IEditorInput editorInput = activeEditor.getEditorInput();
		if (editorInput instanceof FileEditorInput){
			FileEditorInput fileEditorInput = (FileEditorInput) editorInput;
			return fileEditorInput.getFile().getFullPath();
		}
		return null;
	}	
}