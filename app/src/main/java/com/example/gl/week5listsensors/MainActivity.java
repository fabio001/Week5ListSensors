package com.example.gl.week5listsensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private final int MULTIPLE_CHOICE_COUNT =5;

    ArrayList<String> array;
    private  ListView list;
    private ArrayAdapter<String> adapter;

    private HashMap<String, String> dict;

    private ArrayList<String> definitions;

    private String currentWord ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dict = new HashMap<>();

        readAll();


        //adapter= new ArrayAdapter<>(this, R.layout.list_layout, R.id.content,array);
        //set adapter to listview
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(this);

        generateRandom();

        //SensorClass ss = new SensorClass();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
        //Toast.makeText(this, list.getItemAtPosition(index).toString(),Toast.LENGTH_SHORT).show();
        //array.remove(index);
        //adapter.notifyDataSetChanged();

        if(dict.get(currentWord).equals(list.getItemAtPosition(index).toString())){
            Toast.makeText(this, "You got it!!",Toast.LENGTH_SHORT).show();
            generateRandom();
        }else{
            Toast.makeText(this, "Study more!!",Toast.LENGTH_SHORT).show();
        }


    }

    private void readAll(){
        Scanner scanner = new Scanner(getResources().openRawResource(R.raw.mydict));
        while(scanner.hasNext()){
            String line = scanner.nextLine();
            String[] words = line.split("\t");
            dict.put(words[0],words[1]);
        }
        scanner.close();

        definitions = new ArrayList<>();
        array = new ArrayList<>(dict.keySet());

    }
    private void generateRandom(){
        //shuffle array pick one
        Collections.shuffle(array);
        String word = array.get(0);


        //ask question
        TextView the_word = (TextView) findViewById(R.id.the_word);
        the_word.setText(word);
        currentWord = word;

        definitions.clear();
        for(int i=0; i< MULTIPLE_CHOICE_COUNT; i++){
            definitions.add(dict.get(array.get(i)));
        }
        Collections.shuffle(definitions);
        adapter = new ArrayAdapter<String>(
                this,
                R.layout.list_layout,
                R.id.content,
                definitions
        );

        list.setAdapter(adapter);


    }

    private class SensorClass implements SensorEventListener{

        private SensorManager sm;
        private Sensor acc;

        public SensorClass(){
            sm = (SensorManager)getSystemService(SENSOR_SERVICE);
            acc = (Sensor) sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sm.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            String res = event.values[0] + ", " + event.values[1] + ", " + event.values[2];
            TextView the_word = (TextView) findViewById(R.id.the_word);
            the_word.setText(res);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
}
