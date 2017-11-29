package com.sjec.rcms.baidu;

import com.sjec.rcms.dao.DaoMaster;
import com.sjec.rcms.dao.DaoSession;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

public class MyApplication extends Application {

	public static final boolean SHIELD_EXCEPTION = false;
	// 建立全局的daoSession
	public static DaoSession daoSession;
	public static SQLiteDatabase db;

	@Override
	public void onCreate() {
		super.onCreate();
		// 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
		// 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
		// 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
		// 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
		// 数据库的名字是my_data
		DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,
				"my_data", null);
		db = helper.getWritableDatabase(); 
		// 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
		DaoMaster daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
	}

}
