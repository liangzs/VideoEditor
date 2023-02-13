/*
 * Copyright (C) 2015 Tomás Ruiz-López.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qiusuo.videoeditor.ui.widgegt.selection;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.qiusuo.videoeditor.R;


/**
 * Created by tomas on 01/06/15.
 */
public class HeaderViewHolder extends RecyclerView.ViewHolder {

    protected TextView titleText = null;
    protected AppCompatImageView selectAll;

    public HeaderViewHolder(View itemView, @IdRes int titleID) {
        super(itemView);
        titleText = (TextView) itemView.findViewById(titleID);
        selectAll = itemView.findViewById(R.id.select_all);
    }

    public void render(String title) {
        titleText.setText(title);
    }

    public void showSelect(boolean show) {
        if (selectAll != null) {
            selectAll.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }


    public AppCompatImageView getSelectAll() {
        return selectAll;
    }
}
