package com.teamvallartas.autodue;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.view.ActionMode;
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
import android.widget.TextView;
import android.widget.Toast;

import com.grokkingandroid.samplesapp.samples.recyclerviewdemo.R;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    static List<TaskModel> items;
    static int itemCount;
    GestureDetectorCompat gestureDetector;
    ActionMode actionMode;
    ImageButton fab;
    static Context mContext;
    PopupWindow popOver;

    static String AUTODUEFILE = "AUTODUEFile";

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
        //items = RecyclerViewDemoApp.getDemoData();
        items = new ArrayList<TaskModel>();
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

        myCalendar = new com.teamvallartas.autodue.Calendar(this.getApplicationContext());
        mDrawerList = (ListView)findViewById(R.id.navList);
        addDrawerItems();

        checkPastEvents(items);

        // Read from file if exists
        readFromFile();

        // Make notifications
        checkNotification();
    }

    // Notification
    public void checkNotification()
    {
        if(adapter.getSize()>0)
        {
            TaskModel m = adapter.getItem(0);
            NotificationClass.dueTask = m.label;
            Intent intent=new Intent(this,NotificationClass.class);
            AlarmManager manager=(AlarmManager)getSystemService(Activity.ALARM_SERVICE);
            PendingIntent pendingIntent= PendingIntent.getService(this.getApplicationContext(), 0, intent, 0);
            manager.set(AlarmManager.RTC,m.end.getTime(),pendingIntent);
        } else {
            System.out.println("No notifications to show");
        }
    }

    private void readFromFile(){

        // For now only processes one TaskModel
        FileInputStream fin = null;
        try{
            fin = mContext.getApplicationContext().openFileInput(AUTODUEFILE);

            String result="";
            int content;
            while ((content = fin.read()) != -1) {
                // convert to char and display it
                result = result + Character.toString((char) content);
            }

            // Split all contents by newline character
            String[] split;
            split = result.split("\\n");
            fin.close();

            // Get string size for debug
//            System.out.println("No. of split elements:  " + split.length);
//            System.out.println("No. of loops:: " + split.length/8);

            // Create model for contents
            for(int i=0; i<split.length/8; i++)
            {
                TaskModel model = new TaskModel();

                model.label = split[0+i*8];
                model.description = split[1+i*8];
                model.duration = Long.valueOf(split[2+i*8]);
                model.id = Integer.parseInt(split[3+i*8]);
                try{
                    DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                    Date deadlineDate = format.parse(split[4+i*8]);
                    model.deadline = deadlineDate;
                } catch (ParseException e){
                    e.printStackTrace();
                }
                model.priority = Integer.parseInt(split[5+i*8]);
                try{
                    DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                    Date beginDate = format.parse(split[6+i*8]);
                    model.begin = beginDate;
                } catch (ParseException e){
                    e.printStackTrace();
                }
                try{
                    DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                    Date endDate = format.parse(split[7+i*8]);
                    model.end = endDate;
                } catch (ParseException e){
                    e.printStackTrace();
                }
                RecyclerViewDemoApp.addItemToList(model);
                adapter.addData(model, 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (fin != null)
                    fin.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void checkPastEvents(List<TaskModel> items)
    {
        Date d = new Date();
        for(int i=0; i<items.size(); i++)
        {
            if(items.get(i).getDeadline().getTime()< d.getTime())
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

    public static void addItemToList(TaskModel model) {
        //TaskModel model = new TaskModel();
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
        adapter.addData(model,0);

        // After adding to adapter, also add to file
        addToFile(model);

    }

    // Appends to file the model that is input as parameter
    static void addToFile(TaskModel model){

        try{
            FileOutputStream fos = mContext.getApplicationContext().openFileOutput(AUTODUEFILE, Context.MODE_APPEND);

            // Writing TaskModel's attributes separated by newlines
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"),8192);

            bw.write(model.label);
            bw.write("\n");
            bw.write(model.description);
            bw.write("\n");
            bw.write(String.valueOf(model.duration));
            bw.write("\n");
            bw.write(String.valueOf(model.id));
            bw.write("\n");
            bw.write(model.deadline.toString());
            bw.write("\n");
            bw.write(String.valueOf(model.priority));
            bw.write("\n");
            bw.write(model.begin.toString());
            bw.write("\n");
            bw.write(model.end.toString());
            bw.write("\n");

            bw.close();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*private void removeItemFromList() {
        int position = ((LinearLayoutManager) recyclerView.getLayoutManager()).
                findFirstCompletelyVisibleItemPosition();
        RecyclerViewDemoApp.removeItemFromList(position);
        adapter.removeData(position);
    }*/

    // Asides from removing from adapter, also updates file
    public static void removeItemFromListUsingObject(TaskModel model){
        adapter.removeDataUsingObject(model);
        adapter.sort();

        // Empty the file then readd items in adapter List
        try{
            FileOutputStream fos = mContext.getApplicationContext().openFileOutput(AUTODUEFILE, Context.MODE_PRIVATE);
            fos.write(new String().getBytes());
            fos.close();

        }catch(Exception e){
            e.printStackTrace();
        }

        // Get updated adapter list so we can re add to file
        List<TaskModel> toReAdd = adapter.getAllItems();
        for(int i=0; i<toReAdd.size(); i++)
        {
            addToFile(toReAdd.get(i));
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        if(view == null){
            return;
        }
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
            TaskModel data = adapter.getItem(idx);
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
    }

    private void addDrawerItems() {
        String[] navArray = { "Task List", "Calendar", "Settings"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navArray);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                if(position == 0) {
                    DrawerLayout mDrawerLayout;
                    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                    mDrawerLayout.closeDrawers();
                }
                else if(position == 1){
                    openCalendar();
                }
                else if(position == 2){
                    DrawerLayout mDrawerLayout;
                    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                    mDrawerLayout.closeDrawers();
                    startActivity(new Intent(RecyclerViewDemoActivity.this, Settings.class));
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
        com.teamvallartas.autodue.Calendar.clear();
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

