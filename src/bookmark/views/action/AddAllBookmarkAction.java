package bookmark.views.action;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import bookmark.views.TreeObject;
import bookmark.views.TreeParent;

public class AddAllBookmarkAction extends BookmarkAction {

	public AddAllBookmarkAction(TreeViewer viewer) {
		super(viewer);
		init();
	}

	private void init() {
		this.setText("Add all open files here");
		this.setToolTipText("Add all open files here");
		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_BKMRK_TSK));
	}

	public void run() {
		// get active editor info
		String relativePath = "";
		String projectName = "";

		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow window = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		IEditorPart[] editors = page.getEditors();
		if (editors != null) {
			for (int i = 0; i < editors.length; i++) {
				IEditorPart editor = editors[i];

				IFileEditorInput input = (IFileEditorInput) editor.getEditorInput();
				IFile file = input.getFile();
				relativePath = file.getProjectRelativePath().toOSString();
				projectName = file.getProject().getName();

				// create leaf with file info
				TreeObject child = new TreeObject(relativePath, projectName);

				// get invisibleRoot
				TreeParent invisibleRoot = (TreeParent) viewer.getInput();

				// get selection
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				if (obj == null) {
					// default to insert invisibleRoot
					invisibleRoot.addChild(child);
				} else {
					TreeObject targetParent = (TreeObject) obj;
					invisibleRoot.addChild(targetParent, child);
				}

				// keep expand situation
				Object[] expandedElements = viewer.getExpandedElements();
				TreePath[] expandedTreePaths = viewer.getExpandedTreePaths();

				// update data source
				viewer.setInput(invisibleRoot);

				viewer.setExpandedElements(expandedElements);
				viewer.setExpandedTreePaths(expandedTreePaths);

				// save to persistent
				this.savePersistantData(invisibleRoot);
			}
		} else {
			this.showMessage("No active editor");
			return;
		}
	}

}
