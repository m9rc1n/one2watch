package io.github.marcinn.view;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import io.github.marcinn.R;
import io.github.marcinn.model.aidl.TrailerData;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    public static final String GOOGLE_SEARCH = "https://www.google.no/search?q=Movie ";
    private ArrayList<TrailerData> mTrailers;

    public TrailersAdapter(ArrayList<TrailerData> trailers) {
        mTrailers = trailers;
    }

    @Override
    public void onBindViewHolder(final TrailerViewHolder vh, int i) {
        final TrailerData data = mTrailers.get(i);
        vh.thumbnail.setVisibility(View.VISIBLE);
        vh.play.setVisibility(View.VISIBLE);
        vh.video.setVisibility(View.GONE);
        vh.progress.setVisibility(View.GONE);
        vh.video.stopPlayback();
        vh.thumbnail.setImageURI(Uri.parse((data.getThumb().getSmall())));
        vh.title.setText(data.getMovie().getTitle());
        vh.desc.setText(data.getMovie().getPlot());
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup g, int i) {
        View v = LayoutInflater.from(g.getContext()).inflate(R.layout.trailer_card, g, false);
        return new TrailerViewHolder(v, mTrailers);
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public void setTrailers(ArrayList<TrailerData> trailers) {
        this.mTrailers = trailers;
        notifyDataSetChanged();
    }

    public static class TrailerViewHolder extends RecyclerView.ViewHolder {
        ImageView sendButton;
        ProgressBar progress;
        SimpleDraweeView thumbnail;
        TextView title;
        TextView desc;
        ImageView play;
        ImageView google;
        VideoView video;

        TrailerViewHolder(View v, final ArrayList<TrailerData> trailers) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            desc = (TextView) v.findViewById(R.id.desc);
            play = (ImageView) v.findViewById(R.id.play);
            google = (ImageView) v.findViewById(R.id.google);
            thumbnail = (SimpleDraweeView) v.findViewById(R.id.thumbnail);
            video = (VideoView) v.findViewById(R.id.video);
            progress = (ProgressBar) v.findViewById(R.id.progress);
            sendButton = (ImageView) v.findViewById(R.id.fb_send_button);


            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    String url = trailers.get(getAdapterPosition()).getEmbed().getHtml5().get360p();
                    play.setVisibility(View.GONE);
                    thumbnail.setVisibility(View.GONE);
                    video.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.VISIBLE);
                    video.setVideoURI(Uri.parse(url));
                    MediaController controller = new MediaController(v.getContext(), true);
                    controller.setMediaPlayer(video);
                    controller.setAnchorView(video);
                    video.setMediaController(controller);
                    video.requestFocus();
                }
            });

            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    video.bringToFront();
                    video.start();
                    video.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                }
            });

            google.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = trailers.get(getAdapterPosition()).getMovie().getTitle();
                    Intent intent = new Intent(MainActivity.ACTION_RUN_BROWSER);
                    intent.putExtra(MainActivity.EXTRA_URL, GOOGLE_SEARCH + title);
                    v.getContext().sendBroadcast(intent);
                }
            });

            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = trailers.get(getAdapterPosition()).getEmbed().getHtml5().get360p();
                    Intent intent = new Intent(MainActivity.ACTION_SEND_TO_MESSENGER);
                    intent.putExtra(MainActivity.EXTRA_URL, url);
                    v.getContext().sendBroadcast(intent);
                }
            });
        }
    }

}