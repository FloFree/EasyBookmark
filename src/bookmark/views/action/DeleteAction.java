package bookmark.views.action;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import bookmark.views.TreeObject;
import bookmark.views.TreeParent;

public class DeleteAction extends BookmarkAction {

	public DeleteAction(TreeViewer viewer) {
		super(viewer);
		init();
	}

	private void init() {
		this.setText("Delete");
		this.setToolTipText("Delete selected folder or bookmark.");
		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
	}

	public void run() {
		// get invisibleRoot
		TreeParent invisibleRoot = (TreeParent) viewer.getInput();

		// get selection
		ISelection selection = viewer.getSelection();
		Object obj = ((IStructuredSelection) selection).getFirstElement();
		if (obj == null) {
			this.showMessage("No selection in Bookmark Easy.");
		} else {
			TreeObject target = (TreeObject) obj;
			// confirm dialog
			String title = "Confirm";
			String question = "Do you really want to delelte this node?";
			boolean answer = MessageDialog.openConfirm(null, title, question);
			if (answer) {
				invisibleRoot.removeSelectedChild(target);
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

}
