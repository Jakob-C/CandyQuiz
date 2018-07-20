package com.blackskystudios.pizzaquiz;

import java.io.IOException;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.blackskystudios.pizzaquiz.R;

public class SettingsActivity extends Activity {

	SharedPreferences mSharedPreferences;
	Editor e;

	String marketLink = "https://play.google.com/store/apps/details?id=com.blackskystudios.memequiz";

	DAO db;
	Cursor c;

	SoundClass sou;
	CustomDialog dialog;
	private long mLastClickTime = 0;

	TextView soundText, vibrateText, shareText, resetText, impressumText;

	public Typeface tf;

	// =========================================================================================
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		tf = Typeface.createFromAsset(getAssets(), "fonts/" + getResources().getString(R.string.main_font));

		soundText = (TextView)findViewById(R.id.soundText);
		vibrateText = (TextView)findViewById(R.id.vibrateText);
		shareText = (TextView)findViewById(R.id.shareText);
		resetText = (TextView)findViewById(R.id.resetText);
		impressumText = (TextView)findViewById(R.id.impressumText);


		soundText.setTypeface(tf);
		vibrateText.setTypeface(tf);
		shareText.setTypeface(tf);
		resetText.setTypeface(tf);
		impressumText.setTypeface(tf);

		dialog = new CustomDialog(SettingsActivity.this);
		sou = new SoundClass(SettingsActivity.this);

		AdView ad = (AdView) findViewById(R.id.adView);
		if (ad != null) {
			ad.loadAd(new AdRequest.Builder().build());
		}

		mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
		e = mSharedPreferences.edit();

		db = new DAO(this);
		db.open();

		RelativeLayout layout = (RelativeLayout) findViewById(R.id.titleBar);

		TextView title = (TextView) layout.findViewById(R.id.title);
		title.setText(getResources().getString(R.string.settingsTitle).toUpperCase());
		title.setTypeface(tf);

		RelativeLayout scoreAndCoins = (RelativeLayout) layout.findViewById(R.id.scoreAndCoins);
		scoreAndCoins.setVisibility(View.GONE);


		TextView level = (TextView) layout.findViewById(R.id.level);
		level.setVisibility(View.GONE);

		final RelativeLayout sound = (RelativeLayout) findViewById(R.id.sound);
		final TextView soundText = (TextView) findViewById(R.id.soundText);

		if (mSharedPreferences.getInt("sound", 1) == 1) {
			soundText.setText(getResources().getString(R.string.soundBtnOn));
		} else {
			soundText.setText(getResources().getString(R.string.soundBtnOff));
		}

