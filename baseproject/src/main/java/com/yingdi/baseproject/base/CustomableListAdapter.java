package com.yingdi.baseproject.base;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * An adapter can be added multiple data items.
 *
 * @author aaronguo
 */
public abstract class CustomableListAdapter<ListItemType> extends BaseAdapter {

    private MyFilter myFilter;
    private ArrayList<ListItemType> mItems = new ArrayList<>();
    private ArrayList<ListItemType> copyItems = new ArrayList<>();
    private Populator mPopulator;

    public CustomableListAdapter(Populator populator) {
        mPopulator = populator;
    }

    public CustomableListAdapter(List<ListItemType> items, Populator populator) {
        if (items == null) {
            return;
        }
        for (ListItemType o : items) {
            mItems.add(o);
        }
        copyItems.addAll(mItems);
        mPopulator = populator;
    }

    public void setPopulator(Populator populator) {
        mPopulator = populator;
    }

    /**
     * Replace the data of adapter with specified items and refresh the list.
     */
    public void update(List<ListItemType> items) {
        mItems.clear();
        add(items);
        notifyDataSetChanged();
    }

    public void update(int position, ListItemType item) {
        mItems.add(position, item);
        notifyDataSetChanged();
    }

    /**
     * Replace the data of adapter with specified items and refresh the list.
     */
    public void update(ListItemType[] items) {
        mItems.clear();
        add(items);
        notifyDataSetChanged();
    }

    public void add(ListItemType[] items) {
        for (ListItemType item : items) {
            add(item);
        }
    }

    public void add(List<ListItemType> items) {
        for (ListItemType item : items) {
            add(item);
        }
    }

    public void add(int index, ListItemType item) {
        if (!mItems.contains(item)) {
            mItems.add(index, item);
        }
    }

    public void add(int index, List<ListItemType> items) {
        for (int i = items.size() - 1; i >= 0; i--) {
            add(index, items.get(i));
        }
    }

    public void add(int index, ListItemType[] items) {
        for (int i = items.length - 1; i >= 0; i--) {
            add(index, items[i]);
        }
    }

    public void add(ListItemType item) {
        if (!mItems.contains(item)) {
            mItems.add(item);
        }
        if (!copyItems.contains(item)) {
            copyItems.add(item);
        }
    }

    public void remove(Object item) {
        mItems.remove(item);
    }

    public void remove(int position) {
        mItems.remove(position);
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public ArrayList<ListItemType> getItems() {
        return mItems;
    }

    /**
     * How many items are in the data set represented by this Adapter including
     * headers and footers.
     */
    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= mItems.size()) {
            return null;
        }
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mPopulator == null) {
            return null;
        }
        return mPopulator.populate(position, convertView, parent, getItem(position));
    }

    //	@Override
//	public boolean isEmpty() {
//		return false;
//	}
    public Filter getFilter() {
        if (myFilter == null) {
            myFilter = new MyFilter(mItems);
        }
        return myFilter;
    }

    protected class MyFilter extends Filter {
        List<ListItemType> mOriginalList = null;

        public MyFilter(List<ListItemType> myList) {
            this.mOriginalList = myList;
        }

        @Override
        protected synchronized FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (mOriginalList == null) {
                mOriginalList = new ArrayList<>();
            }
            mOriginalList = copyItems;
            if (prefix == null || prefix.length() == 0) {
                results.values = copyItems;
                results.count = copyItems.size();
            } else {
                String prefixString = prefix.toString();
                final int count = mOriginalList.size();
                final ArrayList<Object> newValues = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    final ListItemType user = mOriginalList.get(i);
                    String username = getKey(i);
                    String secondKey = getSecondKey(i);
                    if ((!TextUtils.isEmpty(username) && username.contains(prefixString)) || (!TextUtils.isEmpty(secondKey) && secondKey.contains(prefixString))) {
                        newValues.add(user);
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected synchronized void publishResults(CharSequence constraint,
                                                   FilterResults results) {
            notifyDataChanged((List<ListItemType>) results.values);
        }
    }

    public abstract String getKey(int i);

    public abstract String getSecondKey(int i);

    public abstract void notifyDataChanged(List<ListItemType> datas);
}
