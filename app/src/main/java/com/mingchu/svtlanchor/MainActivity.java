package com.mingchu.svtlanchor;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private TabLayout tab_tagContainer;
    private ScrollChangedScrollView sv_bodyContainer;
    private LinearLayout ll_1;
    private LinearLayout ll_2;
    private LinearLayout ll_3;
    private LinearLayout ll_4;
    private LinearLayout ll_5;
    private LinearLayout ll_6;
    private LinearLayout ll_7;

    private AppBarLayout toolbar_layout;

//    private XRecyclerView mRvOne;

    private RecyclerView mRvThree;

    private WebView mWebView;

    private MyAdapter mMyAdapter;

    private FlyBanner mFlyBanner;

    private List<String> datas = new ArrayList<>();

    //图片轮播资源
    private String[] bannerImages = {"http://image.xinliji.me/o_1bep35vtl1q5m1ucb1ghcftfh2n.png"
            , "http://image.xinliji.me/o_1bb34rj3bdg35g11ia91fuo1v4ti.png",
            "http://image.xinliji.me/o_1b2up2c5p1mmvvr3b7o1sp91lb8i.png",
            "http://image.xinliji.me/o_1b2vq62nu1gqbp125g1slo1fgqn.png"
    };

    // 头部导航标签
    private String[] navigationTag = {"试用反馈", "产品介绍", "品鉴规则", "推荐菜谱", "品鉴申请", "试用者名单", "全部评论"};
    /**
     * 是否是ScrollView主动动作
     * false:是ScrollView主动动作
     * true:是TabLayout 主动动作
     */
    private boolean tagFlag = false;
    /**
     * 用于切换内容模块，相应的改变导航标签，表示当一个所处的位置
     */
    private int lastTagIndex = 0;
    /**
     * 用于在同一个内容模块内滑动，锁定导航标签，防止重复刷新标签
     * true: 锁定
     * false ; 没有锁定
     */
    private boolean content2NavigateFlagInnerLock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeView();
        refreshView();
        initListener();
        initData();
    }

    private void initData() {

        List<String> mImages = new ArrayList<>();
        for (String image : bannerImages) {
            mImages.add(image);
        }

        mFlyBanner.setImagesUrl(mImages);


    }

    boolean isExpand = false;

    public void initializeView() {
        tab_tagContainer = (TabLayout) findViewById(R.id.anchor_tagContainer);
        sv_bodyContainer = (ScrollChangedScrollView) findViewById(R.id.anchor_bodyContainer);

        ll_1 = (LinearLayout) findViewById(R.id.ll_1);
        ll_2 = (LinearLayout) findViewById(R.id.ll_2);
        ll_3 = (LinearLayout) findViewById(R.id.ll_3);
        ll_4 = (LinearLayout) findViewById(R.id.ll_4);
        ll_5 = (LinearLayout) findViewById(R.id.ll_5);
        ll_6 = (LinearLayout) findViewById(R.id.ll_6);
        ll_7 = (LinearLayout) findViewById(R.id.ll_7);

        mFlyBanner = (FlyBanner) findViewById(R.id.fly_banner);
        mWebView = (WebView) findViewById(R.id.web_view);

        toolbar_layout = (AppBarLayout) findViewById(R.id.app_bar);
//
//        toolbar_layout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
//            @Override
//            public void onStateChanged(AppBarLayout appBarLayout, State state, int position) {
//
//            }
//        });

        LinearLayoutManager layoutThree = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };


//        mRvOne.setLayoutManager(layoutOne);


        mMyAdapter = new MyAdapter();
