package io.github.marcinn.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.util.ArrayList;

import io.github.marcinn.R;
import io.github.marcinn.model.aidl.TrailerData;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {

    public static final String GOOGLE_SEARCH = "https://www.google.no/search?q=";
    private final ImageLoader mImageLoader;
    private final Resources mRes;
    private final Context mContext;
    ArrayList<TrailerData> mTrailers;

    public RVAdapter(ArrayList<TrailerData> trailers, Context context) {
        mTrailers = trailers;
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        mRes = context.getResources();
        mContext = context;
    }

    @Override
    public void onViewDetachedFromWindow(PersonViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, int i) {
        final TrailerData data = mTrailers.get(i);
        mImageLoader.loadImage(data.getThumb().getSmall(),
                DisplayImageOptions.createSimple(),
                new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        //                        personViewHolder.video.setBackground(new BitmapDrawable(mRes, loadedImage));
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                    }
                });
        personViewHolder.personName.setText(data.getMovie().getTitle());
        personViewHolder.personAge.setText(data.getMovie().getPlot());
        personViewHolder.video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                personViewHolder.play.setVisibility(View.VISIBLE);
            }
        });
        personViewHolder.video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                personViewHolder.play.setVisibility(View.VISIBLE);
                return false;
            }
        });
        personViewHolder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personViewHolder.video.setVideoURI(Uri.parse(data.getEmbed().getHtml5().get360p()));
                MediaController controller = new MediaController(mContext);
                controller.setAnchorView(personViewHolder.video);
                personViewHolder.video.setMediaController(controller);
                personViewHolder.video.requestFocus();
                v.setVisibility(View.GONE);
            }
        });
        personViewHolder.google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.ACTION_RUN_BROWSER);
                intent.putExtra(MainActivity.EXTRA_URL, GOOGLE_SEARCH + data.getMovie().getTitle());
                mContext.sendBroadcast(intent);
            }
        });
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trailer_card, viewGroup, false);
        return new PersonViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        VideoView video;
        CardView cv;
        TextView personName;
        TextView personAge;
        ImageView play;
        ImageView google;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            personName = (TextView) itemView.findViewById(R.id.person_name);
            personAge = (TextView) itemView.findViewById(R.id.person_age);
            video = (VideoView) itemView.findViewById(R.id.videoView);
            play = (ImageView) itemView.findViewById(R.id.imageView);
            google = (ImageView) itemView.findViewById(R.id.imageView2);
            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    video.start();
                }
            });
        }
    }

}