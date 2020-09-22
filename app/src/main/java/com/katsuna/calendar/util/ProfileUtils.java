/**
* Copyright (C) 2020 Manos Saratsis
*
* This file is part of Katsuna.
*
* Katsuna is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Katsuna is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Katsuna.  If not, see <https://www.gnu.org/licenses/>.
*/
package com.katsuna.calendar.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.widget.ToggleButton;

import com.katsuna.commons.entities.ColorProfile;
import com.katsuna.commons.entities.ColorProfileKey;
import com.katsuna.commons.utils.ColorCalc;
import com.katsuna.commons.utils.DrawUtils;

public class ProfileUtils {

//    public static void applyColor(Context context, ToggleButton button, ColorProfile profile) {

//        int color1 = ColorCalc.getColor(context, ColorProfileKey.ACCENT1_COLOR, profile);
//        int color2 = ColorCalc.getColor(context, ColorProfileKey.ACCENT2_COLOR, profile);
//
//        StateListDrawable stateListDrawable = (StateListDrawable) button.getBackground();
//        LayerDrawable enabledLayer = (LayerDrawable) getCheckedState(stateListDrawable);
//        GradientDrawable mainBg = (GradientDrawable) enabledLayer.getDrawable(0);
//        mainBg.setColor(color1);
//
//        GradientDrawable bar = (GradientDrawable) enabledLayer.getDrawable(1);
//        bar.setColor(color2);
//
//        int grey600 = ContextCompat.getColor(context, com.katsuna.commons.R.color.common_grey600);
//        Drawable[] drawables = button.getCompoundDrawables();
//
//        if (profile == ColorProfile.CONTRAST) {
//            button.setTextColor(ContextCompat.getColorStateList(context,
//                    com.katsuna.commons.R.color.common_toggle_contrast_text_color));
//
//            if (drawables[0] != null) {
//                if (drawables[0] instanceof StateListDrawable) {
//                    Drawable checkedDrawable = getCheckedState((StateListDrawable)drawables[0]);
//                    DrawUtils.setColor(checkedDrawable, color2);
//                }
//            }
//        } else {
//            button.setTextColor(grey600);
//            if (drawables[0] instanceof StateListDrawable) {
//                Drawable checkedDrawable = getCheckedState((StateListDrawable)drawables[0]);
//                DrawUtils.setColor(checkedDrawable, grey600);
//            }
//        }
//    }
}
