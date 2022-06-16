package com.example.taskplannernew.Activity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;


import com.example.taskplannernew.Fragments.AllJuniorsFragment;
import com.example.taskplannernew.Fragments.AssignTask;
import com.example.taskplannernew.Fragments.Calendar;
import com.example.taskplannernew.Fragments.CalendarTodo;
import com.example.taskplannernew.Fragments.HomeFragment;
import com.example.taskplannernew.Fragments.RecentTask;
import com.example.taskplannernew.Fragments.Todo;
import com.example.taskplannernew.R;
import com.example.taskplannernew.utils.GlobalProgressDialog;
import com.example.taskplannernew.utils.SharePref;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

public class MainActivity extends AppCompatActivity {

    private SpaceNavigationView spaceNavigationView;
    FloatingActionButton fab, fab1, fab2, fab3,fab_todocalendar,fab_assigntaskcalendar;
    LinearLayout fabLayout1, fabLayout2, fabLayout3 ,fabLayout_todocalendar ,fabLayout_assigntaskcalendar;
    View fabBGLayout , fabBGLayoutCalendar;
    boolean isFABOpen = false;
    ImageView logout_btn , calendar_fab;
    //SharedPreferences
    SharePref sharePref;
    FrameLayout main_layout_list;
    String userid,token,usertype;
    TextView toolbarTextView;
    GlobalProgressDialog globalProgressDialog;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbarTextView = (TextView)findViewById(R.id.heading);
        globalProgressDialog = new GlobalProgressDialog();
        toolbarTextView.setText("TASK PLANNER");
        MainActivity.this.setTitle("TASK PLANNER");
        sharePref = new SharePref(this);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }*/

        isStoragePermissionGranted();
        isCameraPermissionGranted();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        fabLayout1 = (LinearLayout) findViewById(R.id.fabLayout1);
        fabLayout2 = (LinearLayout) findViewById(R.id.fabLayout2);
        //fabLayout3 = (LinearLayout) findViewById(R.id.fabLayout3);

        fabLayout_todocalendar = (LinearLayout) findViewById(R.id.fabLayout_todocalendar);
        fabLayout_assigntaskcalendar = (LinearLayout) findViewById(R.id.fabLayout_assigntaskcalendar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        //fab3 = (FloatingActionButton) findViewById(R.id.fab3);

        calendar_fab = (ImageView) findViewById(R.id.calendar_fab);
        fab_todocalendar = (FloatingActionButton) findViewById(R.id.fab_todocalendar);
        fab_assigntaskcalendar = (FloatingActionButton) findViewById(R.id.fab_assigntaskcalendar);

        fabBGLayout = findViewById(R.id.fabBGLayout);

        fabBGLayoutCalendar = findViewById(R.id.fabBGLayoutCalendar);
        sharePref = new SharePref(this);
        main_layout_list = (FrameLayout) findViewById(R.id.main_layout_list);

        userid = sharePref.getLoginId();
        token = String.valueOf(sharePref.getTOKEN());
        usertype = sharePref.getUserType();

        Log.d("userid", "" + userid);
        Log.d("token", "" + token);
        Log.d("usertype", "" + usertype);

        loadFragment(new HomeFragment());

        logout_btn=(ImageView)findViewById(R.id.logout_btn);

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupLogout();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) {
                    fabBGLayout.setVisibility(View.VISIBLE);
                    showFABMenu();
                    fabBGLayout.setVisibility(View.VISIBLE);
                } else {
                    fabBGLayout.setVisibility(View.GONE);
                    closeFABMenu();
                    fabBGLayout.setVisibility(View.GONE);
                }
            }
        });
        //The FAB is use for Calendar
        calendar_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) {
                    //fabBGLayoutcalendar.setVisibility(View.VISIBLE);
                    showFABMenuCalendar();

                }
                else {
                    //fabBGLayoutcalendar.setVisibility(View.GONE);
                    closeFABMenuCalendar();
                }
            }
        });
        //View for Calendar Layout
        fabBGLayoutCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
            }
        });
        // FAB for Assign Task Calendar
        fabLayout_assigntaskcalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenuCalendar();
                loadFragment(new Calendar());

            }

        });
        // FAB for Todo Task Calendar
        fabLayout_todocalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenuCalendar();
                loadFragment(new CalendarTodo());
            }

        });

        //View for Center FAB (todo and Assigned Task)
        fabBGLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
            }
        });

        //Assigned Task FAB
        fabLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
                loadFragment(new AssignTask());
            }

        });

        //Todo Task FAB
        fabLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
                loadFragment(new Todo());
            }
        });

        spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem(R.id.navigation_first, "HOME", R.drawable.home));
        spaceNavigationView.addSpaceItem(new SpaceItem(R.id.navigation_second, "TASK", R.drawable.clock_icon));
        spaceNavigationView.addSpaceItem(new SpaceItem(R.id.navigation_third, "CALENDAR", R.drawable.invisible_calendar));
        spaceNavigationView.addSpaceItem(new SpaceItem(R.id.navigation_forth, "USERS", R.drawable.multiple_user));
        spaceNavigationView.shouldShowFullBadgeText(false);


        spaceNavigationView.setCentreButtonId(R.id.navigation_centre);
        spaceNavigationView.setCentreButtonIconColorFilterEnabled(true);

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {

                Log.d("onCentreButtonClick ", "onCentreButtonClick");
                //Toast.makeText(MainActivity.this, "onCentreButtonClick", Toast.LENGTH_SHORT).show();
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onItemClick(int itemIndex, String itemName) {
                Log.d("onItemClick ", "" + itemIndex + " " + itemName);

                if (itemIndex == 0) {
                    loadFragment(new HomeFragment());

                }
                if (itemIndex == 1) {
                    loadFragment(new RecentTask());

                }

                if (itemIndex == 2) {
                    Log.d("onceclicked","onceclicked");
                    //loadFragment(new Calendar());

                }

                if (itemIndex == 3) {
                    if(usertype.contains("MANAGER")) {
                        Log.d("manager","manager");
                        loadFragment(new AllJuniorsFragment());
                    }
                    else{
                        Log.d("manager1","manager1");
                        Toast.makeText(MainActivity.this,"You don't have any Executive.",Toast.LENGTH_SHORT).show();
                    }
                }
                // Toast.makeText(MainActivity.this,"" + itemIndex + " " + itemName,Toast.LENGTH_SHORT).show();
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onItemReselected(int itemIndex, String itemName) {


                if (itemIndex == 0) {
                    loadFragment(new HomeFragment());

                }
                if (itemIndex == 1) {
                    loadFragment(new RecentTask());

                }

                if (itemIndex == 2) {
                    Log.d("onceclickedagain","onceclickedagain");
                    //loadFragment(new Calendar());
                }

                if (itemIndex == 3) {
                    if(usertype.contains("MANAGER")) {
                        Log.d("manager","manager");
                        loadFragment(new AllJuniorsFragment());
                    }
                    else{
                        Log.d("manager1","manager1");
                        Toast.makeText(MainActivity.this,"You don't have any Executive.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        spaceNavigationView.showIconOnly();
        spaceNavigationView.setBackgroundColor(Color.parseColor("#ffb800"));
        //  setUpRecyclerView();
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Grant", "Permission is granted");
                return true;
            } else {

                Log.v("Revoked", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            Log.v("Granted", "Permission is granted");
            return true;
        }
    }

    public boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Grant", "Permission is granted");
                return true;
            } else {

                Log.v("Revoked", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Granted", "Permission is granted");
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        Log.i("MainActivity", "getBackStackEntryCount"+fm.getBackStackEntryCount() );
        if (isFABOpen) {
            closeFABMenu();
        }else if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
            //tera no. ni lg ra ???
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
        else if(getSupportFragmentManager().getBackStackEntryCount() == 1) {
            moveTaskToBack(false);
        }
        else {
            getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }


    private void showExitDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure want to exit");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onStart();
            }
        });
        builder.show();
    }


    private void popupLogout() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure you would like to logout?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logOut();
                        finish();
                        Intent intent = new Intent(MainActivity.this, AppLogin.class);
                        startActivity(intent);
                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void logOut() {
        SharedPreferences settings = getSharedPreferences("abgo_pref", Context.MODE_PRIVATE);
        settings.edit().clear().apply();
    }

    private void showFABMenu() {

        isFABOpen = true;
        Log.d("usertype11", "" + usertype);
        if(usertype.contains("MANAGER")){
            Log.d("usertype21", "" + usertype);
            isFABOpen = true;
            fabLayout1.setVisibility(View.VISIBLE);
            fabLayout2.setVisibility(View.VISIBLE);
            //fabLayout3.setVisibility(View.VISIBLE);
            fabBGLayout.setVisibility(View.VISIBLE);
            fab.animate().rotationBy(180);
            fabLayout1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
            fabLayout2.animate().translationY(-getResources().getDimension(R.dimen.standard_100));

        }
        else if(usertype.contains("EXECUTIVE"))
        {
            Log.d("usertype31", "" + usertype);
            isFABOpen = true;
            fabLayout1.setVisibility(View.VISIBLE);
            fabLayout2.setVisibility(View.INVISIBLE);
            //fabLayout3.setVisibility(View.VISIBLE);
            fabBGLayout.setVisibility(View.VISIBLE);
            fab.animate().rotationBy(180);
            fabLayout1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
            fabLayout2.animate().translationY(-getResources().getDimension(R.dimen.standard_100));

        }
        else
        {
            Log.d("usertype41", "" + usertype);
            (Toast.makeText(MainActivity.this, "You are not Manager as well as Executive", Toast.LENGTH_SHORT)).show();
        }

        //fabLayout3.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
    }


    private void closeFABMenu() {
        isFABOpen = false;
        fabBGLayout.setVisibility(View.GONE);
        fab.animate().rotation(0);
        fabLayout1.animate().translationY(0);
        fabLayout2.animate().translationY(0);
        //fabLayout3.animate().translationY(0);
        fabLayout2.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isFABOpen) {
                    fabLayout1.setVisibility(View.GONE);
                    fabLayout2.setVisibility(View.GONE);
                    //fabLayout3.setVisibility(View.GONE);
                }
/*                if (fab.getRotation() != -180) {
                    fab.setRotation(-180);
                }*/
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }


    //For CALENADAR VIEW
    @SuppressLint("RestrictedApi")
    private void showFABMenuCalendar() {

        isFABOpen = true;
        Log.d("usertype11", "" + usertype);
        if(usertype.contains("MANAGER")){
            Log.d("usertype21", "" + usertype);
            isFABOpen = true;
            fabLayout_todocalendar.setVisibility(View.VISIBLE);
            fabLayout_assigntaskcalendar.setVisibility(View.VISIBLE);
            //fabLayout3.setVisibility(View.VISIBLE);
            fabBGLayoutCalendar.setVisibility(View.VISIBLE);
            fabLayout_todocalendar.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
            fabLayout_assigntaskcalendar.animate().translationY(-getResources().getDimension(R.dimen.standard_100));

        }
        else if(usertype.contains("EXECUTIVE"))
        {
            Log.d("usertype31", "" + usertype);
            isFABOpen = true;
            fabLayout_todocalendar.setVisibility(View.VISIBLE);
            fabLayout_assigntaskcalendar.setVisibility(View.INVISIBLE);
            //fabLayout3.setVisibility(View.VISIBLE);
            fabBGLayout.setVisibility(View.VISIBLE);
            fabLayout_todocalendar.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
            fabLayout_assigntaskcalendar.animate().translationY(-getResources().getDimension(R.dimen.standard_100));

        }
        else
        {
            Log.d("usertype41", "" + usertype);
            (Toast.makeText(MainActivity.this, "You are not Manager as well as Executive", Toast.LENGTH_SHORT)).show();
        }

        //fabLayout3.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
    }


    private void closeFABMenuCalendar() {
        isFABOpen = false;
        fabBGLayoutCalendar.setVisibility(View.GONE);
        calendar_fab.animate().rotation(0);
        fabLayout_todocalendar.animate().translationY(0);
        fabLayout_assigntaskcalendar.animate().translationY(0);
        //fabLayout3.animate().translationY(0);
        fabLayout_assigntaskcalendar.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isFABOpen) {
                    fabLayout_todocalendar.setVisibility(View.GONE);
                    fabLayout_assigntaskcalendar.setVisibility(View.GONE);
                }
/*                if (fab.getRotation() != -180) {
                    fab.setRotation(-180);
                }*/
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }





    private void loadFragment(Fragment fragment) {

        // create a FragmentManager
        FragmentManager fm = getSupportFragmentManager();

        // create a FragmentTransaction to begin the transaction and replace the Fragment
        androidx.fragment.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();

        //fragmentTransaction.addToBackStack(null);
        // replace the FrameLayout with new Fragment
        FragmentTransaction replace = fragmentTransaction.replace(R.id.main_layout_list, fragment);

        fragmentTransaction.commit(); // save the changes
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        spaceNavigationView.onSaveInstanceState(outState);
    }


    @Override
    public void onPause() {
        super.onPause();
        globalProgressDialog.dismissProgressDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        globalProgressDialog.dismissProgressDialog();
    }

}

