package com.blackskystudios.pizzaquiz;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.blackskystudios.pizzaquiz.R;

public class MainActivity extends Activity {

	String marketLink;
	SharedPreferences mSharedPreferences;
	Editor e;
	DAO db;

	JSONArray flags = null;

	String siteUrl, updatesUrl;
	int lastFlag;

	JSONObject json;
	String jsonResultNull = "";
	SoundClass sou;
	CustomDialog dialog;
	private ConnectionDetector cd;
	private long mLastClickTime = 0;


	// ==============================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

		int sWidth = displaymetrics.widthPixels;
		int sHeight = displaymetrics.heightPixels;

		int dens = displaymetrics.densityDpi;
		double wi = (double) sWidth / (double) dens;
		double hi = (double) sHeight / (double) dens;
		double x = Math.pow(wi, 2);
		double y = Math.pow(hi, 2);
		double screenInches = Math.sqrt(x + y);

		if (sWidth > 480 && screenInches >= 4 && screenInches <= 5) {
			setContentView(R.layout.activity_main);
		} else if (screenInches > 6.5 && screenInches < 9) {
			setContentView(R.layout.activity_main);
		} else {
			setContentView(R.layout.activity_main);
		}

		dialog = new CustomDialog(MainActivity.this);
		sou = new SoundClass(MainActivity.this);

		db = new DAO(this);
		db.open();

		cd = new ConnectionDetector(MainActivity.this);
		lastFlag = db.getLastFlag();


		if (cd.isConnectingToInternet()) {
			// Internet Connection is not present

//			Intent checkUpdates = new Intent(MainActivity.this, CheckUpdatesService.class);
//			startService(checkUpdates);
		}

		marketLink = "https://play.google.com/store/apps/details?id=" + getPackageName();

		mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
		e = mSharedPreferences.edit();

//		if (mSharedPreferences.getInt("usingNum", 0) != 100) {
//			countUsingNumForRating();
//		}



		final ImageButton play = (ImageButton) findViewById(R.id.play);
		play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sou.playSound(R.raw.play);
				if (db.getNextFlag() != 0) {
					Intent intent = new Intent(MainActivity.this, GameActivity.class);
					intent.putExtra("FlagId", String.valueOf(db.getNextFlag()));
					startActivity(intent);
				} else {
					dialog.showDialog(R.layout.red_dialog, "finishDlg", getResources().getString(R.string.finishDlg), null);
				}


			}
		});




		final ImageButton settings = (ImageButton) findViewById(R.id.settings);
		settings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sou.playSound(R.raw.buttons);
				Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
				startActivity(intent);

			}
		});


		final ImageButton shop = (ImageButton) findViewById(R.id.shop);
		shop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sou.playSound(R.raw.buttons);
				Intent intent = new Intent(MainActivity.this, ShopActivity.class);
				startActivity(intent);

			}
		});

		final ImageButton exit = (ImageButton) findViewById(R.id.exit);
		exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
					return;
				}
				mLastClickTime = SystemClock.elapsedRealtime();
				sou.playSound(R.raw.buttons);

				dialog.showDialog(R.layout.blue_dialog, "exitDlg", getResources().getString(R.string.exitDlg), null);

			}
		});



	}

	// =========================================================================================

	@Override
	protected void onResume() {
		super.onResume();

		if (!mSharedPreferences.getString("flagsNum", "0").equals("0")) {
			String updatesDlgMsg = String.format(getResources().getString(R.string.updatesDlg), mSharedPreferences.getString("flagsNum", "0"));
			dialog.showDialog(R.layout.blue_dialog, "updatesDlg", updatesDlgMsg, mSharedPreferences.getString("flagsJSON", ""));
			e.putString("flagsNum", "0");
			e.commit();
		}
	}

	// =========================================================================================


	// ==============================================================================



	// ==============================================================================

	public boolean MyStartActivity(Intent aIntent) {
		try {
			startActivity(aIntent);
			return true;
		} catch (ActivityNotFoundException e) {
			return false;
		}
	}

	// ========================================================================================================



	// ========================================================================================================



	private void addFlag2() {
		Log.e("flags2", "Done");
		Cursor cu = db.getFlags();

		if (cu.getCount() != 0) {

			do {
                db.addFlags2(cu.getString(cu.getColumnIndex("fl_name")), cu.getString(cu.getColumnIndex("fl_country")), cu.getString(cu.getColumnIndex("fl_city")), cu.getString(cu.getColumnIndex("fl_wikipedia")), cu.getInt(cu.getColumnIndex("fl_order")), cu.getInt(cu.getColumnIndex("fl_web_id")));
			} while (cu.moveToNext());
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		overridePendingTransition(0, 0);
	}

}
