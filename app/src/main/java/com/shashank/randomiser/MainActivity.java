package com.shashank.randomiser;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public EditText inputString;
    public Button randomise;
    public Button addItem;
    public TextView resultText;
    public Button deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
            SQLiteDatabase database = this.openOrCreateDatabase("storedData", MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS tableOne (inputValues VARCHAR)");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        inputString = (EditText) findViewById(R.id.inputString);
        randomise = (Button) findViewById(R.id.randomise);
        resultText = (TextView) findViewById(R.id.result);
        addItem = (Button) findViewById(R.id.addItem);
        deleteBtn = (Button) findViewById(R.id.reset);
        //ADD ITEMS TO DB
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tempData = inputString.getText().toString();

                try{
                    SQLiteDatabase database = MainActivity.this.openOrCreateDatabase("storedData", MODE_PRIVATE, null);
                    database.execSQL("CREATE TABLE IF NOT EXISTS tableOne (inputValues VARCHAR)");
                    database.execSQL("INSERT INTO tableOne (inputValues) VALUES ('" +tempData+ "')");
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                inputString.setText(null);


            }
        });

        //GET ITEMS FROM DB AND RANDOMISE
        randomise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultText = findViewById(R.id.result);
                ArrayList<String> itemList = new ArrayList<String>();
                try{
                    SQLiteDatabase database = MainActivity.this.openOrCreateDatabase("storedData", MODE_PRIVATE, null);
                    Cursor c = database.rawQuery("SELECT * FROM tableOne", null);
                    int index = c.getColumnIndex("inputValues");
                    c.moveToFirst();
                    while(c!=null){
                        itemList.add(c.getString(index));
                        c.moveToNext();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                Random random = new Random();
                if(itemList.size()!=0) {
                    Log.i("itemlist size", Integer.toString(itemList.size()));
                    int tempRandomNumber = random.nextInt(itemList.size());
                    resultText.setText(itemList.get(tempRandomNumber));
                }
                else
                    Toast.makeText(MainActivity.this, "empty database!", Toast.LENGTH_SHORT).show();
            }
        });

        //DELETE TABLE
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    SQLiteDatabase database = MainActivity.this.openOrCreateDatabase("storedData", MODE_PRIVATE, null);
                    database.execSQL("DROP TABLE IF EXISTS tableOne");
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this, "Deleted all items", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
