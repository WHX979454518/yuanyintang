/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mxkj.yuanyintang.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class PreferenceManager @SuppressLint("CommitPrefEdits")
private constructor(cxt: Context) {

    private val SHARED_KEY_SETTING_NOTIFICATION = "shared_key_setting_notification"
    private val SHARED_KEY_SETTING_SOUND = "shared_key_setting_sound"
    private val SHARED_KEY_SETTING_VIBRATE = "shared_key_setting_vibrate"
    private val SHARED_KEY_SETTING_SPEAKER = "shared_key_setting_speaker"

    var settingMsgNotification: Boolean
        get() = mSharedPreferences.getBoolean(SHARED_KEY_SETTING_NOTIFICATION, true)
        set(paramBoolean) {
            editor.putBoolean(SHARED_KEY_SETTING_NOTIFICATION, paramBoolean)
            editor.apply()
        }

    var settingMsgSound: Boolean
        get() = mSharedPreferences.getBoolean(SHARED_KEY_SETTING_SOUND, true)
        set(paramBoolean) {
            editor.putBoolean(SHARED_KEY_SETTING_SOUND, paramBoolean)
            editor.apply()
        }

    var settingMsgVibrate: Boolean
        get() = mSharedPreferences.getBoolean(SHARED_KEY_SETTING_VIBRATE, true)
        set(paramBoolean) {
            editor.putBoolean(SHARED_KEY_SETTING_VIBRATE, paramBoolean)
            editor.apply()
        }

    var settingMsgSpeaker: Boolean
        get() = mSharedPreferences.getBoolean(SHARED_KEY_SETTING_SPEAKER, true)
        set(paramBoolean) {
            editor.putBoolean(SHARED_KEY_SETTING_SPEAKER, paramBoolean)
            editor.apply()
        }

    var settingAllowChatroomOwnerLeave: Boolean
        get() = mSharedPreferences.getBoolean(SHARED_KEY_SETTING_CHATROOM_OWNER_LEAVE, true)
        set(value) {
            editor.putBoolean(SHARED_KEY_SETTING_CHATROOM_OWNER_LEAVE, value)
            editor.apply()
        }

    var isDeleteMessagesAsExitGroup: Boolean
        get() = mSharedPreferences.getBoolean(SHARED_KEY_SETTING_DELETE_MESSAGES_WHEN_EXIT_GROUP, true)
        set(value) {
            editor.putBoolean(SHARED_KEY_SETTING_DELETE_MESSAGES_WHEN_EXIT_GROUP, value)
            editor.apply()
        }

    var isAutoAcceptGroupInvitation: Boolean
        get() = mSharedPreferences.getBoolean(SHARED_KEY_SETTING_AUTO_ACCEPT_GROUP_INVITATION, true)
        set(value) {
            editor.putBoolean(SHARED_KEY_SETTING_AUTO_ACCEPT_GROUP_INVITATION, value)
            editor.commit()
        }

    var isAdaptiveVideoEncode: Boolean
        get() = mSharedPreferences.getBoolean(SHARED_KEY_SETTING_ADAPTIVE_VIDEO_ENCODE, false)
        set(value) {
            editor.putBoolean(SHARED_KEY_SETTING_ADAPTIVE_VIDEO_ENCODE, value)
            editor.apply()
        }

    var isPushCall: Boolean
        get() = mSharedPreferences.getBoolean(SHARED_KEY_SETTING_OFFLINE_PUSH_CALL, false)
        set(value) {
            editor.putBoolean(SHARED_KEY_SETTING_OFFLINE_PUSH_CALL, value)
            editor.apply()
        }

    var isGroupsSynced: Boolean
        get() = mSharedPreferences.getBoolean(SHARED_KEY_SETTING_GROUPS_SYNCED, false)
        set(synced) {
            editor.putBoolean(SHARED_KEY_SETTING_GROUPS_SYNCED, synced)
            editor.apply()
        }

    var isContactSynced: Boolean
        get() = mSharedPreferences.getBoolean(SHARED_KEY_SETTING_CONTACT_SYNCED, false)
        set(synced) {
            editor.putBoolean(SHARED_KEY_SETTING_CONTACT_SYNCED, synced)
            editor.apply()
        }

    val isBacklistSynced: Boolean
        get() = mSharedPreferences.getBoolean(SHARED_KEY_SETTING_BALCKLIST_SYNCED, false)

    var currentUserNick: String?
        get() = mSharedPreferences.getString(SHARED_KEY_CURRENTUSER_NICK, null)
        set(nick) {
            editor.putString(SHARED_KEY_CURRENTUSER_NICK, nick)
            editor.apply()
        }

    var currentUserAvatar: String?
        get() = mSharedPreferences.getString(SHARED_KEY_CURRENTUSER_AVATAR, null)
        set(avatar) {
            editor.putString(SHARED_KEY_CURRENTUSER_AVATAR, avatar)
            editor.apply()
        }

    val currentUsername: String?
        get() = mSharedPreferences.getString(SHARED_KEY_CURRENTUSER_USERNAME, null)

    var restServer: String?
        get() = mSharedPreferences.getString(SHARED_KEY_REST_SERVER, null)
        set(restServer) {
            editor.putString(SHARED_KEY_REST_SERVER, restServer).commit()
            editor.commit()
        }

    var imServer: String?
        get() = mSharedPreferences.getString(SHARED_KEY_IM_SERVER, null)
        set(imServer) {
            editor.putString(SHARED_KEY_IM_SERVER, imServer)
            editor.commit()
        }

    val isCustomServerEnable: Boolean
        get() = mSharedPreferences.getBoolean(SHARED_KEY_ENABLE_CUSTOM_SERVER, false)

    val isCustomAppkeyEnabled: Boolean
        get() = mSharedPreferences.getBoolean(SHARED_KEY_ENABLE_CUSTOM_APPKEY, false)

    var customAppkey: String
        get() = mSharedPreferences.getString(SHARED_KEY_CUSTOM_APPKEY, "")
        set(appkey) {
            editor.putString(SHARED_KEY_CUSTOM_APPKEY, appkey)
            editor.apply()
        }

    var isMsgRoaming: Boolean
        get() = mSharedPreferences.getBoolean(SHARED_KEY_MSG_ROAMING, false)
        set(isRoaming) {
            editor.putBoolean(SHARED_KEY_MSG_ROAMING, isRoaming)
            editor.apply()
        }

    /**
     * ----------------------------------------- Call Option -----------------------------------------
     */

    /**
     * Min Video kbps
     * if no value was set, return -1
     * @return
     */
    var callMinVideoKbps: Int
        get() = mSharedPreferences.getInt(SHARED_KEY_CALL_MIN_VIDEO_KBPS, -1)
        set(minBitRate) {
            editor.putInt(SHARED_KEY_CALL_MIN_VIDEO_KBPS, minBitRate)
            editor.apply()
        }

    /**
     * Max Video kbps
     * if no value was set, return -1
     * @return
     */
    var callMaxVideoKbps: Int
        get() = mSharedPreferences.getInt(SHARED_KEY_CALL_MAX_VIDEO_KBPS, -1)
        set(maxBitRate) {
            editor.putInt(SHARED_KEY_CALL_MAX_VIDEO_KBPS, maxBitRate)
            editor.apply()
        }

    /**
     * Max frame rate
     * if no value was set, return -1
     * @return
     */
    var callMaxFrameRate: Int
        get() = mSharedPreferences.getInt(SHARED_KEY_CALL_MAX_FRAME_RATE, -1)
        set(maxFrameRate) {
            editor.putInt(SHARED_KEY_CALL_MAX_FRAME_RATE, maxFrameRate)
            editor.apply()
        }

    /**
     * audio sample rate
     * if no value was set, return -1
     * @return
     */
    var callAudioSampleRate: Int
        get() = mSharedPreferences.getInt(SHARED_KEY_CALL_AUDIO_SAMPLE_RATE, -1)
        set(audioSampleRate) {
            editor.putInt(SHARED_KEY_CALL_AUDIO_SAMPLE_RATE, audioSampleRate)
            editor.apply()
        }

    /**
     * back camera resolution
     * format: 320x240
     * if no value was set, return ""
     */
    var callBackCameraResolution: String
        get() = mSharedPreferences.getString(SHARED_KEY_CALL_BACK_CAMERA_RESOLUTION, "")
        set(resolution) {
            editor.putString(SHARED_KEY_CALL_BACK_CAMERA_RESOLUTION, resolution)
            editor.apply()
        }

    /**
     * front camera resolution
     * format: 320x240
     * if no value was set, return ""
     */
    var callFrontCameraResolution: String
        get() = mSharedPreferences.getString(SHARED_KEY_CALL_FRONT_CAMERA_RESOLUTION, "")
        set(resolution) {
            editor.putString(SHARED_KEY_CALL_FRONT_CAMERA_RESOLUTION, resolution)
            editor.apply()
        }

    /**
     * fixed video sample rate
     * if no value was set, return false
     * @return
     */
    var isCallFixedVideoResolution: Boolean
        get() = mSharedPreferences.getBoolean(SHARED_KEY_CALL_FIX_SAMPLE_RATE, false)
        set(enable) {
            editor.putBoolean(SHARED_KEY_CALL_FIX_SAMPLE_RATE, enable)
            editor.apply()
        }

    init {
        mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        editor = mSharedPreferences.edit()
    }

    fun setBlacklistSynced(synced: Boolean) {
        editor.putBoolean(SHARED_KEY_SETTING_BALCKLIST_SYNCED, synced)
        editor.apply()
    }

    fun setCurrentUserName(username: String) {
        editor.putString(SHARED_KEY_CURRENTUSER_USERNAME, username)
        editor.apply()
    }

    fun enableCustomServer(enable: Boolean) {
        editor.putBoolean(SHARED_KEY_ENABLE_CUSTOM_SERVER, enable)
        editor.apply()
    }

    fun enableCustomAppkey(enable: Boolean) {
        editor.putBoolean(SHARED_KEY_ENABLE_CUSTOM_APPKEY, enable)
        editor.apply()
    }

    fun removeCurrentUserInfo() {
        editor.remove(SHARED_KEY_CURRENTUSER_NICK)
        editor.remove(SHARED_KEY_CURRENTUSER_AVATAR)
        editor.apply()
    }

    companion object {
        val PREFERENCE_NAME = "yyt_saveInfo"
        private lateinit var mSharedPreferences: SharedPreferences
        private var mPreferencemManager: PreferenceManager? = null
        private lateinit var editor: SharedPreferences.Editor
        private val SHARED_KEY_SETTING_CHATROOM_OWNER_LEAVE = "shared_key_setting_chatroom_owner_leave"
        private val SHARED_KEY_SETTING_DELETE_MESSAGES_WHEN_EXIT_GROUP = "shared_key_setting_delete_messages_when_exit_group"
        private val SHARED_KEY_SETTING_AUTO_ACCEPT_GROUP_INVITATION = "shared_key_setting_auto_accept_group_invitation"
        private val SHARED_KEY_SETTING_ADAPTIVE_VIDEO_ENCODE = "shared_key_setting_adaptive_video_encode"
        private val SHARED_KEY_SETTING_OFFLINE_PUSH_CALL = "shared_key_setting_offline_push_call"

        private val SHARED_KEY_SETTING_GROUPS_SYNCED = "SHARED_KEY_SETTING_GROUPS_SYNCED"
        private val SHARED_KEY_SETTING_CONTACT_SYNCED = "SHARED_KEY_SETTING_CONTACT_SYNCED"
        private val SHARED_KEY_SETTING_BALCKLIST_SYNCED = "SHARED_KEY_SETTING_BALCKLIST_SYNCED"

        private val SHARED_KEY_CURRENTUSER_USERNAME = "SHARED_KEY_CURRENTUSER_USERNAME"
        private val SHARED_KEY_CURRENTUSER_NICK = "SHARED_KEY_CURRENTUSER_NICK"
        private val SHARED_KEY_CURRENTUSER_AVATAR = "SHARED_KEY_CURRENTUSER_AVATAR"

        private val SHARED_KEY_REST_SERVER = "SHARED_KEY_REST_SERVER"
        private val SHARED_KEY_IM_SERVER = "SHARED_KEY_IM_SERVER"
        private val SHARED_KEY_ENABLE_CUSTOM_SERVER = "SHARED_KEY_ENABLE_CUSTOM_SERVER"
        private val SHARED_KEY_ENABLE_CUSTOM_APPKEY = "SHARED_KEY_ENABLE_CUSTOM_APPKEY"
        private val SHARED_KEY_CUSTOM_APPKEY = "SHARED_KEY_CUSTOM_APPKEY"
        private val SHARED_KEY_MSG_ROAMING = "SHARED_KEY_MSG_ROAMING"

        private val SHARED_KEY_CALL_MIN_VIDEO_KBPS = "SHARED_KEY_CALL_MIN_VIDEO_KBPS"
        private val SHARED_KEY_CALL_MAX_VIDEO_KBPS = "SHARED_KEY_CALL_Max_VIDEO_KBPS"
        private val SHARED_KEY_CALL_MAX_FRAME_RATE = "SHARED_KEY_CALL_MAX_FRAME_RATE"
        private val SHARED_KEY_CALL_AUDIO_SAMPLE_RATE = "SHARED_KEY_CALL_AUDIO_SAMPLE_RATE"
        private val SHARED_KEY_CALL_BACK_CAMERA_RESOLUTION = "SHARED_KEY_CALL_BACK_CAMERA_RESOLUTION"
        private val SHARED_KEY_CALL_FRONT_CAMERA_RESOLUTION = "SHARED_KEY_FRONT_CAMERA_RESOLUTIOIN"
        private val SHARED_KEY_CALL_FIX_SAMPLE_RATE = "SHARED_KEY_CALL_FIX_SAMPLE_RATE"

        @Synchronized
        fun init(cxt: Context) {
            if (mPreferencemManager == null) {
                mPreferencemManager = PreferenceManager(cxt)
            }
        }

        /**
         * get instance of PreferenceManager
         *
         * @param
         * @return
         */
        val instance: PreferenceManager
            @Synchronized get() {
                if (mPreferencemManager == null) {
                    throw RuntimeException("please init first!")
                }
                return mPreferencemManager as PreferenceManager
            }
    }

}
