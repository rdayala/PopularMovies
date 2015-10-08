package com.example.android.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by rdayala on 10/8/2015.
 */
public class GridViewAdapter extends ArrayAdapter<MovieDataItem> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<MovieDataItem> mGridData = new ArrayList<MovieDataItem>();

    public GridViewAdapter(Context mContext, int layoutResourceId, ArrayList<MovieDataItem> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }


    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<MovieDataItem> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) row.findViewById(R.id.movie_poster_grid_item);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        MovieDataItem movieDataItem = mGridData.get(position);
        Picasso.with(mContext).load(movieDataItem.getMoviePosterPathUrl()).into(holder.imageView);
        return row;
    }

    // Using ViewHolder pattern
    static class ViewHolder {
        ImageView imageView;
    }
}