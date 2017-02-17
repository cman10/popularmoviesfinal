package app.portfolio.popularmovies1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailsFragment extends Fragment {

    String overview;
    String rating , title,date,url;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     View rootView=inflater.inflate(R.layout.fragment_movie_details,container,false);
        TextView textView= (TextView) rootView.findViewById(R.id.textView);
        TextView textView2= (TextView) rootView.findViewById(R.id.textView2);
        TextView textView3= (TextView) rootView.findViewById(R.id.textView3);
        TextView textView4= (TextView) rootView.findViewById(R.id.textView5);
        ImageView load_thumb= (ImageView) rootView.findViewById(R.id.imageView);
        load_thumb.setScaleType(ImageView.ScaleType.FIT_XY);
        Intent intent= getActivity().getIntent();
        if (intent!=null && intent.hasExtra("Movie_overview"))
        {
            overview=intent.getStringExtra("Movie_overview");
            textView.setText("\n"+overview);

        }
        if (intent!=null && intent.hasExtra("Movie_rating"))
        {
            rating=intent.getStringExtra("Movie_rating");
            textView2.setText("\n"+rating);

        }
        if (intent!=null && intent.hasExtra("Movie_Title"))
        {
            title=intent.getStringExtra("Movie_Title");
            textView3.setText(title);

        }
        if (intent!=null && intent.hasExtra("Release_Date"))
        {
            date=intent.getStringExtra("Release_Date");
            textView4.setText("\n"+date);

        }
        if (intent!=null && intent.hasExtra("fetchurl"))

        {
            url=intent.getStringExtra("fetchurl");
            Picasso.with(getContext()).load(url).into(load_thumb);

        }


        return rootView;
    }


    public MovieDetailsFragment(){

    }
}
