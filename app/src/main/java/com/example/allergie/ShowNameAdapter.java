package com.example.allergie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;

public class ShowNameAdapter extends RecyclerView.Adapter<ShowNameAdapter.ViewHolder> implements OnItemClickListener {

    private OnItemClickListener listener;   // 리스너 객체 추가
    private ArrayList<NameForAdapterList> myDataList = new ArrayList<>();

    void addItem(NameForAdapterList item){
        myDataList.add(item);
    }
    NameForAdapterList getItem(int position) { return myDataList.get(position); }   // 아이템 리턴하는 함수 추가

    void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount()
    {
        return myDataList.size();
    }
    @Override
    public void onItemClick(ViewHolder viewHolder, View view, int position) throws JSONException {
        if (listener != null) {
            listener.onItemClick(viewHolder, view, position);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.food, parent, false);


        return new ViewHolder(itemView, (OnItemClickListener) this);    // 이 부분 수정
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position)
    {

        viewHolder.setItem(myDataList.get(position));
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        TextView textView_name;
        ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            textView_name = itemView.findViewById(R.id.textView_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        try {
                            listener.onItemClick(ViewHolder.this, view, getAdapterPosition());
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }
        void setItem(NameForAdapterList item) {
            textView.setText(item.getFoodName());
            textView_name.setText(item.getTest());
        }

    }


}