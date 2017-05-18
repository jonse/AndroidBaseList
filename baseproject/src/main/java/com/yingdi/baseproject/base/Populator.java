package com.yingdi.baseproject.base;

import android.view.View;
import android.view.ViewGroup;

/**
 * Implements this interface and registers it into a customableListAdapter object 
 * if you want to use the CustomableListAdapter and do the view population by yourself.
 * 
 * @author aaronguo
 */
public interface Populator {

	/**
	 * Populate data item view in list.
	 * 
	 * @param position	The position of the item within the adapter's data set of the item whose view we want.
	 * @param convertView		The old view to reuse, if possible.
	 * @param parent			The parent that this view will eventually be attached to
	 * @param item				The data item associated with the specified position in the data set.
	 * @return					A View corresponding to the data at the specified position.
	 */
	View populate(int position, View convertView, ViewGroup parent, Object item);
}
