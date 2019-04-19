package bookmark.views.action;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import bookmark.views.Mark;
import bookmark.views.TreeObject;
import bookmark.views.TreeParent;

public class PasteAction extends BookmarkAction {

	private Mark marker;

	public PasteAction(TreeViewer viewer, Mark marker) {
		super(viewer);
		this.marker = marker;
		init();
	}

	private void init() {
		this.setText("Paste");
		this.setToolTipText("Paste folder here");
		this.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
	}

	public void run() {
		if (this.marker.getNode() != null && this.marker.getMode() != null) {
			TreeObject node = this.marker.getNode();
			TreeParent parent = node.getParent();

			// get invisible root
			TreeParent invisibleRoot = (TreeParent) viewer.getInput();

			// get selection
			ISelection selection = viewer.getSelection();
			Object obj = ((IStructuredSelection) selection).getFirstElement();
			if (obj != null) {
				parent.removeChild(node);
				invisibleRoot.addChild((TreeObject) obj, node);
				this.marker.resetMark();
			} else {
				showMessage("Select node in bookmark view");
				return;
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
		} else {
			showMessage("No node setted");
			return;
		}
	}

}
