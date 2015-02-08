package cn.edu.fudan.blueflamingo.handinhand;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;


public class MainActivity extends ActionBarActivity {

	public Global globalVal;

	private final static int AUTH_ACTIVITY = 0;

	private TextView nicknameTextView;
	private ImageView portraitImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		globalVal = (Global) getApplication();
		initToolBar();
		initHomePageFragment();
		if (!globalVal.getLoginFlag()) {
			Intent authIntent = new Intent(this, AuthActivity.class);
			startActivity(authIntent);
		} else {
			nicknameTextView.setText(globalVal.getNickname());
		}
	}

	private void initToolBar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
		if (toolbar != null) {
			//set toolbar features
			toolbar.setTitle("Hand in Hand");
			toolbar.setSubtitle("Welcome!");
			toolbar.inflateMenu(R.menu.menu_main);
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			//initialize drawer
			DrawerLayout mDrawerLayout;
			ActionBarDrawerToggle mDrawerToggle;
			mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
			mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
			mDrawerToggle.syncState();
			mDrawerLayout.setDrawerListener(mDrawerToggle);
			//initial btn_login
			//check whether the login process is needed and change the text of it
			final com.gc.materialdesign.views.ButtonFlat btnLogin = (com.gc.materialdesign.views.ButtonFlat) findViewById(R.id.btn_login);
			if (globalVal.getLoginFlag()) {
				btnLogin.setText("登出");
				btnLogin.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						btnLogin.setText("登出");
						Intent loginIntent = new Intent(MainActivity.this, AuthActivity.class);
						startActivityForResult(loginIntent, AUTH_ACTIVITY);
					}
				});
			}
			//bind
			nicknameTextView = (TextView) findViewById(R.id.drawer_username);
			portraitImageView = (ImageView) findViewById(R.id.drawer_head);
		}
	}

	private void initHomePageFragment() {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.main_fragment, new HomePageFragment())
				.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//for test
		String msg = "";
		switch (id) {
			case R.id.action_edit:
				msg += "Click edit";
				//jump to QuestionEditActivity
				Intent editIntent = new Intent(this, QuestionEditActivity.class);
				startActivity(editIntent);
				break;
			case R.id.action_search:
				msg += "Click search";
				break;
			default:
				break;
		}
		if (!msg.equals("")) {
			Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
		}
		return super.onOptionsItemSelected(item);
	}

	//handle drawer button onClick
	public void BtnLoginOnclick(View view) {
		ButtonFlat btnLogin = (ButtonFlat) findViewById(R.id.btn_login);
		if (btnLogin.getText().equals("登出")) {
			globalVal.setLoginFlag(false);
		}
		Intent loginIntent = new Intent(this, AuthActivity.class);
		startActivityForResult(loginIntent, AUTH_ACTIVITY);
	}

	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent data) {
		super.onActivityResult(reqCode, resCode, data);
		switch (reqCode) {
			case AUTH_ACTIVITY:
				com.gc.materialdesign.views.ButtonFlat btnLogin = (com.gc.materialdesign.views.ButtonFlat) findViewById(R.id.btn_login);
				if (globalVal.getLoginFlag()) {
					btnLogin.setText("登出");
				} else {
					btnLogin.setText("登陆");
				}
				switch (resCode) {
					case AuthActivity.HANG_OUT:
						globalVal.setLoginFlag(false);
						nicknameTextView.setText("随便逛逛的人");
						portraitImageView.setImageResource(R.drawable.app);
						break;
					case AuthActivity.REGISTERED:
					case AuthActivity.LOGINED:
						//TODO:获取头像
						nicknameTextView.setText(globalVal.getNickname());
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void onBackPressed() {
		FragmentManager fragmentManager = getFragmentManager();
		if (fragmentManager.getBackStackEntryCount() != 0) {
			fragmentManager.popBackStack();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		com.gc.materialdesign.views.ButtonFlat btnLogin = (com.gc.materialdesign.views.ButtonFlat) findViewById(R.id.btn_login);
		if (globalVal.getLoginFlag()) {
			btnLogin.setText("登出");
		} else {
			btnLogin.setText("登陆");
		}
	}
}
