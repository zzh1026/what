package com.neishenme.what.adapter;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
public abstract class AddHobbySelectableAdapter extends BaseAdapter {
    //选择的数据
    protected List<String> selectedStr;

    public AddHobbySelectableAdapter() {
        selectedStr = new ArrayList<>();
    }

    //判断是否被选中
    public boolean isSelected(String str) {
        for (int i = 0; i < getSelectedItemCount(); i++) {
            if (selectedStr.get(i).equals(str)) {
                return true;
            }
        }
        return false;
    }


    public void toggleSelection(String str) {
        boolean isContains = false;
        for (int i = 0; i < getSelectedItemCount(); i++) {
            if (selectedStr.get(i).equals(str)) {
                selectedStr.remove(i);
                isContains = true;
            }
        }
        if (isContains == false) {
            selectedStr.add(str);
        }
    }

    public void clearSelection() {
        selectedStr.clear();
    }

    public List<String> getSelectedList() {
        return selectedStr;
    }

    public void setSelectedList(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            selectedStr.add(list.get(i));
        }
    }

    public void setSelected(String str){
        selectedStr.add(str);

    }


    public int getSelectedItemCount() {
        return selectedStr.size();
    }

}
