package cn.ucai.fulicenter.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ucai.fulicenter.Constant;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.SuperWeChatApplication;
import cn.ucai.fulicenter.applib.controller.HXSDKHelper;
import cn.ucai.fulicenter.DemoHXSDKHelper;
import cn.ucai.fulicenter.bean.Contact;
import cn.ucai.fulicenter.bean.Group;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.data.RequestManager;
import cn.ucai.fulicenter.domain.EMUser;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.util.HanziToPinyin;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserUtils {
    public static final String TAG = "UserUtils";

    /**
     * 根据username获取相应user，由于demo没有真实的用户数据，这里给的模拟的数据；
     *
     * @param username
     * @return
     */
    public static EMUser getUserInfo(String username) {
        EMUser user = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList().get(username);
        if (user == null) {
            user = new EMUser(username);
        }

        if (user != null) {
            //demo没有这些数据，临时填充
            if (TextUtils.isEmpty(user.getNick()))
                user.setNick(username);
        }
        return user;
    }

    public static Contact getUserBeanInfo(String username) {
        Contact contact = SuperWeChatApplication.getInstance().getUserList().get(username);
        return contact;
    }


    /**
     * 设置用户头像
     *
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView) {
        EMUser EMUser = getUserInfo(username);
        if (EMUser != null && EMUser.getAvatar() != null) {
            Picasso.with(context).load(EMUser.getAvatar()).placeholder(cn.ucai.fulicenter.R.drawable.default_avatar).into(imageView);
        } else {
            Picasso.with(context).load(cn.ucai.fulicenter.R.drawable.default_avatar).into(imageView);
        }
    }

    public static Group getGroupBeanFromHXID(String hxid) {
        if (hxid != null && !hxid.isEmpty()) {
            ArrayList<Group> groupArrayList = SuperWeChatApplication.getInstance().getGroupList();
            for (Group group : groupArrayList) {
                if (group.getMGroupHxid().equals(hxid)) {
                    return group;
                }
            }
        }
        return null;
    }



    //设置群组头像
    public static void setGroupBeanAvatar(String mGroupHxid, NetworkImageView imageView) {
        if (mGroupHxid != null && !mGroupHxid.isEmpty()) {
            setGroupAvatar(getGroupAvatarPath(mGroupHxid), imageView);
        }
    }
    public static void setGroupAvatar(String url, NetworkImageView imageView) {
        if (url == null || url.isEmpty()) return;
        imageView.setDefaultImageResId(R.drawable.group_icon);
        imageView.setImageUrl(url, RequestManager.getImageLoader());
        imageView.setErrorImageResId(R.drawable.group_icon);
    }
    public static String getGroupAvatarPath(String hxid) {
        if (hxid == null || hxid.isEmpty()) return null;
        return I.REQUEST_DOWNLOAD_AVATAR_GROUP + hxid;
    }




    //设置真实的用户头像  仿写
    public static void setUserBeanAvatar(String username, NetworkImageView imageView) {
        Contact contact = getUserBeanInfo(username);
        Log.e(TAG, "真实用户头像仿写=" + contact);
        if (contact != null && contact.getMContactCname() != null) {
            setUserAvatar(getAvatarPath(username), imageView);
        }
    }

    //搜索页面的头像显示，仿写
    public static void setUserBeanAvatar(User user, NetworkImageView imageView) {
        if (user != null && user.getMUserName() != null) {
            setUserAvatar(getAvatarPath(user.getMUserName()), imageView);
        }
    }


    private static void setUserAvatar(String url, NetworkImageView imageView) {
        Log.e(TAG, "url:" + url);
        if (url == null || url.isEmpty()) return;
        imageView.setDefaultImageResId(R.drawable.default_avatar);
        imageView.setImageUrl(url, RequestManager.getImageLoader());
        imageView.setErrorImageResId(R.drawable.default_avatar);
    }

    //新加的方法
    public static String getAvatarPath(String username) {
        Log.i("main", "username:  " + username);
        if (username == null || username.isEmpty()) return null;

        return I.REQUEST_DOWNLOAD_AVATAR_USER + username;
    }

    /**
     * 设置当前用户头像
     */
    public static void setCurrentUserAvatar(Context context, ImageView imageView) {
        EMUser user = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
        if (user != null && user.getAvatar() != null) {
            Picasso.with(context).load(user.getAvatar()).placeholder(cn.ucai.fulicenter.R.drawable.default_avatar).into(imageView);
        } else {
            Picasso.with(context).load(cn.ucai.fulicenter.R.drawable.default_avatar).into(imageView);
        }
    }

    //仿写设置当前头像
    public static void setCurrentUserAvatar(NetworkImageView imageView) {
        User user = SuperWeChatApplication.getInstance().getUser();
        if (user != null) {
            setUserAvatar(getAvatarPath(user.getMUserName()), imageView);
        }
    }

    /**
     * 设置用户昵称
     */
    public static void setUserNick(String username, TextView textView) {
        EMUser EMUser = getUserInfo(username);
        if (EMUser != null) {
            textView.setText(EMUser.getNick());
        } else {
            textView.setText(username);
        }
    }

    //设置UserBean的nick
    public static void setUserBeanNick(String username, TextView textView) {
        Contact contact = getUserBeanInfo(username);
        if (contact != null) {
            if (contact.getMUserNick() != null) {
                textView.setText(contact.getMUserNick());

            } else if (contact.getMContactCname() != null) {
                textView.setText(contact.getMContactCid());
            }
        } else {
            textView.setText(username);
        }

    }

    //搜索页面昵称设置
    public static void setUserBeanNick(User user, TextView textView) {
        if (user != null) {
            if (user.getMUserNick() != null) {
                textView.setText(user.getMUserNick());
            } else if (user.getMUserName() != null) {
                textView.setText(user.getMUserName());
            }
        }
    }

    /**
     * 设置当前用户昵称
     */
    public static void setCurrentUserNick(TextView textView) {
        EMUser EMUser = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
        if (textView != null) {
            textView.setText(EMUser.getNick());
        }
    }

    //仿写详细页面昵称
    public static void setCurrentUserBeanNick(TextView textView) {
        User user = SuperWeChatApplication.getInstance().getUser();
        if (user != null && user.getMUserNick() != null && textView != null) {
            textView.setText(user.getMUserNick());
        }
    }

    /**
     * 保存或更新某个用户
     */
    public static void saveUserInfo(EMUser newEMUser) {
        if (newEMUser == null || newEMUser.getUsername() == null) {
            return;
        }
        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).saveContact(newEMUser);
    }

    public static void setUserHead(String username, Contact user) {
        String headerName = null;
        if (!TextUtils.isEmpty(user.getMUserNick())) {
            headerName = user.getMUserNick();
        } else {
            headerName = user.getMContactCname();
        }
        if (username.equals(Constant.NEW_FRIENDS_USERNAME)
                || username.equals(Constant.GROUP_USERNAME)) {
            user.setHeader("");
        } else if (Character.isDigit(headerName.charAt(0))) {
            user.setHeader("#");
        } else {
            user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1)
                    .toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }
        }

    }

    public static String getPinYinFromHanZi(String hanzi) {
        String pinyin = "";
        for (int i=0;i<hanzi.length();i++) {
            String s = hanzi.substring(i, i + 1);
            pinyin=pinyin+HanziToPinyin.getInstance()
                    .get(s).get(0).target.toLowerCase();
        }
        return  pinyin;
    }

}
