package com.zkrkj.peoplehospital.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zkrkj.peoplehospital.R;
import com.zkrkj.peoplehospital.User.ChangePasswordActivity;
import com.zkrkj.peoplehospital.User.FeedBackActivity;
import com.zkrkj.peoplehospital.User.MyDocCard;
import com.zkrkj.peoplehospital.User.MyUserActivity;
import com.zkrkj.peoplehospital.User.PersonalDetail;
import com.zkrkj.peoplehospital.User.UnreadMessagesActivity;
import com.zkrkj.peoplehospital.activity.MainActivity;

import base.BaseFragment;
import base.OptsharepreInterface;
import butterknife.Bind;
import butterknife.ButterKnife;
import util.TitleBarUtils;
import util.ToastUtil;

/**
 * Created by lenovo on 2016/3/16.
 */

public class Frag_User extends BaseFragment implements View.OnClickListener {


    @Bind(R.id.titleBar)
    TitleBarUtils titleBar;
    @Bind(R.id.imageView4)
    ImageView imageView4;
    @Bind(R.id.username_text)
    TextView usernameText;
    @Bind(R.id.textView5)
    TextView textView5;
    @Bind(R.id.textView8)
    TextView textView8;
    @Bind(R.id.textView9)
    TextView textView9;
    @Bind(R.id.dangan)
    LinearLayout dangan;
    @Bind(R.id.yuyue)
    LinearLayout yuyue;
    @Bind(R.id.func)
    LinearLayout func;
    @Bind(R.id.yijian)
    LinearLayout yijian;
    @Bind(R.id.about)
    LinearLayout about;
    @Bind(R.id.update)
    LinearLayout update;
    @Bind(R.id.xiugaimima)
    LinearLayout xiugaimima;
    @Bind(R.id.resiglogin)
    LinearLayout resiglogin;
    @Bind(R.id.jiuyika)
    LinearLayout jiuyika;
    @Bind(R.id.tab_message)
    RelativeLayout tabMessage;
    @Bind(R.id.wodejiuzhenren)
    RelativeLayout wodejiuzhenren;
    private Intent intent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_user, null);

        ButterKnife.bind(this, view);
        initView();
        initTitle();
        return view;
    }

    private void initTitle() {
        TitleBarUtils titleBarUtils = (TitleBarUtils) view.findViewById(R.id.titleBar);
        titleBarUtils.setTitle("个人中心");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        o=new OptsharepreInterface(getActivity());
        textView9.setText(o.getPres("total"));
    }

    @Override
    protected void initView() {

        xiugaimima.setOnClickListener(this);

        jiuyika.setOnClickListener(this);

        usernameText.setOnClickListener(this);
        resiglogin.setOnClickListener(this);
        dangan.setOnClickListener(this);
        tabMessage.setOnClickListener(this);
        yijian.setOnClickListener(this);
        wodejiuzhenren.setOnClickListener(this);
    }

    @Override
    protected void initAction() {

    }

    @Override
    public int getViewId() {
        return 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.username_text://个人信息
                intent = new Intent(getActivity(), PersonalDetail.class);
                startActivity(intent);
                break;
            case R.id.dangan:
                ToastUtil.ToastShow(getActivity(), "点击了健康档案", true);
                // Toast.makeText(getActivity(),"点击了健康档案",Toast.LENGTH_SHORT).show();
                break;
            case R.id.yuyue:
                Toast.makeText(getActivity(), "点击了退出登录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setaccount:
                Toast.makeText(getActivity(), "点击了退出登录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.wodejiuzhenren:
                intent = new Intent(getActivity(), MyUserActivity.class);
                startActivity(intent);
                break;
            case R.id.func:
                Toast.makeText(getActivity(), "点击了退出登录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.yijian:
                intent = new Intent(getActivity(), FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.about:
                Toast.makeText(getActivity(), "点击了退出登录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.update:
                Toast.makeText(getActivity(), "点击了退出登录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.resiglogin:
                Toast.makeText(getActivity(), "点击了退出登录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.jiuyika://我的就医卡
                intent = new Intent(getActivity(), MyDocCard.class);
                startActivity(intent);
                break;
            case R.id.tab_message:
                intent = new Intent(getActivity(), UnreadMessagesActivity.class);
                startActivity(intent);
                break;
            case R.id.xiugaimima:
                intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
                break;
        }
    }
}
