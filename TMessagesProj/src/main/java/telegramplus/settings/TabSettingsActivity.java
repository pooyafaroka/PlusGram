package telegramplus.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import ir.mmnotimm.telegramplus.R;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Adapters.BaseFragmentAdapter;
import org.telegram.ui.Cells.EmptyCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextDetailSettingsCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.NumberPicker;


public class TabSettingsActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {

    private ListView listView;
    private ListAdapter listAdapter;

    private int overscrollRow;
    private int emptyRow;

    private int rowCount;
    private int dialogsSectionRow;
    private int dialogsSectionRow2;
    private int dialogsHideTabsCheckRow;
    private int dialogsTabsHeightRow;
    private int dialogsTabsRow;
    private int dialogsDisableTabsAnimationCheckRow;
    private int dialogsInfiniteTabsSwipe;
    private int dialogsHideTabsCounters;
    private int dialogsTabsCountersCountChats;
    private int dialogsTabsCountersCountNotMuted;




    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();

        NotificationCenter.getInstance().addObserver(this, NotificationCenter.refreshTabs);

        rowCount = 0;
        overscrollRow = -1;
        emptyRow = -1;



        dialogsSectionRow = rowCount++;
        dialogsSectionRow2 = rowCount++;

        dialogsHideTabsCheckRow = rowCount++;
        dialogsTabsRow = rowCount++;
        dialogsTabsHeightRow = rowCount++;
        dialogsDisableTabsAnimationCheckRow = rowCount++;
        dialogsInfiniteTabsSwipe = rowCount++;
        dialogsHideTabsCounters = rowCount++;
        dialogsTabsCountersCountNotMuted = rowCount++;
        dialogsTabsCountersCountChats = rowCount++;


        MessagesController.getInstance().loadFullUser(UserConfig.getCurrentUser(), classGuid, true);

