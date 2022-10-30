package com.matchymatchproject.mirassociationdanny.matchymatch;

import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.DragEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matchymatchproject.mirassociationdanny.matchymatch.DBHelper.DBHelper;
import com.matchymatchproject.mirassociationdanny.matchymatch.Utils.AlarmSoundService;
import com.matchymatchproject.mirassociationdanny.matchymatch.Utils.Common;
import com.matchymatchproject.mirassociationdanny.matchymatch.Utils.Constants;
import com.matchymatchproject.mirassociationdanny.matchymatch.Utils.Shaker;
import com.matchymatchproject.mirassociationdanny.matchymatch.Utils.Utils;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.matchymatchproject.mirassociationdanny.matchymatch.Utils.Utils.getUriToResource;

public class CustomMainPuzzleActivity extends AppCompatActivity {

    private static final Integer GREY = Color.parseColor("#D2D2D2");

    ImageView DRAGOne,DRAGtwo,DRAGthree;
    TextToSpeech tts;
    int puzzle = 0;
    int changer = 0;
    MediaPlayer winlevelsound;
    String firstTextVoice,secondTextVoice,thirdTextVoice,fourtTextVoice,fiveTextVoice,sixTextVoice,sevenTextVoice,eightTextVoice,nineTextVoice,tenTextVoice;

    //home button
    ImageView homebtn;

    //DB work
    private static final int SELECT_PHOTO = 7777;
    private static final int CAM_REQUEST = 1313;
    DBHelper dbHelper;
    ImageView invisible_imageView;

    //activity images work
    ImageView image_one,image_two,image_three,image_four,image_five,image_six,image_seven,image_eight,image_nine,image_ten,bgimage;

    // warning photo popup
    ImageView circle_cancel_warning_popup;
    Dialog warningDialog;

    // voiceover record popup
    ImageView circle_voice_record_popup;
    EditText voiceEDT;
    Button voicebtn;
    Dialog voiceDialog;

    // select photo popup
    ImageView circle_cancel_select_popup;
    Button gallery_btn,camera_btn;
    Dialog photoDialog;

    //edit,delete, and upload popup
    ImageView circle_cancel_ten_popup;
    Button upload_image_btn,voiceover_btn,image_delete_btn;
    Dialog tenDialog;

    // save photo popup
    ImageView circle_cancel_save_popup;
    Button cancel_btn,save_image_btn;
    Dialog saveDialog;
    String puzzleName = "";
    ArrayList<CustomMainPuzzle> customMainPuzzles;
    private static final Type TYPE = new TypeToken<ArrayList<CustomMainPuzzle>>() {}.getType();
    SharedPreferences myPuzzle;
    ConstraintLayout container;
    SimpleDraweeView ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//        setContentView(R.layout.activity_green);
        Fresco.initialize(this);
        setContentView(R.layout.custom_puzzle);

        container = findViewById(R.id.parent);
//        bgimage = findViewById(R.id.bgimage);
//        bgimage.setImageResource(getIntent().getIntExtra("bgColor",0));
        Drawable background = getResources().getDrawable(getIntent().getExtras().getInt("bgColor"));
        container.setBackground(background);
        puzzleName = getIntent().getStringExtra("puzzleName");

        SharedPreferences myMusic = this.getSharedPreferences("MyMusic", Context.MODE_PRIVATE);

        ll = findViewById(R.id.ll);

        DraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setUri("android.resource://com.matchymatchproject.mirassociationdanny.matchymatch/drawable/puzzle_end_gif.gif")
                .setUri(getUriToResource(CustomMainPuzzleActivity.this, R.drawable.puzzle_end_gif))
//                .setUri("https://media4.giphy.com/avatars/100soft/WahNEDdlGjRZ.gif")
                .setAutoPlayAnimations(true)
                .build();
        ll.setController(controller);

        myPuzzle = this.getSharedPreferences(puzzleName+"_MyAwesomePuzzle", Context.MODE_PRIVATE);
        puzzle = myPuzzle.getInt("puzzle", 0);
        //puzzle  = getIntent().getExtras().getInt("puzzle");
        customMainPuzzles = loadData();

