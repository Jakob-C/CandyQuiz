package com.blackskystudios.pizzaquiz;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;

/**
 * Created by Jakob2000 on 20.04.2018.
 */

public class SimpleMethods {
    MediaPlayer mp;

    public void saveNumber(String name, int number, Context ctx){
        SharedPreferences prefs = ctx.getSharedPreferences("werte", 0);
        SharedPreferences.Editor editor =prefs.edit();

        editor.putInt(name, number);
        editor.commit();
    }

    public int getNumber(String name, Context ctx){
        SharedPreferences prefs = ctx.getSharedPreferences("werte", 0);
        int result =prefs.getInt(name, 99);

        return result;
    }

    public void saveBoolean(String name, Boolean bl, Context ctx){
        SharedPreferences prefs = ctx.getSharedPreferences("werte", 0);
        SharedPreferences.Editor editor =prefs.edit();

        editor.putBoolean(name, bl);
        editor.commit();
    }

    public boolean getBoolean(String name, Context ctx){
        SharedPreferences prefs = ctx.getSharedPreferences("werte", 0);
        boolean result =prefs.getBoolean(name, false);

        return result;
    }

    public void saveString(String name, String string, Context ctx){
        SharedPreferences prefs = ctx.getSharedPreferences("werte", 0);
        SharedPreferences.Editor editor =prefs.edit();

        editor.putString(name, string);
        editor.commit();
    }

    public String getString(String name, Context ctx){
        SharedPreferences prefs = ctx.getSharedPreferences("werte", 0);
        String result =prefs.getString(name, "-error-");

        return result;
    }

    public void playSound(int soundfile, Context ctx){
        if (mp != null) {
            try {
                mp.stop();
                mp.release();
                mp = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mp = MediaPlayer.create(ctx, soundfile);
        mp.start();
    }

    public void simpleDialog(String title, String text, String button_text, Activity activity, Context ctx){
//        AlertDialog.Builder a_builder;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            a_builder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
//        } else {
//            a_builder = new AlertDialog.Builder(activity);
//        }
//        a_builder.setMessage("Are you sure you want to follow our instagram page? You wont become a second chance...")
//                .setCancelable(true)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Uri uriUrl = Uri.parse("http://redirection.lima-city.de/links/instagram.html");
//                        Intent openUrl = new Intent(Intent.ACTION_VIEW, uriUrl);
//                        startActivity(openUrl);
//
//                        btn_insta.setBackgroundResource(R.drawable.button_shop_diabled);
//                        tv_insta.setAlpha(0.5f);
//
//                        sm.saveBoolean("insta_used", true, ctx);
//                        db.addTotalCoins(Integer.parseInt(getText(R.string.coins_for_insta).toString()));
//                        dialog.cancel();
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.cancel();
//                    }
//                });
//        AlertDialog alert = a_builder.create();
//        alert.setTitle("Instagram");
//        alert.show();
    }
}
