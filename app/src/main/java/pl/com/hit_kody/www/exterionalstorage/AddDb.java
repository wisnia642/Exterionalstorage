package pl.com.hit_kody.www.exterionalstorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Michal-Hit-kody on 2018-06-05.
 */

public class AddDb extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/YOUR_PACKAGE/databases/";

    private static String DB_NAME = "myDBName";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    public AddDb(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, Context myContext) {
        super(context, name, factory, version);
        this.myContext = myContext;
    }

    public void createDataBase() throws IOException{
        boolean dbExist = checkDataBase();

        if(dbExist)
        {
            //database exist
        }
        else{
            this.getReadableDatabase();
            copyDataBase();
        }
    }

    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        String myPath = "smb://192.168.0.85/Nowy/BAZA";
        checkDB = SQLiteDatabase.openDatabase(myPath,null, SQLiteDatabase.OPEN_READONLY);
        checkDB.close();

        return checkDB!=null ? true:false;
    }

    public void copyDataBase(){}


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
