package com.example.comp4521_project.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comp4521_project.R;
import com.example.comp4521_project.data.model.AllBlogModel;
import com.example.comp4521_project.data.model.ProfileModel;
import com.example.comp4521_project.databinding.FragmentDashboardBinding;
import com.example.comp4521_project.helper.CookieGetRequest;
import com.example.comp4521_project.helper.CookiePostRequest;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment implements View.OnClickListener{

    private FragmentDashboardBinding binding;
    private Fragment dashboardFragment;

    private ListView commentList;
    private LinearLayout rlEnroll;
    private ImageView comment;
    private ImageView refresh;
    private RelativeLayout rlComment;
    private TextView hideDown;
    private EditText commentContent;
    private Button commentSend;
    private List<Comment> data;
    private AdapterComment adapterComment;

    private String temp_name = "N/A";
    private LatLng temp_location = new LatLng(0, 0);
    private String temp_str_location = "(0, 0)";
    private String temp_type = "N/A";
    Gson gson = new Gson();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        DashboardViewModel dashboardViewModel =
//                new ViewModelProvider(this).get(DashboardViewModel.class);
//
//        binding = FragmentDashboardBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        final TextView textView = binding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        return root;
        View myView = inflater.inflate(R.layout.fragment_dashboard, container,false);
        commentList = (ListView) myView.findViewById(R.id.comment_list);
        // todo 初始化数据
        data = new ArrayList<>();
        // 然后的话在我们的这个位置的话初始化我们的数据适配器
        adapterComment = new AdapterComment(this, getActivity(), data);
        // 为我们的评论设置我们的适配器
        commentList.setAdapter(adapterComment);

        rlEnroll = myView.findViewById(R.id.rl_enroll);
        comment = myView.findViewById(R.id.comment);
        refresh = myView.findViewById(R.id.refresh);
        rlComment = myView.findViewById(R.id.rl_comment);
        hideDown = myView.findViewById(R.id.hide_down);
        commentContent = myView.findViewById(R.id.comment_content);
        commentSend = myView.findViewById(R.id.comment_send);
        setListener();
        return myView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dashboardFragment = getChildFragmentManager().findFragmentById(R.id.map);
        try {
            refreshPage();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    private void initView() {
//        // 然后的话就是设置我们的id todo 在我们的这个位置的话初始化评论列表
//        commentList = (ListView) findViewById(R.id.comment_list);
//        // todo 初始化数据
//        data = new ArrayList<>();
//        // 然后的话在我们的这个位置的话初始化我们的数据适配器
//        adapterComment = new AdapterComment(getActivity(),data);
//        // 为我们的评论设置我们的适配器
//        commentList.setAdapter(adapterComment);
//
//        rlEnroll = (LinearLayout) findViewById(R.id.rl_enroll);
//        comment = (ImageView) findViewById(R.id.comment);
//        chat = (ImageView) findViewById(R.id.chat);
//        rlComment = (RelativeLayout) findViewById(R.id.rl_comment);
//        hideDown = (TextView) findViewById(R.id.hide_down);
//        commentContent = (EditText) findViewById(R.id.comment_content);
//        commentSend = (Button) findViewById(R.id.comment_send);
//        setListener();
//    }

    public void setListener(){
        comment.setOnClickListener(this);
        refresh.setOnClickListener(this);
        hideDown.setOnClickListener(this);
        commentSend.setOnClickListener(this);
        rlEnroll.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.comment:
                SharedPreferences share = getActivity().getSharedPreferences("myshare", Context.MODE_PRIVATE);
                temp_name = share.getString("username", null);
                clickOnInputField(temp_name,temp_location,temp_type, "");
                break;
            case R.id.hide_down:
                rlEnroll.setVisibility(View.VISIBLE);
                rlComment.setVisibility(View.GONE);
                // 隐藏我们的输入法，然后的话暂存在我们当前的输入框中方便下一次的使用
                InputMethodManager im = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(commentContent.getWindowToken(),0);
                break;
            case R.id.comment_send:
                sendCommentWithRequest();
                break;
            case R.id.refresh:
                try {
                    adapterComment.data.clear();
                    refreshPage();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;

        }
    }

    public void clickOnInputField(String name, LatLng location, String type, String setText) {
        // 然后的话在我们的这个位置弹出我们的输入法
        temp_name = name;
        temp_location = location;
        temp_type = type;
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        commentContent.setText(setText);
        commentContent.requestFocus();
        // 显示我们的评论框
        rlEnroll.setVisibility(View.GONE);
        rlComment.setVisibility(View.VISIBLE);
    }

    private void sendCommentWithRequest() {
        if(commentContent.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Comment cannot be empty",Toast.LENGTH_LONG).show();
        }else{
            String inputText = commentContent.getText().toString();
            Comment comment = new Comment();
            if (temp_location != null) {
                String strLatitude = String.format("%.2f", temp_location.latitude);
                String strLongitude = String.format("%.2f", temp_location.longitude);
                temp_str_location = "(" + strLatitude + ", " + strLongitude + ")";
            }
            comment.setName(temp_name);
            comment.setType(temp_type);
            comment.setLocation(temp_str_location);
            comment.setContent(inputText);
            adapterComment.addComment(comment);
            commentContent.setText("");

            String int_type = "0";
            switch(temp_type) {
                case "Help": int_type = "1"; break;
                case "Notification" : int_type = "2"; break;
                case "Warning" : int_type = "3"; break;
                case "Comment": int_type = "4"; break;
            }

            String url = "http://125.59.138.87:8090/api/blog/writeOwnBlog";
            Map<String, String> params = new HashMap<>();
            params.put("blog_info", inputText);
            params.put("blog_location", temp_str_location);
            params.put("blog_type", int_type);
            // will send request on create
            CookiePostRequest cookiePostRequest = new CookiePostRequest(getActivity(), url, params);

            // 然后的话就是适配我们的数据------todo 这里的话有部分的数据没有编写
//            adapterComment.addComment(comment);
//            commentContent.setText("");
            // 然后的话就是使用我们的toast的话弹出我们的成功或是失败的评论
            Toast.makeText(getActivity(),"Comment send",Toast.LENGTH_LONG).show();

            temp_location = new LatLng(0, 0);
            temp_str_location = "(0, 0)";
            temp_type = "";
        }
    }

    private void refreshPage() throws JSONException {
        List<Comment> commentList = adapterComment.data;
        String url = "http://125.59.138.87:8090/api/blog/readPublicBlog";
        Map<String, String> params = new HashMap<>();
        CookieGetRequest cookieGetRequest = new CookieGetRequest(getActivity(), url);
        cookieGetRequest.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.isEmpty())
                    return;
                AllBlogModel.Response allblogmodel = gson.fromJson(s, AllBlogModel.Response.class);
                Log.i("Get_All test 1st blog", allblogmodel.payload.content[0].blog_info);
                for (int i = 0; i < allblogmodel.payload.item_count; i++) {
                    //String name, Location loc, String type, String content
                    String name = allblogmodel.payload.content[i].author_name;
                    String loc = allblogmodel.payload.content[i].blog_location;
                    String type = "";
                    switch (allblogmodel.payload.content[i].blog_type) {
                        case 0: type = "N/A"; break;
                        case 1: type = "Help"; break;
                        case 2: type = "Notification"; break;
                        case 3: type = "Warning"; break;
                        case 4: type = "Comment"; break;
                    };
                    String content = allblogmodel.payload.content[i].blog_info;
                    int id = allblogmodel.payload.content[i].bid;
                    Comment temp_comment = new Comment(name, loc, type, content, id);
                    adapterComment.addComment(temp_comment);
                }
            }
        });

        //Log.i("All blooooooooogs", JSONResponse.getString("message"));
    }
}