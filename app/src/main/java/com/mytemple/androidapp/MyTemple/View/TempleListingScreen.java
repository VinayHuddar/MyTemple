package com.mytemple.androidapp.MyTemple.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.mytemple.androidapp.Common.Controller.ImageDownloader;
import com.mytemple.androidapp.Common.IntentActionStrings;
import com.mytemple.androidapp.Common.View.BaseActivity;
import com.mytemple.androidapp.Common.View.NavDrawerItemList;
import com.mytemple.androidapp.Common.View.NavigationDrawerCallbacks;
import com.mytemple.androidapp.MyTemple.Controller.TempleListManager;
import com.mytemple.androidapp.MyTemple.Model.Categories;
import com.mytemple.androidapp.MyTemple.Model.Temples;
import com.mytemple.androidapp.R;

import retrofit.RetrofitError;


public class TempleListingScreen extends BaseActivity implements NavigationDrawerCallbacks, Categories.Callback,
        TempleListManager.Callback {
    Activity mActivity;
    private Context mContext;

    int mFirstLevelCatSelection = 0;
    int mSecondLevelCatSelection;

    TempleListManager mTempleListManager;
    TempleListAdapter mTempleListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);

        super.onCreateDrawer();
        mNavigationDrawerFragment.SetDefaultSelectedPosition(NavDrawerItemList.BROWSE_ITEM_ID);

        mContext = mActivity = this;

        mTempleListManager = TempleListManager.GetInstance();
        mTempleListManager.SetCallback(this);
        mTempleListAdapter = new TempleListAdapter ();

        if (savedInstanceState != null) {
            mFirstLevelCatSelection = savedInstanceState.getInt(IntentActionStrings.FIRST_LEVEL_CATEGORY_SELECTION, 0);
            mSecondLevelCatSelection = savedInstanceState.getInt(IntentActionStrings.SECOND_LEVEL_CATEGORY_SELECTION, 0);
        }

        findViewById(R.id.loading_message).setVisibility(View.VISIBLE);
        Categories.GetCategories(this);
    }

    @Override
    public void onSaveInstanceState (Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt(IntentActionStrings.FIRST_LEVEL_CATEGORY_SELECTION, mFirstLevelCatSelection);
        savedInstanceState.putInt(IntentActionStrings.SECOND_LEVEL_CATEGORY_SELECTION, mSecondLevelCatSelection);
    }

    public void OnCategoriesReceived (final Categories.Model model) {
        findViewById(R.id.loading_message).setVisibility(View.INVISIBLE);

        final int numFirstLevelCats = model.GetCategoryCount();
        final String[] firstLevelCats = new String[1 + numFirstLevelCats];
        firstLevelCats[0] = getResources().getString(R.string.filter_label);
        for (int i = 1; i <= numFirstLevelCats; i++)
            firstLevelCats[i] = String.format("%s %s", getResources().getString(R.string.filter_string),
                    model.GetCategory(i).GetCategoryName());

        final TextView catLevel1 = (TextView)findViewById(R.id.Level1Cat);
        catLevel1.setText(firstLevelCats[0]);

        catLevel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu dropdown = new PopupMenu(mActivity, catLevel1);
                for (int i = 0; i < numFirstLevelCats; i++)
                    dropdown.getMenu().add(1, i, i, firstLevelCats[i]);
                dropdown.show();

                dropdown.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (mFirstLevelCatSelection != item.getItemId()) {
                            mFirstLevelCatSelection = item.getItemId();
                            mSecondLevelCatSelection = 0;

                            catLevel1.setText(firstLevelCats[mFirstLevelCatSelection]);

                            // Get to the last but one level. i.e. the parent of the leaf categories
                            Categories.Model.Category catItr = model.GetCategory(mFirstLevelCatSelection);
                            while (catItr.GetCategory(1).GetSubCatCount() != 0)
                                catItr = catItr.GetCategory(1);

                            final Categories.Model.Category parentCat = catItr;
                            final int numSecondLevelCats = parentCat.GetSubCatCount();

                            final String[] secondLevelCats = new String[1 + numSecondLevelCats];
                            secondLevelCats[0] = getResources().getString(R.string.choose_subcategory);
                            for (int i = 1; i <= numSecondLevelCats; i++)
                                secondLevelCats[i] = parentCat.GetCategory(i).GetCategoryName();

                            final TextView catLevel2 = (TextView) findViewById(R.id.Level2Cat);
                            catLevel2.setText(secondLevelCats[mSecondLevelCatSelection]);

                            catLevel2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PopupMenu dropdown = new PopupMenu(mActivity, catLevel2);
                                    dropdown.getMenu().add(1, 0, 0, getResources().getString(R.string.all_temples));
                                    for (int i = 1; i <= numSecondLevelCats; i++)
                                        dropdown.getMenu().add(1, i, i, secondLevelCats[i]);
                                    dropdown.show();

                                    dropdown.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                        @Override
                                        public boolean onMenuItemClick(MenuItem item) {
                                            if (mSecondLevelCatSelection != item.getItemId()) {
                                                findViewById(R.id.TempleList).setVisibility(View.INVISIBLE);
                                                findViewById(R.id.loading_message).setVisibility(View.VISIBLE);

                                                mSecondLevelCatSelection = item.getItemId();
                                                catLevel2.setText(secondLevelCats[mSecondLevelCatSelection]);

                                                if (mSecondLevelCatSelection == 0)
                                                    mTempleListManager.GetTemples(model.GetCategory(mFirstLevelCatSelection).GetCategoryId());
                                                else
                                                    mTempleListManager.GetTemples(parentCat.GetCategory(mSecondLevelCatSelection).GetCategoryId());
                                            }
                                            return false;
                                        }
                                    });

                                }
                            });
                        }

                        return false;
                    }
                });
            }
        });

        mTempleListManager.GetTemples(model.GetCategory(1).GetCategoryId());
    }

    public void OnCategoriesNotReceived (RetrofitError error) {
        Toast.makeText(this, "Didn't recieve data", Toast.LENGTH_SHORT);
    }

    class TempleListAdapter extends BaseAdapter implements ImageDownloader.Callback {
        Temples.Temple[] mTempleList;
        ImageDownloader mImageDownloader = new ImageDownloader(this);

        public void SetList (Temples.Temple[] list) {
            mTempleList = list;
        }

        public int getCount () {
            return mTempleList.length;
        }

        public Object getItem(int position) {
            return mTempleList[position];
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View listItem;
            if (convertView == null) {
                listItem = inflater.inflate(R.layout.layout_temple_list_item, null);
            } else {
                listItem = (View) convertView;
            }

            final Temples.Temple currTemple = mTempleList[position];

            if (currTemple.GetImage() != null) {
                ImageView templeImage = (ImageView) listItem.findViewById(R.id.TempleImage);
                mImageDownloader.download(currTemple.GetImage(), templeImage);
            }

            TextView templeName = (TextView)listItem.findViewById(R.id.TempleName);
            templeName.setText(currTemple.GetName());

            TextView templeLocation = (TextView)listItem.findViewById(R.id.TempleLocation);
            templeLocation.setText(String.format("%s, %s", currTemple.GetArea(), currTemple.GetCity()));

            listItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, MainTempleScreen.class);
                    intent.putExtra(IntentActionStrings.TEMPLE_ID, currTemple.GetTempleId());
                    intent.putExtra(IntentActionStrings.TEMPLE_NAME, currTemple.GetName());
                    startActivity(intent);
                }
            });

            return listItem;
        }

        public void OnImageReceived () {
        }
    }

    public void OnTemplesReceived(Temples.Model model) {
        findViewById(R.id.TempleList).setVisibility(View.VISIBLE);
        findViewById(R.id.loading_message).setVisibility(View.INVISIBLE);

        ListView templesList = (ListView)findViewById(R.id.TempleList);
        mTempleListAdapter.SetList(model.GetAllTemples());
        if (templesList.getAdapter() == null)
            templesList.setAdapter(mTempleListAdapter);
        else
            mTempleListAdapter.notifyDataSetChanged();
    }

    public void OnTemplesNotReceived(RetrofitError error) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search) {
            //Intent intent = new Intent(this, SearchQueryTakerActivity.class);
            //startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
    }

    @Override
    public int getDefaultItemSelectId() {
        return NavDrawerItemList.BROWSE_ITEM_ID;
    }
}
