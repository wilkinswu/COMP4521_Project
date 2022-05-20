package com.example.comp4521_project.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp4521_project.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AdapterComment extends BaseAdapter {
    // 然后的话就是在我们的这个位置的话创建我们的get和我们的set方法
    DashboardFragment dashboardFragment;
    Context context;
    List<Comment> data;
    private ViewHolder holder;

    // 然后的话就是设置我们的适配器
    public AdapterComment(DashboardFragment d, Context c, List<Comment> data){
        dashboardFragment = d;
        this.context = c;
        this.data = data;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        // 在我们的这个位置的话就是设置我们的viewholder
        if(convertView == null){
            holder = new ViewHolder();
            // 然后的话就是将我们的xml布局文件转换为我们的可视化文件
            convertView = LayoutInflater.from(context).inflate(R.layout.item_comment,null);
            // 这个位置的话就是找到我们的id
            holder.commentName = convertView.findViewById(R.id.comment_name);
            holder.commentContent = convertView.findViewById(R.id.comment_content);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        // 数据的适配
        holder.commentContent.setText(data.get(i).getContent());
        //holder.commentName.setText();

        if (data.get(i).getLocation().startsWith("(") && data.get(i).getLocation().charAt(1) != '0'){
            Location loc = StrToLocation(data.get(i).getLocation());
            Geocoder geocoder = new Geocoder(dashboardFragment.getActivity(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String area = addresses.get(0).getAdminArea();
            if (area == null){
                holder.commentName.setText("Name: " + data.get(i).getName() +  "\n" +
                        "Type: " + data.get(i).getType() + "\n" +
                        "From: N/A");
            }
            else {
                holder.commentName.setText("Name: " + data.get(i).getName() +  "\n" +
                        "Type: " + data.get(i).getType() + "\n" +
                        "From: " + area);
            }

        }
        else if (data.get(i).getLocation().startsWith("(0")) {
            holder.commentName.setText("Name: " + data.get(i).getName() +  "\n" +
                    "Type: " + data.get(i).getType() + "\n" +
                    "From: N/A");
        }
        else {
            holder.commentName.setText("Name: " + data.get(i).getName() +  "\n" +
                    "Type: " + data.get(i).getType() + "\n" +
                    "From:" + data.get(i).getLocation());
        }


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // working!!!!!!!!!!!!
                SharedPreferences share = context.getSharedPreferences("myshare", Context.MODE_PRIVATE);
                String myName = share.getString("username", null);
                String postAuthor = data.get(i).getName();
                dashboardFragment.clickOnInputField(myName,
                        new LatLng(0, 0),
                        "Comment",
                        "@"+postAuthor+": ");
                //Toast.makeText(context, data.get(i).getContent(),Toast.LENGTH_LONG).show();
            }
        });
        return convertView;
    }
    public void addComment(Comment comment){
        data.add(comment);
        notifyDataSetChanged();
    }

    //将我们要声明的控件放在我们的viewholder类中,这个的话是我们的静态类方便我们的垃圾回收
    public static class ViewHolder{
        TextView commentName;
        TextView commentContent;
    }

    private Location StrToLocation(String str) {
        String[] arg = str.split(",");
        String a = arg[0].trim().replaceAll("\\(", "");
        String b = arg[1].trim().replaceAll("\\)", "");
        Location l = new Location("");
        l.setLatitude(Double.parseDouble(a));
        l.setLongitude(Double.parseDouble(b));
        return l;
    }
}

