package com.iplug.automation.holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iplug.automation.R;

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    final public View rootView;
    final public TextView tvTitle;


    public HeaderViewHolder(@NonNull final View view) {
        super(view);

        rootView = view;
        tvTitle = view.findViewById(R.id.header_title);

    }
}