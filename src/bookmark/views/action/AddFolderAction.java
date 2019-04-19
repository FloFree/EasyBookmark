package bookmark.views.action;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import bookmark.utils.ValidationUtils;
import bookmark.views.TreeObject;
import bookmark.views.TreeParent;

public class AddFolderAction extends BookmarkAction {

	public AddFolderAction(TreeViewer viewer) {
		super(viewer);
		init();
	}

	private void init() {
		this.setText("Add folder here");
		this.setToolTipText("Add folder here");
		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER));
	}

	public void run() {
		String parentName;
		// create an input dialog to get user input
		String dialogTitle = "Input";
		String dialogMessage = "Please enter folder name:";
		String initialValue = "";
		InputDialog dlg = new InputDialog(null, dialogTitle, dialogMessage, initialValue, ValidationUtils.getIInputValidatorInstance());
		dlg.open();
		if (dlg.getReturnCode() != Window.OK) {
			return;
		} else {
			parentName = dlg.getValue();
		}

		// new a folder
		TreeParent newParent = new TreeParent(parentName);
		// get invisible root
		TreeParent invisibleRoot = (TreeParent) viewer.getInput();

		// get selection
		ISelection selection = viewer.getSelection();
		Object obj = ((IStructuredSelection) selection).getFirstElement();
		if (obj == null) {
			// no selection, default to add to the invisibleRoot
			invisibleRoot.addChild(newParent);
		} else {
			invisibleRoot.addChild((TreeObject) obj, newParent);
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
