package com.example.a522internal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private Spinner language;
    private Button btnChange;
    private EditText mLoginEdTxt;
    private EditText mPassEdTxt;
    private CheckBox checkBox;
    private SharedPreferences mySharedPref;
    private Button Ok;
    private Button btnCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoginEdTxt = findViewById(R.id.login);
        mPassEdTxt = findViewById(R.id.pass);
        checkBox = findViewById(R.id.chkbox);
        Ok = findViewById(R.id.ok);
        btnCheck = findViewById(R.id.check_btn);

        mySharedPref = getSharedPreferences("checkBox", MODE_PRIVATE);
        boolean chkBox = mySharedPref.getBoolean("checkBox", true);
        if (chkBox = true) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        initView();
    }

    public void initView() {
        btnChange = findViewById(R.id.btn_change);
        language = findViewById(R.id.spinner);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String spinner = language.getSelectedItem().toString();

                if ("Русский".equalsIgnoreCase(spinner) | "Russian".equalsIgnoreCase(spinner)) {
                    Locale locale = new Locale("ru");
                    Configuration config = new Configuration();
                    config.setLocale(locale);
                    getResources().updateConfiguration(config,
                            getBaseContext().getResources().getDisplayMetrics());
                    recreate();
                } else if ("Английский".equalsIgnoreCase(spinner) |
                        "English".equalsIgnoreCase(spinner)) {
                    Locale locale = new Locale("en");
                    Configuration config = new Configuration();
                    config.setLocale(locale);
                    getResources().updateConfiguration(config,
                            getBaseContext().getResources().getDisplayMetrics());
                    recreate();
                }
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                } else {

                }
            }
        });

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    String myLogin = "Den41Q";
                    String myPass = "qwerty";
                    String data = loadText();

                    if (data.equalsIgnoreCase(myLogin + "\n" + myPass + "\n")) {
                        Toast.makeText(MainActivity.this, "Логин и пароль верные",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Неверный логин или пароль!",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    read();
                }
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    String data = mLoginEdTxt.getText().toString() + "\n"
                            + mPassEdTxt.getText().toString();
                    saveText(data);
                } else {
                    write();
                }
                SharedPreferences.Editor myEditor = mySharedPref.edit();
                myEditor.putBoolean("checkBox", checkBox.isChecked());
                myEditor.apply();
            }
        });
    }

    private void write() {
        String login = mLoginEdTxt.getText().toString();
        String pass = mPassEdTxt.getText().toString();

        if (login.equalsIgnoreCase("") |
                pass.equalsIgnoreCase("")) {
            Toast.makeText(MainActivity.this, "Заполните все поля!",
                    Toast.LENGTH_SHORT).show();
        }

        try {
            FileOutputStream fos = openFileOutput("Data", Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(outputStreamWriter);

            bw.write(login);
            bw.write(pass);
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read() {
        try {
            String myLogin = "Den41Q";
            String myPass = "qwertY";

            FileInputStream fileInputStream = openFileInput("Data");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();
            while (line != null) {
                Log.d("Tag", line);
                line = reader.readLine();
            }


            if (line.equalsIgnoreCase(myLogin + "\n" + myPass + "\n")) {
                Toast.makeText(MainActivity.this, "Логин и пароль верные",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, line,
                        Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveText(String data) {

        try (Writer writer = new FileWriter(getDataFile())) {
            if (loadText() != null) {
                writer.append(data);
                writer.flush();
            } else {
                writer.write(data);
            }
        } catch (IOException e) {
            Toast.makeText(this, "Не возможно сохранить файл!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private String loadText() {
        File dataFile = getDataFile();
        if (!dataFile.canRead()) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        try (Scanner scanner = new Scanner(new FileReader(dataFile))) {
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine());
                stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    private File getDataFolder() {
        return getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
    }

    private File getDataFile() {
        return new File(getDataFolder(), "data.txt");
    }
}
