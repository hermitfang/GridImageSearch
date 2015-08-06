package com.hermitfang.gridimagesearch.listeners;

import android.view.View;
import android.widget.AdapterView;

import com.hermitfang.gridimagesearch.R;
import com.hermitfang.gridimagesearch.activities.SearchFilterActivity;

public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    public void onItemSelected(AdapterView parent, View view, int pos,long id) {
        String str = parent.getItemAtPosition(pos).toString();
        if (str.equals("Any")) {
            str = "";
        }

        switch (parent.getId()) {
            case R.id.spImageSize:
                SearchFilterActivity.pickedSize = str;
                break;
            case R.id.spColorFilter:
                SearchFilterActivity.pickedColor = str;
                break;
            case R.id.spImageType:
                SearchFilterActivity.pickedType = str;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView arg0) {
        // TODO Auto-generated method stub
    }
}
