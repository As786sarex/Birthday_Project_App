package com.wildcardenter.myfab.for_jahan.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefHelper {

    public static final int TYPE_BOOLEAN=54;
    public static final int TYPE_STRING=58;
    public static final int TYPE_INTEGER=57;
    public static final int TYPE_FLOAT=56;

    public static final String SWIPECARD_KEY="isFirstSwipeCard";
    public static final String PLAYER_KEY="isFirstMusicPlayer";
    public static final String ASSSTANT_KEY="isFirstAssistant";
    public static final String AUTHORIZATION_KEY="isAuthorized";

    private SharedPreferences sharedPreferences;

    public SharedPrefHelper(Context context) {
        Context context1 = context;
        sharedPreferences=context.getSharedPreferences("MySharedData",Context.MODE_PRIVATE);
    }

    public void setData( String key, Object value,int type){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (type==TYPE_BOOLEAN) {
            editor.putBoolean(key, (Boolean) value);
        }
        else if (type==TYPE_STRING){
            editor.putString(key, (String) value);
        }
        else if (type==TYPE_INTEGER){
            editor.putInt(key, (Integer) value);
        }
        else if (type==TYPE_FLOAT){
            editor.putFloat(key, (Float) value);
        }
        editor.apply();

    }
    public Object getData(String key,int type){
        Object data=null;
        if (type==TYPE_BOOLEAN) {
            data=sharedPreferences.getBoolean(key,false);
        }
        else if (type==TYPE_STRING){
            data=sharedPreferences.getString(key,"");
        }
        else if (type==TYPE_INTEGER){
            data=sharedPreferences.getInt(key,1);
        }
        else if (type==TYPE_FLOAT){
            data=sharedPreferences.getFloat(key,1f);
        }
        return data;
    }
}
