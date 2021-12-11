package com.devchie.videomaker.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.devchie.videomaker.R;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.viewHolder> {

    Context context;
  public  static   ArrayList<VideoModel > videoArrayList;
    public OnItemClickListener onItemClickListener;

    public VideoAdapter(Context context, ArrayList<VideoModel > videoArrayList) {
        this.context = context;
        this.videoArrayList = videoArrayList;
    }

    @Override
    public VideoAdapter.viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_list, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VideoAdapter.viewHolder holder, @SuppressLint("RecyclerView") final int i) {
        holder.title.setText(videoArrayList.get(i).getVideoTitle());
        holder.duration.setText(videoArrayList.get(i).getVideoDuration());
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoArrayList.get(i).getVideoPic(), MediaStore.Video.Thumbnails.MICRO_KIND);
        Log.v("kjasdhsd","1"+videoArrayList.get(i).getVideoPic());
        holder.imagePreview.setImageBitmap(bitmap);
        Log.e("uri",videoArrayList.get(i).getVideoUri()+"");

        holder.video_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code here
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_TEXT,videoArrayList.get(i).getVideoUri());
//                sendIntent.setType("text/plain");
//                context.startActivity(Intent.createChooser(sendIntent, "Share link!"));

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_SUBJECT,"Demo Video");
                //intent.setAction(Intent.ACTION_SEND);
                intent.setType("video/mp4");
                intent.putExtra(Intent.EXTRA_STREAM, videoArrayList.get(i).getVideoUri());
               // intent.putExtra(Intent.EXTRA_TEXT, "Watch this video to get a good complement from your friends.");
                try {
                    context.startActivity(Intent.createChooser(intent,"Upload video via:"));

                } catch (android.content.ActivityNotFoundException ex) {

                }



            }
        });

        holder.vedio_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, OrderListById.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
               // Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
               VideoListActivity.Dialog(videoArrayList,i);
              //  removeAt(i);
            }
        });

    }

//    public void removeAt(int position) {
//        videoArrayList.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, videoArrayList.size());
//
//    }

    @Override
    public int getItemCount() {
        return videoArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title, duration;
        ImageView video_share,vedio_delete;
        ImageView imagePreview;
        public viewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            duration = (TextView) itemView.findViewById(R.id.duration);
            imagePreview = (ImageView) itemView.findViewById(R.id.imagePreview);
            video_share = itemView.findViewById(R.id.video_share);
            vedio_delete = itemView.findViewById(R.id.vedio_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });
        }

    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }
}