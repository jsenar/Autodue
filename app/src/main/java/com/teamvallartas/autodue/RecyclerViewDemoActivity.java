package com.teamvallartas.autodue;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeTransform;
import android.util.Log;
import android.view.ActionMode;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.view.Gravity;

import com.grokkingandroid.samplesapp.samples.recyclerviewdemo.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.view.GestureDetector.SimpleOnGestureListener;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class RecyclerViewDemoActivity
        extends Activity
        implements RecyclerView.OnItemTouchListener,
        View.OnClickListener,
        ActionMode.Callback {

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    com.teamvallartas.autodue.Calendar myCalendar;
    static RecyclerView recyclerView;
    static RecyclerViewDemoAdapter adapter;
    static int itemCount;
    GestureDetectorCompat gestureDetector;
    ActionMode actionMode;
    ImageButton fab;
    Context mContext;
    PopupWindow popOver;

    //popup items


    private Button m_button_getEvents;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        Window win = getWindow();
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        win.setAllowReturnTransitionOverlap(true);
        win.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        win.setSharedElementExitTransition(new ChangeTransform());

        setContentView(R.layout.activity_recyclerview_demo);
        fab = (ImageButton) findViewById(R.id.fab_add);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // actually VERTICAL is the default,
        // just remember: LinearLayoutManager
        // supports HORIZONTAL layout out of the box
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // you can set the first visible item like this:
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);


        // allows for optimizations if all items are of the same size:
        recyclerView.setHasFixedSize(true);

        // IMPORTANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        List<DemoModel> items = RecyclerViewDemoApp.getDemoData();
        adapter = new RecyclerViewDemoAdapter(items);
        recyclerView.setAdapter(adapter);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);

        // this is the default; this call is actually only necessary with custom ItemAnimators
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // onClickDetection is done in this Activity's onItemTouchListener
        // with the help of a GestureDetector;
        // Tip by Ian Lake on G+ in a comment to this post:
        // https://plus.google.com/+LucasRocha/posts/37U8GWtYxDE
        recyclerView.addOnItemTouchListener(this);
        gestureDetector =
                new GestureDetectorCompat(this, new RecyclerViewDemoOnGestureListener());

        // fab
        View fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(this);

        myCalendar = new com.teamvallartas.autodue.Calendar();
        mDrawerList = (ListView)findViewById(R.id.navList);
        addDrawerItems();

        checkPastEvents(items);
    }

    private void checkPastEvents(List<DemoModel> items)
    {
        Date d = new Date();
        for(int i=0; i<items.size(); i++)
        {
            if(items.get(i).getDateTime().getTime()< d.getTime())
            {
                Toast.makeText(getApplicationContext(), items.get(i).getLabel() + " is overdue", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_cardview_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_refresh) {
            adapter.sort();
        }
        return true;
    }
    //TODO model needs to pass in values of the popup
    public static void addItemToList(DemoModel model) {
        //DemoModel model = new DemoModel();
        //model.label = "New Task " + itemCount;
        itemCount++;
        int position = ((LinearLayoutManager) recyclerView.getLayoutManager()).
                findFirstVisibleItemPosition();
        // needed to be able to show the animation
        // otherwise the view would be inserted before the first
        // visible item; that is outside of the viewable area
        position++;
        //getCalendarEvents();
        RecyclerViewDemoApp.addItemToList(model);
        adapter.addData(model, position);

    }

    /*private void removeItemFromList() {
        int position = ((LinearLayoutManager) recyclerView.getLayoutManager()).
                findFirstCompletelyVisibleItemPosition();
        RecyclerViewDemoApp.removeItemFromList(position);
        adapter.removeData(position);
    }*/

    public static void removeItemFromListUsingObject(DemoModel model){
        adapter.removeDataUsingObject(model);
        adapter.sort();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_add) {
            // fab click

            getCalendarEvents();
            startActivity(new Intent(RecyclerViewDemoActivity.this, TaskScreen.class));


            //new Intent(RecyclerViewDemoActivity.this, TaskScreen.class);
            //onShowPopup(view);

            //addItemToList();
        } else if (view.getId() == R.id.container_list_item) {
            // item click
            int idx = recyclerView.getChildPosition(view);
            if (actionMode != null) {
                myToggleSelection(idx);
                return;
            }
            DemoModel data = adapter.getItem(idx);
            View innerContainer = view.findViewById(R.id.container_inner_item);
            innerContainer.setTransitionName(Constants.NAME_INNER_CONTAINER + "_" + data.id);
            Intent startIntent = new Intent(this, CardViewDemoActivity.class);
            startIntent.putExtra(Constants.KEY_ID, data.id);
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, innerContainer,
                            Constants.NAME_INNER_CONTAINER);
            this.startActivity(startIntent, options.toBundle());
        }
    }
