package qf.com.day29homework;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SlidingMenu sMenu;

    Button btn1,btn2,btn3,btnTop,btnBottom;

    PullToRefreshListView ptrflv;
    List<String> datas;
    ArrayAdapter<String> adapter;
    Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        initView();
        initData();
        adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.layout,datas);
        ImageView imageView=new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.cc);
        ptrflv.getRefreshableView().addHeaderView(imageView);

        ptrflv.setMode(PullToRefreshBase.Mode.BOTH);
        ptrflv.setAdapter(adapter);

        ptrflv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        datas.add(0,"辟邪剑谱:"+new Date());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                ptrflv.onRefreshComplete();
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //在上啦加载回调方法里面添加老数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("fcr","onUpTo");
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //添加老数据
                        datas.add("六脉神剑"+new Date());//add默认添加至尾部
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();//通知数据发生改变
                                ptrflv.onRefreshComplete();//刷新完成
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private void initData() {
        datas=new ArrayList<>();
        for (int i = 0; i <100 ; i++) {
            datas.add("葵花宝典第"+i+"式");

        }
    }

    private void initView() {
        sMenu=new SlidingMenu(this);
        sMenu.setMode(SlidingMenu.LEFT);
        sMenu.setMenu(R.layout.my_left);
        sMenu.setBehindWidth((int) (getResources().getDisplayMetrics().widthPixels*0.4f));

        sMenu.attachToActivity(this,SlidingMenu.TOUCHMODE_FULLSCREEN);

        ptrflv= (PullToRefreshListView) findViewById(R.id.ptrlvId);

        btn1= (Button) sMenu.findViewById(R.id.btn1Id);
        btn2= (Button) sMenu.findViewById(R.id.btn2Id);
        btn3= (Button) sMenu.findViewById(R.id.btn3Id);
        btnTop= (Button) sMenu.findViewById(R.id.topBtnId);
        btnBottom= (Button) sMenu.findViewById(R.id.bottomBtnId);


        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btnTop.setOnClickListener(this);
        btnBottom.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1Id:
                if (sMenu.isMenuShowing()){
                    sMenu.toggle();
                    //ptrflv.offsetLeftAndRight(100);
                    //ptrflv.offsetTopAndBottom(-1000);
                    //ptrflv.scrollTo(16,1000);
                    ptrflv.getRefreshableView().smoothScrollToPosition(30);
                }
                break;
            case R.id.btn2Id:
                sMenu.toggle();
                ptrflv.getRefreshableView().smoothScrollToPosition(60);
                break;
            case R.id.btn3Id:
                sMenu.toggle();
                ptrflv.getRefreshableView().smoothScrollToPosition(99);
                break;
            case R.id.topBtnId:
                sMenu.toggle();
                ptrflv.getRefreshableView().smoothScrollToPosition(0);
                break;
            case R.id.bottomBtnId:
                sMenu.toggle();
                ptrflv.getRefreshableView().smoothScrollToPosition(99);
                break;

        }
    }
}
