package bookmark.views.action;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;

import bookmark.constant.Constant;
import bookmark.utils.ValidationUtils;
import bookmark.views.TreeObject;
import bookmark.views.TreeParent;

public class RenameAction extends BookmarkAction {

	public RenameAction(TreeViewer viewer) {
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
			if (treeObject.getFlag() == Constant.PARENT) {

				String parentName = treeObject.getName();
				// create an input dialog to get user input
				String dialogTitle = "Input";
				String dialogMessage = "Please enter new folder name:";
				InputDialog dlg = new InputDialog(null, dialogTitle, dialogMessage, parentName, ValidationUtils.getIInputValidatorInstance());
				dlg.open();
				if (dlg.getReturnCode() != Window.OK) {
					return;
				} else {
					parentName = dlg.getValue();
				}

				treeObject.setName(parentName);
			}
		}

		TreeParent invisibleRoot = (TreeParent) viewer.getInput();
		viewer.setInput(invisibleRoot);
		this.savePersistantData(invisibleRoot);
	}

}
