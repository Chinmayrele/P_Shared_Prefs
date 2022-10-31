package com.codingproject.sharedprefs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String MESSAGE_ID = "messages";
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editTextTextPersonName);
        TextView textView = findViewById(R.id.textView001);
        Button btn = findViewById(R.id.button001);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enterText = editText.getText().toString().trim();

                SharedPreferences sharedPreferences = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("message", enterText);
                editor.apply();  // SAVING TO THE DISK!!!
            }
        });

        //GET DATA BACK FROM SP
        SharedPreferences getPreference = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
        String value = getPreference.getString("message", "Nothing yet!");

        textView.setText(value);

    }
    //IMPORTANT
    // TO SAVE HASH-MAP IN SHARED PREFERENCE

    private void hashMapSharedPrefs() {
        //create test hashmap
        HashMap<String, String> testHashMap = new HashMap<String, String>();
        testHashMap.put("key1", "value1");
        testHashMap.put("key2", "value2");

        //convert to string using gson
        // ADD GSON DEPENDENCY IN GRADLE FIRST
        Gson gson = new Gson();
        String hashMapString = gson.toJson(testHashMap);

        //save in shared prefs
        SharedPreferences prefs = getSharedPreferences("test", MODE_PRIVATE);
        prefs.edit().putString("hashString", hashMapString).apply();

        //get from shared prefs
        String storedHashMapString = prefs.getString("hashString", "oopsDintWork");
        java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();
        HashMap<String, String> testHashMap2 = gson.fromJson(storedHashMapString, type);

        //use values
        String toastString = testHashMap2.get("key1") + " | " + testHashMap2.get("key2");
        Toast.makeText(this, toastString, Toast.LENGTH_LONG).show();
    }


    // ANOTHER METHOD TO STORE HASH-MAP IN SHARED-PREFS WITHOUT USING DEPENDENCIES
    private void saveMap(Map<String,Boolean> inputMap) {
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        if (pSharedPref != null){
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            pSharedPref.edit()
                    .remove("My_map")
                    .putString("My_map", jsonString)
                    .apply();
        }
    }

    private Map<String,Boolean> loadMap() {
        Map<String,Boolean> outputMap = new HashMap<>();
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        try {
            if (pSharedPref != null) {
                String jsonString = pSharedPref.getString("My_map", (new JSONObject()).toString());
                if (jsonString != null) {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    Iterator<String> keysItr = jsonObject.keys();
                    while (keysItr.hasNext()) {
                        String key = keysItr.next();
                        Boolean value = jsonObject.getBoolean(key);
                        outputMap.put(key, value);
                    }
                }
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        return outputMap;
    }
}