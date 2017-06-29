package telegramplus.category;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import ir.mmnotimm.telegramplus.R;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.DialogsActivity;

import java.util.ArrayList;
import java.util.List;

import telegramplus.constant;
import telegramplus.users.database.userDBAdapter;


public class categoryManagement extends BaseFragment {
    private final int MENU_SETTINGS = 1;
    private final int ADD_MENU = 2;
    private static final int TEXT_ID = 8;

    private ListView listView;
    private userDBAdapter db;
    List<category> categories = new ArrayList<>();

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
        actionBar.setTitle(LocaleController.getString("CategoryManagement", R.string.CategoryManagement));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                } else if (id == MENU_SETTINGS) {


                    final CharSequence[] items = {context.getResources().getString(R.string.categorySettings)};

                    final ArrayList seletedItems = new ArrayList();

                    final SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("plusconfig", Activity.MODE_PRIVATE);
                    final boolean scr = preferences.getBoolean("categoryMenu", true);
                    final boolean[] checked = {scr};

                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle(context.getResources().getString(R.string.categorySettingsTitle))
                                    .setMultiChoiceItems(items, new boolean[]{!scr}, new DialogInterface.OnMultiChoiceClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                                            if (isChecked) {
                                               checked[0] = false ;

                                            } else {

                                             checked[0] = true ;
                                            }
                                        }
                                    }).setPositiveButton(context.getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            preferences.edit().putBoolean("categoryMenu", checked[0]).commit();
                                            DialogsActivity.refreshToolbarItems();

                                        }
                                    }).setNegativeButton(context.getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            //  Your code when user clicked on Cancel
                                        }
                                    }).create();
                    dialog.show();



                } else if (id == ADD_MENU) {
                    final EditText input = new EditText(context);
                    input.setId(TEXT_ID);
                    input.setPadding(15, 4, 15, 4);

                    // add list item
                    new AlertDialog.Builder(context)
                            .setTitle(context.getResources().getString(R.string.newCategory))
                            .setMessage(context.getResources().getString(R.string.insertCategoryName))
                            .setView(input)
                            .setPositiveButton(context.getResources().getString(R.string.insertCategory), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    categoryDBAdapter catDBAdapter = new categoryDBAdapter(context);
                                    catDBAdapter.open();
                                    category category = new category();
                                    category.setName(input.getText().toString());
                                    catDBAdapter.insert(category);
                                    catDBAdapter.close();
                                    refreshDisplay(context);

                                }
                            })
                            .setNegativeButton(context.getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(R.drawable.ic_menu_category)
                            .show();


                }
            }
        });


        ActionBarMenu menu = actionBar.createMenu();
        menu.addItemWithWidth(ADD_MENU, R.drawable.add, AndroidUtilities.dp(56));
        menu.addItemWithWidth(MENU_SETTINGS, R.drawable.ic_setting_dark, AndroidUtilities.dp(56));

        fragmentView = new FrameLayout(context);
        fragmentView.setLayoutParams(new FrameLayout.LayoutParams(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));


        FrameLayout frameLayout = (FrameLayout) fragmentView;


        db = new userDBAdapter(context);
        listView = new ListView(context);

        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences(AndroidUtilities.THEME_PREFS, AndroidUtilities.THEME_PREFS_MODE);
        listView.setBackgroundColor(preferences.getInt("prefBGColor", 0xffffffff));
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setVerticalScrollBarEnabled(false);
        int def = preferences.getInt("themeColor", AndroidUtilities.defColor);
        int hColor = preferences.getInt("prefHeaderColor", def);
        AndroidUtilities.setListViewEdgeEffectColor(listView, /*AvatarDrawable.getProfileBackColorForId(5)*/ hColor);
        frameLayout.addView(listView);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) listView.getLayoutParams();
        layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.TOP;
        listView.setLayoutParams(layoutParams);
        refreshDisplay(context);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setItems(new CharSequence[]{context.getResources().getString(R.string.Open), context.getResources().getString(R.string.Delete)}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0) {

                            SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                            sharedPreferences.edit().putInt("selectedCat", categories.get(position).getId()).commit();
                            DialogsActivity.needRefreshCategory = true;
                            finishFragment();


                        }

                        if (which == 1) {

                            SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                            if (categories.get(position).getId() == sharedPreferences.getInt("selectedCat", -1)) {

                                sharedPreferences.edit().putInt("selectedCat", -1).commit();
                                DialogsActivity.needRefreshCategory = true;

                                finishFragment();

                            }

                            categoryDBAdapter catDBAdapter = new categoryDBAdapter(context);
                            catDBAdapter.open();
                            catDBAdapter.delete(categories.get(position).getId());
                            catDBAdapter.close();


                            catDBAdapter db = new catDBAdapter(context);
                            db.open();
                            db.deleteAll(categories.get(position).getId());
                            db.close();


                            refreshDisplay(context);

                        }
                    }
                });
                builder.show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setItems(new CharSequence[]{context.getResources().getString(R.string.Open), context.getResources().getString(R.string.Delete)}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0) {

                        }

                        if (which == 1) {

                            categoryDBAdapter catDBAdapter = new categoryDBAdapter(context);
                            catDBAdapter.open();
                            catDBAdapter.delete(categories.get(position).getId());
                            catDBAdapter.close();


                            catDBAdapter db = new catDBAdapter(context);
                            db.open();
                            db.deleteAll(categories.get(position).getId());
                            db.close();


                            refreshDisplay(context);

                        }
                    }
                });
                builder.show();
                return false;
            }
        });

        return fragmentView;
    }


    private void refreshDisplay(Context context) {


        categoryDBAdapter catDBAdapter = new categoryDBAdapter(context);

        catDBAdapter.open();

        categories = catDBAdapter.getAllItms();
        catAdapter listAdapter = new catAdapter(context, R.layout.list_item_cats, categories);
        listView.setAdapter(listAdapter);

    }


    @Override
    public void onResume() {
        super.onResume();
        updateTheme();
        if (constant.AnalyticInitialized)
            ApplicationLoader.getInstance().trackScreenView("category Managemet");
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
