package bookmark.views.action;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import bookmark.views.TreeObject;
import bookmark.views.TreeParent;

public class AddBookmarkAction extends BookmarkAction {

	private ISelectionService service = null;

	public AddBookmarkAction(TreeViewer viewer, ISelectionService service) {
		super(viewer);
		this.service = service;
		init();
	}

	private void init() {
		this.setText("Add bookmark here");
		this.setToolTipText("Add bookmark here");
		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_BKMRK_TSK));
	}

	public void run() {
		// get active editor info
		String relativePath = "";
		String projectName = "";

		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow window = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		IEditorPart editor = page.getActiveEditor();

		if (editor != null) {
			IFileEditorInput input = (IFileEditorInput) editor.getEditorInput();
			IFile file = input.getFile();
			relativePath = file.getProjectRelativePath().toOSString();
			projectName = file.getProject().getName();
		} else {
			// check selection from package explorer
			IStructuredSelection packageExploerSelection = (IStructuredSelection) service.getSelection("org.eclipse.jdt.ui.PackageExplorer");
			if (packageExploerSelection != null) {
				Object obj = packageExploerSelection.getFirstElement();
				if (obj == null) {
					showMessage("No selection in package explorer");
					return;
				} else {
					// get file info for selection from package explorer
					IResource resource = ((ICompilationUnit) obj).getResource();

					if (resource.getType() == IResource.FILE) {
						IFile ifile = (IFile) resource;
						relativePath = ifile.getProjectRelativePath().toOSString();
						projectName = ifile.getProject().getName();
					}
				}
			} else {
				showMessage("No active editor or selection in package explorer");
				return;
			}
		}

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

}
