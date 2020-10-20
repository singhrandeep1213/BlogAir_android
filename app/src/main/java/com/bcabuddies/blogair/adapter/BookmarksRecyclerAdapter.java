package com.bcabuddies.blogair.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bcabuddies.blogair.R;
import com.bcabuddies.blogair.model.Bookmarks;
import com.bumptech.glide.Glide;

import java.util.List;

public class BookmarksRecyclerAdapter extends  RecyclerView.Adapter<BookmarksRecyclerAdapter.bookmarksViewHolder> {

    Context context;
    List <Bookmarks.bookmarks> bookmarksList;
    private static final String TAG = "BookmarksRecyclerAdapte";

    public BookmarksRecyclerAdapter(Context context, List<Bookmarks.bookmarks> bookmarksList) {
        this.context = context;
        this.bookmarksList = bookmarksList;
    }

    @NonNull
    @Override
    public bookmarksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        return new bookmarksViewHolder((inflater.inflate(R.layout.bookmarks_row,parent,false)));
    }

    @Override
    public void onBindViewHolder(@NonNull bookmarksViewHolder holder, int position) {
        try{
            String postImageUrl= bookmarksList.get(position).getPost_image();
            String pid = bookmarksList.get(position).getPid();
            String postUid= bookmarksList.get(position).getUid();

            try{
                Glide.with(context).load(postImageUrl).into(holder.postImageView);
            }catch (Exception e){
                Log.e(TAG, "onBindViewHolder: failedto load image:  "+e.getMessage() );
            }
        }
        catch (Exception e){
            Log.e(TAG, "onBindViewHolder: exception in try:  " );
        }
    }

    @Override
    public int getItemCount() {
        return bookmarksList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class  bookmarksViewHolder extends RecyclerView.ViewHolder{

        ImageView postImageView;

        public bookmarksViewHolder(@NonNull View itemView) {
            super(itemView);
            postImageView=itemView.findViewById(R.id.bookmarks_row_image);
        }
    }

}
