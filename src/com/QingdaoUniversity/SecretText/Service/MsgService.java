/* ���غʹ�����ʷ����   
 * ��������ݿ⣺������
 * ֪ͨ������
 * ʱ�䣺2016��4��5��13:57:10
 * ���ܣ����ض��ţ����ܣ���⣬֪ͨ��ʾ
 * ��ϸ���ܣ�����ע��
 */

package com.QingdaoUniversity.SecretText.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;

import com.QingdaoUniversity.SecretText.DataBase.DatabaseHelper;
import com.QingdaoUniversity.SecretText.Functions.CttsList;
import com.QingdaoUniversity.SecretText.Functions.LoginNotification;
import com.example.uuu.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;
import android.annotation.SuppressLint;

public class MsgService extends Service{
	private boolean D=true;
	private ReceiveMessage mReceiver;
	@Override
	 public IBinder onBind(Intent intent) {
		    // TODO Auto-generated method stub
		    return null;
		  }
	 @Override
	  public void onCreate() {
	    if(D){
	      Log.i("msgservice"," onCreate()");
	    }
	    //ע�ᶯ̬�㲥
	    mReceiver=new ReceiveMessage();
	    IntentFilter filter=new IntentFilter();
	    filter.setPriority(Integer.MAX_VALUE);
	    filter.addAction("android.provider.Telephony.SMS_RECEIVED");
	    registerReceiver(mReceiver, filter);
	    super.onCreate();
	  }
	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
	    if(D){
	      Log.i("msgservice","onStartCommand");
	    }
	    return super.onStartCommand(intent, flags, startId);
	  }
	  @Override
	  public void onDestroy() {
	    if(D){
	      Log.i("msgservice","onDestroy() ");
	    }
	    //ȡ���㲥������
	    unregisterReceiver(mReceiver);
	    mReceiver=null;
	    super.onDestroy();
	  }
	  
	  @SuppressLint("SimpleDateFormat")
	class ReceiveMessage extends BroadcastReceiver {
		 private String address;
		 private String fullMessage;
		 private DatabaseHelper dbHelper;
		 SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm"); 
		 Date curDate;
		 String str; 
	  // �����յ���Ϣʱ������
	 @SuppressLint("NewApi")
	@Override
	  public void onReceive(Context context, Intent intent) {				
		  Bundle bundle = intent.getExtras();
				 // �ж��Ƿ�������
				 if (bundle != null) {
					 // ͨ��pdus���Ի�ý��յ������ж�����Ϣ
					 Object[] pdus = (Object[]) bundle.get("pdus");
					// ������Ϣ����array���������յ��Ķ��󳤶�������array�Ĵ�С
					 SmsMessage[] messages = new SmsMessage[pdus.length];
					 for (int i = 0; i < pdus.length; i++) {
						     messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
						    }
						    address = messages[0].getOriginatingAddress();//��ȡ���ͷ�����
						    if(address.startsWith("+86"))
						    {
						    	address = address.substring(3);
						    }
						    StringBuilder build = new StringBuilder();
						    StringBuilder build1 = new StringBuilder();
						    String temp = "";
						    fullMessage = "";
						    curDate = new Date(System.currentTimeMillis());//��ȡ��ǰʱ�� 
						    str = formatter.format(curDate);
						    //�����Ͷ��ŵ�����ǰ���ַ�����ϣ���ŵ��������ݵ�ǰ����λ���
						    for (SmsMessage message : messages) {
						    	build.append(message.getMessageBody());//��ȡ��������
						    }
					 
							build1.append(build.substring(2, 4));//�յ����ź��ȡ���ŵ�2,3λ���±��0��ʼ������Ϊԭ����0,1λ�����ǹ�ϣ֮��ı�־λ
						    for(int i = 0; i < build1.length(); i++)
						    {
						    	char x = build1.charAt(i);//��2,3λ���� ֮���ϣ �� �յ����ŵ�0,1λ�Ƚ�
						    	x -= 1;
						    	build1.setCharAt(i,x);
						    }
						    String str_build1 = build1.toString().hashCode()+"";
						    String str_build1_2 = str_build1.substring(0, 2);
						    temp += build.toString();
						    fullMessage += temp.substring(2);//��ȡ���ű�Ǻ���Ĳ��� ������fullmessage���涼�Ǽ��ܺ�Ķ�������û�й�ϣ���λ
				        	  if(temp.startsWith(str_build1_2))//�����ض���Ƕ��ţ����أ����Ҵ������ݿ�
					        	  	{
					        		  abortBroadcast();	   // ȡ���㲥�����д��뽫����ϵͳ���ܶ��ų����ղ�����Ϣ��
					        		  Bitmap btm = BitmapFactory.decodeResource(getResources(),
					                          R.drawable.mouse);
					                  Notification.Builder mBuilder = new Notification.Builder(
					                          MsgService.this).setSmallIcon(R.drawable.mouse)
					                          .setContentTitle("1 new message")
					                          .setDefaults( Notification.DEFAULT_SOUND)
					                          .setContentText("ע�������仯����ע��������");
					                  mBuilder.setTicker("������ʾ");//��һ����ʾ��Ϣ��ʱ����ʾ��֪ͨ����
					                  mBuilder.setNumber(12);
					                  mBuilder.setLargeIcon(btm);
					                  mBuilder.setAutoCancel(true);//�Լ�ά��֪ͨ����ʧ			                  
					                  //����һ��Intent Ĭ��ʹ�������
					                  Intent resultIntent;
					                 
					                  boolean isChecked;
					              	  SharedPreferences sp = getSharedPreferences("SettingsConfig", MODE_PRIVATE);
					              	  isChecked = sp.getBoolean("isChecked2", true);
					                  
					                  if( isChecked ){
					                	  //���ѡ����ֱ�ӽ���������
					                	  resultIntent = new Intent(MsgService.this,
					                			  CttsList.class);
					                  }
					                  else{
					                	  resultIntent = new Intent(MsgService.this,
					                			  LoginNotification.class);
					                  }
					                  
					                  //��װһ��Intent
					                  PendingIntent resultPendingIntent = PendingIntent.getActivity(
					                          MsgService.this, 0, resultIntent,
					                          PendingIntent.FLAG_UPDATE_CURRENT);
					                  // ����֪ͨ�������ͼ
					                  mBuilder.setContentIntent(resultPendingIntent);
					                  //��ȡ֪ͨ����������
					                  NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					                  Notification notification = mBuilder.build();
					                  notification.flags = Notification.FLAG_AUTO_CANCEL;
					                  mNotificationManager.notify(1, notification);
					        		  
					        		  //AsyncTask���޸����ݿ�����
					        		  new AsyncTask<Void,Void,Void>(){

					        			  @Override
					        			  	protected Void doInBackground(Void... params) {
					        	 		  //��İ���˶��Ŵ洢�����ݿ����
					        				  try{
					        					  boolean i = true;
					        					  Context context = getApplicationContext();
					        					  dbHelper = new DatabaseHelper(context,"SMSsecret.db",null,1);
					        					  SQLiteDatabase db = dbHelper.getWritableDatabase();
					        					  ContentValues values = new ContentValues();
					        					  values.put("phonenumber",address);
					        					  values.put("time", str);
					        					  values.put("owner",address);
					        					  values.put("readflag","0");
					        					  values.put("type", 0);
					        					  values.put("content",fullMessage);//ֱ�Ӱ��������
					        					  Cursor cursor = db.query("CONTACTS",null,null, null, null, null, null);
					        					  if(cursor.moveToFirst()){
					        						  do{
					        							  String num = cursor.getString(cursor.getColumnIndex("phonenumber"));
					        							  if(num.equals(address)){//�����ϵ�����м�¼
					        								 String name_t = cursor.getString(cursor.getColumnIndex("name"));
					        						//**	 String ime_t = cursor.getString(cursor.getColumnIndex("IME"));//ime_t Ϊ����ϵ�˱��л�ȡ��ĳ��ϵ�˵�IME��
					        						//**	 String fl = cursor.getString(cursor.getColumnIndex("flag"));
					        								 values.put("owner", name_t);
					        						//**	 if(fl.equals(0))//�����ϵ����ļ�¼û�г�ʼ��¼
					        						//**	 db.execSQL("update CONTACTS set IME = ? where phonenumber = ?",new String[]{deviceID,address}); // �ѵ�һ�����ŵ�IME����CONTACTS����
					        								 db.execSQL("update CONTACTS set flag = flag +1 where phonenumber = ?",new String[]{address});
					        								 db.execSQL("update CONTACTS set readflag = ? where phonenumber = ?",new String[]{"0",address});
					        								 i = false;
					        							  }
					        							 
					        						  }while(cursor.moveToNext());
					        						  if(i){//�����ϵ����û�иü�¼
					        							  ContentValues v = new ContentValues();
					        							  	v.put("name", address);
					        								v.put("phonenumber", address);
					        								v.put("flag", 0);
					        								v.put("readflag","1");//�Ķ���ǣ� 0 - ����ϵ����δ�����ţ�1 - ����ϵ��û��δ�����š�Ĭ��Ϊ1
					        					//**		v.put("IME",deviceID); //�����Ż�ȡ��IME���
					        								db.insert("CONTACTS", null, v);
					        							//db.execSQL("insert into CONTACTS(name,flag,readflag,phonenumber)values(?,0,?,?)",new String[]{address,"0",address});
					        							  db.execSQL("update CONTACTS set flag = flag +1 where phonenumber = ?",new String[]{address});
					        							  db.execSQL("update CONTACTS set readflag = ? where phonenumber = ?",new String[]{"0",address});
					        						  }
					        						  cursor.close();
					        					  }
					        					  db.insert("TEXT", null, values);
					        					  db.close();    
					        				  }catch(Exception e){
					        					  System.out.println(e.getMessage());
					        				  }
					        				  	return null;
					        			  }
					        		      @Override
					                      protected void onPostExecute(Void result) {
					                        // TODO Auto-generated method stub
					                        super.onPostExecute(result);
					                      }
					        		  		}.execute();
					        	  		}
					          }
					        	  
					    	  }
					   }
			 }