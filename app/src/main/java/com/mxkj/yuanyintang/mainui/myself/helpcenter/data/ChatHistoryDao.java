//package com.mxkj.yuanyintang.main_ui.myself.helpcenter.data;
//
//import android.content.Context;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.j256.ormlite.dao.Dao;
//import com.mxkj.yuanyintang.main_ui.myself.my_release.up_load_music.scan_sd.Song;
//import com.mxkj.yuanyintang.main_ui.myself.my_release.up_load_music.scan_sd.SongHelper;
//
//import java.sql.SQLException;
//import java.util.Comparator;
//import java.util.List;
//
//import io.reactivex.Observable;
//import io.reactivex.annotations.NonNull;
//import io.reactivex.functions.Consumer;
//import io.reactivex.schedulers.Schedulers;
//
///**
// * Created by zuixia on 2018/4/24.
// */
//
//public class ChatHistoryDao {
//    private Dao<ChatHistoryBean, Integer> parentDao;
//    private Dao<ChatHistoryBean.MutiTextBean, Integer> childDao;
//    private ChatHistoryHelper chatHistoryHelper;
//
//    public ChatHistoryDao(Context context) {
//        try {
//            chatHistoryHelper = ChatHistoryHelper.getInstance(context);
//            chatHistoryDao = chatHistoryHelper.getDao(ChatHistoryBean.class);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 增加一条数据
//     */
//    public void add(ChatHistoryBean chatHistoryBean) {
//        try {
//            chatHistoryDao.create(chatHistoryBean);
//        } catch (Exception e) {
//            Log.e("TAG", "添加机器人聊天记录异常--------: "+e );
//            e.printStackTrace();
//        }
//    }
//
//    //根据id（主键）删除一条数据
//    public void deleteHelper(int id) {
//        try {
//            chatHistoryHelper.getchildDao().deleteById(id);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //删除数据
//    public void deleteAllHelper() {
//        try {
//            chatHistoryHelper.getchartDao().delete(listHelper());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //根据id（主键）更新一条数据
//    public void updateHelper(ChatHistoryBean chatHistoryBean, int id) {
//        try {
//            chatHistoryBean.set_id(id);
//            chatHistoryHelper.getchartDao().update(chatHistoryBean);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //返回list
//    public List<ChatHistoryBean> listHelper() {
//        List<ChatHistoryBean> lists = null;
//        try {
//            lists = chatHistoryHelper.getchartDao().queryForAll();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return lists;
//    }
//
//    public interface RefreshDataLisener {
//        void refreshData(List<Song> Song);
//    }
//}
