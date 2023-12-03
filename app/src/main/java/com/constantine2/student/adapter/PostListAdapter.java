package com.constantine2.student.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.constantine2.student.R;
import com.constantine2.student.Utils;
import com.constantine2.student.databinding.ItemPostBinding;
import com.constantine2.student.model.Post;
import com.constantine2.student.retrofit.RetrofitApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostViewHolder> {


    public interface OnItemClickListener {
        void onItemCLick(int position);
    }

    public interface OnLongClickListener {
        void onItemLongCLick(int position);
    }

    List<Post> postList;
    Context context;
    FireListAdapter.OnItemClickListener onItemClickListener;
    FireListAdapter.OnLongClickListener onLongClickListener;


    public PostListAdapter(Context context, FireListAdapter.OnItemClickListener onItemClickListener, FireListAdapter.OnLongClickListener onLongClickListener) {
        postList= new ArrayList<>();
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        this.onLongClickListener = onLongClickListener;
    }


    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new PostViewHolder(ItemPostBinding.inflate(inflater, parent, false), onItemClickListener, onLongClickListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);


        if (post.getrClient() != null)
            holder.bind.creatorName.setText(post.getrClient().getFirstName() + " " + post.getrClient().getLastName());
        else
            holder.bind.creatorName.setText(context.getString(R.string.unknow_creator));
        holder.bind.postContent.setText(post.getContent());
        holder.bind.postCreatedDate.setText(Utils.convertTimeStampToDate(post.getCreatedAt().getTime()));


        Picasso.get()
//                .load("http://localhost:8080/post/1644738966073.jpg")
                .load(RetrofitApi.getPostPath() + post.getPhotoFilePath())
                .into(holder.bind.firePicture);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
        notifyDataSetChanged();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ItemPostBinding bind;

        public PostViewHolder(@NonNull ItemPostBinding bind,
                              FireListAdapter.OnItemClickListener onItemClickListener,
                              FireListAdapter.OnLongClickListener onLongClickListener) {
            super(bind.getRoot());
            this.bind = bind;

            bind.getRoot().setOnClickListener(v -> {
                onItemClickListener.onItemCLick(getAdapterPosition());
            });
            bind.getRoot().setOnLongClickListener(v -> {
                onLongClickListener.onItemLongCLick(getAdapterPosition());
                return true;
            });
        }
    }
}
