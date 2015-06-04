package com.example.miouground;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	AppSectionPagerAdapter mAppSectionPagerAdapter;
	ViewPager mViewPager;
	//static String url = "http://www.google.com/m/";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 使手機能監聽電話號碼
		SharedPreferences phonenumSP = getSharedPreferences("in_phone_num",
				Context.MODE_PRIVATE);

		// SwipeView
		mAppSectionPagerAdapter = new AppSectionPagerAdapter(
				getSupportFragmentManager());

		final ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);

		// 開啟Tab navigation mode
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionPagerAdapter);

		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {

						actionBar.setSelectedNavigationItem(position);
					}

				});

		for (int i = 0; i < mAppSectionPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mAppSectionPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public static class AppSectionPagerAdapter extends FragmentPagerAdapter {

		public AppSectionPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
			// 沒標註override，且講明是自動生成的，所以不用改寫
		}

		// / Step.3 在為生成的Fragment物件配置記憶體位置前，要先定義好Fragment的內容

		// / Step.4 取得分頁的內容
		// 可以寫switch或if等判斷式讓每個分頁有不同的內容
		@Override
		public Fragment getItem(int i) {
			// TODO Auto-generated method stub

			switch (i) {
/*			
			case 0:
				return new launchpadSectionFragment();
*/			
			case 0:
				return new MyWebViewFragment();	
			default:
				return new CallploiceFragment();
				/*
				 * default:
				 * 
				 * Fragment fragment = new DummySectionFragment(); //
				 * 建立好物件實體，但是裡面沒有內容物
				 * 
				 * Bundle args = new Bundle(); // 裝載內容物的容器
				 * args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
				 * // 容器添加東西進去 fragment.setArguments(args); // 把裝好東西的容器放進物件實體裡面去
				 * 
				 * return fragment;
				 */

			}

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "Section " + (position + 1);
		}

	}

	// App進來時，第一個Fragment分頁的內容，這裡要修改成內建WebView來取得拍照後的圖片
	public static class launchpadSectionFragment extends Fragment {
		// launchpad = launching pad → 發射台、出發點、跳板
		// launch 開始

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub

			View rootView = inflater.inflate(
					R.layout.fragment_section_launchpad, container, false);

			// Demonstration of a collection-browsing activity.
			rootView.findViewById(R.id.demo_collection_button)
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(getActivity(),
									CollectionActivity.class);
							startActivity(intent);
						}
					});

			// Demonstration of navigating to external activities.
			rootView.findViewById(R.id.demo_external_activity)
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							Intent externalActivityIntent = new Intent(
									Intent.ACTION_PICK);

							externalActivityIntent.setType("image/*"); // setType裡的格式是Uri的格式
							externalActivityIntent
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
							startActivity(externalActivityIntent);
						}
					});

			return rootView;
		}

	}

	// fragment::呼叫條子 (測試成功)
	public static class CallploiceFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			View rootView = inflater.inflate(
					R.layout.fragment_section_callpolice, container, false);

			rootView.findViewById(R.id.callButton).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String phoneNumber = "0987532368";
							Intent intentDial = new Intent(
									"android.intent.action.CALL", Uri
											.parse("tel:" + phoneNumber));
							startActivity(intentDial);
						}
					});

			return rootView;
		}

	}
	


	// 其他分頁的內容 (準備替換掉不用)
	public static class DummySectionFragment extends Fragment {

		public static final String ARG_SECTION_NUMBER = "section_number";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub

			// 從onCreateView的建構式可看到回傳值是View
			// 首先我們要生成一個View，他是由inflater.inflate()所建構出來的
			View rootView = inflater.inflate(R.layout.fragment_section_dummy,
					container, false);

			// 有了View之後，我們的View要顯示內容，所顯示的內容要透過Bundle所生成的實體來傳遞
			Bundle args = getArguments(); // 官方Bundle實體取得參數的慣例寫法

			// Once you provide a resource in your application (discussed in
			// Providing Resources),
			// you can apply it by referencing its resource ID.
			// All resource IDs are defined in your project's R class
			// 出處：
			// http://developer.android.com/guide/topics/resources/accessing-resources.html

			((TextView) rootView.findViewById(android.R.id.text1))
					.setText(getString(R.string.dummy_section_text,
							args.getInt(ARG_SECTION_NUMBER)));
			// 在此注意一下，在上面的rootView我們相對應的layout:
			// fragment_section_dummy.xml它的內容物只有個TextView
			// 而這個TextView宣告的ID是用android:id="@android:id/text1"
			// 這個text1是Android內建的

			return rootView;
		}

	}

	
	
	
	// 測試成功
	// 出處:http://android-er.blogspot.tw/2013/04/embed-webview-in-fragment.html
	static public class MyWebViewFragment extends Fragment {
		  
		  WebView myWebView;
		  final static String myBlogAddr = "http://140.135.8.183:8086/showonephoto/NewFile.jsp";
		  // 測試(阿男): http://140.135.8.183:8086/showonephoto/NewFile.jsp
		  // 測試(威齊): http://140.135.8.178:8080/showonephoto/NewFile.jsp
		  // 大神: http://www.google.com.tw/
		  String myUrl;
		  

		  @Override
		  public View onCreateView(LayoutInflater inflater, ViewGroup container,
		    Bundle savedInstanceState) {
		   View view = inflater.inflate(R.layout.layout_webfragment, container, false);
		   myWebView = (WebView)view.findViewById(R.id.mywebview);
		   
		   myWebView.getSettings().setJavaScriptEnabled(true);                
		   myWebView.setWebViewClient(new MyWebViewClient());
		   
		   if(myUrl == null){
		    myUrl = myBlogAddr;
		   }
		   myWebView.loadUrl(myUrl);
		       
		         return view;

		  }
		  
		  private class MyWebViewClient extends WebViewClient {
		         @Override
		         public boolean shouldOverrideUrlLoading(WebView view, String url) {
		          myUrl = url;
		             view.loadUrl(url);
		             return true;
		         }
		     }
	}	

/*	
	@Override
	 public void onBackPressed() {
	  MyWebViewFragment fragment = 
	    (MyWebViewFragment)getFragmentManager().findFragmentById(R.id.myweb_fragment);
	  WebView webView = fragment.myWebView;
	  
	  if(webView.canGoBack()){
	   webView.goBack();
	  }else{
	   super.onBackPressed();
	  }
	 }
*/
	
	
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

}
