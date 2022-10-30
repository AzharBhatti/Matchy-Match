package com.matchymatchproject.mirassociationdanny.matchymatch;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.matchymatchproject.mirassociationdanny.matchymatch.Utils.AlarmSoundService;
import com.matchymatchproject.mirassociationdanny.matchymatch.Utils.Constants;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    MediaPlayer monkeySentence, snakeSentence, elephantSentence, sheepSentence, duckSentence, zebraSentence, fishSentence, giraffeSentence, lionSentence, frogSentence;
    int question = 0;

    // info popup
    TextView infoTV;
    ImageView circle_info_cancelbtn;

    //animalsSounds
    TextToSpeech tts;

    // passward popup
    TextView tvQUESTION;
    ImageView circle_passward_cancelbtn;
    EditText popupNameEDT;
    Button continuebtn;
    static boolean stop = false;

    // parent zone popup
    Button create_your_puzzles_btn, information_btn;
    ImageView circle_parent_cancelbtn;
    public static boolean isPlaying;

    ImageView snackbtn, monkeybtn, elephantbtn, sheepbtn, duckbtn, zebrabtn, fishbtn, giraffebtn, lionbtn, frogbtn, parrotbtn;
    ImageView parentzontbtn, backgroundChanger, musicMute;
    MediaPlayer catSoundMediaPlayer;
    MediaPlayer snakemediaPlayer, monkeymediaPlayer, elephantmediaPlayer, sheepmediaPlayer, duckmediaPlayer, zebramediaPlayer, fishmediaPlayer, giraffemediaPlayer, lionmediaPlayer, frogmediaPlayer;
    Dialog passwardDialog;
    Dialog parentDialog;
    Dialog infoDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_main);


        SharedPreferences myquestion = this.getSharedPreferences("MyAwesomequestion", Context.MODE_PRIVATE);
        question = myquestion.getInt("question", 0);

        passwardDialog = new Dialog(this);
        parentDialog = new Dialog(this);
        infoDialog = new Dialog(this);

        musicMute = findViewById(R.id.musicMute);
        isPlaying = true;
        SharedPreferences myMusic = this.getSharedPreferences("MyMusic", Context.MODE_PRIVATE);
        isPlaying = myMusic.getBoolean("keyMusic", Boolean.parseBoolean(null));

        tts = new TextToSpeech(getApplicationContext(), status -> {
            if(status!=TextToSpeech.ERROR){

                tts.setLanguage(Constants.speechLocale);
                tts.setPitch((float) 1.1);
                tts.setSpeechRate((float) 0.6);
            }
        });

        if (isPlaying) {

            startService(new Intent(MainActivity.this, AlarmSoundService.class));
            musicMute.setImageResource(R.mipmap.onmusic);


        }
        if (!isPlaying) {

            //Stop the Media Player Service to stop sound
            stopService(new Intent(MainActivity.this, AlarmSoundService.class));
            musicMute.setImageResource(R.mipmap.musicmute);
        }


        musicMute.setOnClickListener(v -> {

            if (isPlaying) {

                isPlaying = false;
                SharedPreferences myMusic1 = getSharedPreferences("MyMusic", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = myMusic1.edit();
                editor.putBoolean("keyMusic", isPlaying);
                editor.apply();

                Toast.makeText(MainActivity.this, "Off", Toast.LENGTH_LONG).show();

            } else if (!isPlaying) {

                isPlaying = true;
                SharedPreferences myMusic1 = getSharedPreferences("MyMusic", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = myMusic1.edit();
                editor.putBoolean("keyMusic", isPlaying);
                editor.apply();

                Toast.makeText(MainActivity.this, "On", Toast.LENGTH_LONG).show();

            }

            // Restart the Activity
            /*Intent intent = getIntent();
            finish();
            startActivity(intent);*/

            startActivity(getIntent());
            finish();
            overridePendingTransition(0, 0);
        });

        duckmediaPlayer = MediaPlayer.create(this, R.raw.duck);
        elephantmediaPlayer = MediaPlayer.create(this, R.raw.elephant);
        fishmediaPlayer = MediaPlayer.create(this, R.raw.fish);
        frogmediaPlayer = MediaPlayer.create(this, R.raw.frog);
        giraffemediaPlayer = MediaPlayer.create(this, R.raw.giraffe);
        monkeymediaPlayer = MediaPlayer.create(this, R.raw.monkey);
        sheepmediaPlayer = MediaPlayer.create(this, R.raw.sheep);
        snakemediaPlayer = MediaPlayer.create(this, R.raw.snake);
        lionmediaPlayer = MediaPlayer.create(this, R.raw.lion);
        zebramediaPlayer = MediaPlayer.create(this, R.raw.zebra);
        catSoundMediaPlayer = MediaPlayer.create(this, R.raw.bgmusic);
//        backgroundChanger = (ImageView) findViewById(R.id.backgroundChanger);

        monkeySentence = MediaPlayer.create(this, R.raw.monkeysentence);
        duckSentence = MediaPlayer.create(this, R.raw.ducksentence);
        elephantSentence = MediaPlayer.create(this, R.raw.elephantsentence);
        fishSentence = MediaPlayer.create(this, R.raw.fishsentence);
        frogSentence = MediaPlayer.create(this, R.raw.frogsentence);
        giraffeSentence = MediaPlayer.create(this, R.raw.giraffesentence);
        sheepSentence = MediaPlayer.create(this, R.raw.sheepsentence);
        snakeSentence = MediaPlayer.create(this, R.raw.snakesentence);
        zebraSentence = MediaPlayer.create(this, R.raw.zebrasentence);
        lionSentence = MediaPlayer.create(this, R.raw.lionsentence);


        snackbtn = findViewById(R.id.snackIMG);
        monkeybtn = findViewById(R.id.monkeyIMG);
        elephantbtn = findViewById(R.id.elephantIMG);
        sheepbtn = findViewById(R.id.sheepIMG);
        duckbtn = findViewById(R.id.duckIMG);
        zebrabtn = findViewById(R.id.zebraIMG);
        fishbtn = findViewById(R.id.fishIMG);
        giraffebtn = findViewById(R.id.giraffeIMG);
        lionbtn = findViewById(R.id.lionIMG);
        frogbtn = findViewById(R.id.frogIMG);
        parrotbtn = findViewById(R.id.parrotIMG);

        parentzontbtn = findViewById(R.id.parentzoneIMG);
        parentzontbtn.setOnClickListener(v -> {

            if (question == 10) {

                question = 0;
                SharedPreferences myquestion1 = getSharedPreferences("MyAwesomequestion", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = myquestion1.edit();
                editor.putInt("question", question);
                editor.commit();
            }
            question += 1;

            SharedPreferences myquestion1 = getSharedPreferences("MyAwesomequestion", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = myquestion1.edit();
            editor.putInt("question", question);
            editor.commit();

            passwardPopup();
        });


        monkeybtn.setOnClickListener(v -> {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//            monkeymediaPlayer.start();
            tts.speak("Monkey", TextToSpeech.QUEUE_FLUSH, null);


            final Handler monkeyNavigate = new Handler();
            final Handler monkeySS = new Handler();
            while (tts.isSpeaking())
            {}
            monkeyNavigate.postDelayed(() -> {
                Intent IM = new Intent(MainActivity.this, MainPuzzleActivity.class);
                ArrayList<Integer> puzzleAssets = new ArrayList<>();
                puzzleAssets.add(R.mipmap.cup);
                puzzleAssets.add(R.mipmap.frypan);
                puzzleAssets.add(R.mipmap.spoon);
                puzzleAssets.add(R.mipmap.pot);
                puzzleAssets.add(R.mipmap.glass);
                puzzleAssets.add(R.mipmap.fork);
                puzzleAssets.add(R.mipmap.plate);
                puzzleAssets.add(R.mipmap.bowl);
                puzzleAssets.add(R.mipmap.jug);
                puzzleAssets.add(R.mipmap.knife);

                ArrayList<String> puzzleSounds = new ArrayList<>();
                puzzleSounds.add("cup");
                puzzleSounds.add("fryingpan");
                puzzleSounds.add("spoon");
                puzzleSounds.add("pot");
                puzzleSounds.add("glass");
                puzzleSounds.add("fork");
                puzzleSounds.add("plate");
                puzzleSounds.add("bowl");
                puzzleSounds.add("jug");
                puzzleSounds.add("knife");

                IM.putStringArrayListExtra("puzzle_sounds", puzzleSounds);
                IM.putIntegerArrayListExtra("puzzle_assets", puzzleAssets);
                IM.putExtra("background", R.drawable.monkey_background);
                IM.putExtra("puzzle_name", "monkey");


                startActivity(IM);

            }, 0);


            monkeySS.postDelayed(() -> {

                if (!stop) {
                    monkeySentence.start();
//                   tts.speak("Monkey", TextToSpeech.QUEUE_FLUSH, null);
                }

            }, 2000);


        });

        snackbtn.setOnClickListener(v -> {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//            snakemediaPlayer.start();
            tts.speak("Snake", TextToSpeech.QUEUE_FLUSH, null);

            final Handler snackNavigate = new Handler();
            while (tts.isSpeaking())
            {}
            snackNavigate.postDelayed(() -> {

//                Intent IS = new Intent(MainActivity.this, SnackActivity.class);
                Intent IS = new Intent(MainActivity.this, MainPuzzleActivity.class);
                ArrayList<Integer> puzzleAssets = new ArrayList<>();
                puzzleAssets.add(R.mipmap.circle);
                puzzleAssets.add(R.mipmap.ellipse);
                puzzleAssets.add(R.mipmap.triangle);
                puzzleAssets.add(R.mipmap.hexagon);
                puzzleAssets.add(R.mipmap.rhombus);
                puzzleAssets.add(R.mipmap.star);
                puzzleAssets.add(R.mipmap.heart);
                puzzleAssets.add(R.mipmap.rectangle);
                puzzleAssets.add(R.mipmap.pentagon);
                puzzleAssets.add(R.mipmap.square);

                ArrayList<String> puzzleSounds = new ArrayList<>();
                puzzleSounds.add("circle");
                puzzleSounds.add("oval");
                puzzleSounds.add("triangle");
                puzzleSounds.add("hexagon");
                puzzleSounds.add("diamond");
                puzzleSounds.add("star");
                puzzleSounds.add("heart");
                puzzleSounds.add("rectangle");
                puzzleSounds.add("pentagon");
                puzzleSounds.add("sqaure");

                IS.putStringArrayListExtra("puzzle_sounds", puzzleSounds);
                IS.putIntegerArrayListExtra("puzzle_assets", puzzleAssets);
                IS.putExtra("background", R.drawable.snake_background);
                IS.putExtra("puzzle_name", "snake");
                startActivity(IS);
            }, 0);

            Handler snakeSS = new Handler();
            snakeSS.postDelayed(() -> {

                if (!stop) {
                    snakeSentence.start();
//                    tts.speak("Snake", TextToSpeech.QUEUE_FLUSH, null);
                }

            }, 2000);


        });

        elephantbtn.setOnClickListener(v -> {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//            elephantmediaPlayer.start();
            tts.speak("Elephant", TextToSpeech.QUEUE_FLUSH, null);
            //
            final Handler elephantNavigate = new Handler();
            while (tts.isSpeaking())
            {}
            elephantNavigate.postDelayed(() -> {
//                Intent IE = new Intent(MainActivity.this, ElephantActivity.class);
                Intent IE = new Intent(MainActivity.this, MainPuzzleActivity.class);
                ArrayList<Integer> puzzleAssets = new ArrayList<>();
                puzzleAssets.add(R.mipmap.numone);
                puzzleAssets.add(R.mipmap.numtwo);
                puzzleAssets.add(R.mipmap.numthree);
                puzzleAssets.add(R.mipmap.numfour);
                puzzleAssets.add(R.mipmap.numfive);
                puzzleAssets.add(R.mipmap.numsix);
                puzzleAssets.add(R.mipmap.numseven);
                puzzleAssets.add(R.mipmap.numeight);
                puzzleAssets.add(R.mipmap.numnine);
                puzzleAssets.add(R.mipmap.numten);

                ArrayList<String> puzzleSounds = new ArrayList<>();
                puzzleSounds.add("one");
                puzzleSounds.add("two");
                puzzleSounds.add("three");
                puzzleSounds.add("four");
                puzzleSounds.add("five");
                puzzleSounds.add("six");
                puzzleSounds.add("seven");
                puzzleSounds.add("eight");
                puzzleSounds.add("nine");
                puzzleSounds.add("ten");

                IE.putStringArrayListExtra("puzzle_sounds", puzzleSounds);
                IE.putIntegerArrayListExtra("puzzle_assets", puzzleAssets);
                IE.putExtra("background", R.drawable.elephant_background);
                IE.putExtra("puzzle_name", "elephant");

                startActivity(IE);

            }, 0);

            Handler elephantSS = new Handler();
            elephantSS.postDelayed(() -> {

                if (!stop) {
                    elephantSentence.start();
//                    tts.speak("Elephant", TextToSpeech.QUEUE_FLUSH, null);
                }

            }, 2000);


        });

        duckbtn.setOnClickListener(v -> {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//            duckmediaPlayer.start();
            tts.speak("Duck", TextToSpeech.QUEUE_FLUSH, null);

            final Handler duckNavigate = new Handler();
            while (tts.isSpeaking())
            {}
            duckNavigate.postDelayed(() -> {

//                Intent ID = new Intent(MainActivity.this, DuckActivity.class);
                Intent ID = new Intent(MainActivity.this, MainPuzzleActivity.class);
                ArrayList<Integer> puzzleAssets = new ArrayList<>();
                puzzleAssets.add(R.mipmap.black);
                puzzleAssets.add(R.mipmap.green);
                puzzleAssets.add(R.mipmap.orangecolor);
                puzzleAssets.add(R.mipmap.silver);
                puzzleAssets.add(R.mipmap.purple);
                puzzleAssets.add(R.mipmap.lightpurple);
                puzzleAssets.add(R.mipmap.brown);
                puzzleAssets.add(R.mipmap.yellow);
                puzzleAssets.add(R.mipmap.blue);
                puzzleAssets.add(R.mipmap.red);

                ArrayList<String> puzzleSounds = new ArrayList<>();
                puzzleSounds.add("black");
                puzzleSounds.add("green");
                puzzleSounds.add("orange");
                puzzleSounds.add("grey");
                puzzleSounds.add("purple");
                puzzleSounds.add("pink");
                puzzleSounds.add("brown");
                puzzleSounds.add("yellow");
                puzzleSounds.add("blue");
                puzzleSounds.add("red");

                ID.putStringArrayListExtra("puzzle_sounds", puzzleSounds);
                ID.putIntegerArrayListExtra("puzzle_assets", puzzleAssets);
                ID.putExtra("background", R.drawable.duck_background);
                ID.putExtra("puzzle_name", "duck");
                startActivity(ID);
            }, 0);

            Handler duckSS = new Handler();
            duckSS.postDelayed(() -> {


                if (!stop) {
                    duckSentence.start();
//                    tts.speak("Duck", TextToSpeech.QUEUE_FLUSH, null);
                }

            }, 2000);


        });

        sheepbtn.setOnClickListener(v -> {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//            sheepmediaPlayer.start();
            tts.speak("Sheep", TextToSpeech.QUEUE_FLUSH, null);

            final Handler sheepNavigate = new Handler();

            while (tts.isSpeaking())
            {}
            sheepNavigate.postDelayed(() -> {

//                Intent ISh = new Intent(MainActivity.this, SheepActivity.class);
                Intent ISh = new Intent(MainActivity.this, MainPuzzleActivity.class);
                ArrayList<Integer> puzzleAssets = new ArrayList<>();
                puzzleAssets.add(R.mipmap.car);
                puzzleAssets.add(R.mipmap.truck);
                puzzleAssets.add(R.mipmap.aeroplane);
                puzzleAssets.add(R.mipmap.boat);
                puzzleAssets.add(R.mipmap.motorcycle);
                puzzleAssets.add(R.mipmap.helicopter);
                puzzleAssets.add(R.mipmap.ship);
                puzzleAssets.add(R.mipmap.bus);
                puzzleAssets.add(R.mipmap.cycle);
                puzzleAssets.add(R.mipmap.train);

                ArrayList<String> puzzleSounds = new ArrayList<>();
                puzzleSounds.add("car");
                puzzleSounds.add("truck");
                puzzleSounds.add("aeroplane");
                puzzleSounds.add("boat");
                puzzleSounds.add("motorcycle");
                puzzleSounds.add("helicopter");
                puzzleSounds.add("ship");
                puzzleSounds.add("bus");
                puzzleSounds.add("cycle");
                puzzleSounds.add("train");

                ISh.putStringArrayListExtra("puzzle_sounds", puzzleSounds);
                ISh.putIntegerArrayListExtra("puzzle_assets", puzzleAssets);
                ISh.putExtra("background", R.drawable.sheep_background);
                ISh.putExtra("puzzle_name", "sheep");
                startActivity(ISh);
            }, 0);

            Handler sheepSS = new Handler();
            sheepSS.postDelayed(() -> {

                if (!stop) {
                    sheepSentence.start();
//                    tts.speak("Sheep", TextToSpeech.QUEUE_FLUSH, null);
                }

            }, 2000);


        });

        zebrabtn.setOnClickListener(v -> {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//            zebramediaPlayer.start();
            tts.speak("Zebra", TextToSpeech.QUEUE_FLUSH, null);

            final Handler zebraNavigate = new Handler();
            while (tts.isSpeaking())
            {}
            zebraNavigate.postDelayed(() -> {

//                Intent IZ = new Intent(MainActivity.this, ZebraActivity.class);
                Intent IZ = new Intent(MainActivity.this, MainPuzzleActivity.class);
                ArrayList<Integer> puzzleAssets = new ArrayList<>();
                puzzleAssets.add(R.mipmap.potato);
                puzzleAssets.add(R.mipmap.radishes);
                puzzleAssets.add(R.mipmap.broccoli);
                puzzleAssets.add(R.mipmap.corn);
                puzzleAssets.add(R.mipmap.beetroot);
                puzzleAssets.add(R.mipmap.peper);
                puzzleAssets.add(R.mipmap.peas);
                puzzleAssets.add(R.mipmap.carrot);
                puzzleAssets.add(R.mipmap.eggplant);
                puzzleAssets.add(R.mipmap.cabbage);

                ArrayList<String> puzzleSounds = new ArrayList<>();
                puzzleSounds.add("potato");
                puzzleSounds.add("radishes");
                puzzleSounds.add("broccoli");
                puzzleSounds.add("corn");
                puzzleSounds.add("onion");
                puzzleSounds.add("peper");
                puzzleSounds.add("peas");
                puzzleSounds.add("carrot");
                puzzleSounds.add("eggplant");
                puzzleSounds.add("cabbage");

                IZ.putStringArrayListExtra("puzzle_sounds", puzzleSounds);
                IZ.putIntegerArrayListExtra("puzzle_assets", puzzleAssets);
                IZ.putExtra("background", R.drawable.zebra_background);
                IZ.putExtra("puzzle_name", "zebra");
                startActivity(IZ);
            }, 0);

            Handler zebraSS = new Handler();
            zebraSS.postDelayed(() -> {

                if (!stop) {
                    zebraSentence.start();
//                    tts.speak("Zebra", TextToSpeech.QUEUE_FLUSH, null);
                }

            }, 2000);


        });

        fishbtn.setOnClickListener(v -> {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//            fishmediaPlayer.start();
            tts.speak("Fish", TextToSpeech.QUEUE_FLUSH, null);

            final Handler fishNavigate = new Handler();

            while (tts.isSpeaking())
            {}
            fishNavigate.postDelayed(() -> {

//                Intent IF = new Intent(MainActivity.this, FishActivity.class);
                Intent IF = new Intent(MainActivity.this, MainPuzzleActivity.class);
                ArrayList<Integer> puzzleAssets = new ArrayList<>();
                puzzleAssets.add(R.mipmap.tiger);
                puzzleAssets.add(R.mipmap.snack);
                puzzleAssets.add(R.mipmap.elephant);
                puzzleAssets.add(R.mipmap.lion);
                puzzleAssets.add(R.mipmap.bear);
                puzzleAssets.add(R.mipmap.crocodile);
                puzzleAssets.add(R.mipmap.kangaro);
                puzzleAssets.add(R.mipmap.panda);
                puzzleAssets.add(R.mipmap.giraffe);
                puzzleAssets.add(R.mipmap.fox);

                ArrayList<String> puzzleSounds = new ArrayList<>();
                puzzleSounds.add("tiger");
                puzzleSounds.add("snake");
                puzzleSounds.add("elephant");
                puzzleSounds.add("lion");
                puzzleSounds.add("bear");
                puzzleSounds.add("crocodile");
                puzzleSounds.add("kangaroo");
                puzzleSounds.add("panda");
                puzzleSounds.add("giraffe");
                puzzleSounds.add("fox");

                IF.putStringArrayListExtra("puzzle_sounds", puzzleSounds);
                IF.putIntegerArrayListExtra("puzzle_assets", puzzleAssets);
                IF.putExtra("background", R.drawable.fish_background);
                IF.putExtra("puzzle_name", "fish");
                startActivity(IF);
            }, 0);

            Handler fishSS = new Handler();
            fishSS.postDelayed(() -> {

                if (!stop) {
                    fishSentence.start();
//                    tts.speak("Fish", TextToSpeech.QUEUE_FLUSH, null);
                }

            }, 2000);

        });

        giraffebtn.setOnClickListener(v -> {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//            giraffemediaPlayer.start();
            tts.speak("Giraffe", TextToSpeech.QUEUE_FLUSH, null);

            final Handler giraffeNavigate = new Handler();
            while (tts.isSpeaking())
            {}
            giraffeNavigate.postDelayed(() -> {

//                Intent IG = new Intent(MainActivity.this, GiraffeActivity.class);
                Intent IG = new Intent(MainActivity.this, MainPuzzleActivity.class);
                ArrayList<Integer> puzzleAssets = new ArrayList<>();
                puzzleAssets.add(R.mipmap.graps);
                puzzleAssets.add(R.mipmap.apple);
                puzzleAssets.add(R.mipmap.banana);
                puzzleAssets.add(R.mipmap.watermilon);
                puzzleAssets.add(R.mipmap.mango);
                puzzleAssets.add(R.mipmap.stawbrey);
                puzzleAssets.add(R.mipmap.amrood);
                puzzleAssets.add(R.mipmap.chery);
                puzzleAssets.add(R.mipmap.orange);
                puzzleAssets.add(R.mipmap.pineapple);

                ArrayList<String> puzzleSounds = new ArrayList<>();
                puzzleSounds.add("grapes");
                puzzleSounds.add("apple");
                puzzleSounds.add("banana");
                puzzleSounds.add("watermelon");
                puzzleSounds.add("mango");
                puzzleSounds.add("strawberry");
                puzzleSounds.add("pears");
                puzzleSounds.add("cherry");
                puzzleSounds.add("orange");
                puzzleSounds.add("pineapple");

                IG.putStringArrayListExtra("puzzle_sounds", puzzleSounds);
                IG.putIntegerArrayListExtra("puzzle_assets", puzzleAssets);
                IG.putExtra("background", R.drawable.giraffe_background);
                IG.putExtra("puzzle_name", "giraffe");
                startActivity(IG);
            }, 0);

            Handler giraffeSS = new Handler();
            giraffeSS.postDelayed(() -> {

                if (!stop) {
                    giraffeSentence.start();
//                    tts.speak("Giraffe", TextToSpeech.QUEUE_FLUSH, null);
                }

            }, 2000);


        });


        parrotbtn.setOnClickListener(v -> {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//            lionmediaPlayer.start();
            tts.speak("Parrot", TextToSpeech.QUEUE_FLUSH, null);

            while (tts.isSpeaking())
            {}

            final Handler parrotNavigate = new Handler();
            parrotNavigate.postDelayed(() -> {

//                Intent IL = new Intent(MainActivity.this, LionActivity.class);
                Intent IL = new Intent(MainActivity.this, ParrotActivity.class);

                startActivity(IL);
            }, 0);


        });

        lionbtn.setOnClickListener(v -> {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//            lionmediaPlayer.start();
            tts.speak("Lion", TextToSpeech.QUEUE_FLUSH, null);

            while (tts.isSpeaking())
            {}

            final Handler lionNavigate = new Handler();
            lionNavigate.postDelayed(() -> {

//                Intent IL = new Intent(MainActivity.this, LionActivity.class);
                Intent IL = new Intent(MainActivity.this, MainPuzzleActivity.class);
                ArrayList<Integer> puzzleAssets = new ArrayList<>();
                puzzleAssets.add(R.mipmap.burger);
                puzzleAssets.add(R.mipmap.spegiti);
                puzzleAssets.add(R.mipmap.fish);
                puzzleAssets.add(R.mipmap.chips);
                puzzleAssets.add(R.mipmap.sanwitch);
                puzzleAssets.add(R.mipmap.pizza);
                puzzleAssets.add(R.mipmap.soup);
                puzzleAssets.add(R.mipmap.egg);
                puzzleAssets.add(R.mipmap.biscuit);
                puzzleAssets.add(R.mipmap.macroni);

                ArrayList<String> puzzleSounds = new ArrayList<>();
                puzzleSounds.add("burger");
                puzzleSounds.add("spaghetti");
                puzzleSounds.add("fish");
                puzzleSounds.add("fries");
                puzzleSounds.add("sandwich");
                puzzleSounds.add("pizza");
                puzzleSounds.add("soup");
                puzzleSounds.add("egg");
                puzzleSounds.add("biscuit");
                puzzleSounds.add("macroni");

                IL.putStringArrayListExtra("puzzle_sounds", puzzleSounds);
                IL.putIntegerArrayListExtra("puzzle_assets", puzzleAssets);
                IL.putExtra("background", R.drawable.lion_background);
                IL.putExtra("puzzle_name", "lion");
                startActivity(IL);
            }, 0);

            final Handler lionSS = new Handler();
            lionSS.postDelayed(() -> {

                if (!stop) {
                    lionSentence.start();
//                    tts.speak("Lion", TextToSpeech.QUEUE_FLUSH, null);
                }

            }, 2000);


        });

        frogbtn.setOnClickListener(v -> {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//            frogmediaPlayer.start();
            tts.speak("Frog", TextToSpeech.QUEUE_FLUSH, null);

            while (tts.isSpeaking())
            {}

            final Handler frogNavigate = new Handler();
            frogNavigate.postDelayed(() -> {

                Intent IFr = new Intent(MainActivity.this, MainPuzzleActivity.class);
                ArrayList<Integer> puzzleAssets = new ArrayList<>();
                puzzleAssets.add(R.mipmap.bib);
                puzzleAssets.add(R.mipmap.socks);
                puzzleAssets.add(R.mipmap.feeder);
                puzzleAssets.add(R.mipmap.shirt);
                puzzleAssets.add(R.mipmap.dipper);
                puzzleAssets.add(R.mipmap.lotion);
                puzzleAssets.add(R.mipmap.chsni);
                puzzleAssets.add(R.mipmap.pant);
                puzzleAssets.add(R.mipmap.topa);
                puzzleAssets.add(R.mipmap.shoes);

                ArrayList<String> puzzleSounds = new ArrayList<>();
                puzzleSounds.add("bib");
                puzzleSounds.add("socks");
                puzzleSounds.add("bottle");
                puzzleSounds.add("shirt");
                puzzleSounds.add("nappy");
                puzzleSounds.add("baby oil");
                puzzleSounds.add("dummy");
                puzzleSounds.add("trouser");
                puzzleSounds.add("mittens");
                puzzleSounds.add("shoes");

                IFr.putStringArrayListExtra("puzzle_sounds", puzzleSounds);
                IFr.putIntegerArrayListExtra("puzzle_assets", puzzleAssets);
                IFr.putExtra("background", R.drawable.frog_background);
                IFr.putExtra("puzzle_name", "frog");
                startActivity(IFr);
            }, 0);

            Handler frogSS = new Handler();
            frogSS.postDelayed(() -> {

                if (!stop) {
                    frogSentence.start();
//                    tts.speak("Frog", TextToSpeech.QUEUE_FLUSH, null);
                }

            }, 2000);


        });
    }

    public void passwardPopup() {

        passwardDialog.setContentView(R.layout.passward_popup);
        circle_passward_cancelbtn = passwardDialog.findViewById(R.id.circle_passward_cancelbtn);
        circle_passward_cancelbtn.setOnClickListener(v -> passwardDialog.dismiss());
        tvQUESTION = passwardDialog.findViewById(R.id.QPassTV);
        if (question == 1) {
            //7
            tvQUESTION.setText("2+5 =?");
        }
        if (question == 2) {
            //4
            tvQUESTION.setText("3+1 =?");
        }
        if (question == 3) {
            //9
            tvQUESTION.setText("7+2 =?");
        }
        if (question == 4) {
            //14
            tvQUESTION.setText("4+10 =?");
        }
        if (question == 5) {
            //11
            tvQUESTION.setText("8+3 =?");
        }
        if (question == 6) {
            //2
            tvQUESTION.setText("1+1 =?");
        }
        if (question == 7) {
            //20
            tvQUESTION.setText("10+10 =?");
        }
        if (question == 8) {
            //17
            tvQUESTION.setText("9+8 =?");
        }
        if (question == 9) {
            //6
            tvQUESTION.setText("6+0 =?");
        }
        if (question == 10) {
            //16
            tvQUESTION.setText("11+5 =?");
        }
        popupNameEDT = passwardDialog.findViewById(R.id.popupNameEDT);
        continuebtn = passwardDialog.findViewById(R.id.continuebtn);
        continuebtn.setOnClickListener(v -> {
            String checkPassward = popupNameEDT.getText().toString();
            int check = Integer.parseInt(checkPassward);
            if (question == 1) {
                //7
                if (check == 7) {
                    passwardDialog.dismiss();
                    parentPopup();
                } else {
                    Toast.makeText(MainActivity.this, "Try again", Toast.LENGTH_LONG).show();
                }
            }
            if (question == 2) {
                //4
                if (check == 4) {
                    passwardDialog.dismiss();
                    parentPopup();
                } else {
                    Toast.makeText(MainActivity.this, "Try again", Toast.LENGTH_LONG).show();
                }
            }
            if (question == 3) {
                //9
                if (check == 9) {
                    passwardDialog.dismiss();
                    parentPopup();
                } else {
                    Toast.makeText(MainActivity.this, "Try again", Toast.LENGTH_LONG).show();
                }
            }
            if (question == 4) {
                //14
                if (check == 14) {
                    passwardDialog.dismiss();
                    parentPopup();
                } else {
                    Toast.makeText(MainActivity.this, "Try again", Toast.LENGTH_LONG).show();
                }
            }
            if (question == 5) {
                //11
                if (check == 11) {
                    passwardDialog.dismiss();
                    parentPopup();
                } else {
                    Toast.makeText(MainActivity.this, "Try again", Toast.LENGTH_LONG).show();
                }
            }
            if (question == 6) {
                //2
                if (check == 2) {
                    passwardDialog.dismiss();
                    parentPopup();
                } else {
                    Toast.makeText(MainActivity.this, "Try again", Toast.LENGTH_LONG).show();
                }
            }
            if (question == 7) {
                //20
                if (check == 20) {
                    passwardDialog.dismiss();
                    parentPopup();
                } else {
                    Toast.makeText(MainActivity.this, "Try again", Toast.LENGTH_LONG).show();
                }
            }
            if (question == 8) {
                //17
                if (check == 17) {
                    passwardDialog.dismiss();
                    parentPopup();
                } else {
                    Toast.makeText(MainActivity.this, "Try again", Toast.LENGTH_LONG).show();
                }
            }
            if (question == 9) {
                //6
                if (check == 6) {
                    passwardDialog.dismiss();
                    parentPopup();
                } else {
                    Toast.makeText(MainActivity.this, "Try again", Toast.LENGTH_LONG).show();
                }
            }
            if (question == 10) {
                //16
                if (check == 16) {
                    passwardDialog.dismiss();
                    parentPopup();
                } else {
                    Toast.makeText(MainActivity.this, "Try again", Toast.LENGTH_LONG).show();
                }
            }

        });
        passwardDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        passwardDialog.show();
    }

    public void parentPopup() {

        parentDialog.setContentView(R.layout.parent_zone_popup);
        circle_parent_cancelbtn = parentDialog.findViewById(R.id.circle_parent_cancelbtn);
        circle_parent_cancelbtn.setOnClickListener(v -> parentDialog.dismiss());
        create_your_puzzles_btn = parentDialog.findViewById(R.id.create_your_puzzles_btn);
        create_your_puzzles_btn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CustomMainActivity.class)));
        information_btn = parentDialog.findViewById(R.id.information_btn);
        information_btn.setOnClickListener(v -> {

            infoPopup();
            parentDialog.dismiss();
        });


        parentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        parentDialog.show();

    }

    public void infoPopup() {

        infoDialog.setContentView(R.layout.information_popup);
        circle_info_cancelbtn = infoDialog.findViewById(R.id.circle_info_cancelbtn);
        circle_info_cancelbtn.setOnClickListener(v -> infoDialog.dismiss());
        infoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        infoDialog.show();

    }

    @Override
    protected void onResume() {

        super.onResume();
//        if(duckSentence.isPlaying())
//        {
//            duckSentence.pause();
//            duckSentence.reset();
//        }
//        if(elephantSentence.isPlaying())
//        {
//            elephantSentence.pause();
//            elephantSentence.reset();
//        }
//        if(fishSentence.isPlaying())
//        {
//            fishSentence.stop();
//        }
//        if(frogSentence.isPlaying())
//        {
//            frogSentence.stop();
//        }
//        if(giraffeSentence.isPlaying())
//        {
//            giraffeSentence.stop();
//        }
//        if(lionSentence.isPlaying())
//        {
//            lionSentence.stop();
//        }
//        if(monkeySentence.isPlaying())
//        {
//            monkeySentence.stop();
//        }
//        if(sheepSentence.isPlaying())
//        {
//            sheepSentence.stop();
//        }
//        if(snakeSentence.isPlaying())
//        {
//            snakeSentence.stop();
//        }
//        if(zebraSentence.isPlaying())
//        {
//            zebraSentence.stop();
//        }

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        stop = false;
        if (isPlaying) {

            startService(new Intent(MainActivity.this, AlarmSoundService.class));
            musicMute.setImageResource(R.mipmap.onmusic);


        }
        if (!isPlaying) {

            //Stop the Media Player Service to stop sound
            stopService(new Intent(MainActivity.this, AlarmSoundService.class));
            musicMute.setImageResource(R.mipmap.musicmute);
        }
    }

    @Override
    protected void onPause() {

        super.onPause();
        //Stop the Media Player Service to stop sound
        stopService(new Intent(MainActivity.this, AlarmSoundService.class));
        musicMute.setImageResource(R.mipmap.onmusic);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}

