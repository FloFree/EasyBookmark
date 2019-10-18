package bookmark.views.action;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import bookmark.utils.PathController;
import bookmark.views.TreeObject;

public class DoubleClickAction extends BookmarkAction {

	public DoubleClickAction(TreeViewer viewer) {
		super(viewer);
		init();
	}

	private void init() {
		this.setText("Rename");
		this.setToolTipText("Rename the folder.");
	}

	public void run() {
		ISelection selection = viewer.getSelection();
		Object obj = ((IStructuredSelection) selection).getFirstElement();

		if (obj != null) {
			TreeObject treeObject = (TreeObject) obj;
			if (treeObject.getFlag() == 1) {
				// expand and collapse folder when double click
				if (viewer.getExpandedState(treeObject)) {
					viewer.collapseToLevel(treeObject, 1);
				} else {
					viewer.expandToLevel(treeObject, 1);
				}
				return;
			}
			
			// check system path file 
			String formalPath = treeObject.getName();
			String convertedPath = PathController.conversion(formalPath);
			if (!formalPath.equals(convertedPath)) {
				treeObject.setName(convertedPath);
			}

			IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = workspaceRoot.getProject(treeObject.getProjectName());
			IFile file1 = project.getFile((new Path(convertedPath)));
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			IWorkbenchPage page = window.getActivePage();
			IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(file1.getName());

			// if no right editor to find, use default text editor
			try {
				if (desc == null) {
					page.openEditor(new FileEditorInput(file1), "org.eclipse.ui.DefaultTextEditor");
				} else {
					page.openEditor(new FileEditorInput(file1), desc.getId());
				}
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
	}

}