//        mRvOne.setAdapter(mMyAdapter);

        mRvThree = (RecyclerView) findViewById(R.id.recycler_three);
        mRvThree.setLayoutManager(layoutThree);
        mMyAdapter = new MyAdapter();
        mRvThree.setAdapter(mMyAdapter);


        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(false);
        mWebView.loadUrl("http://webapp.greatchef.com.cn/newsview?id=1994");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


    }

    public void refreshView() {
        // 添加页内导航标签
        for (String item : navigationTag) {
            tab_tagContainer.addTab(tab_tagContainer.newTab().setText(item));
        }
    }

    public void initListener() {
        sv_bodyContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //表明当前的动作是由 ScrollView 触发和主导
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tagFlag = true;
                }
                return false;
            }
        });
        sv_bodyContainer.setScrollViewListener(new ScrollChangedScrollView.ScrollViewListener() {

            @Override
            public void onScrollChanged(ScrollChangedScrollView scrollView, int x, int y, int oldx, int oldy) {
                scrollRefreshNavigationTag(scrollView);
            }

            @Override
            public void onScrollStop(boolean isStop) {
            }
        });

        tab_tagContainer.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //表明当前的动作是由 TabLayout 触发和主导
                tagFlag = false;
                // 根据点击的位置，使ScrollView 滑动到对应区域
                int position = tab.getPosition();
                // 计算点击的导航标签所对应内容区域的高度
                int targetY = 0;
                switch (position) {

                    case 0:
                        targetY = ll_1.getTop();
                        break;
                    case 1:
                        targetY = ll_2.getTop();
                        break;
                    case 2:
                        targetY = ll_3.getTop();
                        break;
                    case 3:
                        targetY = ll_4.getTop();
                        break;
                    case 4:
                        targetY = ll_5.getTop();
                        break;
                    case 5:
                        targetY = ll_6.getTop();
                        break;
                    case 6:
                        targetY = ll_7.getTop();
                        break;


                    default:
                        break;
                }
                // 移动到对应的内容区域
                sv_bodyContainer.smoothScrollTo(0, targetY + 5);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    /**
     * 内容区域滑动刷新导航标签
     *
     * @param scrollView 内容模块容器
     */
    private void scrollRefreshNavigationTag(MyNetScrollview scrollView) {
        if (scrollView == null) {
            return;
        }
        // 获得ScrollView滑动距离
        int scrollY = scrollView.getScrollY();
        // 确定ScrollView当前展示的顶部内容属于哪个内容模块
        if (scrollY > ll_7.getTop()) {
            refreshContent2NavigationFlag(6);

        } else if (scrollY > ll_6.getTop()) {
            refreshContent2NavigationFlag(5);

        } else if (scrollY > ll_5.getTop()) {
            refreshContent2NavigationFlag(4);
        } else if (scrollY > ll_4.getTop()) {
            refreshContent2NavigationFlag(3);

        } else if (scrollY > ll_3.getTop()) {
            refreshContent2NavigationFlag(2);

        } else if (scrollY > ll_2.getTop()) {
            refreshContent2NavigationFlag(1);

        } else if (scrollY > ll_1.getTop()) {
            refreshContent2NavigationFlag(0);

        } else {
            refreshContent2NavigationFlag(0);
        }
    }

    /**
     * 刷新标签
     *
     * @param currentTagIndex 当前模块位置
     */
    private void refreshContent2NavigationFlag(int currentTagIndex) {
        // 上一个位置与当前位置不一致是，解锁内部锁，是导航可以发生变化
        if (lastTagIndex != currentTagIndex) {
            content2NavigateFlagInnerLock = false;
        }
        if (!content2NavigateFlagInnerLock) {
            // 锁定内部锁
            content2NavigateFlagInnerLock = true;
            // 动作是由ScrollView触发主导的情况下，导航标签才可以滚动选中
            if (tagFlag) {
                tab_tagContainer.setScrollPosition(currentTagIndex, 0, true);
            }
        }
        lastTagIndex = currentTagIndex;
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.ll_recommend_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return bannerImages.length;
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            public LinearLayout ll_recommend_one;

            public ViewHolder(View itemView) {
                super(itemView);

                ll_recommend_one = (LinearLayout) itemView.findViewById(R.id.ll_recommend_one);
            }
        }
    }
}