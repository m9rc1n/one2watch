package io.github.marcinn.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import io.github.marcinn.R;
import io.github.marcinn.model.aidl.TrailerData;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    public static final String GOOGLE_SEARCH = "https://www.google.no/search?q=Movie ";
    private ArrayList<TrailerData> mTrailers;
    private VideoView mCurrentVideo;
    private SimpleDraweeView mCurrentThumb;
    private ImageView mCurrentPlay;
    private FrameLayout mVideoLayout;

    public TrailersAdapter(ArrayList<TrailerData> trailers) {
        mTrailers = trailers;
    }

    @Override
    public void onViewDetachedFromWindow(TrailerViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onBindViewHolder(final TrailerViewHolder vh, int i) {
        final TrailerData data = mTrailers.get(i);
        vh.thumbnail.setImageURI(Uri.parse((data.getThumb().getSmall())));
        vh.title.setText(data.getMovie().getTitle());
        vh.desc.setText(data.getMovie().getPlot());
        vh.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vh.play.setVisibility(View.INVISIBLE);
                vh.thumbnail.setVisibility(View.GONE);
                if (mCurrentVideo != null) {
                    mCurrentVideo.stopPlayback();
                    mCurrentThumb.setVisibility(View.VISIBLE);
                    mCurrentPlay.setVisibility(View.VISIBLE);
                }
                mCurrentVideo = vh.video;
                mCurrentPlay = vh.play;
                mCurrentThumb = vh.thumbnail;
                mCurrentVideo.setVideoURI(Uri.parse(data.getEmbed().getHtml5().get360p()));
                MediaController controller = new MediaController(v.getContext(), true);
                controller.setMediaPlayer(mCurrentVideo);
                mCurrentVideo.setMediaController(controller);
                mCurrentVideo.start();
            }
        });
        vh.google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.ACTION_RUN_BROWSER);
                intent.putExtra(MainActivity.EXTRA_URL, GOOGLE_SEARCH + data.getMovie().getTitle());
                v.getContext().sendBroadcast(intent);
            }
        });
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup g, int i) {
        View v = LayoutInflater.from(g.getContext()).inflate(R.layout.trailer_card, g, false);
        return new TrailerViewHolder(v);
    }

    @Override
    public void onViewRecycled(TrailerViewHolder holder) {
        super.onViewRecycled(holder);
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
        SimpleDraweeView thumbnail;
        CardView card;
        TextView title;
        TextView desc;
        ImageView play;
        ImageView google;
        FrameLayout videoContainer;
        VideoView video;

        TrailerViewHolder(View v) {
            super(v);
            card = (CardView) v.findViewById(R.id.card);
            title = (TextView) v.findViewById(R.id.title);
            desc = (TextView) v.findViewById(R.id.desc);
            play = (ImageView) v.findViewById(R.id.play);
            google = (ImageView) v.findViewById(R.id.google);
            thumbnail = (SimpleDraweeView) v.findViewById(R.id.thumbnail);
            videoContainer = (FrameLayout) v.findViewById(R.id.videoContainer);
            video = (VideoView) v.findViewById(R.id.video);
        }
    }

}