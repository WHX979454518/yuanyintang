/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mxkj.yuanyintang.im;

public class EaseConstant {
    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
    public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";

    public static final String MESSAGE_TYPE_RECALL = "message_recall";
    
    public static final String MESSAGE_ATTR_IS_BIG_EXPRESSION = "em_is_big_expression";
    public static final String MESSAGE_ATTR_IS_MUSIC = "em_is_music";
    public static final String MESSAGE_ATTR_EXPRESSION_ID = "em_expression_id";
    
    public static final String MESSAGE_ATTR_AT_MSG = "em_at_list";
    public static final String MESSAGE_ATTR_VALUE_AT_MSG_ALL = "ALL";
    
	public static final int CHATTYPE_SINGLE = 1;
    public static final int CHATTYPE_GROUP = 2;
    public static final int CHATTYPE_CHATROOM = 3;

    public static final String EXTRA_CHAT_TYPE = "chatType";
    public static final String EXTRA_USER_ID = "userId";//TODO 我他妈发现这里这个字段刘杰传的是用户名nickname
    public static final String EXTRA_OTHER_UID = "otherUid";
    public static final String EXTRA_OTHER_NAME = "otherName";
    public static final String EXTRA_OTHER_AVATAR = "otherAvatar";
    public static final String EXTRA_USER_NAME = "userName";
    public static final String EXTRA_USER_AVATAR = "userAvatar";
    public static final String EXTRA_IS_RELATION = "isRelation";//发送者是否关注：0未关注,1已关注,2互相关注
    public static final String EXTRA_IS_MUSIC = "isMusic";//接收者是否音乐会员3.是音乐会员
    public static final String EXTRA_TO_MUSIC = "toMusic";//发送者方是否是会员
    public static final String EXTRA_TO_RELATION = "toRelation";//接收者是否关注发送方
}
