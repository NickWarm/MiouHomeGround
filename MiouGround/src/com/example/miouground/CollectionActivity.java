package com.example.miouground;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CollectionActivity extends FragmentActivity {

	DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle arg0) {

		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_collection_demo);

		// step.1
		// 有許多child views，你需要有個Adapter做溝通
		mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(
				getSupportFragmentManager());
		// Adapter裡傳的參數型態是FragmentManager，所以我們應用getSupportFragmentManager()這方法去取得


		final ActionBar actionBar = getActionBar();
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mDemoCollectionPagerAdapter);

	}

	// 這一段是寫Action Bar的返回主程式時的觸發事件
	// If your activity provides any intent filters that allow other apps to
	// start the activity,
	// you should implement the onOptionsItemSelected() callback such that if
	// the user
	// presses the Up button after entering your activity from another app's
	// task,
	// your app starts a new task with the appropriate back stack before
	// navigating up.
	// 出處：
	// http://developer.android.com/training/implementing-navigation/ancestral.html#SpecifyParent
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent UpIntent = new Intent(this, MainActivity.class);
			// upIntent: An intent representing the target destination for up
			// navigation
			if (NavUtils.shouldUpRecreateTask(this, UpIntent)) {
				TaskStackBuilder.create(this).addNextIntent(UpIntent)
						.startActivities();

				finish();
				// Call this when your activity is done and should be closed.
				// The ActivityResult is propagated back to whoever launched you
				// via onActivityResult().
				// propagate 傳播, 傳導

			} else {
				NavUtils.navigateUpTo(this, UpIntent);
				// navigateUpFromSameTask(). This is equivalent to calling
				// navigateUpTo()
				// When you call this method,
				// it finishes the current activity and starts (or resumes) the
				// appropriate parent activity.
			}
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	// step.1
	// 有FragmentPagerAdapter與FragmentStatePagerAdapter
	// 滑動的頁數如果是未確定的(不固定的) 則使用FragmentStatePagerAdapter
	// 如果是固定的(頁數較少的)則使用FragmentPagerAdapter
	public static class DemoCollectionPagerAdapter extends
			FragmentStatePagerAdapter {

		public DemoCollectionPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int i) { // 取得我們定義的fragment
			// TODO Auto-generated method stub
			// Step.2 要先寫好我們Fragment裡的內容
			Fragment fragment = new DemoObjectFragment();


			Bundle args = new Bundle();
			args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1);
			fragment.setArguments(args);

			return fragment;
		}

		@Override
		public int getCount() { // 定義pagers的生成上限
			// TODO Auto-generated method stub
			// 生成pages的上限
			return 10;
		}

		@Override
		public CharSequence getPageTitle(int position) { // 定義每個分頁title的文字
			// 此為PageAdapter裡的方法
			// TODO Auto-generated method stub
			return "OBJECT" + (position + 1); // position從0開始計算
		}

	}

	// Step.2 定義Fragment的內容物
	public static class DemoObjectFragment extends Fragment {

		public static final String ARG_OBJECT = "object";

		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			View rootView = inflater.inflate(
					R.layout.fragment_collection_object, container, false);
			
			Bundle args = getArguments();
			((TextView) rootView.findViewById(android.R.id.text1))
					.setText(Integer.toString(args.getInt(ARG_OBJECT)));
			

			return rootView;
		}

	}

}
