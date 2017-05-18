package com.yingdi.baseproject.base;

import android.content.Context;

public interface ViewHolderFactory<T> {

	ViewHolder<T> createViewHolder(Context context, int position);
}
