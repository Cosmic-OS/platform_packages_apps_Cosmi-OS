package org.os.cosmic_os;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<TeamData> mDataSource;
    private Context mContext;

    //Adapter Constructor
    TeamAdapter(Context context, ArrayList<TeamData> devItems)
    {
        mContext = context;
        mDataSource = devItems;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    static class ViewHolder  extends RecyclerView.ViewHolder{
        TextView nameTextView, descTextView;
        CircularImageView developerImage;
        ImageView github, thread, telegram;
        LinearLayout devPicBackground;

        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.dev_name);
            descTextView = itemView.findViewById(R.id.dev_disc);
            developerImage = itemView.findViewById(R.id.dev_image);
            github = itemView.findViewById(R.id.github);
            thread = itemView.findViewById(R.id.thread);
            telegram = itemView.findViewById(R.id.telegram);
            devPicBackground = itemView.findViewById(R.id.picture_bg);
        }
    }

    @Override
    public TeamAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.team_fragment_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TeamData teamData = getItem(position);

        holder.devPicBackground.setBackgroundColor(Color.parseColor(teamData.bgColor));
        holder.nameTextView.setText(teamData.developerName);
        holder.descTextView.setText(teamData.description);

        //Developer Links
        holder.github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent githubIntent = new Intent(Intent.ACTION_VIEW);
                githubIntent.setData(Uri.parse(teamData.github));
                mContext.startActivity(githubIntent);
            }
        });
        if (!Objects.equals(teamData.thread, "null")) {
            holder.thread.setVisibility(View.VISIBLE);
            holder.thread.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent threadIntent = new Intent(Intent.ACTION_VIEW);
                    threadIntent.setData(Uri.parse(teamData.thread));
                    mContext.startActivity(threadIntent);
                }
            });
        }
        else holder.thread.setVisibility(View.GONE);
        holder.telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telegramIntent = new Intent(Intent.ACTION_VIEW);
                telegramIntent.setData(Uri.parse(teamData.telegram));
                mContext.startActivity(telegramIntent);
            }
        });

        //Load developer image from source server
        Picasso.with(mContext).load(teamData.devImage)
                .placeholder(R.drawable.ic_cosmic_banner).into(holder.developerImage);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    //Get TeamData objects for each developer from ArrayList
    private TeamData getItem(int position){
        return mDataSource.get(position);
    }

}
