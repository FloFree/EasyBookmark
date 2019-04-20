package bookmark.views.action;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import bookmark.constant.Constant;
import bookmark.views.Mark;
import bookmark.views.TreeObject;
import bookmark.views.TreeParent;

public class CutAction extends BookmarkAction {

	private Mark marker;

	public CutAction(TreeViewer viewer, Mark marker) {
		super(viewer);
		this.marker = marker;
		init();
	}

	private void init() {
		this.setText("Cut");
		this.setToolTipText("Cut folder");
		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
	}

	public void run() {
		ISelection selection = viewer.getSelection();
		Object obj = ((IStructuredSelection) selection).getFirstElement();

		if (obj != null) {
			TreeObject treeObject = (TreeObject) obj;
			if (treeObject.getFlag() == Constant.PARENT) {
				this.marker.setNode(treeObject);
				this.marker.setMode(Boolean.FALSE);
			}
		} else {
			showMessage("Select node in bookmark view");
			return;
		}
	}

}
