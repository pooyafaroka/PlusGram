/*
 * This is the source code of Telegram for Android v. 3.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2016.
 */

package org.telegram.ui.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import Utility.X_DrawableMenu;
import ir.mmnotimm.telegramplus.R;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.Cells.DividerCell;
import org.telegram.ui.Cells.DrawerActionCell;
import org.telegram.ui.Cells.DrawerProfileCell;
import org.telegram.ui.Cells.EmptyCell;
import org.telegram.ui.Cells.TextInfoCell;
import org.telegram.ui.LaunchActivity;

import java.util.ArrayList;

public class DrawerLayoutAdapter extends BaseAdapter {

    private ArrayList<X_DrawableMenu> drawableMenuList;
    private Context mContext;
    private DrawerActionCell actionCell;

    public DrawerLayoutAdapter(Context context, ArrayList<X_DrawableMenu> drawableMenuList) {
        mContext = context;
        this.drawableMenuList = drawableMenuList;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return !( i == 1 || i == 50 || i == 110 || i == 130 );
    }

    @Override
    public int getCount() {
        return UserConfig.isClientActivated() ? drawableMenuList.size() + 2:0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        SharedPreferences themePrefs = ApplicationLoader.applicationContext.getSharedPreferences(AndroidUtilities.THEME_PREFS, AndroidUtilities.THEME_PREFS_MODE);
        if (position == 0)
        {
            if (view == null)
            {
                view = new DrawerProfileCell(mContext);
            }
            ((DrawerProfileCell) view).setUser(MessagesController.getInstance().getUser(UserConfig.getClientUserId()));
            ((DrawerProfileCell) view).refreshAvatar(themePrefs.getInt("drawerAvatarSize", 64), themePrefs.getInt("drawerAvatarRadius", 32));
        }
        else if (position == 1)
        {
            if (view == null)
            {
                view = new EmptyCell(mContext, AndroidUtilities.dp(8));
            }
            updateViewColor(view);
        }
        else
        {
            if (view == null)
            {
                view = new DrawerActionCell(mContext);
            }
            updateViewColor(view);
            actionCell = (DrawerActionCell) view;
            actionCell.setTextAndIcon(this.drawableMenuList.get(position - 2).getText(), this.drawableMenuList.get(position - 2).getDrawable());

        }
        return view;
    }

    private void updateViewColor(View v) {
        SharedPreferences themePrefs = ApplicationLoader.applicationContext.getSharedPreferences(AndroidUtilities.THEME_PREFS, AndroidUtilities.THEME_PREFS_MODE);
        int mainColor = themePrefs.getInt("drawerListColor", 0xffffffff);
        int value = themePrefs.getInt("drawerRowGradient", 0);
        boolean b = true;//themePrefs.getBoolean("drawerRowGradientListCheck", false);
        if (value > 0 && !b) {
            GradientDrawable.Orientation go;
            switch (value) {
                case 2:
                    go = GradientDrawable.Orientation.LEFT_RIGHT;
                    break;
                case 3:
                    go = GradientDrawable.Orientation.TL_BR;
                    break;
                case 4:
                    go = GradientDrawable.Orientation.BL_TR;
                    break;
                default:
                    go = GradientDrawable.Orientation.TOP_BOTTOM;
            }

            int gradColor = themePrefs.getInt("drawerRowGradientColor", 0xffffffff);
            int[] colors = new int[]{mainColor, gradColor};
            GradientDrawable gd = new GradientDrawable(go, colors);
            v.setBackgroundDrawable(gd);
        }
    }

    @Override
    public int getItemViewType(int i) {
        if (i == 0) {
            return 0;
        } else if (i == 1) {
            return 1;
        }
        return 3;
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public boolean isEmpty() {
        return !UserConfig.isClientActivated();
    }
}
