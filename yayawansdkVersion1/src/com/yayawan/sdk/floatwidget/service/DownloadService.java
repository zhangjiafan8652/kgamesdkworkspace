package com.yayawan.sdk.floatwidget.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.yayawan.sdk.utils.ResourceUtil;


@SuppressLint("Registered")
public class DownloadService extends Service {
    private NotificationManager notificationMrg;
    private Map<String,Notification> notificationCache = new HashMap<String,Notification>();
    private Holder holder;
     private static final int THREAD_COUNT = 4;
     @SuppressWarnings("unused")
    private CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
     @SuppressWarnings("unused")
    private int completeLength = 0;
     @SuppressWarnings("unused")
    private long fileLength;
     public String url;
     public String name;
     public static String SaveFile;
    int flag=0;
    
    private Context mContext = this;
    public void onCreate() {
        super.onCreate();
        notificationMrg = (NotificationManager) this.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
    }   
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);   
        url = intent.getStringExtra("url");
        name = intent.getStringExtra("name");
        SaveFile=Environment.getExternalStorageDirectory()+"/DownFile/"+".apk";
//      注:此处url必须声明为final常量，否则是不会被子线程中读取到的
        
        Runnable start = new Runnable() {
          public void run() {             
              loadFile(url, name, ++flag);
            }
       };
        new Thread(start ){     
        }.start();      
        return START_REDELIVER_INTENT;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
//    状态栏视图更新
    private Notification displayNotificationMessage(Notification notification,int count,int flag,String url, String name) 
    {   
        RemoteViews contentView1 =notification.contentView; 
        Log.i("TAG","updata   flag=="+flag);
        Log.i("TAG","updata   count=="+count);
        contentView1.setTextViewText(ResourceUtil.getId(mContext, "n_title"), name);
        contentView1.setTextViewText(ResourceUtil.getId(mContext, "n_text"), "当前进度："+count+"% ");
        contentView1.setProgressBar(ResourceUtil.getId(mContext, "n_progress"), 100, count, false);
        notification.contentView = contentView1;
//      提交一个通知在状态栏中显示。如果拥有相同标签和相同id的通知已经被提交而且没有被移除，该方法会用更新的信息来替换之前的通知。  
        notificationMrg.notify(flag, notification);
        return notification;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("deprecation")
    public void loadFile(String url, String name, int flag)
    {
        Log.v("T-MAC", name);
//      Intent notificationIntent = new Intent(getApplicationContext(), this.getClass());
//      notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
////        addflag设置跳转类型
//      PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,notificationIntent, 0);
        // 创建Notifcation对象，设置图标，提示文字
        long number = 100;
        Notification notification = new Notification(ResourceUtil.getDrawableId(mContext, "noavatar_big"),"DnwoLoadManager",number);// 设定Notification出现时的声音，一般不建议自定义 System.currentTimeMillis()
        notification.flags |= Notification.FLAG_ONGOING_EVENT;//出现在 “正在运行的”栏目下面 
        RemoteViews contentView1 = new RemoteViews(getPackageName(),ResourceUtil.getLayoutId(mContext, "notification_version"));                
        contentView1.setTextViewText(ResourceUtil.getId(mContext, "n_title"),"准备下载");
        contentView1.setTextViewText(ResourceUtil.getId(mContext, "n_text"), "当前进度："+0+"% ");
        contentView1.setProgressBar(ResourceUtil.getId(mContext, "n_progress"), 100, 0, false);
        notification.contentView = contentView1;
//      notification.contentIntent = contentIntent;
        double m= 0.0;
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        HttpResponse response;
        try {
            response = client.execute(get);
            HttpEntity entity = response.getEntity();
            double length = entity.getContentLength();
            InputStream is = entity.getContent();
//          使用InputStream对文件进行读取，就是字节流的输入
            FileOutputStream fileOutputStream = null;
            
            File file = null;
            if (is != null){        
                file = new File(Environment.getExternalStorageDirectory(), name + ".apk");
                fileOutputStream = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int ch = -1;
                float count = 0;
//              ch中存放从buf字节数组中读取到的字节个数
                while ((ch = is.read(buf)) != -1) {
                    fileOutputStream.write(buf, 0, ch);
                    count += ch;
//                  从字节数组读取数据read(buf)后，返回，读取的个数，count中保存，已下载的数据字节数
                    double temp=count/length;
                    if(temp>=m){
                    Log.i("TAG", "读取字节循环中的count"+temp);
                    m+=0.1;
                    int load=(int) ( count*100/length);
                    sendMsg(1,load,url, name, notification,flag,null);}
//                  函数调用handler发送信息
                }
            }
            
//          文件输出流为空，则表示下载完成，安装apk文件
            Uri Url=Uri.fromFile(file);
            Log.i("TAG", "下载完成，传递文件位置Url  "+Url);       
            sendMsg(2,0,url, name, notification,0,Url);
            fileOutputStream.flush();
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } catch (Exception e) {
            sendMsg(-1,0,url, name, notification,0,null);
        }
    }
    
    private void sendMsg(int what,int c,String url, String name, Notification notification,int flag,Uri uri) {
        Message msg = new Message();
        msg.what= what;//用来识别发送消息的类型
        msg.arg1=0;
        holder=new Holder();
        holder.count=c;
        holder.url=url;
        holder.flag=flag;
        holder.notify=notification;
        holder.Uri=uri;
        holder.name = name;
        msg.obj=holder;//消息传递的自定义对象信息
        handler.sendMessage(msg);
    }
    // 定义一个Handler，用于处理下载线程与主线程间通讯
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressWarnings("static-access")
        @Override
        public void handleMessage(Message msg) {
            final Holder data=(Holder) msg.obj;
            if (!Thread.currentThread().isInterrupted()) {
//              判断下载线程是否中断
                switch (msg.what) {
                case 1:
                    Log.i("TAG", "handlemessage中的 case 1: data.count     "+data.count);
                    Log.i("TAG", "handlemessage中的 case 1: flag          "+data.flag);
                    if(data.count>=99){
                        notificationMrg.cancel(data.flag);  
                        break;
                    }
                    Notification notification;
                    if(notificationCache.containsKey(data.url)){    
                        //每次更新时，先以key，扫描hashmap，存在则读取出来。
                        notification = notificationCache.get(data.url);             
                        notification=displayNotificationMessage(notification,data.count ,data.flag,data.url, data.name);
                        notificationCache.put(data.url, notification);}
                    else  {
//                      //第一次更新时，传入notification对象每次，将notification对象存入hashmap中
                    notification=data.notify;
                    notification=displayNotificationMessage(notification,data.count ,data.flag,data.url, data.name);
                    notificationCache.put(data.url, notification);
                    }
//                  }                   
                    break;
                case 2:             
                    Toast it2 = null;
                    Log.v("akname", data.name);
                    it2.makeText(getApplication(), data.name + "下载成功", 1).show();
                    Log.i("TAG", "case 2中Uri ：  "+data.Uri);
                    openfile(data.Uri);
                    stopSelf();
                    break;
                case -1:
                    String error = msg.getData().getString("error");
                    Toast.makeText(getApplication(), error, Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            super.handleMessage(msg);
        }
    };
    public void openfile(Uri url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(url,"application/vnd.android.package-archive");
        startActivity(intent);
    }
    
    public class Holder{
        Notification notify;
        String url;
        String name;
        int count;
        int flag;
        Uri Uri;
    }
}

