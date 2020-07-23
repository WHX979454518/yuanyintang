package com.mxkj.yuanyintang.mainui.search;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.database.DBManager;

import java.util.List;

import static com.mxkj.yuanyintang.mainui.home.fragment.HomeFragment.SEARCH_WORDS;

public class SearchHisAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> searchList;

    public SearchHisAdapter(Context mContext, List<String> searchList) {
        this.mContext = mContext;
        this.searchList = searchList;
    }

    @Override
    public int getCount() {

        if (searchList.size() < 8) {
            return searchList.size();
        }
        return 8;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Nullable
    @Override
    public View getView(final int position, @Nullable View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_search_history, null);
            holder.img_delete = (ImageView) convertView.findViewById(R.id.img_delete);
            holder.tv_searchName = (TextView) convertView.findViewById(R.id.tv_search_name);
            holder.rl_search_history = (RelativeLayout) convertView.findViewById(R.id.rl_search_history);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (searchList != null && searchList.size() > 0 && searchList.size() > position) {
            holder.tv_searchName.setText(searchList.get(position));
        }
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBManager manager = new DBManager(mContext);
                if (searchList.size() > position) {
                    manager.delSrchHistrory(searchList.get(position));
                    searchList.remove(position);
                    notifyDataSetChanged();
                }
            }
        });
        holder.rl_search_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SearchResultActivity.class);
                intent.putExtra(SEARCH_WORDS, searchList.get(position));
                mContext.startActivity(intent);
            }
        });


        return convertView;
    }

    class ViewHolder {
        ImageView img_delete;
        TextView tv_searchName;
        RelativeLayout rl_search_history;
    }
}
