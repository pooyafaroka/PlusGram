package telegramplus;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.File;

import static android.content.Context.CLIPBOARD_SERVICE;
import static org.telegram.messenger.AndroidUtilities.getTypeface;




public class ReportHelpActivity  extends BaseFragment {

        int copy = 1 ;
        int spambot = 2 ;

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
        actionBar.setTitle(LocaleController.getString("ReportProblemHelp", R.string.ReportProblemHelp));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }else if (id == spambot){
                    String link_spam_bot = constant.Spam_BOT_URL;
                    Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse(link_spam_bot));

                    if (!BuildConfig.DEBUG) {
                        telegram.setPackage(BuildVars.BUILD_PACKAGENAME);
                    } else
                        telegram.setPackage(BuildVars.BUILD_PACKAGENAME);

                    context.startActivity(telegram);
                }else if (id==copy){
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label",context.getText( R.string.ReportProblemHelpDetail22));
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(context, "پیام در کلیپورد ذخیره شد.", Toast.LENGTH_SHORT).show();

                }
            }
        });
        actionBar.createMenu().addItem(copy, R.drawable.ic_copy);
        actionBar.createMenu().addItem(spambot, R.drawable.ic_spam_bot_light);






        fragmentView = new FrameLayout(context);
        fragmentView.setLayoutParams(new FrameLayout.LayoutParams(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));


        fragmentView = new LinearLayout(context);
        ((LinearLayout) fragmentView).setOrientation(LinearLayout.VERTICAL);
        ScrollView scrollView = new ScrollView(context);
        ((LinearLayout) fragmentView).addView(scrollView, LayoutHelper.createLinear(-1, -1));
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView textView = new TextView(context);
        textView.setTextSize(1, 18.0f);
        textView.setTextColor(Theme.MSG_RED_TEXT_COLOR);
        textView.setGravity(5);
        textView.setTypeface(getTypeface("fonts/rmedium.ttf"));
        textView.setText(AndroidUtilities.replaceTags(LocaleController.getString("ReportProblemHelpDetail1", R.string.ReportProblemHelpDetail1)));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -1, LocaleController.isRTL ? 5 : 3, 15, 10, 15, 0));
        textView = new TextView(context);
        textView.setTextSize(1, 17.0f);
        textView.setTextColor(0xFF222222);
        textView.setGravity(5);
        textView.setTypeface(getTypeface("fonts/rmedium.ttf"));
        textView.setText(AndroidUtilities.replaceTags(LocaleController.getString("ReportProblemHelpDetail2", R.string.ReportProblemHelpDetail2)));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -1, LocaleController.isRTL ? 5 : 3, 15, 10, 15, 0));
        textView = new TextView(context);
        textView.setTextSize(1, 18.0f);
        textView.setTextColor(Theme.MSG_RED_TEXT_COLOR);
        textView.setGravity(5);
        textView.setTypeface(getTypeface("fonts/rmedium.ttf"));
        textView.setText(AndroidUtilities.replaceTags(LocaleController.getString("ReportProblemHelpDetail3", R.string.ReportProblemHelpDetail3)));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -1, LocaleController.isRTL ? 5 : 3, 15, 10, 15, 0));
        textView = new TextView(context);
        textView.setTextSize(1, 17.0f);
        textView.setTextColor(0xFF222222);
        textView.setGravity(5);
        textView.setTypeface(getTypeface("fonts/rmedium.ttf"));
        textView.setText(AndroidUtilities.replaceTags(LocaleController.getString("ReportProblemHelpDetail4", R.string.ReportProblemHelpDetail4)));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -1, LocaleController.isRTL ? 5 : 3, 15, 10, 15, 0));
        textView = new TextView(context);
        textView.setTextSize(1, 18.0f);
        textView.setTextColor(Theme.MSG_RED_TEXT_COLOR);
        textView.setGravity(5);
        textView.setTypeface(getTypeface("fonts/rmedium.ttf"));
        textView.setText(AndroidUtilities.replaceTags(LocaleController.getString("ReportProblemHelpDetail5", R.string.ReportProblemHelpDetail5)));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -1, LocaleController.isRTL ? 5 : 3, 15, 10, 15, 0));
        textView = new TextView(context);
        textView.setTextSize(1, 17.0f);
        textView.setTextColor(0xFF222222);
        textView.setGravity(5);
        textView.setTypeface(getTypeface("fonts/rmedium.ttf"));
        textView.setText(AndroidUtilities.replaceTags(LocaleController.getString("ReportProblemHelpDetail6", R.string.ReportProblemHelpDetail6)));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -1, LocaleController.isRTL ? 5 : 3, 15, 10, 15, 0));
        textView = new TextView(context);
        textView.setTextSize(1, 18.0f);
        textView.setTextColor(Theme.MSG_TEXT_COLOR);
        textView.setGravity(5);
        textView.setTypeface(getTypeface("fonts/rmedium.ttf"));
        textView.setText(AndroidUtilities.replaceTags(LocaleController.getString("ReportProblemHelpDetail7", R.string.ReportProblemHelpDetail7)));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -1, LocaleController.isRTL ? 5 : 3, 15, 10, 15, 0));
        textView = new TextView(context);
        textView.setTextSize(1, 18.0f);
        textView.setTextColor(Theme.MSG_TEXT_COLOR);
        textView.setGravity(5);
        textView.setTypeface(getTypeface("fonts/rmedium.ttf"));
        textView.setText(AndroidUtilities.replaceTags(LocaleController.getString("ReportProblemHelpDetail8", R.string.ReportProblemHelpDetail8)));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -1, LocaleController.isRTL ? 5 : 3, 15, 10, 15, 0));
        textView = new TextView(context);
        textView.setTextSize(1, 17.0f);
        textView.setTextColor(0xFF222222);
        textView.setGravity(5);
        textView.setTypeface(getTypeface("fonts/rmedium.ttf"));
        textView.setText(AndroidUtilities.replaceTags(LocaleController.getString("ReportProblemHelpDetail9", R.string.ReportProblemHelpDetail9)));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -1, LocaleController.isRTL ? 5 : 3, 15, 10, 15, 0));
        textView = new TextView(context);
        textView.setTextSize(1, 18.0f);
        textView.setTextColor(Theme.MSG_TEXT_COLOR);
        textView.setGravity(5);
        textView.setTypeface(getTypeface("fonts/rmedium.ttf"));
        textView.setText(AndroidUtilities.replaceTags(LocaleController.getString("ReportProblemHelpDetail20", R.string.ReportProblemHelpDetail20)));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -1, LocaleController.isRTL ? 5 : 3, 15, 10, 15, 0));
        textView = new TextView(context);
        textView.setTextSize(1, 17.0f);
        textView.setTextColor(0xFF222222);
        textView.setGravity(5);
        textView.setTypeface(getTypeface("fonts/rmedium.ttf"));
        textView.setText(AndroidUtilities.replaceTags(LocaleController.getString("ReportProblemHelpDetail21", R.string.ReportProblemHelpDetail21)));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -1, LocaleController.isRTL ? 5 : 3, 15, 10, 15, 0));
        textView = new TextView(context);
        textView.setTextSize(1, 18.0f);
        textView.setTextColor(Theme.MSG_TEXT_COLOR);
        textView.setGravity(5);
        textView.setTypeface(getTypeface("fonts/rmedium.ttf"));
        textView.setText(AndroidUtilities.replaceTags(LocaleController.getString("ReportProblemHelpDetail22", R.string.ReportProblemHelpDetail22)));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -1, LocaleController.isRTL ? 5 : 3, 15, 10, 15, 0));
        textView = new TextView(context);
        textView.setTextSize(1, 17.0f);
        textView.setTextColor(0xFF222222);
        textView.setGravity(5);
        textView.setTypeface(getTypeface("fonts/rmedium.ttf"));
        textView.setText(AndroidUtilities.replaceTags(LocaleController.getString("ReportProblemHelpDetail23", R.string.ReportProblemHelpDetail23)));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -1, LocaleController.isRTL ? 5 : 3, 15, 10, 15, 0));
        scrollView.addView(linearLayout, LayoutHelper.createLinear(-1, -1));
        return fragmentView;


    }


    @Override
    public void onResume() {
        super.onResume();
        updateTheme();
        if (constant.AnalyticInitialized)
            ApplicationLoader.getInstance().trackScreenView("user Changes");
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

    public static void SendMAil(Context context, String str, String str2, String str3, File file) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("plain/text");
        intent.putExtra("android.intent.extra.EMAIL", new String[]{str});
        intent.putExtra("android.intent.extra.SUBJECT", str2);
        if (file != null) {
            intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(file));
        }
        intent.putExtra("android.intent.extra.TEXT", str3);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.send_email)));
    }


}