        return true;
    }

    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.refreshTabs);
    }

    @Override
    public View createView(Context context) {
        //actionBar.setItemsBackground(AvatarDrawable.getButtonColorForId(5));
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);

        if (AndroidUtilities.isTablet()) {
            actionBar.setOccupyStatusBar(false);
        }
        actionBar.setTitle(LocaleController.getString("TabSettings", R.string.TabSettings));

        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });

        listAdapter = new ListAdapter(context);

        fragmentView = new FrameLayout(context);
        FrameLayout frameLayout = (FrameLayout) fragmentView;


        listView = new ListView(context);
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences(AndroidUtilities.THEME_PREFS, AndroidUtilities.THEME_PREFS_MODE);
        listView.setBackgroundColor(preferences.getInt("prefBGColor", 0xffffffff));
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setVerticalScrollBarEnabled(false);

        listView.setAdapter(listAdapter);

        int bgColor = preferences.getInt("prefBGColor", 0xffffffff);
        int def = preferences.getInt("themeColor", AndroidUtilities.defColor);
        int hColor = preferences.getInt("prefHeaderColor", def);

        AndroidUtilities.setListViewEdgeEffectColor(listView, hColor);
        frameLayout.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP | Gravity.LEFT));
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                 if (i == dialogsHideTabsCheckRow) {
                    SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);
                    boolean hide = preferences.getBoolean("hideTabs", false);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("hideTabs", !hide);
                    editor.apply();

                    boolean hideUsers = preferences.getBoolean("hideUsers", false);
                    boolean hideGroups = preferences.getBoolean("hideGroups", false);
                    boolean hideSGroups = preferences.getBoolean("hideSGroups", false);
                    boolean hideChannels = preferences.getBoolean("hideChannels", false);
                    boolean hideBots = preferences.getBoolean("hideBots", false);
                    boolean hideFavs = preferences.getBoolean("hideFavs", false);
                    if (hideUsers && hideGroups && hideSGroups && hideChannels && hideBots && hideFavs) {
                        //editor.putBoolean("hideUsers", false).apply();
                        //editor.putBoolean("hideGroups", false).apply();
                        if (listView != null) {
                            listView.invalidateViews();
                        }
                    }
                    NotificationCenter.getInstance().postNotificationName(NotificationCenter.refreshTabs, 10);
                    if (view instanceof TextCheckCell) {
                        ((TextCheckCell) view).setChecked(!hide);
                    }
                } else if (i == dialogsDisableTabsAnimationCheckRow) {
                    SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);
                    boolean disable = preferences.getBoolean("disableTabsAnimation", true);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("disableTabsAnimation", !disable);
                    editor.apply();
                    NotificationCenter.getInstance().postNotificationName(NotificationCenter.refreshTabs, 11);
                    if (view instanceof TextCheckCell) {
                        ((TextCheckCell) view).setChecked(!disable);
                    }
                } else if (i == dialogsInfiniteTabsSwipe) {
                    SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);
                    boolean disable = preferences.getBoolean("infiniteTabsSwipe", false);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("infiniteTabsSwipe", !disable);
                    editor.apply();
                    if (view instanceof TextCheckCell) {
                        ((TextCheckCell) view).setChecked(!disable);
                    }
                } else if (i == dialogsHideTabsCounters) {
                    SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);
                    boolean disable = preferences.getBoolean("hideTabsCounters", false);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("hideTabsCounters", !disable);
                    editor.apply();
                    if (view instanceof TextCheckCell) {
                        ((TextCheckCell) view).setChecked(!disable);
                    }
                } else if (i == dialogsTabsCountersCountChats) {
                    SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);
                    boolean disable = preferences.getBoolean("tabsCountersCountChats", false);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("tabsCountersCountChats", !disable);
                    editor.apply();
                    if (view instanceof TextCheckCell) {
                        ((TextCheckCell) view).setChecked(!disable);
                    }
                } else if (i == dialogsTabsCountersCountNotMuted) {
                    SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);
                    boolean disable = preferences.getBoolean("tabsCountersCountNotMuted", false);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("tabsCountersCountNotMuted", !disable);
                    editor.apply();
                    if (view instanceof TextCheckCell) {
                        ((TextCheckCell) view).setChecked(!disable);
                    }
                } else if (i == dialogsTabsHeightRow) {
                    if (getParentActivity() == null) {
                        return;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                    builder.setTitle(LocaleController.getString("TabsHeight", R.string.TabsHeight));
                    final NumberPicker numberPicker = new NumberPicker(getParentActivity());
                    numberPicker.setMinValue(30);
                    numberPicker.setMaxValue(48);
                    SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);
                    numberPicker.setValue(preferences.getInt("tabsHeight", AndroidUtilities.isTablet() ? 42 : 40));
                    builder.setView(numberPicker);
                    builder.setNegativeButton(LocaleController.getString("Done", R.string.Done), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt("tabsHeight", numberPicker.getValue());
                            editor.apply();
                            NotificationCenter.getInstance().postNotificationName(NotificationCenter.refreshTabs, 12);
                            if (listView != null) {
                                listView.invalidateViews();
                            }
                        }
                    });
                    showDialog(builder.create());
                } else if (i == dialogsTabsRow) {
                    if (getParentActivity() == null) {
                        return;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                    createTabsDialog(builder);
                    builder.setNegativeButton(LocaleController.getString("Done", R.string.Done), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NotificationCenter.getInstance().postNotificationName(NotificationCenter.refreshTabs, 13);
                            if (listView != null) {
                                listView.invalidateViews();
                            }
                        }
                    });
                    showDialog(builder.create());
                }
            }
        });

        frameLayout.addView(actionBar);

        return fragmentView;
    }

    private AlertDialog.Builder createTabsDialog(AlertDialog.Builder builder) {
        builder.setTitle(LocaleController.getString("HideShowTabs", R.string.HideShowTabs));

        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);
        boolean hideUsers = preferences.getBoolean("hideUsers", false);
        boolean hideGroups = preferences.getBoolean("hideGroups", false);
        boolean hideSGroups = preferences.getBoolean("hideSGroups", false);
        boolean hideChannels = preferences.getBoolean("hideChannels", false);
        boolean hideBots = preferences.getBoolean("hideBots", false);
        boolean hideFavs = preferences.getBoolean("hideFavs", false);

        builder.setMultiChoiceItems(
                new CharSequence[]{LocaleController.getString("Users", R.string.Users), LocaleController.getString("Groups", R.string.Groups), LocaleController.getString("SuperGroups", R.string.SuperGroups), LocaleController.getString("Channels", R.string.Channels), LocaleController.getString("Bots", R.string.Bots), LocaleController.getString("Favorites", R.string.Favorites)},
                new boolean[]{!hideUsers, !hideGroups, !hideSGroups, !hideChannels, !hideBots, !hideFavs},
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        if (which == 0) {
                            editor.putBoolean("hideUsers", !isChecked);
                        } else if (which == 1) {
                            editor.putBoolean("hideGroups", !isChecked);
                        } else if (which == 2) {
                            editor.putBoolean("hideSGroups", !isChecked);
                        } else if (which == 3) {
                            editor.putBoolean("hideChannels", !isChecked);
                        } else if (which == 4) {
                            editor.putBoolean("hideBots", !isChecked);
                        } else if (which == 5) {
                            editor.putBoolean("hideFavs", !isChecked);
                        }
                        editor.apply();

                        boolean hideUsers = preferences.getBoolean("hideUsers", false);
                        boolean hideGroups = preferences.getBoolean("hideGroups", false);
                        boolean hideSGroups = preferences.getBoolean("hideSGroups", false);
                        boolean hideChannels = preferences.getBoolean("hideChannels", false);
                        boolean hideBots = preferences.getBoolean("hideBots", false);
                        boolean hideFavs = preferences.getBoolean("hideFavs", false);
                        if (hideUsers && hideGroups && hideSGroups && hideChannels && hideBots && hideFavs) {
                            editor.putBoolean("hideTabs", true);
                            editor.apply();
                            if (listView != null) {
                                listView.invalidateViews();
                            }
                        }
                        NotificationCenter.getInstance().postNotificationName(NotificationCenter.refreshTabs, which);
                    }
                });
        return builder;
    }

    private AlertDialog.Builder createSharedOptions(AlertDialog.Builder builder) {
        builder.setTitle(LocaleController.getString("SharedMedia", R.string.SharedMedia));

        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);
        boolean hideMedia = preferences.getBoolean("hideSharedMedia", false);
        boolean hideFiles = preferences.getBoolean("hideSharedFiles", false);
        boolean hideMusic = preferences.getBoolean("hideSharedMusic", false);
        boolean hideLinks = preferences.getBoolean("hideSharedLinks", false);
        CharSequence[] cs = BuildVars.DEBUG_VERSION ? new CharSequence[]{LocaleController.getString("SharedMediaTitle", R.string.SharedMediaTitle), LocaleController.getString("DocumentsTitle", R.string.DocumentsTitle), LocaleController.getString("AudioTitle", R.string.AudioTitle), LocaleController.getString("LinksTitle", R.string.LinksTitle)} :
                new CharSequence[]{LocaleController.getString("SharedMediaTitle", R.string.SharedMediaTitle), LocaleController.getString("DocumentsTitle", R.string.DocumentsTitle), LocaleController.getString("AudioTitle", R.string.AudioTitle)};
        boolean[] b = BuildVars.DEBUG_VERSION ? new boolean[]{!hideMedia, !hideFiles, !hideMusic, !hideLinks} :
                new boolean[]{!hideMedia, !hideFiles, !hideMusic};
        builder.setMultiChoiceItems(cs, b,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        if (which == 0) {
                            editor.putBoolean("hideSharedMedia", !isChecked);
                        } else if (which == 1) {
                            editor.putBoolean("hideSharedFiles", !isChecked);
                        } else if (which == 2) {
                            editor.putBoolean("hideSharedMusic", !isChecked);
                        } else if (which == 3) {
                            editor.putBoolean("hideSharedLinks", !isChecked);
                        }
                        editor.apply();
                    }
                });
        return builder;
    }



    @Override
    public void onResume() {
        super.onResume();
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }

        updateTheme();
        fixLayout();
    }

    private void updateTheme() {
        SharedPreferences themePrefs = ApplicationLoader.applicationContext.getSharedPreferences(AndroidUtilities.THEME_PREFS, AndroidUtilities.THEME_PREFS_MODE);
        int def = themePrefs.getInt("themeColor", AndroidUtilities.defColor);
        actionBar.setBackgroundColor(themePrefs.getInt("prefHeaderColor", def));
        actionBar.setTitleColor(themePrefs.getInt("prefHeaderTitleColor", 0xffffffff));

        Drawable back = getParentActivity().getResources().getDrawable(R.drawable.ic_ab_back);
        back.setColorFilter(themePrefs.getInt("prefHeaderIconsColor", 0xffffffff), PorterDuff.Mode.MULTIPLY);
        actionBar.setBackButtonDrawable(back);

        Drawable other = getParentActivity().getResources().getDrawable(R.drawable.ic_ab_other);
        other.setColorFilter(themePrefs.getInt("prefHeaderIconsColor", 0xffffffff), PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        fixLayout();
    }

    private void fixLayout() {
        if (fragmentView == null) {
            return;
        }
        fragmentView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (fragmentView != null) {
                    fragmentView.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return true;
            }
        });
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {

    }


    private class ListAdapter extends BaseFragmentAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int i) {
            return  i == dialogsTabsHeightRow || i == dialogsTabsRow ||
                   i == dialogsHideTabsCheckRow || i == dialogsDisableTabsAnimationCheckRow ||
                    i == dialogsInfiniteTabsSwipe || i == dialogsHideTabsCounters || i == dialogsTabsCountersCountChats || i == dialogsTabsCountersCountNotMuted;
        }

        @Override
        public int getCount() {
            return rowCount;
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
            return false;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            int type = getItemViewType(i);
            if (type == 0) {
                if (view == null) {
                    view = new EmptyCell(mContext);
                }
                if (i == overscrollRow) {
                    ((EmptyCell) view).setHeight(AndroidUtilities.dp(88));
                } else {
                    ((EmptyCell) view).setHeight(AndroidUtilities.dp(16));
                }
            } else if (type == 1) {
                if (view == null) {
                    view = new ShadowSectionCell(mContext);
                }
            } else if (type == 2) {
                if (view == null) {
                    view = new TextSettingsCell(mContext);
                }
                TextSettingsCell textCell = (TextSettingsCell) view;
                SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);
                //SharedPreferences mainPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", Activity.MODE_PRIVATE);
                 if (i == dialogsTabsHeightRow) {
                    //SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);
                    int size = preferences.getInt("tabsHeight", AndroidUtilities.isTablet() ? 42 : 40);
                    textCell.setTextAndValue(LocaleController.getString("TabsHeight", R.string.TabsHeight), String.format("%d", size), true);
                }
            } else if (type == 3) {
                if (view == null) {
                    view = new TextCheckCell(mContext);
                }
                TextCheckCell textCell = (TextCheckCell) view;

                SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);
                //SharedPreferences mainPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", Activity.MODE_PRIVATE);
               if (i == dialogsHideTabsCheckRow) {
                    textCell.setTextAndCheck(LocaleController.getString("HideTabs", R.string.HideTabs), preferences.getBoolean("hideTabs", false), true);
                } else if (i == dialogsDisableTabsAnimationCheckRow) {
                    textCell.setTextAndCheck(LocaleController.getString("DisableTabsAnimation", R.string.DisableTabsAnimation), preferences.getBoolean("disableTabsAnimation", true), true);
                } else if (i == dialogsInfiniteTabsSwipe) {
                    textCell.setTextAndCheck(LocaleController.getString("InfiniteSwipe", R.string.InfiniteSwipe), preferences.getBoolean("infiniteTabsSwipe", false), true);
                } else if (i == dialogsHideTabsCounters) {
                    textCell.setTextAndCheck(LocaleController.getString("HideTabsCounters", R.string.HideTabsCounters), preferences.getBoolean("hideTabsCounters", false), true);
                } else if (i == dialogsTabsCountersCountChats) {
                    textCell.setTextAndCheck(LocaleController.getString("HeaderTabCounterCountChats", R.string.HeaderTabCounterCountChats), preferences.getBoolean("tabsCountersCountChats", false), true);
                } else if (i == dialogsTabsCountersCountNotMuted) {
                    textCell.setTextAndCheck(LocaleController.getString("HeaderTabCounterCountNotMuted", R.string.HeaderTabCounterCountNotMuted), preferences.getBoolean("tabsCountersCountNotMuted", false), true);
                }
            } else if (type == 4) {
                if (view == null) {
                    view = new HeaderCell(mContext);
                }
               if (i == dialogsSectionRow2) {
                    ((HeaderCell) view).setText(LocaleController.getString("DialogsSettings", R.string.DialogsSettings));
                }
            } else if (type == 6) {
                if (view == null) {
                    view = new TextDetailSettingsCell(mContext);
                }
                TextDetailSettingsCell textCell = (TextDetailSettingsCell) view;

                if (i == dialogsTabsRow) {
                    String value;
                    SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);

                    boolean hideUsers = preferences.getBoolean("hideUsers", false);
                    boolean hideGroups = preferences.getBoolean("hideGroups", false);
                    boolean hideSGroups = preferences.getBoolean("hideSGroups", false);
                    boolean hideChannels = preferences.getBoolean("hideChannels", false);
                    boolean hideBots = preferences.getBoolean("hideBots", false);
                    boolean hideFavs = preferences.getBoolean("hideFavs", false);

                    value = LocaleController.getString("HideShowTabs", R.string.HideShowTabs);

                    String text = "";
                    if (!hideUsers) {
                        text += LocaleController.getString("Users", R.string.Users);
                    }
                    if (!hideGroups) {
                        if (text.length() != 0) {
                            text += ", ";
                        }
                        text += LocaleController.getString("Groups", R.string.Groups);
                    }
                    if (!hideSGroups) {
                        if (text.length() != 0) {
                            text += ", ";
                        }
                        text += LocaleController.getString("SuperGroups", R.string.SuperGroups);
                    }
                    if (!hideChannels) {
                        if (text.length() != 0) {
                            text += ", ";
                        }
                        text += LocaleController.getString("Channels", R.string.Channels);
                    }
                    if (!hideBots) {
                        if (text.length() != 0) {
                            text += ", ";
                        }
                        text += LocaleController.getString("Bots", R.string.Bots);
                    }
                    if (!hideFavs) {
                        if (text.length() != 0) {
                            text += ", ";
                        }
                        text += LocaleController.getString("Favorites", R.string.Favorites);
                    }
                    if (text.length() == 0) {
                        text = "";
                    }
                    textCell.setTextAndValue(value, text, true);
                }
            } else if (type == 7) {
                if (view == null) {
                    view = new TextInfoPrivacyCell(mContext);
                }

            }
            return view;
        }

        @Override
        public int getItemViewType(int i) {
            if (i == emptyRow || i == overscrollRow) {
                return 0;
            }
            if ( i == dialogsSectionRow ) {
                return 1;
            } else if ( i == dialogsHideTabsCheckRow || i == dialogsDisableTabsAnimationCheckRow || i == dialogsInfiniteTabsSwipe ||
                    i == dialogsHideTabsCounters || i == dialogsTabsCountersCountChats || i == dialogsTabsCountersCountNotMuted ) {
                return 3;
            } else if ( i == dialogsTabsHeightRow ) {
                return 2;
            } else if (i == dialogsTabsRow ) {
                return 6;
            }  else if ( i == dialogsSectionRow2 ) {
                return 4;
            } else {
                return 2;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 8;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }
}

