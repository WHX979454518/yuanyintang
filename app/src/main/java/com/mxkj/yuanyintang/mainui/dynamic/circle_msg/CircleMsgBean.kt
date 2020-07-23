package com.mxkj.yuanyintang.mainui.dynamic.circle_msg

import com.chad.library.adapter.base.entity.MultiItemEntity

import java.io.Serializable

import com.mxkj.yuanyintang.mainui.home.data.Constant.CircleMsgItemType.AGREE_MY_COMMENT
import com.mxkj.yuanyintang.mainui.home.data.Constant.CircleMsgItemType.AGREE_MY_DYNAMIC
import com.mxkj.yuanyintang.mainui.home.data.Constant.CircleMsgItemType.AGREE_MY_REPLY
import com.mxkj.yuanyintang.mainui.home.data.Constant.CircleMsgItemType.COMMENT_DYNAMIC
import com.mxkj.yuanyintang.mainui.home.data.Constant.CircleMsgItemType.REPLY_COMMENT_FOR_DYNAMIC
import com.mxkj.yuanyintang.mainui.home.data.Constant.CircleMsgItemType.TV_HISTORY_MSG

class CircleMsgBean : Serializable {

    var code: Int = 0
    var msg: String? = null
    var data: DataBeanX? = null

    class DataBeanX {

        var count: CountBean? = null
        var data: List<DataBean>? = null

        class CountBean {
            var notice_msg_count: Int = 0
            var system_msg_count: Int = 0
            var comment_msg_count: Int = 0
            var dynamic_msg_count: Int = 0
            var circle_msg_center_count: Int = 0
        }

        class DataBean : MultiItemEntity {

            var id: Int = 0
            var title: String? = null
            var body: String? = null
            var type: Int = 0
            var from_type: Int = 0
            var create_time: Int = 0
            var imgpic: String? = null
            var urlmodel: String? = null
            var app_url: String? = null
            var web_url: String? = null
            var from_id: Int = 0
            var fid: Int = 0
            var from_uid: Int = 0
            var status: Int = 0
            var item_id: Int = 0
            var is_new_target: Int = 0
            var create_time_desc: String? = null
            var item_info: ItemInfoBean? = null
            var from_info: FromInfoBean? = null
            var user_info: UserInfoBean? = null
            override fun getItemType(): Int {
                when (from_type) {
                    1//对动态评论(from_info)
                    -> return if (fid > 0) {//回复评论
                        REPLY_COMMENT_FOR_DYNAMIC
                    } else {//评论动态
                        COMMENT_DYNAMIC
                    }
                    2 -> return REPLY_COMMENT_FOR_DYNAMIC
                    3//对动态点赞(from_info)
                    -> return AGREE_MY_DYNAMIC
                    4//点赞(reply_info)
                    -> return if (fid > 0) {//对评论的回复点赞
                        AGREE_MY_REPLY
                    } else {//对评论的点赞
                        AGREE_MY_COMMENT
                    }
                    1024//加载历史消息文字
                    -> return TV_HISTORY_MSG
                }
                return 0
            }

            /*动态*/
            class ItemInfoBean {
                /**
                 * id : 1231
                 * depict : 深情沧桑，好听呀
                 * uid : 33345
                 * imglist :
                 * delete_time : 0
                 * type : 1
                 * item_id : 2296
                 * nickname : 麻花
                 * is_delete : 0
                 */

                var id: Int = 0
                var depict: String? = null
                var uid: Int = 0
                var imglist_info: List<ImglistInfoBean>? = null

                var delete_time: Int = 0
                var type: Int = 0
                var item_id: Int = 0
                var nickname: String? = null
                var is_delete: Int = 0

                class ImglistInfoBean : Serializable {
                    /**
                     * ext : jpg
                     * w : 200
                     * h : 200
                     * size : 9329
                     * is_long : 0
                     * link : http://api.demo.com//image/ce81fba58689fa96ab2ad9860a6b2461/3
                     * md5 : ce81fba58689fa96ab2ad9860a6b2461
                     */

                    var ext: String? = null
                    var w: String? = null
                    var h: String? = null
                    var size: String? = null
                    var is_long: String? = null
                    var link: String? = null
                    var md5: String? = null
                }
            }

            /*评论*/
            class FromInfoBean {
                /**
                 * id : 79730
                 * uid : 2222
                 * content : xxxxxxxxxxxxxxxxxxxxxx
                 * create_time : 1524626754
                 * agrees : 3
                 * delete_time : 0
                 * nickname :
                 * is_delete : 0
                 * reply_info : {"id":79729,"uid":50782,"content":"1新消息xxxxx","create_time":1524626542,"agrees":2,"delete_time":0,"nickname":"源音塘699551","is_delete":0,"agrees_text":2}
                 * agrees_text : 3
                 */

                var id: Int = 0
                var uid: Int = 0
                var content: String? = null
                var create_time: Int = 0
                var agrees: Int = 0
                var delete_time: Int = 0
                var nickname: String? = null
                var is_delete: Int = 0
                var reply_info: ReplyInfoBean? = null
                var agrees_text: Int = 0

                class ReplyInfoBean {
                    /**
                     * id : 79729
                     * uid : 50782
                     * content : 1新消息xxxxx
                     * create_time : 1524626542
                     * agrees : 2
                     * delete_time : 0
                     * nickname : 源音塘699551
                     * is_delete : 0
                     * agrees_text : 2
                     */

                    var id: Int = 0
                    var uid: Int = 0
                    var content: String? = null
                    var create_time: Int = 0
                    var agrees: Int = 0
                    var delete_time: Int = 0
                    var nickname: String? = null
                    var is_delete: Int = 0
                    var agrees_text: Int = 0
                }

            }

            class UserInfoBean {
                /**
                 * id : 50952
                 * nickname : kijghjewer3e tf
                 * head : 3d3d27527715c56204c6e063e34883ae60384a97
                 * is_music : 0
                 * head_link : http://api.demo.com//image/3d3d27527715c56204c6e063e34883ae60384a97/1
                 * head_info :
                 */

                var id: String? = null
                var nickname: String? = null
                var head: String? = null
                var is_music: String? = null
                var head_link: String? = null
                var head_info: String? = null
            }
        }
    }
}
