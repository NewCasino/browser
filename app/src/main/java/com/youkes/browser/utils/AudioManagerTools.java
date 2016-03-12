package com.youkes.browser.utils;

import android.content.Context;
import android.media.AudioManager;

import com.youkes.browser.MainApp;

public class AudioManagerTools {

    /**AudioManager */
    private AudioManager mAudioManager = null;
    private static AudioManagerTools mInstance;
    private AudioManagerTools() {

    }

    /**单例方法*/
    public static AudioManagerTools getInstance() {

        if(mInstance == null) {
            mInstance = new AudioManagerTools();
        }

        return mInstance;
    }

    /**
     * 返回当前所持有的AudioManager访问实例
     * @return
     */
    public final AudioManager getAudioManager() {
        if(mAudioManager == null) {
            mAudioManager = (AudioManager) MainApp.getContext().getSystemService(Context.AUDIO_SERVICE);
        }

        return mAudioManager;
    }
}
