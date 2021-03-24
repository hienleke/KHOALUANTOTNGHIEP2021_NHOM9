package com.example.testdoan;

import android.content.Context;
import android.os.Debug;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.testdoan.dao.AppDatabase;
import com.example.testdoan.dao.CategoryDao;
import com.example.testdoan.dao.UserDao;
import com.example.testdoan.model.Category;
import com.example.testdoan.model.Expense;
import com.example.testdoan.model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.testdoan", appContext.getPackageName());

    }

    private UserDao userDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = getApplicationContext();
        db = AppDatabase.getDatabase(context);
        userDao = db.userDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        User user = new User("asdfas", "sadfasfd", "asdfsa@gmail.com", "3424");
        //User user = new User("ASfdsa", "2324", "lekehien5431@gmail.com", "2342");
       // AppDatabase.getDatabase(getApplicationContext()).userDao().insert(user);
        Expense e = new Expense(0, Calendar.getInstance(),"sadfsa",12313.4,true,true);
        Category category = new Category("xxx", "san pham");
        AppDatabase.getDatabase(getApplicationContext()).categoryDao().insert(category);
        AppDatabase.getDatabase(getApplicationContext()).expenseDao().insert(e);
       // userDao.insert(user);
       // user.setId(0);
        //LiveData<List<User>> byName = AppDatabase.getDatabase(getApplicationContext()).userDao().getAllusers();equalTo(user);
      //  assertThat(byName.getValue().size(), equalTo(1));

    }

    @Test
    public void testCalendaranddate()
    {
        Calendar c = Calendar.getInstance();
        Log.d("testssss", c.getTime().toString());
    }
};