package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Response;

import java.util.ArrayList;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.SuperWeChatApplication;
import cn.ucai.fulicenter.activity.BaseActivity;
import cn.ucai.fulicenter.bean.Group;
import cn.ucai.fulicenter.data.ApiParams;
import cn.ucai.fulicenter.data.GsonRequest;
import cn.ucai.fulicenter.utils.Utils;

/**
 * Created by Administrator on 2016/5/23 0023.
 */
public class DownloadAllGroupTask extends BaseActivity {
    private static final String TAG = DownloadAllGroupTask.class.getName();
    Context mContext;
    String username;
    String path;

    public DownloadAllGroupTask(Context mContext, String username) {
        this.mContext = mContext;
        this.username = username;
        initPath();

    }

    private void initPath() {
        try {
            path = new ApiParams()
                    .with(I.Contact.USER_NAME, username)
                    .getRequestUrl(I.REQUEST_DOWNLOAD_GROUPS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute() {
        executeRequest(new GsonRequest<Group[]>(path, Group[].class,
                responseDownloadAllGroupTaskListener(), errorListener()));
    }

    private Response.Listener<Group[]> responseDownloadAllGroupTaskListener() {
        return new Response.Listener<Group[]>() {
            @Override
            public void onResponse(Group[] groups) {
                if (groups != null && groups.length>0) {
                    ArrayList<Group> list = Utils.array2List(groups);
                    ArrayList<Group> publicGroupList = SuperWeChatApplication
                            .getInstance()
                            .getPublicGroupList();
                    for (Group gs : list) {
                        if (!publicGroupList.contains(gs)) {
                            publicGroupList.add(gs);

                        }
                    }
                    mContext.sendStickyBroadcast(new Intent("update_group_list"));
                }
            }

        };

    }
}



