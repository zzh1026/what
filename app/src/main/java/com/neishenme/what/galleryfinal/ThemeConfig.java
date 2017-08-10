/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.neishenme.what.galleryfinal;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.neishenme.what.R;

import java.io.Serializable;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/16 下午2:49
 */
public class ThemeConfig implements Serializable {

    //默认主题
    public static ThemeConfig DEFAULT = new ThemeConfig.Builder().build();
    //黑色主题
    public static ThemeConfig BLACK = new ThemeConfig.Builder()
            .setTitleBarBgColor(Color.BLACK)
            .setTitleBarTextColor(Color.WHITE)
            .setTitleBarIconColor(Color.WHITE)
            .setFabNornalColor(Color.BLACK)
            .setFabPressedColor(Color.BLACK)
            .setCheckNornalColor(Color.WHITE)
            .setCheckSelectedColor(Color.WHITE)
            .setIconBack(R.drawable.icon_back2x)
            .setIconCrop(R.drawable.ic_action_crop)
            .setCropControlColor(Color.BLACK)
            .build();


    private int titleBarTextColor;
    private int titleBarBgColor;
    private int titleBarIconColor;
    private int checkNornalColor;
    private int checkSelectedColor;
    private int fabNornalColor;
    private int fabPressedColor;
    private int cropControlColor;

    private int iconBack;
    private int iconCamera;
    private int iconCrop;
    private int iconRotate;
    private int iconClear;
    private int iconFolderArrow;
    private int iconDelete;
    private int iconCheck;
    private int iconFab;
    private int iconPreview;

    private Drawable bgEditTexture;
    private Drawable bgPreveiw;

    private ThemeConfig(Builder builder) {
        this.titleBarTextColor = builder.titleBarTextColor;
        this.titleBarBgColor = builder.titleBarBgColor;
        this.titleBarIconColor = builder.titleBarIconColor;
        this.checkNornalColor = builder.checkNornalColor;
        this.checkSelectedColor = builder.checkSelectedColor;
        this.fabNornalColor = builder.fabNornalColor;
        this.fabPressedColor = builder.fabPressedColor;
        this.cropControlColor = builder.cropControlColor;
        this.iconBack = builder.iconBack;
        this.iconCamera = builder.iconCamera;
        this.iconCrop = builder.iconCrop;
        this.iconRotate = builder.iconRotate;
        this.iconClear = builder.iconClear;
        this.iconDelete = builder.iconDelete;
        this.iconFolderArrow = builder.iconFolderArrow;
        this.iconCheck = builder.iconCheck;
        this.iconFab = builder.iconFab;
        this.bgEditTexture = builder.bgEditTexture;
        this.iconPreview = builder.iconPreview;
        this.bgPreveiw = builder.bgPreveiw;
    }

    public static class Builder {
        private int titleBarTextColor = Color.WHITE;
        private int titleBarBgColor = Color.rgb(0x3F, 0x51, 0xB5);
        private int titleBarIconColor = Color.WHITE;
        private int checkNornalColor = Color.rgb(0xd2, 0xd2, 0xd7);
        private int checkSelectedColor = Color.rgb(0x3F, 0x51, 0xB5);
        private int fabNornalColor = Color.rgb(0x3F, 0x51, 0xB5);
        private int fabPressedColor = Color.rgb(0x30, 0x3f, 0x9f);
        private int cropControlColor = Color.rgb(0x3F, 0x51, 0xB5);

        private int iconBack = R.drawable.ic_gf_back;
        private int iconCamera = R.drawable.ic_gf_camera;
        private int iconCrop = R.drawable.ic_gf_crop;
        private int iconRotate = R.drawable.ic_gf_rotate;
        private int iconClear = R.drawable.ic_gf_clear;
        private int iconFolderArrow = R.drawable.ic_gf_triangle_arrow;
        private int iconDelete = R.drawable.ic_delete_photo;
        private int iconCheck = R.drawable.ic_folder_check;
        private int iconFab = R.drawable.ic_folder_check;
        private int iconPreview = R.drawable.ic_gf_preview;

