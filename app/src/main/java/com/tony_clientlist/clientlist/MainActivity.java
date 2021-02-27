package com.tony_clientlist.clientlist;


import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import com.tony_clientlist.clientlist.database.AppDataBase;
import com.tony_clientlist.clientlist.database.AppExecuter;
import com.tony_clientlist.clientlist.database.Client;
import com.tony_clientlist.clientlist.database.DataAdapter;
import com.tony_clientlist.clientlist.settings.SettingsActivity;
import com.tony_clientlist.clientlist.utils.Constans;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private AppDataBase myDb;
    private DataAdapter adapter;
    private List<Client> listClient;
    private RecyclerView recyclerView;
    private DrawerLayout drawerLayout;
    private DataAdapter.AdapterOnItemClicked adapterOnItemClicked;
    private ActionBarDrawerToggle toggle;
    private TextView text_empty;
    private TextView text_no_result;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        this.setTitle(R.string.my_clietns);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        FloatingActionButton fab = findViewById(R.id.fab);
        NavigationView nav_view = findViewById(R.id.nav_view);
        adapterOnItemClicked = new DataAdapter.AdapterOnItemClicked() {
            @Override
            public void onAdapterItemClicked(int position) {

                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra(Constans.NAME_KEY, listClient.get(position).getName());
                i.putExtra(Constans.SEC_NAME_KEY, listClient.get(position).getSec_name());
                i.putExtra(Constans.TEL_KEY, listClient.get(position).getNumber());
                i.putExtra(Constans.DESC_KEY, listClient.get(position).getDescription());
                i.putExtra(Constans.IMP_KEY, listClient.get(position).getImportance());
                i.putExtra(Constans.SP_KEY, listClient.get(position).getSpecial());
                i.putExtra(Constans.ID_KEY, listClient.get(position).getId());
                startActivity(i);
            }
        };
        nav_view.setNavigationItemSelectedListener(this);
        init();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                startActivity(i);


            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        AppExecuter.getInstance().getDiscIO().execute(new Runnable() {
            @Override
            public void run() {
                listClient = myDb.clientDAO().getClientList();
                AppExecuter.getInstance().getMainIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter != null) {
                            Collections.reverse(listClient);
                            adapter.updateAdapter(listClient);
                        }
                        if(listClient.size()>0){
                            text_empty.setVisibility(View.GONE);

                        } else {
                            text_empty.setVisibility(View.VISIBLE);
                        }


                    }
                });

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        SearchManager sManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        SearchView sView = (SearchView) menu.findItem(R.id.id_search).getActionView();
        assert sManager != null;
        sView.setSearchableInfo(sManager.getSearchableInfo(this.getComponentName()));
        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                AppExecuter.getInstance().getDiscIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        listClient = myDb.clientDAO().getClientListName(newText);
                        AppExecuter.getInstance().getMainIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                if (adapter != null) {
                                    adapter.updateAdapter(listClient);
                                }
                                if(listClient.size()>0){
                                    text_no_result.setVisibility(View.GONE);

                                } else {
                                    text_no_result.setVisibility(View.VISIBLE);
                                    text_empty.setVisibility(View.GONE);
                                }


                            }
                        });

                    }
                });
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.id_client) {
            AppExecuter.getInstance().getDiscIO().execute(new Runnable() {
                @Override
                public void run() {
                    listClient = myDb.clientDAO().getClientList();
                    AppExecuter.getInstance().getMainIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (adapter != null) {

                                Collections.reverse(listClient);
                                adapter.updateAdapter(listClient);
                            }
                            if(listClient.size()>0){
                                text_empty.setVisibility(View.GONE);

                            } else {
                                text_empty.setVisibility(View.VISIBLE);
                            }

                        }
                    });

                }
            });

        } else if (id == R.id.id_web) {
            goTo("https://www.donationalerts.com/r/antonyhepel");
        } else if (id == R.id.id_settings) {
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
        } else if (id == R.id.id_special) {
            AppExecuter.getInstance().getDiscIO().execute(new Runnable() {
                @Override
                public void run() {
                    listClient = myDb.clientDAO().getClientListSpecial();
                    AppExecuter.getInstance().getMainIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (adapter != null) {

                                Collections.reverse(listClient);
                                adapter.updateAdapter(listClient);
                            }
                            if(listClient.size()>0){
                                text_empty.setVisibility(View.GONE);

                            } else {
                                text_empty.setVisibility(View.VISIBLE);
                            }

                        }
                    });

                }
            });
        } else if (id == R.id.id_important) {
            AppExecuter.getInstance().getDiscIO().execute(new Runnable() {
                @Override
                public void run() {
                    listClient = myDb.clientDAO().getClientListImportant(0);
                    AppExecuter.getInstance().getMainIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (adapter != null) {
                                Collections.reverse(listClient);
                                adapter.updateAdapter(listClient);
                            }
                            if(listClient.size()>0){
                                text_empty.setVisibility(View.GONE);

                            } else {
                                text_empty.setVisibility(View.VISIBLE);
                            }

                        }
                    });

                }
            });
        } else if (id == R.id.id_normal) {
            AppExecuter.getInstance().getDiscIO().execute(new Runnable() {
                @Override
                public void run() {
                    listClient = myDb.clientDAO().getClientListImportant(1);
                    AppExecuter.getInstance().getMainIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (adapter != null) {

                                Collections.reverse(listClient);
                                adapter.updateAdapter(listClient);
                            }
                            if(listClient.size()>0){
                                text_empty.setVisibility(View.GONE);

                            } else {
                                text_empty.setVisibility(View.VISIBLE);
                            }

                        }
                    });

                }
            });
        }else if (id == R.id.id_no_important) {
            AppExecuter.getInstance().getDiscIO().execute(new Runnable() {
                @Override
                public void run() {
                    listClient = myDb.clientDAO().getClientListImportant(2);
                    AppExecuter.getInstance().getMainIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (adapter != null) {

                                Collections.reverse(listClient);
                                adapter.updateAdapter(listClient);
                            }
                            if(listClient.size()>0){
                                text_empty.setVisibility(View.GONE);

                            } else {
                                text_empty.setVisibility(View.VISIBLE);
                            }

                        }
                    });

                }
            });
        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goTo(String url) {
        Intent brIntent, chooser;
        brIntent = new Intent(Intent.ACTION_VIEW);
        brIntent.setData(Uri.parse(url));
        chooser = Intent.createChooser(brIntent, "Открыть с");
        if (brIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    private void init() {
        text_empty = findViewById(R.id.text_empty);
        text_no_result = findViewById(R.id.text_no_result);

        recyclerView = findViewById(R.id.rView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myDb = AppDataBase.getInstance(getApplicationContext());
        listClient = new ArrayList<>();
        adapter = new DataAdapter(listClient, adapterOnItemClicked, this);
        recyclerView.setAdapter(adapter);

    }
}