		sound.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (mSharedPreferences.getInt("sound", 1) == 1) {
					e.putInt("sound", 0);
					soundText.setText(getResources().getString(R.string.soundBtnOff));
				} else {
					e.putInt("sound", 1);
					soundText.setText(getResources().getString(R.string.soundBtnOn));

					MediaPlayer sound = new MediaPlayer();

					AssetFileDescriptor fd = getResources().openRawResourceFd(R.raw.play);
					try {
						sound.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
						sound.prepare();
						sound.start();

						sound.setOnCompletionListener(new OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								// Do the work after completion of audio
								mp.release();
							}
						});

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				e.commit(); // save changes
			}
		});

		final RelativeLayout vibrate = (RelativeLayout) findViewById(R.id.vibrate);
		final TextView vibrateText = (TextView) findViewById(R.id.vibrateText);

		if (mSharedPreferences.getInt("vibrate", 1) == 1) {
			vibrateText.setText(getResources().getString(R.string.vibrateBtnOn));
		} else {
			vibrateText.setText(getResources().getString(R.string.vibrateBtnOff));
		}

		vibrate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sou.playSound(R.raw.buttons);
				if (mSharedPreferences.getInt("vibrate", 1) == 1) {
					e.putInt("vibrate", 0);
					vibrateText.setText(getResources().getString(R.string.vibrateBtnOff));
				} else {
					Vibrator vib = (Vibrator) SettingsActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
					// Vibrate for 500 milliseconds
					vib.vibrate(500);

					e.putInt("vibrate", 1);
					vibrateText.setText(getResources().getString(R.string.vibrateBtnOn));
				}
				e.commit(); // save changes

			}
		});


		final RelativeLayout share = (RelativeLayout) findViewById(R.id.share);
		final TextView shareText = (TextView) findViewById(R.id.shareText);
		shareText.setText(getResources().getString(R.string.shareBtn));
		
		share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
					return;
				}
				mLastClickTime = SystemClock.elapsedRealtime();
				sou.playSound(R.raw.buttons);
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				String shareMessage = getResources().getString(R.string.shareDlgMessage) + marketLink;
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.shareDlgSubject));
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
				startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.shareDlgTitle)));

			}
		});



		final RelativeLayout reset = (RelativeLayout) findViewById(R.id.reset);
		final TextView resetText = (TextView) findViewById(R.id.resetText);
		resetText.setText(getResources().getString(R.string.resetBtn));
		
		reset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
					return;
				}
				mLastClickTime = SystemClock.elapsedRealtime();
				sou.playSound(R.raw.buttons);
				String msg = getResources().getString(R.string.resetDlg);
				dialog.showDialog(R.layout.blue_dialog, "resetDlg", msg, null);

			}
		});


		final RelativeLayout impressum = (RelativeLayout) findViewById(R.id.impressum);
		final TextView impressum_text = (TextView) findViewById(R.id.impressumText);

		impressum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
					return;
				}
				mLastClickTime = SystemClock.elapsedRealtime();
				sou.playSound(R.raw.buttons);
				Intent intent = new Intent(SettingsActivity.this, ImpressumActivity.class);
				startActivity(intent);



			}
		});

		ImageButton back = (ImageButton) layout.findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				sou.playSound(R.raw.buttons);
				finish();
				startActivity(intent);

			}
		});
		if (getResources().getString(R.string.langDirection).equals("rtl")) {
			ImageView soundImage = (ImageView) findViewById(R.id.soundImage);
			ImageView vibrateImage = (ImageView) findViewById(R.id.vibrateImage);
			ImageView shareImage = (ImageView) findViewById(R.id.shareImage);
			ImageView resetImage = (ImageView) findViewById(R.id.resetImage);
			
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) soundImage.getLayoutParams();
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);			

			soundImage.setLayoutParams(params);
			vibrateImage.setLayoutParams(params);
			shareImage.setLayoutParams(params);
			resetImage.setLayoutParams(params);
		}
		

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

	// ==============================================================================

	private boolean MyStartActivity(Intent aIntent) {
		try {
			startActivity(aIntent);
			return true;
		} catch (ActivityNotFoundException e) {
			return false;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		sou.playSound(R.raw.buttons);
		finish();
		startActivity(intent);
	}

	// ==============================================================================
	// private void addLevels() {
	// c = db.getLevels2();
	//
	// if (c.getCount() != 0) {
	//
	// do {
	// db.addLevels2(c.getString(c.getColumnIndex("le_country")),
	// c.getInt(c.getColumnIndex("_leid")));
	//
	// } while (c.moveToNext());
	// }
	// }

	// ==============================================================================
	// private void addFlags() {
	// c = db.getFlags2();
	//
	// if (c.getCount() != 0) {
	//
	// do {
	// db.addFlags2(c.getString(c.getColumnIndex("lo_name")),
	// c.getInt(c.getColumnIndex("lo_level")),
	// c.getString(c.getColumnIndex("lo_wikipedia")),
	// c.getString(c.getColumnIndex("lo_info")),
	// c.getString(c.getColumnIndex("lo_player")),
	// c.getInt(c.getColumnIndex("_loid")));
	//
	// } while (c.moveToNext());
	// }
	// }
	@Override
	public void onPause() {
		super.onPause();
		overridePendingTransition(0, 0);
	}
}