//    public void onShowPopup(View v){
//        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View inflatedView = layoutInflater.inflate(R.layout.taskpopup, null, false);
//        Display display = getWindowManager().getDefaultDisplay();
//        final Point size = new Point();
//        display.getSize(size);
//        int mDeviceHeight = size.y;
//        popOver = new PopupWindow(inflatedView, size.x - 50,size.y - 400, true );
//        popOver.setBackgroundDrawable(getResources().getDrawable(R.drawable.popover_background));
//        popOver.setFocusable(true);
//        // make it outside touchable to dismiss the popup window
//        popOver.setOutsideTouchable(true);
//        popOver.showAtLocation(v, Gravity.BOTTOM, 0, 200);
//
//
//    }
    private void myToggleSelection(int idx) {
        adapter.toggleSelection(idx);
        String title = getString(R.string.selected_count, adapter.getSelectedItemCount());
        actionMode.setTitle(title);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        // Inflate a menu resource providing context menu items
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.menu_cab_recyclerviewdemoactivity, menu);
        fab.setVisibility(View.GONE);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            /*case R.id.menu_delete:
                List<Integer> selectedItemPositions = adapter.getSelectedItems();
                int currPos;
                for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
                    currPos = selectedItemPositions.get(i);
                    RecyclerViewDemoApp.removeItemFromList(currPos);
                    adapter.removeData(currPos);
                }
                actionMode.finish();
                return true;*/
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        this.actionMode = null;
        adapter.clearSelections();
        fab.setVisibility(View.VISIBLE);
    }

    private class RecyclerViewDemoOnGestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            onClick(view);
            return super.onSingleTapConfirmed(e);
        }

        public void onLongPress(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (actionMode != null) {
                return;
            }
            // Start the CAB using the ActionMode.Callback defined above
            actionMode = startActionMode(RecyclerViewDemoActivity.this);
            int idx = recyclerView.getChildPosition(view);
            myToggleSelection(idx);
            super.onLongPress(e);
        }
    }

    private void addDrawerItems() {
        String[] navArray = { "Task List", "Calendar", "Settings"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navArray);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                if(position == 0){
                    DrawerLayout mDrawerLayout;
                    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                    mDrawerLayout.closeDrawers();
                }else if(position == 1){
                    openCalendar();
                }
            }
        });


    }

    private void getLastThreeEvents() {
        long startMillis = 0;
        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, startMillis);
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
        startActivity(intent);
    }
    private void getCalendarEvents(){
        //String DEBUG_TAG = "MyActivity";
        String[] INSTANCE_PROJECTION = new String[] {
                CalendarContract.Instances.EVENT_ID,      // 0
                CalendarContract.Instances.BEGIN,         // 1
                CalendarContract.Instances.TITLE,          // 2
                CalendarContract.Instances.END              //3
        };

// The indices for the projection array above.
        int PROJECTION_ID_INDEX = 0;
        int PROJECTION_BEGIN_INDEX = 1;
       int PROJECTION_TITLE_INDEX = 2;
        int PROJECTION_END_INDEX = 3;
// Specify the date range you want to search for recurring
// event instances

        java.util.Calendar beginTime = java.util.Calendar.getInstance();
        //beginTime.set(1970, 9, 23, 8, 0);
        long startMillis = beginTime.getTimeInMillis();
        java.util.Calendar endTime = java.util.Calendar.getInstance();
        endTime.setTimeInMillis(endTime.getTimeInMillis()+ (1000*60*60*24*365));
        long endMillis = endTime.getTimeInMillis();

        Cursor cur = null;
        ContentResolver cr = getContentResolver();

// The ID of the recurring event whose instances you are searching
// for in the Instances table
        //String selection = CalendarContract.Instances.EVENT_ID + " = ?";
        //String[] selectionArgs = new String[] {"207"};

// Construct the query with the desired date range.

        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startMillis);
        ContentUris.appendId(builder, endMillis);

// Submit the query
        cur =  cr.query(builder.build(),
                INSTANCE_PROJECTION,
                null,
                null,
                //selection,
                //selectionArgs,
                null);

        int i = 0;
        while (cur.moveToNext()) {
            i++;
            String title = null;
            long eventID = 0;
            long beginVal = 0;
            long endVal = 0;
            eventID = cur.getLong(PROJECTION_ID_INDEX);
            beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
            title = cur.getString(PROJECTION_TITLE_INDEX);
            endVal = cur.getLong(PROJECTION_END_INDEX);
            com.teamvallartas.autodue.Calendar.insert(new Event(new Date(beginVal),new Date(endVal),title));
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTimeInMillis(beginVal);
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        }

    }
    public void openCalendar(){
        long startMillis = new Date().getTime();

        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, startMillis);
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(builder.build());
        startActivity(intent);
    }

}

