package com.example.testapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import model.InfoModel;

public class MemoRecyclerAdapter extends RecyclerView.Adapter<MemoRecyclerAdapter.MemoViewHolder> {

    Context context;
    List<InfoModel> memos;

    public MemoRecyclerAdapter(Context context, List<InfoModel> memos) {
        this.context = context;
        this.memos = memos;
    }

    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_recycler_memo_item, parent, false);
        return new MemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder holder, int position) {
        InfoModel memo = memos.get(position);
        holder.memoTitle.setText(memo.getTitle());
        holder.memoContent.setText(memo.getContent());
        holder.itemView.setOnClickListener(holder);
    }

    @Override
    public int getItemCount() {
        return memos.size();
    }

    public class MemoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView memoTitle;
        TextView memoContent;

        public MemoViewHolder(@NonNull View itemView) {
            super(itemView);
            memoTitle = itemView.findViewById(R.id.memo_title_txt);
            memoContent = itemView.findViewById(R.id.memo_content_txt);

        }

        @Override
        public void onClick(View v) {
            sendData();
        }

        private void sendData() {


            Intent intent = new Intent(context, AddMemoActivity.class);
            intent.putExtra(AddMemoActivity.TITLE, memoTitle.getText().toString());
            intent.putExtra(AddMemoActivity.CONTENT, memoContent.getText().toString());
            context.startActivity(intent);

        }
    }
}
