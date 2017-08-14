package com.trust.shengyu.calltaxidriver.activitys;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trust.shengyu.calltaxidriver.R;
import com.trust.shengyu.calltaxidriver.base.BaseActivity;
import com.trust.shengyu.calltaxidriver.base.BaseRecyclerViewAdapter;
import com.trust.shengyu.calltaxidriver.tools.L;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @BindView(R.id.main_recycler_show_unread_read_num)
    TextView mainRecyclerShowUnreadReadNum;
    @BindView(R.id.main_recycler_show_unread_read)
    LinearLayout mainRecyclerShowUnreadRead;
    private MainRecyclerAdapter mainRecyclerAdapter;
    private Context context = MainActivity.this;
    @BindView(R.id.main_recycler)
    RecyclerView mainRecycler;
    private ArrayList<Object> ml;
    private int lastPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        initData();
    }


    private void initView() {
        Toolbar toolbar = (Toolbar) bindView(this,R.id.main_base_include,R.id.base_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        mainRecyclerAdapter = new MainRecyclerAdapter(context);
        mainRecyclerAdapter.setItemOnClickListener(new BaseRecyclerViewAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClickListener(View v, int pos, Object msg) {
                L.d("你点击了xxx:" + pos);
            }
        });
        mainRecyclerAdapter.setOnSubimtListener(new MainRecyclerAdapter.onSubimtListener() {
            @Override
            public void resultSubmit(View v, int pos, Object object) {
                L.d("点击了提交订单:"+pos);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mainRecycler.setLayoutManager(layoutManager);
        mainRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //得到当前显示的最后一个item的view
                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
                //通过这个lastChildView得到这个view当前的position值

                    lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);

                L.d("当前显示最后一个item pos:" + lastPosition);
                if (lastPosition == ml.size() - 1) {
                    L.d("数据源最后一个");

                    mainRecyclerShowUnreadReadNum.setText(0+"");
                } else {
                    int num = ml.size() - 1 - lastPosition;
                    mainRecyclerShowUnreadReadNum.setText(num+"");
                }
            }
        });
        mainRecycler.setAdapter(mainRecyclerAdapter);
    }

    private void initData() {
        ml = new ArrayList<>();
        ml.add("2");
        ml.add("3");
        ml.add("1");
        ml.add("2");
        ml.add("3");
        ml.add("2");
        ml.add("3");
        ml.add("1");
        ml.add("2");
        ml.add("3");
        ml.add("2");
        ml.add("3");
        ml.add("1");
        ml.add("2");
        ml.add("3");
        ml.add("2");
        ml.add("3");
        ml.add("1");
        ml.add("2");
        ml.add("3");
        ml.add("2");
        ml.add("3");
        ml.add("1");
        ml.add("2");
        ml.add("3");
        ml.add("2");
        ml.add("3");
        ml.add("1");
        ml.add("2");
        ml.add("3");
        ml.add("2");
        ml.add("3");
        ml.add("1");
        ml.add("2");
        ml.add("3");
        ml.add("2");
        ml.add("3");
        ml.add("1");
        ml.add("2");
        ml.add("3");
        ml.add("2");
        ml.add("3");
        ml.add("1");
        ml.add("2");
        ml.add("3");
        ml.add("2");
        ml.add("3");
        ml.add("1");
        ml.add("2");
        ml.add("3");
        mainRecyclerAdapter.setMl(ml);
        mainRecyclerAdapter.notifyDataSetChanged();
        hand.sendEmptyMessageDelayed(1, 1000 * 5);
        mainRecyclerShowUnreadRead.setVisibility(View.VISIBLE);

    }

    Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            ml.add("3");
//            mainRecycler.smoothScrollToPosition(ml.size()-1);
            mainRecyclerAdapter.setMl(ml);
            mainRecyclerAdapter.notifyDataSetChanged();
            hand.sendEmptyMessageDelayed(1, 1000 * 5);

            int num = ml.size() - 1 - lastPosition;
            mainRecyclerShowUnreadReadNum.setText(num+"");
            L.d("有多少条未读:" + num + "|当前是第:" + lastPosition);
        }
    };

}
