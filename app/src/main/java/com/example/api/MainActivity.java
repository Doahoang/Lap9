package com.example.api;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CONTACT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        REQUEST_CONTACT
                );
            } else {
                readContacts();
            }
        } else {
            readContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CONTACT) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.d("CONTACT", "Đã cấp quyền");
                readContacts();

            } else {
                Log.d("CONTACT", "Người dùng từ chối quyền đọc danh bạ");
            }
        }
    }



    private void readContacts() {

        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                        )
                );

                String phone = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                        )
                );

                Log.d("CONTACT", "Tên: " + name + " | SĐT: " + phone);
            }
            cursor.close();
        }
    }
}
