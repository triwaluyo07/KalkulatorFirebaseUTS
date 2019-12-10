package com.example.kalkulator_uts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>
{
    private List<Hitstory> HistoryList ;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Adapter(List<Hitstory> HistoryList)
    {
        this.HistoryList = HistoryList;
    }
    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, final int position)
    {
        final Hitstory history = HistoryList.get(position);
        holder.textResult.setText(history.getHistory());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                return deleteData(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return HistoryList.size();
    }

    public boolean deleteData(int position)
    {
        db.collection("KalkulatorApik").document(String.valueOf(getItemId(position))).delete();
        return (true);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textResult;

        ViewHolder(View itemView)
        {
            super(itemView);
            textResult = itemView.findViewById(R.id.hasil);
        }

    }
}
