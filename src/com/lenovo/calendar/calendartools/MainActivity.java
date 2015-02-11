
package com.lenovo.calendar.calendartools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    public static final String[] CALENDAR_PROJECTION = {
            Calendars.NAME,
            Calendars.ACCOUNT_NAME,
            Calendars.ACCOUNT_TYPE,
            Calendars.CALENDAR_ACCESS_LEVEL,
            Calendars.CALENDAR_DISPLAY_NAME,
            Calendars.CALENDAR_TIME_ZONE,
            Calendars.DELETED

    };
    public static final int CALENDAR_NAME_INDEX = 0;
    public static final int CALENDAR_ACCOUNT_NAME_INDEX = 1;
    public static final int CALENDAR_ACCOUNT_TYPE_INDEX = 2;
    public static final int CALENDAR_CALENDAR_ACCESS_LEVEL_INDEX = 3;
    public static final int CALENDAR_CALENDAR_DISPLAY_NAME_INDEX = 4;
    public static final int CALENDAR_CALENDAR_TIME_ZONE_INDEX = 5;
    public static final int CALENDAR_DELETED_INDEX = 6;

    public static final String[] EVENT_PROJECTION = {
            Events.CALENDAR_ID, 
            Events.TITLE, 
            Events.DESCRIPTION,
            Events.EVENT_LOCATION,
            Events.STATUS,
            Events.LAST_SYNCED,
            Events.DTSTART,
            Events.DTEND,
            Events.DURATION,
            Events.EVENT_TIMEZONE,
            Events.ALL_DAY,
            Events.HAS_ALARM,
            Events.RRULE,
            Events.RDATE,
            Events.ORIGINAL_ID,
            Events.ORIGINAL_SYNC_ID,
            Events.ORIGINAL_INSTANCE_TIME,
            Events.ORIGINAL_ALL_DAY,
            Events.LAST_DATE,
            Events.HAS_ATTENDEE_DATA,
            Events.ORGANIZER,
            Events.DELETED
    };
    
    public static final int EVENT_CALENDAR_ID_INDEX = 0;
    public static final int EVENT_TITLE_INDEX = 1;
    public static final int EVENT_DESCRIPTION_INDEX = 2;
    public static final int EVENT_EVENT_LOCATION_INDEX = 3;
    public static final int EVENT_STATUS_INDEX = 4;
    public static final int EVENT_LAST_SYNCED_INDEX = 5;
    public static final int EVENT_DTSTART_INDEX = 6;
    public static final int EVENT_DTEND_INDEX = 7;
    public static final int EVENT_DURATION_INDEX = 8;
    public static final int EVENT_EVENT_TIMEZONE_INDEX = 9;
    public static final int EVENT_ALL_DAY_INDEX = 10;
    public static final int EVENT_HAS_ALARM_INDEX = 11;
    public static final int EVENT_RRULE_INDEX = 12;
    public static final int EVENT_RDATE_INDEX = 13;
    public static final int EVENT_ORIGINAL_ID_INDEX = 14;
    public static final int EVENT_ORIGINAL_SYNC_ID_INDEX = 15;
    public static final int EVENT_ORIGINAL_INSTANCE_TIME_INDEX = 16;
    public static final int EVENT_ORIGINAL_ALL_DAY_INDEX = 17;
    public static final int EVENT_LAST_DATE_INDEX = 18;
    public static final int EVENT_HAS_ATTENDEE_DATA_INDEX = 19;
    public static final int EVENT_ORGANIZER_INDEX = 20;
    public static final int EVENT_DELETED_INDEX = 21;

    private Button mBtn;
    private Button mSendBtn;
    private TextView mTextView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtn = (Button) findViewById(R.id.action_btn);
        mSendBtn = (Button) findViewById(R.id.send_btn);
        mTextView = (TextView) findViewById(R.id.text);
        mTextView.setText("请点击 Test 按钮");
        mSendBtn.setVisibility(View.GONE);
        mBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new QueryTask(MainActivity.this).execute();
                mBtn.setEnabled(false);
                mTextView.setText("Loading......");
            }

        });
        mSendBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sendFile(mFilePath);
            }

        });
    }
    
    private class QueryTask extends AsyncTask<Void, Void, Boolean> {

        private Context mContext;

        public QueryTask(Context context) {
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            StringBuilder sb = new StringBuilder();
            sb.append(new SimpleDateFormat("### MM-dd HH:mm:ss SSS  ").format(new Date()));
            sb.append("###");
            sb.append("\n");
            sb.append("\n");
            sb.append("Start query calendars");
            sb.append("\n");
            
            
            Cursor calendarCursor = mContext.getContentResolver().query(Calendars.CONTENT_URI,
                    CALENDAR_PROJECTION, null, null, null);
            if (calendarCursor != null) {
                int columnCount = calendarCursor.getCount();
                sb.append("calendar count: " + columnCount);
                sb.append("\n");
                sb.append("<" + Calendars.NAME + ">  ");
                sb.append("<" + Calendars.ACCOUNT_NAME + ">  ");
                sb.append("<" + Calendars.ACCOUNT_TYPE + ">  ");
                sb.append("<" + Calendars.CALENDAR_ACCESS_LEVEL + ">  ");
                sb.append("<" + Calendars.CALENDAR_DISPLAY_NAME + ">  ");
                sb.append("<" + Calendars.CALENDAR_TIME_ZONE + ">  ");
                sb.append("<" + Calendars.DELETED + ">  ");
                sb.append("\n");
                
                while (calendarCursor.moveToNext()) {
                    String name = calendarCursor.getString(CALENDAR_NAME_INDEX);
                    String accountName = calendarCursor.getString(CALENDAR_ACCOUNT_NAME_INDEX);
                    String accountType = calendarCursor.getString(CALENDAR_ACCOUNT_TYPE_INDEX);
                    int calendarAccessLevel = calendarCursor.getInt(CALENDAR_CALENDAR_ACCESS_LEVEL_INDEX);
                    String calendarDisplayName = calendarCursor.getString(CALENDAR_CALENDAR_DISPLAY_NAME_INDEX);
                    String calendarTimeZone = calendarCursor.getString(CALENDAR_CALENDAR_TIME_ZONE_INDEX);
                    int deleted = calendarCursor.getInt(CALENDAR_DELETED_INDEX);
                    sb.append("<" + name + ">  ");
                    sb.append("<" + accountName + ">  ");
                    sb.append("<" + accountType + ">  ");
                    sb.append("<" + calendarAccessLevel + ">  ");
                    sb.append("<" + calendarDisplayName + ">  ");
                    sb.append("<" + calendarTimeZone + ">  ");
                    sb.append("<" + deleted + ">  ");
                    sb.append("\n");
                }
                calendarCursor.close();
            }
            sb.append("Finish query calendars");
            sb.append("\n");
            sb.append("\n");
            sb.append("Start query events");
            sb.append("\n");
            
            List<EventModel> events = new ArrayList<EventModel>();
            Cursor eventCursor = mContext.getContentResolver().query(Events.CONTENT_URI,
                    EVENT_PROJECTION, null, null, null);
            if (eventCursor != null) {
                int columnCount = eventCursor.getCount();
                sb.append("events count: " + columnCount);
                sb.append("\n");
                sb.append("<" + Events.CALENDAR_ID + ">  ");
                sb.append("<" + Events.TITLE + ">  ");
                sb.append("<" + Events.DESCRIPTION + ">  ");
                sb.append("<" + Events.EVENT_LOCATION + ">  ");
                sb.append("<" + Events.STATUS + ">  ");
                sb.append("<" + Events.LAST_SYNCED + ">  ");
                sb.append("<" + Events.DTSTART + ">  ");
                sb.append("<" + Events.DTEND + ">  ");
                sb.append("<" + Events.DURATION + ">  ");
                sb.append("<" + Events.EVENT_TIMEZONE + ">  ");
                sb.append("<" + Events.ALL_DAY + ">  ");
                sb.append("<" + Events.HAS_ALARM + ">  ");
                sb.append("<" + Events.RRULE + ">  ");
                sb.append("<" + Events.RDATE + ">  ");
                sb.append("<" + Events.ORIGINAL_ID + ">  ");
                sb.append("<" + Events.ORIGINAL_SYNC_ID + ">  ");
                sb.append("<" + Events.ORIGINAL_INSTANCE_TIME + ">  ");
                sb.append("<" + Events.ORIGINAL_ALL_DAY + ">  ");
                sb.append("<" + Events.LAST_DATE + ">  ");
                sb.append("<" + Events.HAS_ATTENDEE_DATA + ">  ");
                sb.append("<" + Events.ORGANIZER + ">  ");
                sb.append("<" + Events.DELETED + ">  ");
                sb.append("\n");
                while (eventCursor.moveToNext()) {
                    long calendarId = eventCursor.getLong(EVENT_CALENDAR_ID_INDEX);
                    String title = eventCursor.getString(EVENT_TITLE_INDEX);
                    String description = eventCursor.getString(EVENT_DESCRIPTION_INDEX);
                    String eventLocation = eventCursor.getString(EVENT_EVENT_LOCATION_INDEX);
                    int status = eventCursor.getInt(EVENT_STATUS_INDEX);
                    int lastSynced = eventCursor.getInt(EVENT_LAST_SYNCED_INDEX);
                    long dtstart = eventCursor.getLong(EVENT_DTSTART_INDEX);
                    long dtend = eventCursor.getLong(EVENT_DTEND_INDEX);
                    String duration = eventCursor.getString(EVENT_DURATION_INDEX);
                    String eventTimezone = eventCursor.getString(EVENT_EVENT_TIMEZONE_INDEX);
                    int allDay = eventCursor.getInt(EVENT_ALL_DAY_INDEX);
                    int hasAlarm = eventCursor.getInt(EVENT_HAS_ALARM_INDEX);
                    String rrule = eventCursor.getString(EVENT_RRULE_INDEX);
                    String rdate = eventCursor.getString(EVENT_RDATE_INDEX);
                    String originalId = eventCursor.getString(EVENT_ORIGINAL_ID_INDEX);
                    String originalSyncId = eventCursor.getString(EVENT_ORIGINAL_SYNC_ID_INDEX);
                    long originalInstanceTime = eventCursor.getLong(EVENT_ORIGINAL_INSTANCE_TIME_INDEX);
                    int originalAllDay = eventCursor.getInt(EVENT_ORIGINAL_ALL_DAY_INDEX);
                    long lastDate = eventCursor.getLong(EVENT_LAST_DATE_INDEX);
                    int hasAttendeeData = eventCursor.getInt(EVENT_HAS_ATTENDEE_DATA_INDEX);
                    String organizer = eventCursor.getString(EVENT_ORGANIZER_INDEX);
                    int deleted = eventCursor.getInt(EVENT_DELETED_INDEX);
                    
                    EventModel em = new EventModel();
                    em.calendarId = calendarId;
                    em.title = title;
                    em.description = description;
                    em.eventLocation = eventLocation;
                    em.status = status;
                    em.lastSynced = lastSynced;
                    em.dtstart = dtstart;
                    em.dtend = dtend;
                    em.duration = duration;
                    em.eventTimezone = eventTimezone;
                    em.allDay = allDay;
                    em.hasAlarm = hasAlarm;
                    em.rrule = rrule;
                    em.rdate = rdate;
                    em.originalId = originalId;
                    em.originalSyncId = originalSyncId;
                    em.originalInstanceTime = originalInstanceTime;
                    em.originalAllDay = originalAllDay;
                    em.lastDate = lastDate;
                    em.hasAttendeeData = hasAttendeeData;
                    em.organizer = organizer;
                    em.deleted = deleted;
                    events.add(em);
                    
                    sb.append("<" + calendarId + ">  ");
                    sb.append("<" + title + ">  ");
                    sb.append("<" + description + ">  ");
                    sb.append("<" + eventLocation + ">  ");
                    sb.append("<" + status + ">  ");
                    sb.append("<" + lastSynced + ">  ");
                    sb.append("<" + dtstart + ">  ");
                    sb.append("<" + dtend + ">  ");
                    sb.append("<" + duration + ">  ");
                    sb.append("<" + eventTimezone + ">  ");
                    sb.append("<" + allDay + ">  ");
                    sb.append("<" + hasAlarm + ">  ");
                    sb.append("<" + rrule + ">  ");
                    sb.append("<" + rdate + ">  ");
                    sb.append("<" + originalId + ">  ");
                    sb.append("<" + originalSyncId + ">  ");
                    sb.append("<" + originalInstanceTime + ">  ");
                    sb.append("<" + originalAllDay + ">  ");
                    sb.append("<" + lastDate + ">  ");
                    sb.append("<" + hasAttendeeData + ">  ");
                    sb.append("<" + organizer + ">  ");
                    sb.append("<" + deleted + ">  ");
                    sb.append("\n");
                }
                eventCursor.close();
            }
            sb.append("Finish query events");
            sb.append("\n");
            sb.append("\n");
            writeString(sb.toString(), events);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mBtn.setEnabled(true);
            File file = new File(mFilePath);
            mTextView.setText("完成！");
            if (result && !TextUtils.isEmpty(mFilePath) && file.exists()) {
                mTextView.setText("完成！ 生成文件 data.txt ，保存在SD卡的LenovoCalendar目录下，或直接点击Send按钮发送该文件");
                mSendBtn.setVisibility(View.VISIBLE);
                mBtn.setEnabled(false);
            }
        }
    }

    static String mFilePath;
    
    public static void writeString(String ss, List<EventModel> events) {
        File sdPath = Environment.getExternalStorageDirectory();
        String fileDir = sdPath.getPath() + "/LenovoCalendar";
        File dir = new File(fileDir);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                return;
            }
        }
        mFilePath = fileDir + "/" + "data" + ".txt";
        File file = new File(mFilePath);
        if (file.exists()) {
            file.delete();
        }
        
        try {
            FileWriter fileWriter;
            fileWriter = new FileWriter(mFilePath, false);
            fileWriter.append(ss);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        Log.e("ykm", "yykkmm events.size" + events.size());
        if (!events.isEmpty()) {
            toJsonFile(events, fileDir, "data.txt");
        }
        
       /* byte[] bytes = String.valueOf(ss).getBytes();
        try {
            FileOutputStream out = new FileOutputStream(filePath);
            out.write(bytes);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
    
    
    public static <T> void toJsonFile(List<T> list, String backupDirPath, String fileName) {
        Log.e("ykm", "yykkmm toJsonFile");
        try {
            byte[] byteArry = toJsonBody(list);
            File backupDir = new File(backupDirPath);
            if (!backupDir.exists()) {
                backupDir.mkdirs();
            }
            File jsonFile = new File(backupDir + "/" + fileName);
            try {
                FileOutputStream f = new FileOutputStream(jsonFile, true);
                f.write(byteArry);
                f.flush();
                f.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO: handle exception
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected static byte[] toJsonBody(Object obj) throws UnsupportedEncodingException
    {
        Gson gson = new Gson();
        String str = gson.toJson(obj);
        return str.getBytes("utf-8");
    }
    
    
    private void sendFile(String filePath) {
        File temp = new File(filePath);
        if (temp.exists()) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            String mimeType = MimeTypeMap.getSingleton()
                    .getMimeTypeFromExtension("txt");
            intent.setType(mimeType);
            Uri uri = Uri.fromFile(temp);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{
                    String.valueOf("yekm1@lenovo.com"),
                    String.valueOf("wugh2@lenovo.com")
            });
            startActivity(intent);
        }
    }
}