        private Drawable bgEditTexture;
        private Drawable bgPreveiw;

        public Builder setTitleBarTextColor(int titleBarTextColor) {
            this.titleBarTextColor = titleBarTextColor;
            return this;
        }

        public Builder setTitleBarBgColor(int titleBarBgColor) {
            this.titleBarBgColor = titleBarBgColor;
            return this;
        }

        public Builder setTitleBarIconColor(int iconColor) {
            this.titleBarIconColor = iconColor;
            return this;
        }

        public Builder setCheckNornalColor(int checkNornalColor) {
            this.checkNornalColor = checkNornalColor;
            return this;
        }

        public Builder setCheckSelectedColor(int checkSelectedColor) {
            this.checkSelectedColor = checkSelectedColor;
            return this;
        }

        public Builder setCropControlColor(int cropControlColor) {
            this.cropControlColor = cropControlColor;
            return this;
        }

        public Builder setFabNornalColor(int fabNornalColor) {
            this.fabNornalColor = fabNornalColor;
            return this;
        }

        public Builder setFabPressedColor(int fabPressedColor) {
            this.fabPressedColor = fabPressedColor;
            return this;
        }

        public Builder setIconBack(int iconBack) {
            this.iconBack = iconBack;
            return this;
        }

        public Builder setIconCamera(int iconCamera) {
            this.iconCamera = iconCamera;
            return this;
        }

        public Builder setIconCrop(int iconCrop) {
            this.iconCrop = iconCrop;
            return this;
        }

        public Builder setIconRotate(int iconRotate) {
            this.iconRotate = iconRotate;
            return this;
        }

        public Builder setIconClear(int iconClear) {
            this.iconClear = iconClear;
            return this;
        }

        public Builder setIconFolderArrow(int iconFolderArrow) {
            this.iconFolderArrow = iconFolderArrow;
            return this;
        }

        public Builder setIconDelete(int iconDelete) {
            this.iconDelete = iconDelete;
            return this;
        }

        public Builder setIconCheck(int iconCheck) {
            this.iconCheck = iconCheck;
            return this;
        }

        public Builder setIconFab(int iconFab) {
            this.iconFab = iconFab;
            return this;
        }

        public Builder setEditPhotoBgTexture(Drawable bgEditTexture) {
            this.bgEditTexture = bgEditTexture;
            return this;
        }

        public Builder setIconPreview(int iconPreview) {
            this.iconPreview = iconPreview;
            return this;
        }

        public Builder setPreviewBg(Drawable bgPreveiw) {
            this.bgPreveiw = bgPreveiw;
            return this;
        }

        public ThemeConfig build() {
            return new ThemeConfig(this);
        }
    }

    public int getTitleBarTextColor() {
        return titleBarTextColor;
    }

    public int getTitleBarBgColor() {
        return titleBarBgColor;
    }

    public int getCheckNornalColor() {
        return checkNornalColor;
    }

    public int getCheckSelectedColor() {
        return checkSelectedColor;
    }

    public int getTitleBarIconColor() {
        return titleBarIconColor;
    }

    public int getFabNornalColor() {
        return fabNornalColor;
    }

    public int getFabPressedColor() {
        return fabPressedColor;
    }

    public int getCropControlColor() {
        return cropControlColor;
    }

    public int getIconBack() {
        return iconBack;
    }

    public int getIconCamera() {
        return iconCamera;
    }

    public int getIconCrop() {
        return iconCrop;
    }

    public int getIconRotate() {
        return iconRotate;
    }

    public int getIconClear() {
        return iconClear;
    }

    public int getIconFolderArrow() {
        return iconFolderArrow;
    }

    public int getIconDelete() {
        return iconDelete;
    }

    public int getIconCheck() {
        return iconCheck;
    }

    public int getIconFab() {
        return iconFab;
    }

    public int getIconPreview() {
        return iconPreview;
    }

    public Drawable getPreviewBg() {
        return bgPreveiw;
    }

    public Drawable getEditPhotoBgTexture() {
        return bgEditTexture;
    }
}
