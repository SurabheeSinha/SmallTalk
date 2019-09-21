package com.surabheesinha.smalltalk;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech myTTS;
    private SpeechRecognizer mySpeechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeSpeechRecognizer();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                Log.d("Recording", "REcording Started 1");
                mySpeechRecognizer.startListening(intent);
                Log.d("Recording", "REcording Started2");


            }


        });

        initializeTextToSpeech();
        Log.d("Recording", "REcording Started3");

        Log.d("Recording", "REcording Started5");

    }

    private void initializeSpeechRecognizer() {
        Log.d("Recording", "REcording initializeSpeechRecognizer ");
        if(SpeechRecognizer.isRecognitionAvailable(this)){
            mySpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            mySpeechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle bundle) {
                    List<String> results = bundle.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION
                    );
                    processResult(results.get(0));
                    Log.d("Recording", "REcording results ");

                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    private void processResult(String command) {
        command = command.toLowerCase();
        //what is your name?
        //what is teh time ??'
        //open a browser
        if(command.indexOf("what") != -1) {
            if (command.indexOf(" your name") != -1) {
                speak(("My name is Micheal Scott"));
                Log.d("Recording", "REcording processresults 1 ");
            }
            if (command.indexOf("time") != -1) {
                Date now = new Date();
                String time = DateUtils.formatDateTime(this, now.getTime(), DateUtils.FORMAT_SHOW_TIME);
                speak("The time now is" + time);
                Log.d("Recording", "REcording processresults 2 ");

            }
        }
        else if (command.indexOf("open") != -1){
            if (command.indexOf("browser")!= -1){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://googl.com"));
                startActivity(intent);
                Log.d("Recording", "REcording processresults 3 ");
            }
        }
    }


    private void initializeTextToSpeech() {
        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(MainActivity.this,"There is no Text to speech on your device", Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Log.d("Recording", "REcording Started4");
                    myTTS.setLanguage(Locale.US);
                    speak("HELLO Baby");

                }
            }
        });
    }

    private void speak(String message){
        if(Build.VERSION.SDK_INT>=21){
            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH,null, null);
        }
        else{
            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH,null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        myTTS.shutdown();
    }
}
