package com.garyhu.rcvdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： garyhu.
 * 时间： 2017/3/1.
 */

public class SecondActivity extends AppCompatActivity {

    private FoldExpandableListView explistview;
    private String[][] childrenData = new String[10][10];
    private String[] groupData = new String[10];
    private int expandFlag = -1;//控制列表的展开
    private PinnedHeaderExpandableAdapter adapter;

    private static final int UPDATE = 0x02;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATE:
                    initMView();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
//        ButterKnife.bind(this);
//        initView();
        initData();
    }

    /**
     * 初始化VIEW
     */
    private void initView() {
        explistview = (FoldExpandableListView)findViewById(R.id.explistview);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<10;i++){
                    groupData[i] = "分组"+i;
                }

                for(int i=0;i<10;i++){
                    for(int j=0;j<10;j++){
                        childrenData[i][j] = "好友"+i+"-"+j;
                    }
                }
                handler.sendEmptyMessageDelayed(UPDATE,2000);
            }
        }).start();
//        for(int i=0;i<10;i++){
//            groupData[i] = "分组"+i;
//        }
//
//        for(int i=0;i<10;i++){
//            for(int j=0;j<10;j++){
//                childrenData[i][j] = "好友"+i+"-"+j;
//            }
//        }
//        initMView();
    }

    public void initMView(){
        initView();
        //设置悬浮头部VIEW
        explistview.setHeaderView(getLayoutInflater().inflate(R.layout.group_head,
                explistview, false));
        adapter = new PinnedHeaderExpandableAdapter(childrenData, groupData, getApplicationContext(),explistview);
        Log.d("garyhu","adapter == "+adapter);
        explistview.setAdapter(adapter);

        //设置单个分组展开
        //explistview.setOnGroupClickListener(new GroupClickListener());
    }

    class GroupClickListener implements ExpandableListView.OnGroupClickListener {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v,
                                    int groupPosition, long id) {
            if (expandFlag == -1) {
                // 展开被选的group
                explistview.expandGroup(groupPosition);
                // 设置被选中的group置于顶端
                explistview.setSelectedGroup(groupPosition);
                expandFlag = groupPosition;
            } else if (expandFlag == groupPosition) {
                explistview.collapseGroup(expandFlag);
                expandFlag = -1;
            } else {
                explistview.collapseGroup(expandFlag);
                // 展开被选的group
                explistview.expandGroup(groupPosition);
                // 设置被选中的group置于顶端
                explistview.setSelectedGroup(groupPosition);
                expandFlag = groupPosition;
            }
            return true;
        }
    }

}
