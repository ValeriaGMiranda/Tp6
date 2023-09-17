package com.valesoft.tp6;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.util.Log;

public class Servicio extends Service {

    private Thread hilo;
    private boolean bandera = true;

    public Servicio() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        acceder();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        bandera = false;
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void acceder(){
        Uri sms = Telephony.Sms.Inbox.CONTENT_URI;
        ContentResolver cr = this.getContentResolver();

        hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (bandera){

                        Cursor cursor = cr.query(sms ,null,null,null,Telephony.Sms.Inbox.DEFAULT_SORT_ORDER);
                        String address = null;
                        String body = null;
                        StringBuilder resultado = new StringBuilder();

                        if(cursor.getCount()>0){
                            int contador = 1;

                            while(cursor.moveToNext()  && contador<=5){
                                int colA= cursor.getColumnIndex(Telephony.Sms.ADDRESS);
                                int colB = cursor.getColumnIndex(Telephony.Sms.BODY);
                                address = cursor.getString(colA);
                                body = cursor.getString(colB);
                                resultado.append("NÚMERO: " + address + " BODY:" + body + "\n");
                                contador++;
                            }
                            Log.d("salida",resultado.toString());
                        }

                        Thread.sleep(9000);
                    }

                }catch (InterruptedException ex){
                    Log.d("salida","Hilo interrumpido");
                }
            }
        });

        hilo.start();
    }
}