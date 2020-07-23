package com.mxkj.yuanyintang.mainui.message.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.message.bean.MsgListean;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import java.util.List;

import static com.mxkj.yuanyintang.mainui.home.data.Constant.MsgCenterItem.SystemMsgItemType.SYS_MSG;

/**
 * Created by LiuJie on 2017/11/10.
 */

public class SystemMessageListAdapterNew extends BaseMultiItemQuickAdapter<MsgListean.DataBeanX.DataBean, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public SystemMessageListAdapterNew(List<MsgListean.DataBeanX.DataBean> data) {
        super(data);
        addItemType(SYS_MSG, R.layout.message_item_systemmsg_list);
    }

    @Override
    protected void convert(com.chad.library.adapter.base.BaseViewHolder helper, MsgListean.DataBeanX.DataBean item) {
        if (item.getItemType()!=0&&item.getType()==2) {
            helper.setText(R.id.creat_time, StringUtils.isEmpty(item.getCreate_time_desc()));
            helper.setText(R.id.title, item.getTitle());
            helper.setText(R.id.content, item.getBody());
        }
    }
}
