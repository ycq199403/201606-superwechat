/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
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
package cn.ucai.fulicenter.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Group;
import cn.ucai.fulicenter.utils.UserUtils;

public class GroupAdapter extends BaseAdapter implements SectionIndexer {

    private LayoutInflater inflater;
    private String newGroup;
    private String addPublicGroup;
    ArrayList<Group> mGrouplist;
    ArrayList<Group> mCopyGroupList;
    private SparseIntArray positionOfSection;
    private SparseIntArray sectionOfPosition;
    List<String> list;
    private MyFilter myFilter;
    private boolean notiyfyByFilter;
    Context mContext;

    public GroupAdapter(Context context, int res, ArrayList<Group> groups) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        newGroup = context.getResources().getString(R.string.The_new_group_chat);
        addPublicGroup = context.getResources().getString(R.string.add_public_group_chat);
        mGrouplist = groups;
        Log.i("Login", "GroupAdapter--->onCreate--->mGroupList=" + mGrouplist);
        mCopyGroupList = new ArrayList<Group>();
        mCopyGroupList.addAll(mGrouplist);
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else if (position == 1) {
            return 1;
        } else if (position == 2) {
            return 2;
        } else {
            return 3;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == 0) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.search_bar_with_padding, null);
            }
            final EditText query = (EditText) convertView.findViewById(R.id.query);
            final ImageButton clearSearch = (ImageButton) convertView.findViewById(R.id.search_clear);
            query.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    getFilter().filter(s);
                    if (s.length() > 0) {
                        clearSearch.setVisibility(View.VISIBLE);
                    } else {
                        clearSearch.setVisibility(View.INVISIBLE);
                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                }
            });
            clearSearch.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    query.getText().clear();
                }
            });
        }  else if (getItemViewType(position) == 1) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_add_group, null);
            }
            ((ImageView) convertView.findViewById(R.id.avatar)).setImageResource(R.drawable.create_group);
            ((TextView) convertView.findViewById(R.id.name)).setText(newGroup);
        } else if (getItemViewType(position) == 2) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_add_group, null);
            }
            ((ImageView) convertView.findViewById(R.id.avatar)).setImageResource(R.drawable.add_public_group);
            ((TextView) convertView.findViewById(R.id.name)).setText(addPublicGroup);
            ((TextView) convertView.findViewById(R.id.header)).setVisibility(View.VISIBLE);

        } else{

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_group, null);
            }
            Group group = getItem(position);
            Log.e("Login", "+++++++++++++++"+group);
            ((TextView) convertView.findViewById(R.id.name)).setText(group.getMGroupName());
            UserUtils.setGroupBeanAvatar(group.getMGroupHxid(),((NetworkImageView) convertView.findViewById(R.id.avatar)));
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return mGrouplist == null ? 3 : mGrouplist.size() + 3;
    }

    @Override
    public Group getItem(int position) {
        if (position >= 3) {
            return mGrouplist.get(position - 3);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void initList(ArrayList<Group> list) {
        Log.i("Login", "GroupAdapter-----> initList ---->list=" + list);
        mGrouplist.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public Object[] getSections() {
        positionOfSection = new SparseIntArray();
        sectionOfPosition = new SparseIntArray();
        int count = getCount();
        list = new ArrayList<String>();
        list.add(mContext.getString(R.string.search_header));
        positionOfSection.put(0, 0);
        sectionOfPosition.put(0, 0);
        for (int i = 1; i < count; i++) {
            String letter = getItem(i).getHeader();
            Log.i("Login", "contactadapter getsection getHeader:" + letter + " name:" + getItem(i).getMGroupName());
            int section = list.size() - 1;
            if (list.get(section) != null && !list.get(section).equals(letter)) {
                list.add(letter);
                section++;
                positionOfSection.put(section, i);
            }
            sectionOfPosition.put(i, section);
        }
        return list.toArray(new String[list.size()]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return positionOfSection.get(sectionIndex);
    }

    @Override
    public int getSectionForPosition(int position) {
        return sectionOfPosition.get(position);
    }


    public Filter getFilter() {
        if (myFilter == null) {
            myFilter = new MyFilter(mGrouplist);
        }
        return myFilter;
    }

    private class MyFilter extends Filter {
        List<Group> mOriginalList = null;

        public MyFilter(List<Group> myList) {
            this.mOriginalList = myList;
        }

        @Override
        protected synchronized FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (mOriginalList == null) {
                mOriginalList = new ArrayList<Group>();
            }
            Log.i("Login", "contacts original size: " + mOriginalList.size());
            Log.i("Login", "contacts copy size: " + mCopyGroupList.size());

            if (prefix == null || prefix.length() == 0) {
                results.values = mCopyGroupList;
                results.count = mCopyGroupList.size();
            } else {
                String prefixString = prefix.toString();
                final int count = mOriginalList.size();
                final ArrayList<Group> newValues = new ArrayList<Group>();
                for (int i = 0; i < count; i++) {
                    final Group group = mOriginalList.get(i);
                    String username = group.getMGroupName();
                    String nick = UserUtils.getPinYinFromHanZi(group.getMGroupName());
                    String id = UserUtils.getPinYinFromHanZi(group.getMGroupHxid());


                    if (username.contains(prefixString)||username.startsWith(prefixString)||id.contains(prefix)) {
                        newValues.add(group);
                    }
                    else {
                        final String[] words = username.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with space(s)
                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].startsWith(prefixString)) {
                                newValues.add(group);
                                break;
                            }
                        }
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }
            Log.i("Login", "contacts filter results size: " + results.count);
            return results;
        }

        @Override
        protected synchronized void publishResults(CharSequence constraint,
                                                   FilterResults results) {
            mGrouplist.clear();
            mGrouplist.addAll((List<Group>) results.values);
            Log.i("Login", "publish contacts filter results size: " + results.count);
            if (results.count > 0) {
                notiyfyByFilter = true;
                notifyDataSetChanged();
                notiyfyByFilter = false;
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (!notiyfyByFilter) {
            mCopyGroupList.clear();
            mCopyGroupList.addAll(mGrouplist);
        }
    }

}