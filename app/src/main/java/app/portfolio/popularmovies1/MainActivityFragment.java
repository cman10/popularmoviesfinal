package app.portfolio.popularmovies1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener {
    ImageAdapter myCustomArrayAdapter;
    GridView gv;
    String fetchurl;
    float[] ratingdet;
    String[] posterUrl;
    String[] Datedetail;
    ImageView imageView;
    String[] moviedetail;
    String[] titledetail;
    AppBarLayout movie;
    List<String> urllist = new ArrayList<String>();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootview = inflater.inflate(R.layout.fragment_main, container, false);
        gv = (GridView) rootview.findViewById(R.id.grid_view);
        gv.setOnItemClickListener(this);
        setHasOptionsMenu(true);
        myCustomArrayAdapter = new ImageAdapter(getActivity(), urllist);
        gv.setAdapter(myCustomArrayAdapter);
        return rootview;

    }
    boolean CheckConnection()
    {
        ConnectivityManager cm =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

  return true;  }



    @Override
    public void onStart() {


        new fetchposter().execute("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=b2131e0b7ca718dd5b831c5076e66d5c");
        super.onStart();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        Intent movieintent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);

        String movieOverviewdata = moviedetail[i];
        String titledata = titledetail[i];
        Float rating = ratingdet[i];
        String date = Datedetail[i];
        String finalurl = posterUrl[i];
        movieintent.putExtra("Movie_overview", movieOverviewdata);
        movieintent.putExtra("Movie_Title", titledata);
        movieintent.putExtra("Movie_rating", rating.toString());
        movieintent.putExtra("Release_Date", date);
        movieintent.putExtra("fetchurl", finalurl);

        startActivity(movieintent);
    }

    public class fetchposter extends AsyncTask<String, Void, List<String>> {

        private final String log_tag = fetchposter.class.getSimpleName();

        @Override
        protected List<String> doInBackground(String... params) {
            String imgurl = "http://image.tmdb.org/t/p/w500";
            HttpURLConnection urlConnection = null;
            String jsonstr = null;
            String sortBy = params[0];
            URL url = null;

            try {
                url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by="+sortBy+"&api_key=b2131e0b7ca718dd5b831c5076e66d5c");
               // http://api.themoviedb.org/3/discover/movie?sort_by=top_rated.desc&api_key=b2131e0b7ca718dd5b831c5076e66d5c

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputstream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputstream));
                StringBuffer buffer = new StringBuffer();
                if (inputstream == null) {
                    return null;
                }

                String line;
                while ((line = br.readLine()) != null) {

                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    // Stream was empty.
                    return null;
                }
                jsonstr = buffer.toString();
                JSONObject obj = new JSONObject(jsonstr);
                JSONArray jarray = obj.getJSONArray("results");
                titledetail = new String[jarray.length()];
                moviedetail = new String[jarray.length()];
                ratingdet = new float[jarray.length()];
                Datedetail = new String[jarray.length()];
                posterUrl = new String[jarray.length()];
                List<String> strings = new ArrayList<String>();

                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject object = jarray.getJSONObject(i);
                    String posterPath = object.getString("poster_path");
                    fetchurl = imgurl + posterPath;
                    strings.add(fetchurl);
                    Log.v(log_tag, "poster_path" + posterPath.toString());
                    moviedetail[i] = object.getString("overview");
                    Datedetail[i] = object.getString("release_date");
                    posterUrl[i] = fetchurl;
                    titledetail[i] = object.getString("title");
                    ratingdet[i] = Float.valueOf(object.getString("vote_average"));
                }
                Log.v(log_tag, "url list size" + strings.size());
                return strings;


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(List<String> strings) {
            myCustomArrayAdapter.clear();

            super.onPostExecute(strings);
            myCustomArrayAdapter.addAll(strings);
            Log.v(log_tag, "String list size" + strings.size());
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sort_popular, menu);
        inflater.inflate(R.menu.sort_top_rated, menu);

        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_popular:
                new fetchposter().execute("popularity.desc");

return true;

            case  R.id.sort_top:{
                new fetchposter().execute("top_rated.desc");
return true;


            }
            default:new fetchposter().execute("popularity.desc");
                return super.onOptionsItemSelected(item);
        }
    }
}