        homebtn = findViewById(R.id.homebtn);
        homebtn.setOnClickListener(v -> startActivity(new Intent(CustomMainPuzzleActivity.this,CustomMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));

        winlevelsound = MediaPlayer.create(CustomMainPuzzleActivity.this,R.raw.kidscheering);

        if(puzzle == 0){

            puzzle+=1;

            myPuzzle = getSharedPreferences(puzzleName+"_MyAwesomePuzzle", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = myPuzzle.edit();
            editor.putInt("puzzle", puzzle);
            editor.commit();

        }

        else {

        }

        tts = new TextToSpeech(getApplicationContext(), status -> {
            if(status!=TextToSpeech.ERROR){

                tts.setLanguage(Constants.speechLocale);
                tts.setPitch((float) 1.1);
                tts.setSpeechRate((float) 0.6);
            }
        });

        dbHelper = new DBHelper(this);

        warningDialog = new Dialog(this);
        voiceDialog = new Dialog(this);
        photoDialog = new Dialog(this);
        tenDialog = new Dialog(this);
        saveDialog = new Dialog(this);
//        invisible_imageView  = (ImageView) findViewById(R.id.save_green_image_uri);
        invisible_imageView  = findViewById(R.id.save_image_uri);

        // target image linear layout
//        image_one  = (ConstraintLayout) findViewById(R.id.image_one);
//        image_two  = (ConstraintLayout) findViewById(R.id.image_two);
//        image_three  = (ConstraintLayout) findViewById(R.id.image_three);
//        image_four  = (ConstraintLayout) findViewById(R.id.image_four);
//        image_five  = (ConstraintLayout) findViewById(R.id.image_five);
//        image_six  = (ConstraintLayout) findViewById(R.id.image_six);
//        image_seven  = (ConstraintLayout) findViewById(R.id.image_seven);
//        image_eight  = (ConstraintLayout) findViewById(R.id.image_eight);
//        image_nine  = (ConstraintLayout) findViewById(R.id.image_nine);
//        image_ten = (ConstraintLayout) findViewById(R.id.image_ten);

        // drag image boxes
        DRAGOne = findViewById(R.id.image_puzzle_one);
        DRAGtwo = findViewById(R.id.image_puzzle_two);
        DRAGthree = findViewById(R.id.image_puzzle_three);

        // declear images
        image_one    = findViewById(R.id.image_one);
        image_two    = findViewById(R.id.image_two);
        image_three  = findViewById(R.id.image_three);
        image_four   = findViewById(R.id.image_four);
        image_five   = findViewById(R.id.image_five);
        image_six    = findViewById(R.id.image_six);
        image_seven  = findViewById(R.id.image_seven);
        image_eight  = findViewById(R.id.image_eight);
        image_nine   = findViewById(R.id.image_nine);
        image_ten    = findViewById(R.id.image_ten);

//        if(getIntent().getBooleanExtra("isCreating", false)) {
//
//            image_one.setOnDragListener(dragListener);
//            image_two.setOnDragListener(dragListener);
//            image_three.setOnDragListener(dragListener);
//            image_four.setOnDragListener(dragListener);
//            image_five.setOnDragListener(dragListener);
//            image_six.setOnDragListener(dragListener);
//            image_seven.setOnDragListener(dragListener);
//            image_eight.setOnDragListener(dragListener);
//            image_nine.setOnDragListener(dragListener);
//            image_ten.setOnDragListener(dragListener);
//
//            DRAGOne.setOnTouchListener(Common.touchListener);
//            DRAGtwo.setOnTouchListener(Common.touchListener);
//            DRAGthree.setOnTouchListener(Common.touchListener);
//
//        }
//        DRAGthree.setOnLongClickListener(longClickListener);


        //-------------------------------------this code for record voice name start here....................

        //change first Voice
        try {


//            SharedPreferences myFirstVoice = this.getSharedPreferences("MyFirstVoice", Context.MODE_PRIVATE);
//            firstTextVoice = myFirstVoice.getString("keyFirstVoice",null);
            if(customMainPuzzles.get(0) != null) {
                firstTextVoice = customMainPuzzles.get(0).getVoice();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //change second Voice
        try {


//            SharedPreferences mySecondVoice = this.getSharedPreferences("MySecondVoice", Context.MODE_PRIVATE);
//            secondTextVoice = mySecondVoice.getString("keySecondVoice",null);
            if(customMainPuzzles.get(1) != null) {
                secondTextVoice = customMainPuzzles.get(1).getVoice();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //change third Voice
        try {

//            SharedPreferences myThirdVoice = this.getSharedPreferences("MyThirdVoice", Context.MODE_PRIVATE);
//            thirdTextVoice = myThirdVoice.getString("keyThirdVoice",null);
            if(customMainPuzzles.get(2) != null) {
                thirdTextVoice = customMainPuzzles.get(2).getVoice();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //change fourt Voice
        try {

//            SharedPreferences myFourtVoice = this.getSharedPreferences("MyFourtVoice", Context.MODE_PRIVATE);
//            fourtTextVoice = myFourtVoice.getString("keyFourtVoice",null);
            if(customMainPuzzles.get(3) != null) {
                fourtTextVoice = customMainPuzzles.get(3).getVoice();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //change five Voice
        try {


//            SharedPreferences myFiveVoice = this.getSharedPreferences("MyFiveVoice", Context.MODE_PRIVATE);
//            fiveTextVoice = myFiveVoice.getString("keyFiveVoice",null);
            if(customMainPuzzles.get(4) != null) {
                fiveTextVoice = customMainPuzzles.get(4).getVoice();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //change six Voice
        try {

//            SharedPreferences mySixVoice = this.getSharedPreferences("MySixVoice", Context.MODE_PRIVATE);
//            sixTextVoice = mySixVoice.getString("keySixVoice",null);
            if(customMainPuzzles.get(5) != null) {
                sixTextVoice = customMainPuzzles.get(5).getVoice();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        //change seven Voice
        try {

//            SharedPreferences mySevenVoice = this.getSharedPreferences("MySevenVoice", Context.MODE_PRIVATE);
//            sevenTextVoice = mySevenVoice.getString("keySevenVoice",null);
            if(customMainPuzzles.get(6) != null) {
                sevenTextVoice = customMainPuzzles.get(6).getVoice();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //change eight Voice
        try {

//            SharedPreferences myEightVoice = this.getSharedPreferences("MyEightVoice", Context.MODE_PRIVATE);
//            eightTextVoice = myEightVoice.getString("keyEightVoice",null);
            if(customMainPuzzles.get(7) != null) {
                eightTextVoice = customMainPuzzles.get(7).getVoice();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //change nine Voice
        try {

//            SharedPreferences myNineVoice = this.getSharedPreferences("MyNineVoice", Context.MODE_PRIVATE);
//            nineTextVoice = myNineVoice.getString("keyNineVoice",null);
            if(customMainPuzzles.get(8) != null) {
                nineTextVoice = customMainPuzzles.get(8).getVoice();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //change ten Voice
        try {

//            SharedPreferences myTenVoice = this.getSharedPreferences("MyTenVoice", Context.MODE_PRIVATE);
//            tenTextVoice = myTenVoice.getString("keyTenVoice",null);
            if(customMainPuzzles.get(9) != null) {
                tenTextVoice = customMainPuzzles.get(9).getVoice();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //-------------------------------------this code for changing record voice end here....................


        //-------------------------------------this code for fetching puzzle images start here....................

        //image fetching for imageView one
        try {
            if (image_one.getDrawable() == null){

//                byte[] data = dbHelper.GetBitmapByName("firstgreen");
                Bitmap bitmap = null;
                if(customMainPuzzles != null && customMainPuzzles.get(0) != null && !customMainPuzzles.get(0).getImage().equalsIgnoreCase(""))
                {
                    byte[] b = Base64.decode(customMainPuzzles.get(0).getImage(), Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                }

                if (bitmap != null)
                {
//                    Bitmap bitmap = Utils.getImage(data);
                    image_one.setImageBitmap(bitmap);
                }else
                {
                    image_one.setImageDrawable(getDrawable(R.drawable.image_placeholder));
                }

            }else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //image fetching for imageView two
        try {
            if (image_two.getDrawable() == null){

                Bitmap bitmap = null;
                if(customMainPuzzles != null && customMainPuzzles.get(1) != null && !customMainPuzzles.get(1).getImage().equalsIgnoreCase(""))
                {
                    byte[] b = Base64.decode(customMainPuzzles.get(1).getImage(), Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                }

                if (bitmap != null)
                {
//                    Bitmap bitmap = Utils.getImage(data);
                    image_two.setImageBitmap(bitmap);
                }else
                {
                    image_two.setImageDrawable(getDrawable(R.drawable.image_placeholder));
                }

            }else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //image fetching for imageView three
        try {
            if (image_three.getDrawable() == null){

                Bitmap bitmap = null;
                if(customMainPuzzles != null && customMainPuzzles.get(2) != null && !customMainPuzzles.get(2).getImage().equalsIgnoreCase(""))
                {
                    byte[] b = Base64.decode(customMainPuzzles.get(2).getImage(), Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                }

                if (bitmap != null)
                {
//                    Bitmap bitmap = Utils.getImage(data);
                    image_three.setImageBitmap(bitmap);
                }else
                {
                    image_three.setImageDrawable(getDrawable(R.drawable.image_placeholder));
                }

            }else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //image fetching for imageView four
        try {
            if (image_four.getDrawable() == null){

                Bitmap bitmap = null;
                if(customMainPuzzles != null && customMainPuzzles.get(3) != null && !customMainPuzzles.get(3).getImage().equalsIgnoreCase(""))
                {
                    byte[] b = Base64.decode(customMainPuzzles.get(3).getImage(), Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                }

                if (bitmap != null)
                {
//                    Bitmap bitmap = Utils.getImage(data);
                    image_four.setImageBitmap(bitmap);
                }else
                {
                    image_four.setImageDrawable(getDrawable(R.drawable.image_placeholder));
                }

            }else {


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //image fetching for imageView five
        try {
            if (image_five.getDrawable() == null){

                Bitmap bitmap = null;
                if(customMainPuzzles != null && customMainPuzzles.get(4) != null && !customMainPuzzles.get(4).getImage().equalsIgnoreCase(""))
                {
                    byte[] b = Base64.decode(customMainPuzzles.get(4).getImage(), Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                }

                if (bitmap != null)
                {
//                    Bitmap bitmap = Utils.getImage(data);
                    image_five.setImageBitmap(bitmap);
                }else
                {
                    image_five.setImageDrawable(getDrawable(R.drawable.image_placeholder));
                }

            }else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //image fetching for imageView six
        try {
            if (image_six.getDrawable() == null){

                Bitmap bitmap = null;
                if(customMainPuzzles != null && customMainPuzzles.get(5) != null && !customMainPuzzles.get(5).getImage().equalsIgnoreCase(""))
                {
                    byte[] b = Base64.decode(customMainPuzzles.get(5).getImage(), Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                }

                if (bitmap != null)
                {
//                    Bitmap bitmap = Utils.getImage(data);
                    image_six.setImageBitmap(bitmap);
                }else
                {
                    image_six.setImageDrawable(getDrawable(R.drawable.image_placeholder));
                }
            }else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //image fetching for imageView seven
        try {
            if (image_seven.getDrawable() == null){

                Bitmap bitmap = null;
                if(customMainPuzzles != null && customMainPuzzles.get(6) != null && !customMainPuzzles.get(6).getImage().equalsIgnoreCase(""))
                {
                    byte[] b = Base64.decode(customMainPuzzles.get(6).getImage(), Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                }

                if (bitmap != null)
                {
//                    Bitmap bitmap = Utils.getImage(data);
                    image_seven.setImageBitmap(bitmap);
                }else
                {
                    image_seven.setImageDrawable(getDrawable(R.drawable.image_placeholder));
                }

            }else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //image fetching for imageView eight
        try {
            if (image_eight.getDrawable() == null){

                Bitmap bitmap = null;
                if(customMainPuzzles != null && customMainPuzzles.get(7) != null && !customMainPuzzles.get(7).getImage().equalsIgnoreCase(""))
                {
                    byte[] b = Base64.decode(customMainPuzzles.get(7).getImage(), Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                }

                if (bitmap != null)
                {
//                    Bitmap bitmap = Utils.getImage(data);
                    image_eight.setImageBitmap(bitmap);
                }else
                {
                    image_eight.setImageDrawable(getDrawable(R.drawable.image_placeholder));
                }

            }else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //image fetching for imageView nine
        try {
            if (image_nine.getDrawable() == null){

                Bitmap bitmap = null;
                if(customMainPuzzles != null && customMainPuzzles.get(8) != null && !customMainPuzzles.get(8).getImage().equalsIgnoreCase(""))
                {
                    byte[] b = Base64.decode(customMainPuzzles.get(8).getImage(), Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                }

                if (bitmap != null)
                {
//                    Bitmap bitmap = Utils.getImage(data);
                    image_nine.setImageBitmap(bitmap);
                }else
                {
                    image_nine.setImageDrawable(getDrawable(R.drawable.image_placeholder));
                }

            }else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //image fetching for imageView ten
        try {
            if (image_ten.getDrawable() == null){

                Bitmap bitmap = null;
                if(customMainPuzzles != null && customMainPuzzles.get(9) != null && !customMainPuzzles.get(9).getImage().equalsIgnoreCase(""))
                {
                    byte[] b = Base64.decode(customMainPuzzles.get(9).getImage(), Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                }

                if (bitmap != null)
                {
//                    Bitmap bitmap = Utils.getImage(data);
                    image_ten.setImageBitmap(bitmap);
                }else
                {
                    image_ten.setImageDrawable(getDrawable(R.drawable.image_placeholder));
                }

            }else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //----------------------------------------end for fetching------------------------------------------

        //Drag and Drop work in OnCreate Method ................Start Here........................
        if (image_one.getDrawable()==null || image_two.getDrawable()==null || image_three.getDrawable()==null || image_four.getDrawable()==null
                || image_five.getDrawable()==null || image_six.getDrawable()==null || image_seven.getDrawable()==null || image_eight.getDrawable()==null
                || image_nine.getDrawable()==null || image_ten.getDrawable()==null) {



        }
        else {
            //..................................drag drop work for score one...............................

            try {
                if(puzzle == 1){
                    // 5,7,1
                    //set image five in DRAGOne
                    try {
                        if (DRAGOne.getDrawable() == null){

                            Bitmap bitmap = null;
                            if(customMainPuzzles != null && customMainPuzzles.get(4) != null && !customMainPuzzles.get(4).getImage().equalsIgnoreCase(""))
                            {
                                byte[] b = Base64.decode(customMainPuzzles.get(4).getImage(), Base64.DEFAULT);
                                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                            }

                            if (bitmap != null)
                            {
//                    Bitmap bitmap = Utils.getImage(data);
                                DRAGOne.setImageBitmap(bitmap);
                            }else
                            {
                                DRAGOne.setImageDrawable(getDrawable(R.drawable.image_placeholder));
                            }
                        }else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //set image seven in DRAGtwo
                    try {
                        if (DRAGtwo.getDrawable() == null){

                            Bitmap bitmap = null;
                            if(customMainPuzzles != null && customMainPuzzles.get(6) != null && !customMainPuzzles.get(6).getImage().equalsIgnoreCase(""))
                            {
                                byte[] b = Base64.decode(customMainPuzzles.get(6).getImage(), Base64.DEFAULT);
                                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                            }

                            if (bitmap != null)
                            {
//                    Bitmap bitmap = Utils.getImage(data);
                                DRAGtwo.setImageBitmap(bitmap);
                            }else
                            {
                                DRAGtwo.setImageDrawable(getDrawable(R.drawable.image_placeholder));
                            }
                        }else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //set image one in DRAGthree
                    try {
                        if (DRAGthree.getDrawable() == null){

                            Bitmap bitmap = null;
                            if(customMainPuzzles != null && customMainPuzzles.get(0) != null && !customMainPuzzles.get(0).getImage().equalsIgnoreCase(""))
                            {
                                byte[] b = Base64.decode(customMainPuzzles.get(0).getImage(), Base64.DEFAULT);
                                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                            }

                            if (bitmap != null)
                            {
//                    Bitmap bitmap = Utils.getImage(data);
                                DRAGthree.setImageBitmap(bitmap);
                            }else
                            {
                                DRAGthree.setImageDrawable(getDrawable(R.drawable.image_placeholder));
                            }

                        }else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(puzzle == 2){
                    // 3,4,10
                    //set image three in DRAGOne
                    try {
                        if (DRAGOne.getDrawable() == null){

                            Bitmap bitmap = null;
                            if(customMainPuzzles != null && customMainPuzzles.get(2) != null && !customMainPuzzles.get(2).getImage().equalsIgnoreCase(""))
                            {
                                byte[] b = Base64.decode(customMainPuzzles.get(2).getImage(), Base64.DEFAULT);
                                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                            }

                            if (bitmap != null)
                            {
//                    Bitmap bitmap = Utils.getImage(data);
                                DRAGOne.setImageBitmap(bitmap);
                            }else
                            {
                                DRAGOne.setImageDrawable(getDrawable(R.drawable.image_placeholder));
                            }

                        }else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //set image four in DRAGtwo
                    try {
                        if (DRAGtwo.getDrawable() == null){

                            Bitmap bitmap = null;
                            if(customMainPuzzles != null && customMainPuzzles.get(3) != null && !customMainPuzzles.get(3).getImage().equalsIgnoreCase(""))
                            {
                                byte[] b = Base64.decode(customMainPuzzles.get(3).getImage(), Base64.DEFAULT);
                                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                            }

                            if (bitmap != null)
                            {
//                    Bitmap bitmap = Utils.getImage(data);
                                DRAGtwo.setImageBitmap(bitmap);
                            }else
                            {
                                DRAGtwo.setImageDrawable(getDrawable(R.drawable.image_placeholder));
                            }

                        }else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //set image ten in DRAGthree
                    try {
                        if (DRAGthree.getDrawable() == null){

                            Bitmap bitmap = null;
                            if(customMainPuzzles != null && customMainPuzzles.get(9) != null && !customMainPuzzles.get(9).getImage().equalsIgnoreCase(""))
                            {
                                byte[] b = Base64.decode(customMainPuzzles.get(9).getImage(), Base64.DEFAULT);
                                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                            }

                            if (bitmap != null)
                            {
//                    Bitmap bitmap = Utils.getImage(data);
                                DRAGthree.setImageBitmap(bitmap);
                            }else
                            {
                                DRAGthree.setImageDrawable(getDrawable(R.drawable.image_placeholder));
                            }

                        }else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if(puzzle == 3){
                    // 2,6,9
                    //set image two in DRAGOne
                    try {
                        if (DRAGOne.getDrawable() == null){

                            Bitmap bitmap = null;
                            if(customMainPuzzles != null && customMainPuzzles.get(1) != null && !customMainPuzzles.get(1).getImage().equalsIgnoreCase(""))
                            {
                                byte[] b = Base64.decode(customMainPuzzles.get(1).getImage(), Base64.DEFAULT);
                                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                            }

                            if (bitmap != null)
                            {
//                    Bitmap bitmap = Utils.getImage(data);
                                DRAGOne.setImageBitmap(bitmap);
                            }else
                            {
                                DRAGOne.setImageDrawable(getDrawable(R.drawable.image_placeholder));
                            }

                        }else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //set image six in DRAGtwo
                    try {
                        if (DRAGtwo.getDrawable() == null){

                            Bitmap bitmap = null;
                            if(customMainPuzzles != null && customMainPuzzles.get(5) != null && !customMainPuzzles.get(5).getImage().equalsIgnoreCase(""))
                            {
                                byte[] b = Base64.decode(customMainPuzzles.get(5).getImage(), Base64.DEFAULT);
                                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                            }

                            if (bitmap != null)
                            {
//                    Bitmap bitmap = Utils.getImage(data);
                                DRAGtwo.setImageBitmap(bitmap);
                            }else
                            {
                                DRAGtwo.setImageDrawable(getDrawable(R.drawable.image_placeholder));
                            }

                        }else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //set image nine in DRAGthree
                    try {
                        if (DRAGthree.getDrawable() == null){

                            Bitmap bitmap = null;
                            if(customMainPuzzles != null && customMainPuzzles.get(8) != null && !customMainPuzzles.get(8).getImage().equalsIgnoreCase(""))
                            {
                                byte[] b = Base64.decode(customMainPuzzles.get(8).getImage(), Base64.DEFAULT);
                                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                            }

                            if (bitmap != null)
                            {
//                    Bitmap bitmap = Utils.getImage(data);
                                DRAGthree.setImageBitmap(bitmap);
                            }else
                            {
                                DRAGthree.setImageDrawable(getDrawable(R.drawable.image_placeholder));
                            }

                        }else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if(puzzle == 4){
                    // 8,9,3
                    //set image eight in DRAGOne
                    try {
                        if (DRAGOne.getDrawable() == null){

                            Bitmap bitmap = null;
                            if(customMainPuzzles != null && customMainPuzzles.get(7) != null && !customMainPuzzles.get(7).getImage().equalsIgnoreCase(""))
                            {
                                byte[] b = Base64.decode(customMainPuzzles.get(7).getImage(), Base64.DEFAULT);
                                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                            }

                            if (bitmap != null)
                            {
//                    Bitmap bitmap = Utils.getImage(data);
                                DRAGOne.setImageBitmap(bitmap);
                            }else
                            {
                                DRAGOne.setImageDrawable(getDrawable(R.drawable.image_placeholder));
                            }

                        }else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //set image nine in DRAGtwo
                    try {
                        if (DRAGtwo.getDrawable() == null){

                            Bitmap bitmap = null;
                            if(customMainPuzzles != null && customMainPuzzles.get(8) != null && !customMainPuzzles.get(8).getImage().equalsIgnoreCase(""))
                            {
                                byte[] b = Base64.decode(customMainPuzzles.get(8).getImage(), Base64.DEFAULT);
                                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                            }

                            if (bitmap != null)
                            {
//                    Bitmap bitmap = Utils.getImage(data);
                                DRAGtwo.setImageBitmap(bitmap);
                            }else
                            {
                                DRAGtwo.setImageDrawable(getDrawable(R.drawable.image_placeholder));
                            }

                        }else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //set image three in DRAGthree
                    try {
                        if (DRAGthree.getDrawable() == null){

                            Bitmap bitmap = null;
                            if(customMainPuzzles != null && customMainPuzzles.get(2) != null && !customMainPuzzles.get(2).getImage().equalsIgnoreCase(""))
                            {
                                byte[] b = Base64.decode(customMainPuzzles.get(2).getImage(), Base64.DEFAULT);
                                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                            }

                            if (bitmap != null)
                            {
//                    Bitmap bitmap = Utils.getImage(data);
                                DRAGthree.setImageBitmap(bitmap);
                            }else
                            {
                                DRAGthree.setImageDrawable(getDrawable(R.drawable.image_placeholder));
                            }
                        }else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        //Drag and Drop work in OnCreate Method ................End Here........................

        //on Click get ImageView Id.................................

//        if(getIntent().getBooleanExtra("isCreating", false)) {

            image_one.setOnClickListener(v -> {

                int id = v.getId();
                tenPopup(id, 0);
            });

            image_two.setOnClickListener(v -> {

                int id = v.getId();
                tenPopup(id, 1);
            });

            image_three.setOnClickListener(v -> {

                int id = v.getId();
                tenPopup(id, 2);
            });

            image_four.setOnClickListener(v -> {

                int id = v.getId();
                tenPopup(id, 3);
            });

            image_five.setOnClickListener(v -> {

                int id = v.getId();
                tenPopup(id, 4);
            });

            image_six.setOnClickListener(v -> {

                int id = v.getId();
                tenPopup(id, 5);
            });

            image_seven.setOnClickListener(v -> {

                int id = v.getId();
                tenPopup(id, 6);
            });

            image_eight.setOnClickListener(v -> {

                int id = v.getId();
                tenPopup(id, 7);
            });

            image_nine.setOnClickListener(v -> {

                int id = v.getId();
                tenPopup(id, 8);
            });

            image_ten.setOnClickListener(v -> {

                int id = v.getId();
                tenPopup(id, 9);
            });
        }

//    }

    //------------------------------start here warningPopup-------------------------------
    public void warningPopup(){

        warningDialog.setContentView(R.layout.image_warning);
        circle_cancel_warning_popup = warningDialog.findViewById(R.id.circle_cancel_warning_popup);
        circle_cancel_warning_popup.setOnClickListener(v -> warningDialog.dismiss());


        warningDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        warningDialog.show();

    }

    //---------------------------warning popup end here....................................

    //------------------------------start here voice Popup-------------------------------
    public void voicePopup(final int id, CustomMainPuzzle customMainPuzzle){

        voiceDialog.setContentView(R.layout.voiceover_popup);
        circle_voice_record_popup = voiceDialog.findViewById(R.id.circle_voice_cancelbtn);
        circle_voice_record_popup.setOnClickListener(v -> {
            voiceDialog.dismiss();
            //restart activity
            startActivity(getIntent());
            finish();
            overridePendingTransition( 0, 0);
        });
        voiceEDT = voiceDialog.findViewById(R.id.voiceEDT);
        voicebtn = voiceDialog.findViewById(R.id.voicebtn);
        voicebtn.setOnClickListener(v -> {

            if(customMainPuzzles == null)
            {
                customMainPuzzles = new ArrayList<>();

                customMainPuzzles.add(null);
                customMainPuzzles.add(null);
                customMainPuzzles.add(null);
                customMainPuzzles.add(null);
                customMainPuzzles.add(null);
                customMainPuzzles.add(null);
                customMainPuzzles.add(null);
                customMainPuzzles.add(null);
                customMainPuzzles.add(null);
                customMainPuzzles.add(null);
            }

            if (image_one.getId() == id) {

                String firstVoice = voiceEDT.getText().toString();

//                    SharedPreferences myFirstVoice = getSharedPreferences("MyFirstVoice", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = myFirstVoice.edit();
//                    editor.putString("keyFirstVoice", firstVoice);
//                    editor.commit();
                customMainPuzzle.setVoice(firstVoice);
                customMainPuzzles.set(0, customMainPuzzle);

            }else if (image_two.getId() == id){

                String secondVoice = voiceEDT.getText().toString();

//                    SharedPreferences mySecondVoice = getSharedPreferences("MySecondVoice", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = mySecondVoice.edit();
//                    editor.putString("keySecondVoice", secondVoice);
//                    editor.commit();
                customMainPuzzle.setVoice(secondVoice);
                customMainPuzzles.set(1, customMainPuzzle);

            } else if (image_three.getId() == id){

                String thirdVoice = voiceEDT.getText().toString();

//                    SharedPreferences myThirdVoice = getSharedPreferences("MyThirdVoice", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = myThirdVoice.edit();
//                    editor.putString("keyThirdVoice", thirdVoice);
//                    editor.commit();
                customMainPuzzle.setVoice(thirdVoice);
                customMainPuzzles.set(2, customMainPuzzle);

            } else if (image_four.getId() == id){

                String fourthVoice = voiceEDT.getText().toString();

//                    SharedPreferences myFourtVoice = getSharedPreferences("MyFourtVoice", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = myFourtVoice.edit();
//                    editor.putString("keyFourtVoice", fourtVoice);
//                    editor.commit();
                customMainPuzzle.setVoice(fourthVoice);
                customMainPuzzles.set(3, customMainPuzzle);

            } else if (image_five.getId() == id){

                String fiveVoice = voiceEDT.getText().toString();

//                    SharedPreferences myFiveVoice = getSharedPreferences("MyFiveVoice", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = myFiveVoice.edit();
//                    editor.putString("keyFiveVoice", fiveVoice);
//                    editor.commit();
                customMainPuzzle.setVoice(fiveVoice);
                customMainPuzzles.set(4, customMainPuzzle);

            } else if (image_six.getId() == id){

                String sixVoice = voiceEDT.getText().toString();

//                    SharedPreferences mySixVoice = getSharedPreferences("MySixVoice", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = mySixVoice.edit();
//                    editor.putString("keySixVoice", sixVoice);
//                    editor.commit();
                customMainPuzzle.setVoice(sixVoice);
                customMainPuzzles.set(5, customMainPuzzle);

            }else if (image_seven.getId() == id) {

                String sevenVoice = voiceEDT.getText().toString();

//                    SharedPreferences mySevenVoice = getSharedPreferences("MySevenVoice", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = mySevenVoice.edit();
//                    editor.putString("keySevenVoice", sevenVoice);
//                    editor.commit();
                customMainPuzzle.setVoice(sevenVoice);
                customMainPuzzles.set(6, customMainPuzzle);

            }else if (image_eight.getId() == id){

                String eightVoice = voiceEDT.getText().toString();

//                    SharedPreferences myEightVoice = getSharedPreferences("MyEightVoice", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = myEightVoice.edit();
//                    editor.putString("keyEightVoice", eightVoice);
//                    editor.commit();
                customMainPuzzle.setVoice(eightVoice);
                customMainPuzzles.set(7, customMainPuzzle);

            }else if (image_nine.getId() == id){

                String nineVoice = voiceEDT.getText().toString();

//                    SharedPreferences myNineVoice = getSharedPreferences("MyNineVoice", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = myNineVoice.edit();
//                    editor.putString("keyNineVoice", nineVoice);
//                    editor.commit();
                customMainPuzzle.setVoice(nineVoice);
                customMainPuzzles.set(8, customMainPuzzle);

            }else if (image_ten.getId() == id){

                String tenVoice = voiceEDT.getText().toString();

//                    SharedPreferences myTenVoice = getSharedPreferences("MyTenVoice", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = myTenVoice.edit();
//                    editor.putString("keyTenVoice", tenVoice);
//                    editor.commit();
                customMainPuzzle.setVoice(tenVoice);
                customMainPuzzles.set(9, customMainPuzzle);

            }

            saveData(customMainPuzzles);
            // Restart the Activity
            startActivity(getIntent());
            finish();
            overridePendingTransition( 0, 0);

        });

        voiceDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        voiceDialog.show();

    }

    //---------------------------voice popup end here....................................


    //------------------------------start here savePopup-------------------------------
    public void savePopup(final int id){

        saveDialog.setContentView(R.layout.save_btn_popup);
        circle_cancel_save_popup = saveDialog.findViewById(R.id.circle_cancel_save_popup);
        circle_cancel_save_popup.setOnClickListener(v -> saveDialog.dismiss());
        cancel_btn = saveDialog.findViewById(R.id.cancelbtn);
        cancel_btn.setOnClickListener(v -> saveDialog.dismiss());
        save_image_btn = saveDialog.findViewById(R.id.savebtn);
        save_image_btn.setOnClickListener(v -> {


            saveImage(id);

//            if (image_one.getId() == id) {
//
//                if (image_one.getDrawable() == null || MainPuzzleActivity.areDrawablesIdentical(image_one.getDrawable() ,getResources().getDrawable(R.drawable.image_placeholder))) {
//
//                   saveImage(id);
//
//                }else {
//                    Toast.makeText(CustomMainPuzzleActivity.this, "Delete Previous Image", Toast.LENGTH_LONG).show();
//                }
//
//            }if (image_two.getId() == id){
//
//                if (image_two.getDrawable() == null || MainPuzzleActivity.areDrawablesIdentical(image_two.getDrawable() ,getResources().getDrawable(R.drawable.image_placeholder))){
//
//                    saveImage(id);
//
//                }else {
//                    Toast.makeText(CustomMainPuzzleActivity.this, "Delete Previous Image", Toast.LENGTH_LONG).show();
//                }
//
//
//            } if (image_three.getId() == id){
//
//                if (image_three.getDrawable() == null || MainPuzzleActivity.areDrawablesIdentical(image_three.getDrawable() ,getResources().getDrawable(R.drawable.image_placeholder))){
//
//                    saveImage(id);
//
//                }else {
//                    Toast.makeText(CustomMainPuzzleActivity.this, "Delete Previous Image", Toast.LENGTH_LONG).show();
//                }
//
//            } if (image_four.getId() == id){
//
//                if (image_four.getDrawable() == null || MainPuzzleActivity.areDrawablesIdentical(image_four.getDrawable() ,getResources().getDrawable(R.drawable.image_placeholder))){
//
//                    saveImage(id);
//                }else {
//                    Toast.makeText(CustomMainPuzzleActivity.this, "Delete Previous Image", Toast.LENGTH_LONG).show();
//                }
//
//            } if (image_five.getId() == id){
//
//                if (image_five.getDrawable() == null || MainPuzzleActivity.areDrawablesIdentical(image_five.getDrawable() ,getResources().getDrawable(R.drawable.image_placeholder))){
//
//                    saveImage(id);
//
//                }else {
//                    Toast.makeText(CustomMainPuzzleActivity.this, "Delete Previous Image", Toast.LENGTH_LONG).show();
//                }
//
//            } if (image_six.getId() == id){
//
//                if (image_six.getDrawable() == null || MainPuzzleActivity.areDrawablesIdentical(image_six.getDrawable() ,getResources().getDrawable(R.drawable.image_placeholder))){
//
//                    saveImage(id);
//
//                }else {
//                    Toast.makeText(CustomMainPuzzleActivity.this, "Delete Previous Image", Toast.LENGTH_LONG).show();
//                }
//
//            }if (image_seven.getId() == id){
//
//                if (image_seven.getDrawable() == null || MainPuzzleActivity.areDrawablesIdentical(image_seven.getDrawable() ,getResources().getDrawable(R.drawable.image_placeholder))){
//
//                    saveImage(id);
//
//                }else {
//                    Toast.makeText(CustomMainPuzzleActivity.this, "Delete Previous Image", Toast.LENGTH_LONG).show();
//                }
//
//            }if (image_eight.getId() == id){
//
//                if (image_eight.getDrawable() == null || MainPuzzleActivity.areDrawablesIdentical(image_eight.getDrawable() ,getResources().getDrawable(R.drawable.image_placeholder))){
//
//                    saveImage(id);
//
//                }else {
//                    Toast.makeText(CustomMainPuzzleActivity.this, "Delete Previous Image", Toast.LENGTH_LONG).show();
//                }
//
//            }if (image_nine.getId() == id){
//
//                if (image_nine.getDrawable() == null || MainPuzzleActivity.areDrawablesIdentical(image_nine.getDrawable() ,getResources().getDrawable(R.drawable.image_placeholder))){
//
//                    saveImage(id);
//
//                }else {
//                    Toast.makeText(CustomMainPuzzleActivity.this, "Delete Previous Image", Toast.LENGTH_LONG).show();
//                }
//
//            }if (image_ten.getId() == id){
//
//                if (image_ten.getDrawable() == null || MainPuzzleActivity.areDrawablesIdentical(image_ten.getDrawable() ,getResources().getDrawable(R.drawable.image_placeholder))){
//
//                    saveImage(id);
//
//                }else {
//                    Toast.makeText(CustomMainPuzzleActivity.this, "Delete Previous Image", Toast.LENGTH_LONG).show();
//                }
//
//            }

            saveDialog.dismiss();

        });

        saveDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        saveDialog.show();

    }

    private void saveImage(int id)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = ((BitmapDrawable) invisible_imageView.getDrawable()).getBitmap();

                int max = 1300;
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();
                if (max >= w || max >= h) {
//                            dbHelper.addBitmap(pick_green_first, Utils.getBytes(bitmap));
                    CustomMainPuzzle customPuzzle = new CustomMainPuzzle();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    bitmap.recycle();
                    String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    customPuzzle.setImage(encodedImage);
                    saveDialog.dismiss();
                    voicePopup(id, customPuzzle);

                    Toast.makeText(CustomMainPuzzleActivity.this, "Save Success", Toast.LENGTH_LONG).show();
                }else {
                    warningPopup();
                }
            }
        });


    }

    //---------------------------save popup end here....................................

    //--------------------------------------start here ten images popup work-------------------------------------
    public void tenPopup(final int id, int index){

        tenDialog.setContentView(R.layout.delete_edit_photo);
        circle_cancel_ten_popup = tenDialog.findViewById(R.id.circle_cancel_ten_popup);
        circle_cancel_ten_popup.setOnClickListener(v -> tenDialog.dismiss());
        upload_image_btn = tenDialog.findViewById(R.id.upload_image_btn);
        upload_image_btn.setOnClickListener(v -> {

            photoPopup(id);
            tenDialog.dismiss();
        });
        voiceover_btn = tenDialog.findViewById(R.id.voice_edit_btn);
        voiceover_btn.setOnClickListener(v -> {

            voicePopup(id, customMainPuzzles.get(index));
            tenDialog.dismiss();
        });

//        image_delete_btn = tenDialog.findViewById(R.id.image_delete_btn);
//        image_delete_btn.setOnClickListener(v -> {
//
//
//
//            if (image_one.getId() == id) {
//
//                if (image_one.getDrawable() == null) {
//
//                    photoPopup(id);
//
//                }else {
//
////                        String dele = dbHelper.DeleteImage("firstgreen");
//                    Toast.makeText(CustomMainPuzzleActivity.this, "Image Deleted", Toast.LENGTH_LONG).show();
//
//                    customMainPuzzles.set(0, null);
//                    saveData(customMainPuzzles);
//                    // Restart the Activity
//                    startActivity(getIntent());
//                    finish();
//                    overridePendingTransition( 0, 0);
//                }
//
//            }if (image_two.getId() == id){
//
//                if (image_two.getDrawable() == null){
//
//                    photoPopup(id);
//
//                }else {
//
////                        String dele = dbHelper.DeleteImage("secondgreen");
//                    Toast.makeText(CustomMainPuzzleActivity.this, "Image Deleted", Toast.LENGTH_LONG).show();
//                    customMainPuzzles.set(1, null);
//                    saveData(customMainPuzzles);
//                    // Restart the Activity
//                    startActivity(getIntent());
//                    finish();
//                    overridePendingTransition( 0, 0);
//                }
//
//
//
//            } if (image_three.getId() == id){
//
//                if (image_three.getDrawable() == null){
//
//                    photoPopup(id);
//
//                }else {
//
////                        String dele = dbHelper.DeleteImage("thirdgreen");
//                    Toast.makeText(CustomMainPuzzleActivity.this, "Image Deleted", Toast.LENGTH_LONG).show();
//                    customMainPuzzles.set(2, null);
//                    saveData(customMainPuzzles);
//                    // Restart the Activity
//                    startActivity(getIntent());
//                    finish();
//                    overridePendingTransition( 0, 0);
//                }
//
//            } if (image_four.getId() == id){
//
//                if (image_four.getDrawable() == null){
//
//                    photoPopup(id);
//
//                }else {
//
////                        String dele = dbHelper.DeleteImage("fourtgreen");
//                    Toast.makeText(CustomMainPuzzleActivity.this, "Image Deleted", Toast.LENGTH_LONG).show();
//                    customMainPuzzles.set(3, null);
//                    saveData(customMainPuzzles);
//                    // Restart the Activity
//                    startActivity(getIntent());
//                    finish();
//                    overridePendingTransition( 0, 0);
//                }
//
//
//            } if (image_five.getId() == id){
//
//                if (image_five.getDrawable() == null){
//
//                    photoPopup(id);
//
//                }else {
//
////                        String dele = dbHelper.DeleteImage("fivegreen");
//                    Toast.makeText(CustomMainPuzzleActivity.this, "Image Deleted", Toast.LENGTH_LONG).show();
//                    customMainPuzzles.set(4, null);
//                    saveData(customMainPuzzles);
//                    // Restart the Activity
//                    startActivity(getIntent());
//                    finish();
//                    overridePendingTransition( 0, 0);
//                }
//
//
//            } if (image_six.getId() == id){
//
//                if (image_six.getDrawable() == null){
//
//                    photoPopup(id);
//
//                }else {
//
////                        String dele = dbHelper.DeleteImage("sixgreen");
//                    Toast.makeText(CustomMainPuzzleActivity.this, "Image Deleted", Toast.LENGTH_LONG).show();
//                    customMainPuzzles.set(5, null);
//                    saveData(customMainPuzzles);
//                    // Restart the Activity
//                    startActivity(getIntent());
//                    finish();
//                    overridePendingTransition( 0, 0);
//                }
//
//
//            }
//
//            if (image_seven.getId() == id){
//
//                if (image_seven.getDrawable() == null){
//
//                    photoPopup(id);
//
//                }else {
//
////                        String dele = dbHelper.DeleteImage("sevengreen");
//                    Toast.makeText(CustomMainPuzzleActivity.this, "Image Deleted", Toast.LENGTH_LONG).show();
//                    customMainPuzzles.set(6, null);
//                    saveData(customMainPuzzles);
//                    // Restart the Activity
//                    startActivity(getIntent());
//                    finish();
//                    overridePendingTransition( 0, 0);
//                }
//
//            }if (image_eight.getId() == id){
//
//                if (image_eight.getDrawable() == null){
//
//                    photoPopup(id);
//
//                }else {
//
////                        String dele = dbHelper.DeleteImage("eightgreen");
//                    Toast.makeText(CustomMainPuzzleActivity.this, "Image Deleted", Toast.LENGTH_LONG).show();
//                    customMainPuzzles.set(7, null);
//                    saveData(customMainPuzzles);
//                    // Restart the Activity
//                    startActivity(getIntent());
//                    finish();
//                    overridePendingTransition( 0, 0);
//                }
//
//            }if (image_nine.getId() == id){
//
//                if (image_nine.getDrawable() == null){
//
//                    photoPopup(id);
//
//                }else {
//
////                        String dele = dbHelper.DeleteImage("ninegreen");
//                    Toast.makeText(CustomMainPuzzleActivity.this, "Image Deleted", Toast.LENGTH_LONG).show();
//                    customMainPuzzles.set(8, null);
//                    saveData(customMainPuzzles);
//                    // Restart the Activity
//                    startActivity(getIntent());
//                    finish();
//                    overridePendingTransition( 0, 0);
//                }
//
//            }if (image_ten.getId() == id){
//
//                if (image_ten.getDrawable() == null){
//
//                    photoPopup(id);
//
//                }else {
//
////                        String dele = dbHelper.DeleteImage("tengreen");
//                    Toast.makeText(CustomMainPuzzleActivity.this, "Image Deleted", Toast.LENGTH_LONG).show();
//                    customMainPuzzles.set(9, null);
//                    saveData(customMainPuzzles);
//                    // Restart the Activity
//                    startActivity(getIntent());
//                    finish();
//                    overridePendingTransition( 0, 0);
//                }
//
//            }
//
//
//            tenDialog.dismiss();
//        });

        tenDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tenDialog.show();

    }
    //---------------------------ten images popup end here....................................

    //---------------------------select photo popup start here....................................

    public void photoPopup(final int id){

        photoDialog.setContentView(R.layout.select_photo_popup);
        circle_cancel_select_popup = photoDialog.findViewById(R.id.circle_cancel_select_popup);
        circle_cancel_select_popup.setOnClickListener(v -> photoDialog.dismiss());
        gallery_btn = photoDialog.findViewById(R.id.gallerybtn);
        gallery_btn.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,SELECT_PHOTO);
            photoDialog.dismiss();
            savePopup(id);
        });
        camera_btn = photoDialog.findViewById(R.id.camerabtn);
        camera_btn.setOnClickListener(v -> {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,CAM_REQUEST);
            photoDialog.dismiss();
            savePopup(id);
        });

        photoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        photoDialog.show();

    }

    //---------------------------select popup end here....................................
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null)
        {
            Uri pickedimage = data.getData();
            invisible_imageView.setImageURI(pickedimage);


        }if (requestCode == CAM_REQUEST && resultCode == RESULT_OK && data != null)
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            invisible_imageView.setImageBitmap(bitmap);

        }
    }

    View.OnLongClickListener longClickListener = v -> {

        ClipData clipdata = ClipData.newPlainText("","");
        View.DragShadowBuilder myshadowBuilder = new View.DragShadowBuilder(v);
        v.startDrag(clipdata,myshadowBuilder,v,0);
        return true;
    };

    View.OnDragListener dragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {

            int dragEvent = event.getAction();
            final View view = (View) event.getLocalState();

            switch (dragEvent){

                case DragEvent.ACTION_DRAG_ENTERED:

                    break;

                case DragEvent.ACTION_DRAG_EXITED:

                    break;

                case DragEvent.ACTION_DROP:

//                    if (view.getId() == R.id.image_puzzle_one && v.getId() == R.id.image_five) {
//                        if(puzzle != 1) {
//                            Shaker shake = new Shaker(image_five, 0, 15, GREY, Color.RED);
//                            shake.shake();
//                            Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                            vibe.vibrate(100);
//                            tts.speak(getString(R.string.try_again), TextToSpeech.QUEUE_FLUSH, null);
//                        }
//                    } if (view.getId() == R.id.image_puzzle_one && v.getId() == R.id.image_seven) {
//
//                    Shaker shake = new Shaker(image_seven, 0, 15, GREY, Color.RED);
//                    shake.shake();
//                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    vibe.vibrate(100);
//                    tts.speak(getString(R.string.try_again),TextToSpeech.QUEUE_FLUSH,null);
//                }if (view.getId() == R.id.image_puzzle_two && v.getId() == R.id.image_five) {
//
//                    Shaker shake = new Shaker(image_five, 0, 15, GREY, Color.RED);
//                    shake.shake();
//                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    vibe.vibrate(100);
//                    tts.speak(getString(R.string.try_again),TextToSpeech.QUEUE_FLUSH,null);
//
//                }if (view.getId() == R.id.image_puzzle_two && v.getId() == R.id.image_seven) {
//                    if(puzzle != 1) {
//                        Shaker shake = new Shaker(image_seven, 0, 15, GREY, Color.RED);
//                        shake.shake();
//                        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                        vibe.vibrate(100);
//                        tts.speak(getString(R.string.try_again), TextToSpeech.QUEUE_FLUSH, null);
//                    }
//                }if (view.getId() == R.id.image_puzzle_three && v.getId() == R.id.image_five) {
//
//                    Shaker shake = new Shaker(image_five, 0, 15, GREY, Color.RED);
//                    shake.shake();
//                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    vibe.vibrate(100);
//                    tts.speak(getString(R.string.try_again),TextToSpeech.QUEUE_FLUSH,null);
//
//                }if (view.getId() == R.id.image_puzzle_three && v.getId() == R.id.image_seven) {
//
//                    Shaker shake = new Shaker(image_seven, 0, 15, GREY, Color.RED);
//                    shake.shake();
//                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    vibe.vibrate(100);
//                    tts.speak(getString(R.string.try_again),TextToSpeech.QUEUE_FLUSH,null);
//
//                }if (view.getId() == R.id.image_puzzle_one && v.getId() == R.id.image_three) {
//                    if(puzzle != 2) {
//                        Shaker shake = new Shaker(image_three, 0, 15, GREY, Color.RED);
//                        shake.shake();
//                        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                        vibe.vibrate(100);
//                        tts.speak(getString(R.string.try_again), TextToSpeech.QUEUE_FLUSH, null);
//                    }
//                }if (view.getId() == R.id.image_puzzle_one && v.getId() == R.id.image_four) {
//
//                    Shaker shake = new Shaker(image_four, 0, 15, GREY, Color.RED);
//                    shake.shake();
//                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    vibe.vibrate(100);
//                    tts.speak(getString(R.string.try_again),TextToSpeech.QUEUE_FLUSH,null);
//
//                } if (view.getId() == R.id.image_puzzle_two && v.getId() == R.id.image_three) {
//
//                    Shaker shake = new Shaker(image_three, 0, 15, GREY, Color.RED);
//                    shake.shake();
//                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    vibe.vibrate(100);
//                    tts.speak(getString(R.string.try_again),TextToSpeech.QUEUE_FLUSH,null);
//
//                }  if (view.getId() == R.id.image_puzzle_two && v.getId() == R.id.image_four) {
//                    if(puzzle != 2) {
//                        Shaker shake = new Shaker(image_four, 0, 15, GREY, Color.RED);
//                        shake.shake();
//                        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                        vibe.vibrate(100);
//                        tts.speak(getString(R.string.try_again), TextToSpeech.QUEUE_FLUSH, null);
//                    }
//                }if (view.getId() == R.id.image_puzzle_three && v.getId() == R.id.image_three) {
//                    if(puzzle != 4) {
//                        Shaker shake = new Shaker(image_three, 0, 15, GREY, Color.RED);
//                        shake.shake();
//                        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                        vibe.vibrate(100);
//                        tts.speak(getString(R.string.try_again), TextToSpeech.QUEUE_FLUSH, null);
//                    }
//                }  if (view.getId() == R.id.image_puzzle_three && v.getId() == R.id.image_four) {
//
//                    Shaker shake = new Shaker(image_four, 0, 15, GREY, Color.RED);
//                    shake.shake();
//                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    vibe.vibrate(100);
//                    tts.speak(getString(R.string.try_again),TextToSpeech.QUEUE_FLUSH,null);
//
//                } if (view.getId() == R.id.image_puzzle_one && v.getId() == R.id.image_one) {
//
//                    Shaker shake = new Shaker(image_one, 0, 15, GREY, Color.RED);
//                    shake.shake();
//                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    vibe.vibrate(100);
//                    tts.speak(getString(R.string.try_again),TextToSpeech.QUEUE_FLUSH,null);
//
//                } if (view.getId() == R.id.image_puzzle_one && v.getId() == R.id.image_six) {
//
//                    Shaker shake = new Shaker(image_six, 0, 15, GREY, Color.RED);
//                    shake.shake();
//                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    vibe.vibrate(100);
//                    tts.speak(getString(R.string.try_again),TextToSpeech.QUEUE_FLUSH,null);
//
//                }if (view.getId() == R.id.image_puzzle_two && v.getId() == R.id.image_one) {
//
//                    Shaker shake = new Shaker(image_one, 0, 15, GREY, Color.RED);
//                    shake.shake();
//                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    vibe.vibrate(100);
//                    tts.speak(getString(R.string.try_again),TextToSpeech.QUEUE_FLUSH,null);
//
//                }if (view.getId() == R.id.image_puzzle_two && v.getId() == R.id.image_six) {
//                    if(puzzle != 3) {
//                        Shaker shake = new Shaker(image_six, 0, 15, GREY, Color.RED);
//                        shake.shake();
//                        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                        vibe.vibrate(100);
//                        tts.speak(getString(R.string.try_again), TextToSpeech.QUEUE_FLUSH, null);
//                    }
//                }if (view.getId() == R.id.image_puzzle_three && v.getId() == R.id.image_one) {
//                    if(puzzle != 1) {
//                        Shaker shake = new Shaker(image_one, 0, 15, GREY, Color.RED);
//                        shake.shake();
//                        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                        vibe.vibrate(100);
//                        tts.speak(getString(R.string.try_again), TextToSpeech.QUEUE_FLUSH, null);
//                    }
//                }if (view.getId() == R.id.image_puzzle_three && v.getId() == R.id.image_six) {
//
//                    Shaker shake = new Shaker(image_six, 0, 15, GREY, Color.RED);
//                    shake.shake();
//                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    vibe.vibrate(100);
//                    tts.speak(getString(R.string.try_again),TextToSpeech.QUEUE_FLUSH,null);
//
//                }if (view.getId() == R.id.image_puzzle_one && v.getId() == R.id.image_eight) {
//                    if(puzzle != 4) {
//                        Shaker shake = new Shaker(image_eight, 0, 15, GREY, Color.RED);
//                        shake.shake();
//                        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                        vibe.vibrate(100);
//                        tts.speak(getString(R.string.try_again), TextToSpeech.QUEUE_FLUSH, null);
//                    }
//                } if (view.getId() == R.id.image_puzzle_one && v.getId() == R.id.image_nine) {
//
//                    Shaker shake = new Shaker(image_nine, 0, 15, GREY, Color.RED);
//                    shake.shake();
//                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    vibe.vibrate(100);
//                    tts.speak(getString(R.string.try_again),TextToSpeech.QUEUE_FLUSH,null);
//
//                } if (view.getId() == R.id.image_puzzle_one && v.getId() == R.id.image_two) {
//                    if(puzzle != 3) {
//                        Shaker shake = new Shaker(image_two, 0, 15, GREY, Color.RED);
//                        shake.shake();
//                        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                        vibe.vibrate(100);
//                        tts.speak(getString(R.string.try_again), TextToSpeech.QUEUE_FLUSH, null);
//                    }
//                } if (view.getId() == R.id.image_puzzle_one && v.getId() == R.id.image_ten) {
//
//                    Shaker shake = new Shaker(image_ten, 0, 15, GREY, Color.RED);
//                    shake.shake();
//                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    vibe.vibrate(100);
//                    tts.speak(getString(R.string.try_again),TextToSpeech.QUEUE_FLUSH,null);
//
//                }if (view.getId() == R.id.image_puzzle_two && v.getId() == R.id.image_eight) {
//
//                    Shaker shake = new Shaker(image_eight, 0, 15, GREY, Color.RED);
//                    shake.shake();
//                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    vibe.vibrate(100);
//                    tts.speak(getString(R.string.try_again),TextToSpeech.QUEUE_FLUSH,null);
//
//                } if (view.getId() == R.id.image_puzzle_two && v.getId() == R.id.image_nine) {
//                    if(puzzle != 4) {
//                        Shaker shake = new Shaker(image_nine, 0, 15, GREY, Color.RED);
//                        shake.shake();
//                        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                        vibe.vibrate(100);
//                        tts.speak(getString(R.string.try_again), TextToSpeech.QUEUE_FLUSH, null);
//                    }
//                } if (view.getId() == R.id.image_puzzle_two && v.getId() == R.id.image_two) {
//
//                    Shaker shake = new Shaker(image_two, 0, 15, GREY, Color.RED);
//                    shake.shake();
//                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    vibe.vibrate(100);
//                    tts.speak(getString(R.string.try_again),TextToSpeech.QUEUE_FLUSH,null);
//
//                } if (view.getId() == R.id.image_puzzle_two && v.getId() == R.id.image_ten) {
//
//                    Shaker shake = new Shaker(image_ten, 0, 15, GREY, Color.RED);
//                    shake.shake();
//                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    vibe.vibrate(100);
//                    tts.speak(getString(R.string.try_again),TextToSpeech.QUEUE_FLUSH,null);
//
//                }if (view.getId() == R.id.image_puzzle_three && v.getId() == R.id.image_eight) {
//
//                    Shaker shake = new Shaker(image_eight, 0, 15, GREY, Color.RED);
//                    shake.shake();
//                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    vibe.vibrate(100);
//                    tts.speak(getString(R.string.try_again),TextToSpeech.QUEUE_FLUSH,null);
//
//                } if (view.getId() == R.id.image_puzzle_three && v.getId() == R.id.image_nine) {
//                    if(puzzle != 3) {
//                        Shaker shake = new Shaker(image_nine, 0, 15, GREY, Color.RED);
//                        shake.shake();
//                        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                        vibe.vibrate(100);
//                        tts.speak(getString(R.string.try_again), TextToSpeech.QUEUE_FLUSH, null);
//                    }
//                } if (view.getId() == R.id.image_puzzle_three && v.getId() == R.id.image_two) {
//
//                    Shaker shake = new Shaker(image_two, 0, 15, GREY, Color.RED);
//                    shake.shake();
//                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    vibe.vibrate(100);
//                    tts.speak(getString(R.string.try_again),TextToSpeech.QUEUE_FLUSH,null);
//
//                } if (view.getId() == R.id.image_puzzle_three && v.getId() == R.id.image_ten) {
//                    if (puzzle != 2) {
//                        Shaker shake = new Shaker(image_ten, 0, 15, GREY, Color.RED);
//                        shake.shake();
//                        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                        vibe.vibrate(100);
//                        tts.speak(getString(R.string.try_again), TextToSpeech.QUEUE_FLUSH, null);
//                    }
//
//                }

                    if (!MainPuzzleActivity.areDrawablesIdentical(((ImageView) view).getDrawable() ,((ImageView) v).getDrawable())) {

                        Shaker shake = new Shaker(v, 0, 15, GREY, Color.RED);
                        shake.shake();
                        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vibe.vibrate(100);
                        tts.speak(getString(R.string.try_again), TextToSpeech.QUEUE_FLUSH, null);

                    }
                    else {


                        if (puzzle == 1) {
                            // 5,7,1
                            if (view.getId() == R.id.image_puzzle_one && v.getId() == R.id.image_five) {
//                                ConstraintLayout oldParent = (ConstraintLayout) view.getParent();
//                                oldParent.removeView(view);
//                                ConstraintLayout newParent = (ConstraintLayout) v;
//                                image_five.setVisibility(View.GONE);
//                                newParent.addView(view);
                                view.setAlpha(0.5f);
                                v.setAlpha(0.5f);

                                try {
                                    tts.speak(fiveTextVoice, TextToSpeech.QUEUE_ADD, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                changer += 1;
                                if (changer == 3) {
                                    if (puzzle == 4) {

                                        puzzle = 0;
                                        SharedPreferences myPuzzle = getSharedPreferences(puzzleName + "_MyAwesomePuzzle", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = myPuzzle.edit();
                                        editor.putInt("puzzle", puzzle);
                                        editor.commit();

                                    }

                                    puzzle += 1;

                                    SharedPreferences myPuzzle = getSharedPreferences(puzzleName + "_MyAwesomePuzzle", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = myPuzzle.edit();
                                    editor.putInt("puzzle", puzzle);
                                    editor.commit();

                                    //restart activity
                                    startActivity(getIntent());
                                    finish();
                                    overridePendingTransition(0, 0);
                                }

                            }


                            if (view.getId() == R.id.image_puzzle_two && v.getId() == R.id.image_seven) {
//                                ConstraintLayout oldParent = (ConstraintLayout) view.getParent();
//                                oldParent.removeView(view);
//                                ConstraintLayout newParent = (ConstraintLayout) v;
//                                image_seven.setVisibility(View.GONE);
//                                newParent.addView(view);
                                view.setAlpha(0.5f);
                                v.setAlpha(0.5f);

                                try {
                                    tts.speak(sevenTextVoice, TextToSpeech.QUEUE_ADD, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                changer += 1;
                                if (changer == 3) {
                                    if (puzzle == 4) {

                                        puzzle = 0;
                                        SharedPreferences myPuzzle = getSharedPreferences(puzzleName + "_MyAwesomePuzzle", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = myPuzzle.edit();
                                        editor.putInt("puzzle", puzzle);
                                        editor.commit();

                                    }

                                    puzzle += 1;

                                    SharedPreferences myPuzzle = getSharedPreferences(puzzleName + "_MyAwesomePuzzle", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = myPuzzle.edit();
                                    editor.putInt("puzzle", puzzle);
                                    editor.commit();

                                    //restart activity
                                    startActivity(getIntent());
                                    finish();
                                    overridePendingTransition(0, 0);
                                }

                            }

                            if (view.getId() == R.id.image_puzzle_three && v.getId() == R.id.image_one) {
//                                ConstraintLayout oldParent = (ConstraintLayout) view.getParent();
//                                oldParent.removeView(view);
//                                ConstraintLayout newParent = (ConstraintLayout) v;
//                                image_one.setVisibility(View.GONE);
//                                newParent.addView(view);
                                view.setAlpha(0.5f);
                                v.setAlpha(0.5f);

                                try {
                                    tts.speak(firstTextVoice, TextToSpeech.QUEUE_ADD, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                changer += 1;
                                if (changer == 3) {
                                    if (puzzle == 4) {

                                        puzzle = 0;
                                        SharedPreferences myPuzzle = getSharedPreferences(puzzleName + "_MyAwesomePuzzle", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = myPuzzle.edit();
                                        editor.putInt("puzzle", puzzle);
                                        editor.commit();

                                    }

                                    puzzle += 1;

                                    SharedPreferences myPuzzle = getSharedPreferences(puzzleName + "_MyAwesomePuzzle", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = myPuzzle.edit();
                                    editor.putInt("puzzle", puzzle);
                                    editor.commit();

                                    //restart activity
                                    startActivity(getIntent());
                                    finish();
                                    overridePendingTransition(0, 0);
                                }

                            }
                        }

                        if (puzzle == 2) {
                            // 3,4,10
                            if (view.getId() == R.id.image_puzzle_one && v.getId() == R.id.image_three) {
//                                ConstraintLayout oldParent = (ConstraintLayout) view.getParent();
//                                oldParent.removeView(view);
//                                ConstraintLayout newParent = (ConstraintLayout) v;
//                                image_three.setVisibility(View.GONE);
//                                newParent.addView(view);
                                view.setAlpha(0.5f);
                                v.setAlpha(0.5f);

                                try {
                                    tts.speak(thirdTextVoice, TextToSpeech.QUEUE_ADD, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                changer += 1;
                                if (changer == 3) {
                                    if (puzzle == 4) {

                                        puzzle = 0;
                                        SharedPreferences myPuzzle = getSharedPreferences(puzzleName + "_MyAwesomePuzzle", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = myPuzzle.edit();
                                        editor.putInt("puzzle", puzzle);
                                        editor.commit();

                                    }

                                    puzzle += 1;

                                    SharedPreferences myPuzzle = getSharedPreferences(puzzleName + "_MyAwesomePuzzle", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = myPuzzle.edit();
                                    editor.putInt("puzzle", puzzle);
                                    editor.commit();

                                    //restart activity
                                    startActivity(getIntent());
                                    finish();
                                    overridePendingTransition(0, 0);
                                }

                            }


                            if (view.getId() == R.id.image_puzzle_two && v.getId() == R.id.image_four) {
//                                ConstraintLayout oldParent = (ConstraintLayout) view.getParent();
//                                oldParent.removeView(view);
//                                ConstraintLayout newParent = (ConstraintLayout) v;
//                                image_four.setVisibility(View.GONE);
//                                newParent.addView(view);
                                view.setAlpha(0.5f);
                                v.setAlpha(0.5f);

                                try {
                                    tts.speak(fourtTextVoice, TextToSpeech.QUEUE_ADD, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                changer += 1;
                                if (changer == 3) {
                                    if (puzzle == 4) {

                                        puzzle = 0;
                                        SharedPreferences myPuzzle = getSharedPreferences(puzzleName + "_MyAwesomePuzzle", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = myPuzzle.edit();
                                        editor.putInt("puzzle", puzzle);
                                        editor.commit();

                                    }

                                    puzzle += 1;

                                    SharedPreferences myPuzzle = getSharedPreferences(puzzleName + "_MyAwesomePuzzle", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = myPuzzle.edit();
                                    editor.putInt("puzzle", puzzle);
                                    editor.commit();

                                    //restart activity
                                    startActivity(getIntent());
                                    finish();
                                    overridePendingTransition(0, 0);
                                }

                            }


                            if (view.getId() == R.id.image_puzzle_three && v.getId() == R.id.image_ten) {
//                                ConstraintLayout oldParent = (ConstraintLayout) view.getParent();
//                                oldParent.removeView(view);
//                                ConstraintLayout newParent = (ConstraintLayout) v;
//                                image_ten.setVisibility(View.GONE);
//                                newParent.addView(view);
                                view.setAlpha(0.5f);
                                v.setAlpha(0.5f);

                                try {
                                    tts.speak(tenTextVoice, TextToSpeech.QUEUE_ADD, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                changer += 1;
                                if (changer == 3) {
                                    if (puzzle == 4) {

                                        puzzle = 0;
                                        SharedPreferences myPuzzle = getSharedPreferences(puzzleName + "_MyAwesomePuzzle", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = myPuzzle.edit();
                                        editor.putInt("puzzle", puzzle);
                                        editor.commit();

                                    }

                                    puzzle += 1;

                                    SharedPreferences myPuzzle = getSharedPreferences(puzzleName + "_MyAwesomePuzzle", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = myPuzzle.edit();
                                    editor.putInt("puzzle", puzzle);
                                    editor.commit();

                                    //restart activity
                                    startActivity(getIntent());
                                    finish();
                                    overridePendingTransition(0, 0);
                                }
                            }
                        }

                        if (puzzle == 3) {
                            // 2,6,9
                            if (view.getId() == R.id.image_puzzle_one && v.getId() == R.id.image_two) {
//                                ConstraintLayout oldParent = (ConstraintLayout) view.getParent();
//                                oldParent.removeView(view);
//                                ConstraintLayout newParent = (ConstraintLayout) v;
//                                image_two.setVisibility(View.GONE);
//                                newParent.addView(view);
                                view.setAlpha(0.5f);
                                v.setAlpha(0.5f);

                                try {
                                    tts.speak(secondTextVoice, TextToSpeech.QUEUE_ADD, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                changer += 1;
                                if (changer == 3) {
                                    if (puzzle == 4) {

                                        puzzle = 0;
                                        SharedPreferences myPuzzle = getSharedPreferences(puzzleName + "_MyAwesomePuzzle", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = myPuzzle.edit();
                                        editor.putInt("puzzle", puzzle);
                                        editor.commit();

                                    }

                                    puzzle += 1;

                                    SharedPreferences myPuzzle = getSharedPreferences(puzzleName + "_MyAwesomePuzzle", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = myPuzzle.edit();
                                    editor.putInt("puzzle", puzzle);
                                    editor.commit();

                                    //restart activity
                                    startActivity(getIntent());
                                    finish();
                                    overridePendingTransition(0, 0);
                                }

                            }


                            if (view.getId() == R.id.image_puzzle_two && v.getId() == R.id.image_six) {
//                                ConstraintLayout oldParent = (ConstraintLayout) view.getParent();
//                                oldParent.removeView(view);
//                                ConstraintLayout newParent = (ConstraintLayout) v;
//                                image_six.setVisibility(View.GONE);
//                                newParent.addView(view);
                                view.setAlpha(0.5f);
                                v.setAlpha(0.5f);

                                try {
                                    tts.speak(sixTextVoice, TextToSpeech.QUEUE_ADD, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                changer += 1;
                                if (changer == 3) {
                                    if (puzzle == 4) {

                                        puzzle = 0;
                                        SharedPreferences myPuzzle = getSharedPreferences(puzzleName + "_MyAwesomePuzzle", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = myPuzzle.edit();
                                        editor.putInt("puzzle", puzzle);
                                        editor.commit();

                                    }

                                    puzzle += 1;

                                    SharedPreferences myPuzzle = getSharedPreferences(puzzleName + "_MyAwesomePuzzle", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = myPuzzle.edit();
                                    editor.putInt("puzzle", puzzle);
                                    editor.commit();

                                    //restart activity
                                    startActivity(getIntent());
                                    finish();
                                    overridePendingTransition(0, 0);
                                }

                            }

                            if (view.getId() == R.id.image_puzzle_three && v.getId() == R.id.image_nine) {
//                                ConstraintLayout oldParent = (ConstraintLayout) view.getParent();
//                                oldParent.removeView(view);
//                                ConstraintLayout newParent = (ConstraintLayout) v;
//                                image_nine.setVisibility(View.GONE);
//                                newParent.addView(view);
                                view.setAlpha(0.5f);
                                v.setAlpha(0.5f);

                                try {
                                    tts.speak(nineTextVoice, TextToSpeech.QUEUE_ADD, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                changer += 1;
                                if (changer == 3) {
                                    if (puzzle == 4) {

                                        puzzle = 0;
                                        SharedPreferences myPuzzle = getSharedPreferences(puzzleName + "_MyAwesomePuzzle", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = myPuzzle.edit();
                                        editor.putInt("puzzle", puzzle);
                                        editor.commit();

                                    }

                                    puzzle += 1;

                                    SharedPreferences myPuzzle = getSharedPreferences(puzzleName + "_MyAwesomePuzzle", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = myPuzzle.edit();
                                    editor.putInt("puzzle", puzzle);
                                    editor.commit();

                                    //restart activity
                                    startActivity(getIntent());
                                    finish();
                                    overridePendingTransition(0, 0);
                                }
                            }
                        }

                        if (puzzle == 4) {
                            // 8,9,3
                            if (view.getId() == R.id.image_puzzle_one && v.getId() == R.id.image_eight) {
//                                ConstraintLayout oldParent = (ConstraintLayout) view.getParent();
//                                oldParent.removeView(view);
//                                ConstraintLayout newParent = (ConstraintLayout) v;
//                                image_eight.setVisibility(View.GONE);
//                                newParent.addView(view);
                                view.setAlpha(0.5f);
                                v.setAlpha(0.5f);

                                try {
                                    tts.speak(eightTextVoice, TextToSpeech.QUEUE_ADD, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                changer += 1;
                                if (changer == 3) {
                                    if (puzzle == 4) {

                                        container.setAlpha(0.5f);
//                                    bgimage.setAlpha(0.5f);
                                        winlevelsound.start();

//                                    ImageView ll = findViewById(R.id.ll);
                                        ll.setVisibility(View.VISIBLE);
                                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                        final Handler GreenBackNavigate = new Handler();
                                        GreenBackNavigate.postDelayed(() -> {

                                            Intent IDB = new Intent(CustomMainPuzzleActivity.this, CustomMainActivity.class);
                                            //IDB.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            IDB.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(IDB);
                                        }, 5000);

                                        puzzle = 0;
                                        SharedPreferences myPuzzle = getSharedPreferences(puzzleName + "_MyAwesomePuzzle", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = myPuzzle.edit();
                                        editor.putInt("puzzle", puzzle);
                                        editor.commit();

                                    }

                                    puzzle += 1;

                                    SharedPreferences myPuzzle = getSharedPreferences(puzzleName + "_MyAwesomePuzzle", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = myPuzzle.edit();
                                    editor.putInt("puzzle", puzzle);
                                    editor.commit();


                                }

                            }

                            if (view.getId() == R.id.image_puzzle_two && v.getId() == R.id.image_nine) {
//                                ConstraintLayout oldParent = (ConstraintLayout) view.getParent();
//                                oldParent.removeView(view);
//                                ConstraintLayout newParent = (ConstraintLayout) v;
//                                image_nine.setVisibility(View.GONE);
//                                newParent.addView(view);
                                view.setAlpha(0.5f);
                                v.setAlpha(0.5f);

                                try {
                                    tts.speak(nineTextVoice, TextToSpeech.QUEUE_ADD, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                changer += 1;
                                if (changer == 3) {
                                    if (puzzle == 4) {

                                        container.setAlpha(0.5f);
//                                    bgimage.setAlpha(0.5f);
                                        winlevelsound.start();

//                                    ImageView ll = findViewById(R.id.ll);
                                        ll.setVisibility(View.VISIBLE);
                                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                        final Handler GreenBackNavigate = new Handler();
                                        GreenBackNavigate.postDelayed(() -> {

                                            Intent IDB = new Intent(CustomMainPuzzleActivity.this, CustomMainActivity.class);
                                            //IDB.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            IDB.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(IDB);
                                        }, 5000);

                                        puzzle = 0;
                                        SharedPreferences myPuzzle = getSharedPreferences(puzzleName + "_MyAwesomePuzzle", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = myPuzzle.edit();
                                        editor.putInt("puzzle", puzzle);
                                        editor.commit();

                                    }

                                    puzzle += 1;

                                    SharedPreferences myPuzzle = getSharedPreferences(puzzleName + "_MyAwesomePuzzle", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = myPuzzle.edit();
                                    editor.putInt("puzzle", puzzle);
                                    editor.commit();


                                }

                            }


                            if (view.getId() == R.id.image_puzzle_three && v.getId() == R.id.image_three) {
//                                ConstraintLayout oldParent = (ConstraintLayout) view.getParent();
//                                oldParent.removeView(view);
//                                ConstraintLayout newParent = (ConstraintLayout) v;
//                                image_three.setVisibility(View.GONE);
//                                newParent.addView(view);
                                view.setAlpha(0.5f);
                                v.setAlpha(0.5f);

                                try {
                                    tts.speak(thirdTextVoice, TextToSpeech.QUEUE_ADD, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                changer += 1;
                                if (changer == 3) {
                                    if (puzzle == 4) {

//                                    container.setAlpha(0.5f);
                                        winlevelsound.start();

//                                    ImageView ll = findViewById(R.id.ll);
                                        ll.setVisibility(View.VISIBLE);
                                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                        final Handler GreenBackNavigate = new Handler();
                                        GreenBackNavigate.postDelayed(() -> {

                                            Intent IDB = new Intent(CustomMainPuzzleActivity.this, CustomMainActivity.class);
                                            //IDB.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            IDB.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(IDB);
                                        }, 5000);

                                        puzzle = 0;
                                        SharedPreferences myPuzzle = getSharedPreferences(puzzleName + "_MyAwesomePuzzle", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = myPuzzle.edit();
                                        editor.putInt("puzzle", puzzle);
                                        editor.commit();

                                    }

                                    puzzle += 1;

                                    SharedPreferences myPuzzle = getSharedPreferences(puzzleName + "_MyAwesomePuzzle", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = myPuzzle.edit();
                                    editor.putInt("puzzle", puzzle);
                                    editor.commit();


                                }
                            }
                        }
                    }




                    break;
            }
            return true;
        }
    };

    public ArrayList<CustomMainPuzzle> loadData() {
        customMainPuzzles = new Gson().fromJson(myPuzzle.getString("customPuzzles", null), TYPE);

        return customMainPuzzles;
    }

    public void saveData (ArrayList<CustomMainPuzzle> list) {
        new Thread(() -> {
            SharedPreferences.Editor editor = myPuzzle.edit();

            boolean isComplete = false;

            for(int i = 0; i < list.size(); i++)
            {
                if(list.get(i) != null)
                {
                    isComplete = true;
                }
                else
                {
                    isComplete = false;
                    break;
                }
            }

            editor.putString("customPuzzles", new Gson().toJson(list));
            editor.putBoolean("isComplete", isComplete);
            editor.apply();
        }).start();

        customMainPuzzles = list;
    }
///
    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();
        //Stop the Media Player Service to stop sound
        stopService(new Intent(CustomMainPuzzleActivity.this, AlarmSoundService.class));
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

    private void setAlpha()
    {
        if(puzzle == 1)
        {

        }
    }

}
