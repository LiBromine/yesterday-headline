package com.java.libingrui;

import java.util.List;
import androidx.room.*;

import android.app.Activity;

/**
 * NewsManager is the middleware between frontend and backend.
 * And we use Singleton Pattern for this Class.
 */
class NewsManager {
    private static NewsManager instance;
    private static int counter = 0;

    final private AppDataBase db;
    private RemoteServiceManager remoteServiceManager;

    /**
     * Singleton constructor : will be called no more than once.
     */
    private NewsManager(Activity app) {
        ++counter;
        db = Room.databaseBuilder(app.getApplicationContext(), AppDataBase.class, "MyDataBase").build();
        remoteServiceManager = new RemoteServiceManager();
    }

    /**
     * get the one and the only one NewsManager
     * if the instance doesn't exist, create it.
     * @param mainActivity the main activity of the App, notice that the backend will instantiate a database in this activity.
     * @return the instance of NewsManager
     */
    static public NewsManager getInstanceAtMainActivity(Activity mainActivity) {
        if( counter == 0) {
            instance = new NewsManager(mainActivity);
        }
        return instance;
    }

    /**
     * get the instance, if it not exist, it will return null
     * @return the only instance of NewsManager
     */
    static public NewsManager getInstanceAtAnyWhere() {
        return instance;
    }

    /**
     * flush news by {type,pagination, size of a page}
     * @param type all,event,points,news,paper
     * @param PageNumber index start from 1
     * @param PageSize page size, no less than 5
     */
    public void flushByPagination(String type, int PageNumber, int PageSize) {
        //@TODO not finish here
    }

    /**
     * flush all news, and store the result in the database.
     * It will create a new thread to work, and lock the database during working.
     */
    public void flushAll() {
        Thread thread = new Thread() {
            public void run() {
                List<NewsAbstract> datas = remoteServiceManager.getAllNewsAbstract();
                NewsAbstract[] newsAbstracts = new NewsAbstract[datas.size()];
                datas.toArray(newsAbstracts);
                synchronized (db) {
                    db.newsAbstractDao().insertAll(newsAbstracts);
                }
            }
        };
        thread.start();
    }

    /**
     * get all news id.
     * @return the list of all news id
     */
    public List<String> getAllNewsId() {
        ThreadManager< List<String> > thread = new ThreadManager< List<String> >() {
            public void run() {
                synchronized (db) {
                    result = db.newsAbstractDao().getAllId();
                }
            }
        };

        thread.start();
        try {
            thread.join();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return thread.getResult();
    }

    /**
     * get one page's news id
     * @param PageNumber The page which user want to get
     * @return the list of news id in this page
     */
    public List<String> getNewsIdByPagination(int PageNumber) {
        return null;
        //@TODO not finish here
    }

    /**
     * get one news' abstract by id, return a json format info
     * @param id news id
     * @return json format information about news abstract, refer to API DOC provided by TA for more details about what you can get
     */
    public String getNewsAbstractByNewsId(final String id) {
        ThreadManager<NewsAbstract> thread = new ThreadManager<NewsAbstract>() {
            public void run() {
                synchronized (db) {
                    result = db.newsAbstractDao().findById(id);
                }
            }
        };

        thread.start();
        try {
            thread.join();
        }
        catch ( Exception e) {
            e.printStackTrace();
        }

        return thread.getResult().toJson();
    }

    /**
     * get one news' details by id, return a json format info
     * @param id news id
     * @return json format information about news details, refer to API DOC provided by TA for more details about what you can get
     */
    public String getNewsDetailsByNewsId(String id) {
        return null;
        // @TODO not finish here
    }

    static class ThreadManager<T> extends Thread {
        protected T result;

        public T getResult(){
            return result;
        }
    }
}
