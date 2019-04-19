package bookmark.views.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.google.gson.Gson;

import bookmark.constant.Constant;
import bookmark.views.TreeParent;

public class BookmarkAction extends Action {

	protected TreeViewer viewer;

	public BookmarkAction(TreeViewer viewer) {
		this.viewer = viewer;
	}

	protected void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), "Bookmark Easy", message);
	}

	/**
	 * Use eclipse Preferences API to make data persistent
	 *
	 * @param dataSource
	 */
	protected void savePersistantData(TreeParent dataSource) {
		Preferences prefs = InstanceScope.INSTANCE.getNode(Constant.ID);

		// change object to string
		Gson gson = new Gson();

		// change object byte array
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o;
		try {
			o = new ObjectOutputStream(b);
			o.writeObject(dataSource);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		byte[] byteDataArray = b.toByteArray();

		// use gson to change byte array to string
		String json_str = gson.toJson(byteDataArray);

		prefs.put(Constant.DATA_STORE_KEY, json_str);
		try {
			// store to disk
			prefs.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

}
