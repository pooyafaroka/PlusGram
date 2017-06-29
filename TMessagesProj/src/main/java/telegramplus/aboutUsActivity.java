package telegramplus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import ir.mmnotimm.telegramplus.BuildConfig;
import ir.mmnotimm.telegramplus.R;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

import static org.telegram.messenger.AndroidUtilities.getTypeface;


public class aboutUsActivity extends BaseFragment {


    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();


        return true;
    }

    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
    }

    @Override
    public View createView(final Context context) {
        actionBar.setBackgroundColor(Theme.ACTION_BAR_MEDIA_PICKER_COLOR);
        actionBar.setItemsBackgroundColor(Theme.ACTION_BAR_PICKER_SELECTOR_COLOR);
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(LocaleController.getString("aboutUs", R.string.aboutUs));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });


        fragmentView = new FrameLayout(context);
        fragmentView.setLayoutParams(new FrameLayout.LayoutParams(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        fragmentView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.about_us_activity, null);

        ImageView b = (ImageView) fragmentView.findViewById(R.id.contact_us_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = constant.OFFICIAL_ID_URL;
                Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse(link));

                if (!BuildConfig.DEBUG) {
                    telegram.setPackage(BuildVars.BUILD_PACKAGENAME);
                } else
                    telegram.setPackage(BuildVars.BUILD_PACKAGENAME);

                context.startActivity(telegram);
            }
        });




        ImageView our_channel = (ImageView) fragmentView.findViewById(R.id.our_channel);
        our_channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link2 = constant.OFFICIALCHANNEL_URL;
                Intent telegram2 = new Intent(Intent.ACTION_VIEW, Uri.parse(link2));

                if (!BuildConfig.DEBUG) {
                    telegram2.setPackage(BuildVars.BUILD_PACKAGENAME);
                } else
                    telegram2.setPackage(BuildVars.BUILD_PACKAGENAME);

                context.startActivity(telegram2);
            }
        });

        CustomTextview txthasti = (CustomTextview) fragmentView.findViewById(R.id.txthasti);
        CustomTextview txtver = (CustomTextview) fragmentView.findViewById(R.id.txtver);
        TextView txtcontent = (TextView) fragmentView.findViewById(R.id.txtcontent);
        txthasti.setTypeface(getTypeface("fonts/iransans.ttf"));
        txtver.setTypeface(getTypeface("fonts/iransans.ttf"));
        txtcontent.setTypeface(getTypeface("fonts/iransans.ttf"));




        return fragmentView;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateTheme();
        if (constant.AnalyticInitialized)
            ApplicationLoader.getInstance().trackScreenView("About us Activity");
    }

    private void updateTheme() {
        SharedPreferences themePrefs = ApplicationLoader.applicationContext.getSharedPreferences(AndroidUtilities.THEME_PREFS, AndroidUtilities.THEME_PREFS_MODE);
        int def = themePrefs.getInt("themeColor", AndroidUtilities.defColor);
        actionBar.setBackgroundColor(themePrefs.getInt("prefHeaderColor", def));
        actionBar.setTitleColor(themePrefs.getInt("prefHeaderTitleColor", 0xffffffff));

        Drawable back = getParentActivity().getResources().getDrawable(R.drawable.ic_ab_back);
        back.setColorFilter(themePrefs.getInt("prefHeaderIconsColor", 0xffffffff), PorterDuff.Mode.MULTIPLY);
        actionBar.setBackButtonDrawable(back);
    }


}
