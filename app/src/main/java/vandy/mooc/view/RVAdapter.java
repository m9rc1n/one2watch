package vandy.mooc.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.util.ArrayList;

import vandy.mooc.R;
import vandy.mooc.model.aidl.TrailerData;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {

    private final ImageLoader mImageLoader;
    ArrayList<TrailerData> mTrailers;

    public RVAdapter(ArrayList<TrailerData> trailers, Context context) {
        mTrailers = trailers;
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, int i) {
        final TrailerData data = mTrailers.get(i);
        mImageLoader.displayImage(data.getThumb().getSmall(),
                personViewHolder.personPhoto,
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

                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
        personViewHolder.personName.setText(data.getMovie().getTitle());
        personViewHolder.personAge.setText(data.getMovie().getPlot());
        personViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personViewHolder.video.setVideoURI(Uri.parse(data.getEmbed().getHtml5().get360p()));
                personViewHolder.video.start();
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
        ImageView personPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            personName = (TextView) itemView.findViewById(R.id.person_name);
            personAge = (TextView) itemView.findViewById(R.id.person_age);
            personPhoto = (ImageView) itemView.findViewById(R.id.person_photo);
            video = (VideoView) itemView.findViewById(R.id.videoView);
        }
    }

}