package com.zkrkj.peoplehospital.registered;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zkrkj.peoplehospital.R;
import com.zkrkj.peoplehospital.User.AddUserActivity;
import com.zkrkj.peoplehospital.login.LoginActivity;

import java.util.List;
import java.util.Map;

import base.BaseActivity;
import base.OptsharepreInterface;
import butterknife.Bind;
import butterknife.ButterKnife;
import myinterface.IMyItemClick;
import util.Constants;
import util.JsonUtils;
import util.TitleBarUtils;
import util.ToastUtil;
import widget.ProgressDialogStyle;
import widget.SelePatientWindow;

/**
 * Describe:     预约挂号页
 * User:         LF
 * Date:         2016/3/23 15:28
 */
public class RegisteredDetail extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_hospital)
    TextView tvHospital;//医院
    @Bind(R.id.tv_department)
    TextView tvDepartment;//科室
    @Bind(R.id.tv_doc_date)
    TextView tvDocDate;//就诊日期
    @Bind(R.id.tv_outpatient_type)
    TextView tvOutpatientType;//门诊类型
    @Bind(R.id.tv_price)
    TextView tvPrice;//门诊价格
    @Bind(R.id.tv_name)
    TextView tvName;//就诊人
    @Bind(R.id.tv_sex)
    TextView tvSex;//性别
    @Bind(R.id.tv_phone)
    TextView tvPhone;//手机号
    @Bind(R.id.tv_idcard)
    TextView tvIdcard;//身份证号
    @Bind(R.id.iv_head)
    ImageView ivHead;//头像
    @Bind(R.id.btn_submit)
    Button btnSubmit;//预约
    @Bind(R.id.ll_change_patient)
    LinearLayout llChangePatient;

    RequestQueue queue;
    @Bind(R.id.ll_main)
    LinearLayout llMain;
    private Dialog pb;
    private OptsharepreInterface share;

    Map<String, Object> patientObj = null;
    Map<String, Object> object = null;
    private List<Map<String, Object>> mLists;//所有就诊人列表
    SelePatientWindow popupwindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_detail);
        ButterKnife.bind(this);
        share = new OptsharepreInterface(this);
        init();
    }


    private void init() {
        initTitle();
        initPatient();
        initListener();
    }

    /**
     * Describe:     初始化当前就诊人信息
     * User:         LF
     * Date:         2016/4/6 16:29
     */
    private void initPatient() {
        pb = ProgressDialogStyle.createLoadingDialog(this, "请求中...");
        pb.show();
        queue = Volley.newRequestQueue(this);
        String url = Constants.SERVER_ADDRESS + "patient/" + share.getPres("id") + "?token=" + share.getPres("token");
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadPatient(response);
                pb.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.ToastShow(RegisteredDetail.this, Constants.VOLLEY_ERROR, true);
                pb.dismiss();
            }
        });
        queue.add(request);
    }

    /**
     * Describe:     格式化就诊人信息
     * User:         LF
     * Date:         2016/4/6 11:06
     */
    private void loadPatient(String json) {
        String success = "";
        try {
            patientObj = JsonUtils.getMapObj(json);
            success = patientObj.get("success").toString();
            if (success.equals("0")) {
                ToastUtil.ToastShow(this, patientObj.get("msg").toString(), true);
            } else if (success.equals("1")) {
                if (TextUtils.isEmpty(patientObj.get("data").toString())) {//未设置我的就诊人
                    ToastUtil.ToastShow(this, "未添加就诊人", true);
                    Intent intent = new Intent(this, AddUserActivity.class);
                    startActivity(intent);
                } else {
                    patientObj = JsonUtils.getMapObj(patientObj.get("data").toString());
                    setPatientData();
                }
            } else {
                ToastUtil.ToastShow(this, "登录过期", true);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
        }
    }

    private void setPatientData() {
        //名字
        if (TextUtils.isEmpty(patientObj.get("name").toString())) {
            tvName.setText("无");
        } else {
            tvName.setText(patientObj.get("name").toString());
        }
        //性别
        if (TextUtils.isEmpty(patientObj.get("gender").toString())) {
            tvSex.setText("无");
        } else {
            tvSex.setText(patientObj.get("gender").toString());
        }
        //身份证号
        if (TextUtils.isEmpty(patientObj.get("idNo").toString())) {
            tvIdcard.setText("无");
        } else {
            tvIdcard.setText(patientObj.get("idNo").toString());
        }
        //手机号
        if (TextUtils.isEmpty(patientObj.get("phone").toString())) {
            tvPhone.setText("无");
        } else {
            tvPhone.setText(patientObj.get("phone").toString());
        }
    }

    private void initListener() {
        btnSubmit.setOnClickListener(this);
        llChangePatient.setOnClickListener(this);
    }

    private void initTitle() {
        TitleBarUtils titleBarUtils = (TitleBarUtils) findViewById(R.id.titleBar);
        titleBarUtils.setTitle("预约挂号");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit://下一步：预约
                Intent intent = new Intent(this, RegisteredSuccess.class);
                startActivity(intent);
                break;
            case R.id.ll_change_patient://切换就诊人
                getAllPatient();
                break;
        }
    }

    /**
     * Describe:     获取所有就诊人信息
     * User:         LF
     * Date:         2016/4/6 17:26
     */
    private void getAllPatient() {
        pb = ProgressDialogStyle.createLoadingDialog(this, "请求中...");
        pb.show();
        queue = Volley.newRequestQueue(this);
        String url = Constants.SERVER_ADDRESS + "patients?token=" + share.getPres("token");
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadAllPatient(response);
                pb.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.ToastShow(RegisteredDetail.this, Constants.VOLLEY_ERROR, true);
                pb.dismiss();
            }
        });
        queue.add(request);
    }

    /**
     * Describe:     格式化所有就诊人信息并弹窗选择
     * User:         LF
     * Date:         2016/4/6 11:06
     */
    private void loadAllPatient(String json) {
        String success = "";
        try {
            object = JsonUtils.getMapObj(json);
            success = object.get("success").toString();
            if (success.equals("0")) {
                ToastUtil.ToastShow(this, object.get("msg").toString(), true);
            } else if (success.equals("1")) {
                if (TextUtils.isEmpty(object.get("data").toString())) {//未设置我的就诊人
                    ToastUtil.ToastShow(this, "未添加就诊人", true);
                    Intent intent = new Intent(this, AddUserActivity.class);
                    startActivity(intent);
                } else {
                    mLists = JsonUtils.getListMap(object.get("data").toString());
                    popupwindow = new SelePatientWindow(this, mLists);
                    popupwindow.showAtLocation(llMain,Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);
                    popupwindow.setiMyItemClick(new IMyItemClick() {
                        @Override
                        public void myItemOnClick(int position) {
                            patientObj=mLists.get(position);
                            setPatientData();
                            popupwindow.dismiss();
                        }
                    });
                }
            } else {
                ToastUtil.ToastShow(this, "登录过期", true);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            Log.e(Constants.TAG,e.getMessage());
        }
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initAction() {

    }
}
