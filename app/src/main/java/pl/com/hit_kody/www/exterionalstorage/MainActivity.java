package pl.com.hit_kody.www.exterionalstorage;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

public class MainActivity extends AppCompatActivity {

    Button przycisk = null,przycisk2=null;
    private static final String SAMPLE_DB_NAME = "Baza";
    SmbFile sfile = null;
    String url = "";
    private final Context context;
    SQLiteDatabase sampleDB = null;

    int poloaczenie=0;
    static ResultSet rs;
    static Statement st;
    PreparedStatement ps;
    FileInputStream fis = null;
    Connection connection = null;

    TextView text=null;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    public MainActivity() {
        context = null;
    }


    public static boolean Writefile() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Log.i("test", "Can write the file :) ");
            return true;
        }
        return false;
    }

    // Storage Permissions to save data on device
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }


    }


    private void ToDataBaseSqllight() {

        try {

            //  File outFile = new File(Environment.getDataDirectory(), outFileName);
            // outFile.setWritable(true);
            //   SQLiteDatabase.openDatabase(outFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
            // sfile.createNewFile();
            //   String dupa ="";
            //   dupa = sfile.getPath();
            //  Log.i("test","dd: "+dupa);
            // sampleDB = SQLiteDatabase.openOrCreateDatabase(nowy,null);


            String myPath = "MICHALW/Nowy/BAZA";
            sampleDB = SQLiteDatabase.openDatabase(myPath,null, SQLiteDatabase.OPEN_READONLY);
            sampleDB.close();

          //  sampleDB = SQLiteDatabase.openDatabase(context.getDatabasePath(sfile).getParent(), null);


            // sampleDB = SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath(url+SAMPLE_DB_NAME).getAbsolutePath(), null);
            // File dbFile = myContext.getDatabasePath(String.valueOf(sfile));
            // SQLiteDatabase sampleDB = SQLiteDatabase.openOrCreateDatabase(dbFile, null);


           // sampleDB.execSQL("CREATE TABLE IF NOT EXISTS edit (Id VARCHAR, Gatunek VARCHAR,Odmiana VARCHAR,Data_Godzina VARCHAR,Powierzchnia VARCHAR,Nr_Działki VARCHAR," +
            //        "Preparat VARCHAR,Dawka VARCHAR,Substancja_czynna VARCHAR,Temperatura VARCHAR,Faza_rozwoju VARCHAR,Przyczyny_zabiegu VARCHAR,Uwagi VARCHAR, Uzytkownik VARCHAR);");
           // sampleDB.close();

        } catch (Exception e) {
            Log.i("test", "" + e);
        }
    }

    private boolean executeCommand() {
        System.out.println("executeCommand");
        Runtime runtime = Runtime.getRuntime();
        try {
            Process mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 192.168.0.85");
            int mExitValue = mIpAddrProcess.waitFor();
            System.out.println(" mExitValue " + mExitValue);
            if (mExitValue == 0) {
                return true;
            } else {
                return false;
            }
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
            System.out.println(" Exception:" + ignore);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(" Exception:" + e);
        }
        return false;
    }


    //tworzenie polaczenia z baza danych
    public void connect() {
        String url = "jdbc:mysql://192.168.0.85/HitBaza";
        String user = "sa";
        String pass = "michal123";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        poloaczenie = 1;

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
              showToast("brak polaczenia z internetem");
            Log.i("aaa", String.valueOf(e));
            poloaczenie = 0;
            return;
        }

        try {
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            showToast("brak polaczenia z internetem");
            Log.i("aaa", String.valueOf(e));
            poloaczenie = 0;
            return;
        }

    }

    public void connect2()
    {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
          //  connection = DriverManager.getConnection("jdbc:jdbc://192.168.0.85/HitBaza;encrypt=false;user=sa;password=michal123;instance=MICHALW/SQLEXPRESS;");


            String user="";
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(""
                    + "jdbc:jtds:sqlserver://192.168.0.85/HitBaza;instance=SQLEXPRESS;"
                    + "user=sa;password=michal123;");

         //   connection = DriverManager.getConnection(""
          //          + "jdbc:jtds:sqlserver://192.168.0.8/HitBazaMefa2;instance=SQL2014;"
          //          + "user=mw;password=michalw;");

          //   Statement stmt = connection.createStatement();
           //  ResultSet reset = stmt.executeQuery("select * from dokument");


        } catch (SQLException e) {
            e.printStackTrace();
            Log.i("aaa", String.valueOf(e));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.i("aaa", String.valueOf(e));
        } catch (InstantiationException e) {
            e.printStackTrace();
            Log.i("aaa", String.valueOf(e));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.i("aaa", String.valueOf(e));
        }
    }

    public void connect3()
    {

        ArrayList user = new ArrayList();
        String sql = ("select * from operacja ");
        int i =0;

        try {
            PreparedStatement stmt2 = connection.prepareStatement(sql);
             rs = stmt2.executeQuery();

           while (rs.next()) {
                user.add(rs.getString("nazwa"));
                i++;
           }
            Log.d("test",""+user.size()+"  "+i);

        } catch (SQLException e1) {
            e1.printStackTrace();
            Log.i("myTag", "3" + e1);
        }

        try {
            if (connection != null)
                connection.close();
        } catch (SQLException se) {
           Log.i("myTag", "4" + se);
               showToast("brak polaczenia z internetem");
       }


    }

    public void insert()
    {

        for (int j = 0; j <= 100000000; j++) {


            String sql1 = "INSERT INTO logi (blad,nazwa_proc,uzytkownik,status) VALUES (?,?,?,?)";

            try {
                connection.setAutoCommit(false);
                ps = connection.prepareStatement(sql1);

                //    ps.setString(1, String.valueOf(j + 1));
                ps.setString(1, String.valueOf("Testowy insert into log"));
                ps.setString(2, String.valueOf("mw"));
                ps.setString(3, String.valueOf("1"));
                ps.setString(4, String.valueOf("W"));
                ps.executeUpdate();
                connection.commit();
                Log.i("myTag", "4" + j);
               // text.setText(String.valueOf(j));

            } catch (SQLException e) {
                Log.i("myTag", "4" + e);
                // showToast("brak połączenia z internetem" + e);
            }

        }
            try {
                if (connection != null)
                    connection.commit();
                connection.close();
            } catch (SQLException se) {
                Log.i("myTag", "4" + se);
                //     showToast("brak połączenia z internetem" +se);
            }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(MainActivity.this);

        przycisk = (Button) findViewById(R.id.button);
        przycisk2 = (Button) findViewById(R.id.button2);

        text = (TextView) findViewById(R.id.textView);

      //  for(int i=0;i<=100;i++)
      //  {
      //      text.setText(String.valueOf(i));
      //  }

        przycisk2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Activity_name.class);
                startActivity(i);
            }
        });

        przycisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  connect();
                connect2();
               // connect3();
                insert();


           //     StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
             //   StrictMode.setThreadPolicy(policy);

/*
                try {
                    String z ="dupa";
                    url = "smb://192.168.0.85/Nowy/"
                    + "log12.txt";

                    NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(
                            null, "Michal-Hit-Kody", "Kaseta1990");
                    sfile = new SmbFile(url, auth);


                    Log.i("test", "" + String.valueOf(sfile));

                    ToDataBaseSqllight();

                       if (!sfile.exists()) {
                           sfile.createNewFile();
                           SmbFileOutputStream sfos = new SmbFileOutputStream(sfile);
                           sfos.write(z.getBytes());
                           sfos.close();
                           z = "Created the file for you!!!!";
                            Log.i("test","3  "+z.toString());
                        } else
                            z = "Already exists at the specified location!!!!";
                        Log.i("test","3  "+z.toString());

                } catch (Exception ex) {
                    // TODO: handle exception
                    Log.i("test", "3  " + ex.getMessage().toString());
                }

*/
            }


        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.mipmap.ic_launcher);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        Log.i("test","resume ");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.i("test","stop  ");
        super.onStop();
    }

    @Override
    protected void onStart() {
        Log.i("test","start  ");
        super.onStart();
    }
}
