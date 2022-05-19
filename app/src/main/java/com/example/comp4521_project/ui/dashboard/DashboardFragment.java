package com.example.comp4521_project.ui.dashboard;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.comp4521_project.R;
import com.example.comp4521_project.databinding.FragmentDashboardBinding;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements View.OnClickListener{

    private FragmentDashboardBinding binding;
    private Fragment dashboardFragment;

    private ListView commentList;
    private LinearLayout rlEnroll;
    private ImageView comment;
    private ImageView chat;
    private RelativeLayout rlComment;
    private TextView hideDown;
    private EditText commentContent;
    private Button commentSend;
    private List<Comment> data;
    private AdapterComment adapterComment;

    private String temp_name = "N/A";
    private LatLng temp_location = new LatLng(0, 0);
    private String temp_type = "N/A";

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
        adapterComment = new AdapterComment(getActivity(),data);
        // 为我们的评论设置我们的适配器
        commentList.setAdapter(adapterComment);

        rlEnroll = myView.findViewById(R.id.rl_enroll);
        comment = myView.findViewById(R.id.comment);
        chat = myView.findViewById(R.id.chat);
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
        hideDown.setOnClickListener(this);
        commentSend.setOnClickListener(this);
        rlEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Clicked post",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            // 设置我们的输入法
            case R.id.comment:
                clickOnInputField(temp_name,temp_location,temp_type);
                break;
            // 隐藏我们的评论框
            case R.id.hide_down:
                rlEnroll.setVisibility(View.VISIBLE);
                rlComment.setVisibility(View.GONE);
                // 隐藏我们的输入法，然后的话暂存在我们当前的输入框中方便下一次的使用
                InputMethodManager im = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(commentContent.getWindowToken(),0);
                break;
            case R.id.comment_send:
                // 然后在我们的这个位置的话设置一个发送评论的方法
                sendComment();
                break;
            default:
                break;

        }
    }

    public void clickOnInputField(String name, LatLng location, String type) {
        // 然后的话在我们的这个位置弹出我们的输入法
        temp_name = name;
        temp_location = location;
        temp_type = type;
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        commentContent.requestFocus();
        // 显示我们的评论框
        rlEnroll.setVisibility(View.GONE);
        rlComment.setVisibility(View.VISIBLE);
    }

    private void sendComment() {
        if(commentContent.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Comment cannot be empty",Toast.LENGTH_LONG).show();
        }else{
            // 然后的话这个的话就是生成评论的数
            Comment comment = new Comment();
            String strLatitude = String.format("%.2f", temp_location.latitude);
            String strLongitude = String.format("%.2f", temp_location.longitude);
            comment.setName("Name: " + temp_name +  "\nType: " + temp_type + "\nFrom:" + "(" + strLatitude + ", " + strLongitude + ")");
            comment.setContent(commentContent.getText().toString());
            // 然后的话就是适配我们的数据------todo 这里的话有部分的数据没有编写
            adapterComment.addComment(comment);
            commentContent.setText("");

            // 然后的话就是使用我们的toast的话弹出我们的成功或是失败的评论
            Toast.makeText(getActivity(),"Comment send",Toast.LENGTH_LONG).show();
        }
    }
}