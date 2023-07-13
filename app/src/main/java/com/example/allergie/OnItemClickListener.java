package com.example.allergie;

import android.view.View;

import org.json.JSONException;

public interface OnItemClickListener {
    void onItemClick(ShowNameAdapter.ViewHolder viewHolder, View view, int position) throws JSONException;
}