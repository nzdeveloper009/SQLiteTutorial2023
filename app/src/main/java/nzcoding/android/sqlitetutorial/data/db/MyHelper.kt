package nzcoding.android.sqlitetutorial.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import nzcoding.android.sqlitetutorial.utils.ConstName.COL_MEANING
import nzcoding.android.sqlitetutorial.utils.ConstName.COL_NAME
import nzcoding.android.sqlitetutorial.utils.ConstName.TB_NAME

class MyHelper(context: Context) : SQLiteOpenHelper(context,"ACDB",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TB_NAME(_id integer primary key autoincrement, $COL_NAME TEXT, $COL_MEANING TEXT)")
        db?.execSQL("INSERT INTO $TB_NAME($COL_NAME,$COL_MEANING)VALUES('WWW','World Wide Web')")
        db?.execSQL("INSERT INTO $TB_NAME($COL_NAME,$COL_MEANING)VALUES('GDG','Google Developer Group')")
        db?.execSQL("INSERT INTO $TB_NAME($COL_NAME,$COL_MEANING)VALUES('AVD','Android Virtual Device')")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
    }

}