package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.MenuItem;


import com.example.todolist.adapters.ListPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private FragmentPagerAdapter pagerAdapter;
    private FragmentManager supportFragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewPager viewPager = findViewById(R.id.vpPager);
        pagerAdapter = new ListPagerAdapter(supportFragmentManager);
        viewPager.setAdapter(pagerAdapter);

        setTitle(R.string.tasks);
        //Toolbar
        Toolbar toolbar = findViewById(R.id.actionbar_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo_actionbar);

        final TabLayout tab = findViewById(R.id.tab_layout);
        tab.addTab(tab.newTab().setText(getResources().getString(R.string.pending)));
        tab.addTab(tab.newTab().setText(getResources().getString(R.string.done)));
        tab.addTab(tab.newTab().setText(getResources().getString(R.string.all)));
        tab.setTabGravity(TabLayout.GRAVITY_FILL);

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                MainActivity.this.pagerAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tab.getTabAt(position).select();
                MainActivity.this.pagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if( state == ViewPager.SCROLL_STATE_SETTLING){
                    MainActivity.this.pagerAdapter.notifyDataSetChanged();
                }
            }
        });

        FloatingActionButton fabAddTask = findViewById(R.id.addTask);
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NewActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_context, menu);
    }
}
