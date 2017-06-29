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



public class forwardSettingsActivity  extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {

    private ListView listView;
    private ListAdapter listAdapter;

    private int overscrollRow;
    private int emptyRow;




    private int messagesSectionRow;
    private int messagesSectionRow2;




    private int rowCount;




    private int chatShowDirectShareBtn;
    private int chatDirectShareToMenu;
    private int chatDirectShareReplies;
    private int chatDirectShareFavsFirst;
    private int chatSearchUserOnTwitterRow;


    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();

        NotificationCenter.getInstance().addObserver(this, NotificationCenter.refreshTabs);

        rowCount = 0;
        overscrollRow = -1;
        emptyRow = -1;



        messagesSectionRow = rowCount++;
        messagesSectionRow2 = rowCount++;


        chatShowDirectShareBtn = rowCount++;
        chatDirectShareReplies = rowCount++;
        chatDirectShareToMenu = rowCount++;
        chatDirectShareFavsFirst = rowCount++;

        chatSearchUserOnTwitterRow = rowCount++;




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
        actionBar.setTitle(LocaleController.getString("ForwardSettings", R.string.ForwardSettings));

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

               if (i == chatDirectShareReplies) {
                    SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);
                    boolean send = preferences.getBoolean("directShareReplies", false);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("directShareReplies", !send);
                    editor.apply();
                    if (view instanceof TextCheckCell) {
                        ((TextCheckCell) view).setChecked(!send);
                    }
                } else if (i == chatDirectShareToMenu) {
                    SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);
                    boolean send = preferences.getBoolean("directShareToMenu", true);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("directShareToMenu", !send);
                    editor.apply();
                    if (view instanceof TextCheckCell) {
                        ((TextCheckCell) view).setChecked(!send);
                    }
                } else if (i == chatDirectShareFavsFirst) {
                    SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);
                    boolean send = preferences.getBoolean("directShareFavsFirst", false);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("directShareFavsFirst", !send);
                    editor.apply();
                    if (view instanceof TextCheckCell) {
                        ((TextCheckCell) view).setChecked(!send);
                    }
                } else if (i == chatShowDirectShareBtn) {
                    if (getParentActivity() == null) {
                        return;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                    createDialog(builder, chatShowDirectShareBtn);
                    builder.setNegativeButton(LocaleController.getString("Done", R.string.Done), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (listView != null) {
                                listView.invalidateViews();
                            }
                        }
                    });
                    showDialog(builder.create());
                } else if (i == chatSearchUserOnTwitterRow) {
                    SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);
                    boolean hide = preferences.getBoolean("searchOnTwitter", true);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("searchOnTwitter", !hide);
                    editor.apply();
                    if (view instanceof TextCheckCell) {
                        ((TextCheckCell) view).setChecked(!hide);
                    }
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

    private AlertDialog.Builder createDialog(AlertDialog.Builder builder, int i) {
        if (i == chatShowDirectShareBtn) {
            builder.setTitle(LocaleController.getString("ShowDirectShareButton", R.string.ShowDirectShareButton));

            SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);
            //SharedPreferences mainPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", Activity.MODE_PRIVATE);
            boolean showDSBtnUsers = preferences.getBoolean("showDSBtnUsers", false);
            boolean showDSBtnGroups = preferences.getBoolean("showDSBtnGroups", true);
            boolean showDSBtnSGroups = preferences.getBoolean("showDSBtnSGroups", true);
            boolean showDSBtnChannels = preferences.getBoolean("showDSBtnChannels", true);
            boolean showDSBtnBots = preferences.getBoolean("showDSBtnBots", true);

            builder.setMultiChoiceItems(
                    new CharSequence[]{LocaleController.getString("Users", R.string.Users), LocaleController.getString("Groups", R.string.Groups), LocaleController.getString("SuperGroups", R.string.SuperGroups), LocaleController.getString("Channels", R.string.Channels), LocaleController.getString("Bots", R.string.Bots)},
                    new boolean[]{showDSBtnUsers, showDSBtnGroups, showDSBtnSGroups, showDSBtnChannels, showDSBtnBots},
                    new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            //Log.e("createDialog","which " + which + " isChecked " + isChecked);
                            if (which == 0) {
                                editor.putBoolean("showDSBtnUsers", isChecked);
                            } else if (which == 1) {
                                editor.putBoolean("showDSBtnGroups", isChecked);
                            } else if (which == 2) {
                                editor.putBoolean("showDSBtnSGroups", isChecked);
                            } else if (which == 3) {
                                editor.putBoolean("showDSBtnChannels", isChecked);
                            } else if (which == 4) {
                                editor.putBoolean("showDSBtnBots", isChecked);
                            }
                            editor.apply();

                        }
                    });
        }

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
            return    i == chatShowDirectShareBtn ||
                    i == chatDirectShareToMenu || i == chatDirectShareReplies
                    || i == chatDirectShareFavsFirst
                    ||  i == chatSearchUserOnTwitterRow ;
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

            } else if (type == 3) {
                if (view == null) {
                    view = new TextCheckCell(mContext);
                }
                TextCheckCell textCell = (TextCheckCell) view;

                SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);
                //SharedPreferences mainPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", Activity.MODE_PRIVATE);
                if (i == chatDirectShareReplies) {
                    textCell.setTextAndCheck(LocaleController.getString("DirectShareReplies", R.string.DirectShareReplies), preferences.getBoolean("directShareReplies", false), true);
                } else if (i == chatDirectShareToMenu) {
                    textCell.setTextAndCheck(LocaleController.getString("DirectShareToMenu", R.string.DirectShareToMenu), preferences.getBoolean("directShareToMenu", true), true);
                } else if (i == chatDirectShareFavsFirst) {
                    textCell.setTextAndCheck(LocaleController.getString("DirectShareShowFavsFirst", R.string.DirectShareShowFavsFirst), preferences.getBoolean("directShareFavsFirst", false), true);
                } else if (i == chatSearchUserOnTwitterRow) {
                    textCell.setTextAndCheck(LocaleController.getString("SearchUserOnTwitter", R.string.SearchUserOnTwitter), preferences.getBoolean("searchOnTwitter", true), false);
                }
            } else if (type == 4) {
                if (view == null) {
                    view = new HeaderCell(mContext);
                }
                if (i == messagesSectionRow2) {
                    ((HeaderCell) view).setText(LocaleController.getString("MessagesSettings", R.string.MessagesSettings));
                }
            } else if (type == 6) {
                if (view == null) {
                    view = new TextDetailSettingsCell(mContext);
                }
                TextDetailSettingsCell textCell = (TextDetailSettingsCell) view;

                if (i == chatShowDirectShareBtn) {
                    String value;
                    SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);
                    boolean showDSBtnUsers = preferences.getBoolean("showDSBtnUsers", false);
                    boolean showDSBtnGroups = preferences.getBoolean("showDSBtnGroups", true);
                    boolean showDSBtnSGroups = preferences.getBoolean("showDSBtnSGroups", true);
                    boolean showDSBtnChannels = preferences.getBoolean("showDSBtnChannels", true);
                    boolean showDSBtnBots = preferences.getBoolean("showDSBtnBots", true);

                    value = LocaleController.getString("ShowDirectShareButton", R.string.ShowDirectShareButton);

                    String text = "";
                    if (showDSBtnUsers) {
                        text += LocaleController.getString("Users", R.string.Users);
                    }
                    if (showDSBtnGroups) {
                        if (text.length() != 0) {
                            text += ", ";
                        }
                        text += LocaleController.getString("Groups", R.string.Groups);
                    }
                    if (showDSBtnSGroups) {
                        if (text.length() != 0) {
                            text += ", ";
                        }
                        text += LocaleController.getString("SuperGroups", R.string.SuperGroups);
                    }
                    if (showDSBtnChannels) {
                        if (text.length() != 0) {
                            text += ", ";
                        }
                        text += LocaleController.getString("Channels", R.string.Channels);
                    }
                    if (showDSBtnBots) {
                        if (text.length() != 0) {
                            text += ", ";
                        }
                        text += LocaleController.getString("Bots", R.string.Bots);
                    }

                    if (text.length() == 0) {
                        text = LocaleController.getString("Channels", R.string.UsernameEmpty);
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
            if (i == messagesSectionRow ) {
                return 1;
            } else if ( i == chatDirectShareToMenu || i == chatDirectShareReplies || i == chatDirectShareFavsFirst ||
                  i == chatSearchUserOnTwitterRow) {
                return 3;
            }else if (i == chatShowDirectShareBtn ) {
                return 6;
            } else if ( i == messagesSectionRow2 ) {
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

