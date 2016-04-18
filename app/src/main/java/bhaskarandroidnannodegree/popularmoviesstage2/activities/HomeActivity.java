/*
 * Copyright (C) 2016 Anupam Das
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bhaskarandroidnannodegree.popularmoviesstage2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import bhaskarandroidnannodegree.popularmoviesstage2.R;
import bhaskarandroidnannodegree.popularmoviesstage2.fragments.DetailActivityFragment;
import bhaskarandroidnannodegree.popularmoviesstage2.fragments.MainActivityFragment;
import bhaskarandroidnannodegree.popularmoviesstage2.utils.Utils;

public class HomeActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    private final String DETAIL_FRAGMENT_TAG = "DFTAG";
    private boolean mHasTwoPane;
    private String mSorting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSorting = Utils.getPreferredSorting(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_detail_container) != null) {
            mHasTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailActivityFragment(), DETAIL_FRAGMENT_TAG)
                        .commit();
            }
        } else {
            mHasTwoPane = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String sortOrder = Utils.getPreferredSorting(this);
        // update the sorting Order in our second pane using the fragment manager
        if (sortOrder != null && !sortOrder.equals(mSorting)) {
            MainActivityFragment ff = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main_activity);
            if (null != ff) {
                ff.onSortingChanged();
            }
            DetailActivityFragment df = (DetailActivityFragment) getSupportFragmentManager().findFragmentByTag(DETAIL_FRAGMENT_TAG);
            if (null != df) {
                df.onSortingChanged();
            }
            mSorting = sortOrder;
        }
    }

    @Override
    public void onItemSelected(String movieId) {
        if (mHasTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putString(Intent.EXTRA_TEXT, movieId);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAIL_FRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_TEXT, movieId);
            startActivity(intent);
        }
    }
}
