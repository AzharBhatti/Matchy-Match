package com.matchymatchproject.mirassociationdanny.matchymatch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.DragEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matchymatchproject.mirassociationdanny.matchymatch.Utils.AlarmSoundService;
import com.matchymatchproject.mirassociationdanny.matchymatch.Utils.Common;
import com.matchymatchproject.mirassociationdanny.matchymatch.Utils.Constants;
import com.matchymatchproject.mirassociationdanny.matchymatch.Utils.Shaker;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

import static com.matchymatchproject.mirassociationdanny.matchymatch.Utils.Utils.getUriToResource;

public class CustomMainPuzzlePlayActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler{

//    ArrayList<Integer> puzzleAssets, puzzleAssetsChange, puzzleAssetsSave;
//    ArrayList<String> puzzleSounds;
    int score = 0;
    int changer = 0;
    private static final Integer GREY = Color.parseColor("#D2D2D2");
    ImageView imageOne, imageTwo, imageThree, imageFour, imageFive, imageSix, imageSeven, imageEight, imageNine, imageTen, DRAGOne, DRAGtwo, DRAGThree, invisible_imageview;
    TextToSpeech tts;
    ConstraintLayout container;
    MediaPlayer winlevelsound;
    SimpleDraweeView ll;
    Random random;
    private String puzzle_name = "";
    private static final String LEN_PREFIX = "Count_";
    private static final String VAL_PREFIX = "IntValue_";
    //    private int value1, value2;
    private BillingProcessor bp;
    ImageView homebutton;
    ArrayList<CustomMainPuzzle> customMainPuzzles, customMainPuzzlesChange, customMainPuzzlesSave;
    private static final Type TYPE = new TypeToken<ArrayList<CustomMainPuzzle>>() {}.getType();
    SharedPreferences myPuzzle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.custom_puzzle);

        puzzle_name = getIntent().getExtras().getString("puzzleName");
        myPuzzle = this.getSharedPreferences(puzzle_name+"_MyAwesomePuzzle", Context.MODE_PRIVATE);
        customMainPuzzles = loadData();
        ConstraintLayout parent = findViewById(R.id.parent);
        Drawable background = getResources().getDrawable(getIntent().getExtras().getInt("bgColor"));
        parent.setBackground(background);

        bp = BillingProcessor.newBillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnWJ5AbDt+NZ/X8WznbxOzt1/l8cFBzGRBx6mvdyB0Ab0OqgTqvgY96TqhGfKDYNmI9CdrxSTDGQN7eKvESVztN/O3quQVAse7xTA5jqH5Taj4rTE3960M0eGoUXPILjDmqObtFGvcBRpg3C0XOeRyZWG57U4JF3LmpWExaNNLnA7QSoJQ6yoMajNkvnMvmwYa9+CkrbjgMhHITEzOWgsaCSINADlDXXF0Pg/V31Kmstr1qts+6Q76BuAHLr+fR9ikbMckXRhbpmOqU4+Cd6UJGnsKTxNEbhtJGUQT1gmEAKQdE+zvLWzd2RnCJC5YcP+0HM6Ugeb0hn4912fS/jE5wIDAQAB", this); // doesn't bind
        bp.initialize(); // binds

        if(customMainPuzzles != null) {
            for (int i = 0; i < customMainPuzzles.size(); i++) {
                ImageView imageView = (ImageView) parent.getChildAt(i);
                Bitmap bitmap = null;
                if (customMainPuzzles != null && customMainPuzzles.get(i) != null && !customMainPuzzles.get(i).getImage().equalsIgnoreCase("")) {
                    byte[] b = Base64.decode(customMainPuzzles.get(i).getImage(), Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                }

                if (bitmap != null) {
//                    Bitmap bitmap = Utils.getImage(data);
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setImageDrawable(getDrawable(R.drawable.image_placeholder));
                }
            }
        }

        random = new Random();
        winlevelsound = MediaPlayer.create(CustomMainPuzzlePlayActivity.this,R.raw.kidscheering);
        tts = new TextToSpeech(getApplicationContext(), status -> {
            if(status!=TextToSpeech.ERROR){

                tts.setLanguage(Constants.speechLocale);
                tts.setPitch((float) 1.1);
                tts.setSpeechRate((float) 0.6);
            }
        });
        ll = findViewById(R.id.ll);

        DraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setUri("android.resource://com.matchymatchproject.mirassociationdanny.matchymatch/drawable/puzzle_end_gif.gif")
                .setUri(getUriToResource(CustomMainPuzzlePlayActivity.this, R.drawable.puzzle_end_gif))
//                .setUri("https://media4.giphy.com/avatars/100soft/WahNEDdlGjRZ.gif")
                .setAutoPlayAnimations(true)
                .build();
        ll.setController(controller);

        Handler forWait = new Handler();
        forWait.postDelayed(() -> getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE), 4000);


        imageOne = findViewById(R.id.image_one);
        imageTwo = findViewById(R.id.image_two);
        imageThree = findViewById(R.id.image_three);
        imageFour = findViewById(R.id.image_four);
        imageFive = findViewById(R.id.image_five);
        imageSix = findViewById(R.id.image_six);
        imageSeven = findViewById(R.id.image_seven);
        imageEight = findViewById(R.id.image_eight);
        imageNine = findViewById(R.id.image_nine);
        imageTen = findViewById(R.id.image_ten);
        DRAGOne = findViewById(R.id.image_puzzle_one);
        DRAGtwo = findViewById(R.id.image_puzzle_two);
        DRAGThree = findViewById(R.id.image_puzzle_three);
        invisible_imageview = findViewById(R.id.invisible_imageview);

        container = findViewById(R.id.parent);

        homebutton = findViewById(R.id.homebtn);

        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LH = new Intent(CustomMainPuzzlePlayActivity.this , ParrotActivity.class);
                LH.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(LH);
            }
        });

        score = myPuzzle.getInt("score", 0);

        if(score != 0 && score != 10)
        {
            customMainPuzzlesChange = loadSavedData();
            customMainPuzzlesSave = loadSavedData();

            Bitmap bitmap = null;
            for(int i = 0; i < customMainPuzzles.size(); i++){
                ImageView imageView = (ImageView)parent.getChildAt(i);
                boolean isMatched = false;
                byte[] b = Base64.decode(customMainPuzzles.get(i).getImage(), Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                invisible_imageview.setImageBitmap(bitmap);
                Drawable puzzleAssetDrawable = invisible_imageview.getDrawable();
                for(int j = 0; j < customMainPuzzlesChange.size(); j++)
                {
                    b = Base64.decode(customMainPuzzlesChange.get(j).getImage(), Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    invisible_imageview.setImageBitmap(bitmap);

                    Drawable puzzleAssetChangeDrawable = invisible_imageview.getDrawable();
                    if(areDrawablesIdentical(puzzleAssetDrawable, puzzleAssetChangeDrawable))
                    {
                        isMatched = true;
                        break;
                    }
                    else{
                        isMatched = false;
                    }
                }
                if(!isMatched)
                {
                    imageView.setAlpha(0.5f);
                }
            }

        }
        else {
            customMainPuzzlesChange = loadData();
            customMainPuzzlesSave = loadData();
        }

        int num = random.nextInt(customMainPuzzlesChange.size());

        byte[] b = Base64.decode(customMainPuzzlesChange.get(num).getImage(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        DRAGOne.setImageBitmap(bitmap);
//        value1 = puzzleAssetsChange.get(num);
        customMainPuzzlesChange.remove(num);
        if(customMainPuzzlesChange.size() > 0) {
            num = random.nextInt(customMainPuzzlesChange.size());
            b = Base64.decode(customMainPuzzlesChange.get(num).getImage(), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            DRAGtwo.setImageBitmap(bitmap);
//            value2 = puzzleAssetsChange.get(num);
            customMainPuzzlesChange.remove(num);
        }
        if(customMainPuzzlesChange.size() > 0) {
            num = random.nextInt(customMainPuzzlesChange.size());
            b = Base64.decode(customMainPuzzlesChange.get(num).getImage(), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            DRAGThree.setImageBitmap(bitmap);
//            value2 = puzzleAssetsChange.get(num);
            customMainPuzzlesChange.remove(num);
        }

        imageOne.setOnDragListener(dragListener);
        imageTwo.setOnDragListener(dragListener);
        imageThree.setOnDragListener(dragListener);
        imageFour.setOnDragListener(dragListener);
        imageFive.setOnDragListener(dragListener);
        imageSix.setOnDragListener(dragListener);
        imageSeven.setOnDragListener(dragListener);
        imageEight.setOnDragListener(dragListener);
        imageNine.setOnDragListener(dragListener);
        imageTen.setOnDragListener(dragListener);

        //DRAGOne.setOnLongClickListener(longClickListener);
        //DRAGtwo.setOnLongClickListener(longClickListener);
        DRAGOne.setOnTouchListener(Common.touchListener);
        DRAGtwo.setOnTouchListener(Common.touchListener);
        DRAGThree.setOnTouchListener(Common.touchListener);
        winlevelsound = MediaPlayer.create(CustomMainPuzzlePlayActivity.this,R.raw.kidscheering);



    }


    public static boolean areDrawablesIdentical(Drawable drawableA, Drawable drawableB) {
        Drawable.ConstantState stateA = drawableA.getConstantState();
        Drawable.ConstantState stateB = drawableB.getConstantState();
        // If the constant state is identical, they are using the same drawable resource.
        // However, the opposite is not necessarily true.
        return (stateA != null && stateB != null && stateA.equals(stateB))
                || getBitmap(drawableA).sameAs(getBitmap(drawableB));
    }

    public static Bitmap getBitmap(Drawable drawable) {
        Bitmap result;
        if (drawable instanceof BitmapDrawable) {
            result = ((BitmapDrawable) drawable).getBitmap();
        } else {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            // Some drawables have no intrinsic width - e.g. solid colours.
            if (width <= 0) {
                width = 1;
            }
            if (height <= 0) {
                height = 1;
            }

            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        return result;
    }

    private void onDragSuccess(){
        if(customMainPuzzlesChange.size() > 0) {
            int index = random.nextInt(customMainPuzzlesChange.size());
            byte[] b = Base64.decode(customMainPuzzlesChange.get(index).getImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            invisible_imageview.setImageBitmap(bitmap);

            if (DRAGtwo.getDrawable() != null && !(areDrawablesIdentical(invisible_imageview.getDrawable(), DRAGtwo.getDrawable())) && DRAGThree.getDrawable() != null && !(areDrawablesIdentical(invisible_imageview.getDrawable(), DRAGThree.getDrawable()))) {
                DRAGOne.setImageBitmap(bitmap);
                customMainPuzzlesChange.remove(index);

            } else {
                DRAGOne.setImageDrawable(null);
            }
        }else {
            DRAGOne.setImageDrawable(null);
        }
    }

    View.OnDragListener dragListener = new View.OnDragListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
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

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (!areDrawablesIdentical(((ImageView) view).getDrawable(), ((ImageView) v).getDrawable())) {

                                Shaker shake = new Shaker(v, 0, 15, GREY, Color.RED);
                                shake.shake();
                                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                vibe.vibrate(100);
                                tts.speak(getString(R.string.try_again), TextToSpeech.QUEUE_FLUSH, null);

                            }
                            else {

                                v.setAlpha(0.5f);


                                changer+=1;

                                score += 1;


                                if(view.getId() == R.id.image_puzzle_one)
                                {
                                    if(customMainPuzzlesChange.size() > 0) {
                                        int index = random.nextInt(customMainPuzzlesChange.size());
                                        byte[] b = Base64.decode(customMainPuzzlesChange.get(index).getImage(), Base64.DEFAULT);
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                                        invisible_imageview.setImageBitmap(bitmap);

                                        if (DRAGtwo.getDrawable() != null && !(areDrawablesIdentical(invisible_imageview.getDrawable(), DRAGtwo.getDrawable())) && DRAGThree.getDrawable() != null && !(areDrawablesIdentical(invisible_imageview.getDrawable(), DRAGThree.getDrawable()))) {
                                            DRAGOne.setImageBitmap(bitmap);
                                            customMainPuzzlesChange.remove(index);

                                        } else {
                                            DRAGOne.setImageDrawable(null);
                                        }
                                    }else {
                                        DRAGOne.setImageDrawable(null);
                                    }
                                }
                                else if(view.getId() == R.id.image_puzzle_two)
                                {

                                    if(customMainPuzzlesChange.size() > 0) {
                                        int index = random.nextInt(customMainPuzzlesChange.size());
                                        byte[] b = Base64.decode(customMainPuzzlesChange.get(index).getImage(), Base64.DEFAULT);
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                                        invisible_imageview.setImageBitmap(bitmap);

                                        if (DRAGOne.getDrawable() != null && !(areDrawablesIdentical(invisible_imageview.getDrawable(), DRAGOne.getDrawable())) && DRAGThree.getDrawable() != null && !(areDrawablesIdentical(invisible_imageview.getDrawable(), DRAGThree.getDrawable()))) {
                                            DRAGtwo.setImageBitmap(bitmap);
                                            customMainPuzzlesChange.remove(index);
                                        } else {
                                            DRAGtwo.setImageDrawable(null);
                                        }
                                    } else {
                                        DRAGtwo.setImageDrawable(null);
                                    }
                                }
                                else if(view.getId() == R.id.image_puzzle_three)
                                {

                                    if(customMainPuzzlesChange.size() > 0) {
                                        int index = random.nextInt(customMainPuzzlesChange.size());
                                        byte[] b = Base64.decode(customMainPuzzlesChange.get(index).getImage(), Base64.DEFAULT);
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                                        invisible_imageview.setImageBitmap(bitmap);

                                        if (DRAGOne.getDrawable() != null && !(areDrawablesIdentical(invisible_imageview.getDrawable(), DRAGOne.getDrawable())) && DRAGtwo.getDrawable() != null && !(areDrawablesIdentical(invisible_imageview.getDrawable(), DRAGtwo.getDrawable()))) {
                                            DRAGThree.setImageBitmap(bitmap);
                                            customMainPuzzlesChange.remove(index);
                                        } else {
                                            DRAGThree.setImageDrawable(null);
                                        }
                                    } else {
                                        DRAGThree.setImageDrawable(null);
                                    }
                                }

                                Handler handler = new Handler();

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        ArrayList<CustomMainPuzzle> tempList = new ArrayList<>();

                                        for(int i = 0; i < customMainPuzzles.size(); i++) {
                                            byte[] b = Base64.decode(customMainPuzzles.get(i).getImage(), Base64.DEFAULT);
                                            invisible_imageview.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));
                                            Drawable drawable = invisible_imageview.getDrawable();
                                            if(areDrawablesIdentical(drawable, ((ImageView) v).getDrawable()))
                                            {
                                                tts.speak(customMainPuzzles.get(i).getVoice(),TextToSpeech.QUEUE_FLUSH,null);
                                                for(int j = 0; j < customMainPuzzlesSave.size(); j++)
                                                {
                                                    byte[] bytes = Base64.decode(customMainPuzzlesSave.get(j).getImage(), Base64.DEFAULT);
                                                    invisible_imageview.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                                                    Drawable drawableSave = invisible_imageview.getDrawable();
                                                    if(areDrawablesIdentical(drawableSave,drawable))
                                                    {
                                                        customMainPuzzlesSave.remove(j);
                                                        break;
                                                    }
                                                }
                                                //                                customMainPuzzles.remove(i);
                                                //                                puzzleSounds.remove(i);
                                                tempList.add(customMainPuzzles.get(i));
                                            }
                                        }

                                        customMainPuzzles.removeAll(tempList);
                                        tempList.clear();
                                        saveData(customMainPuzzlesSave);

                                        if(score == 10){

                                            container.setAlpha(0.5f);
                                            //                                    bgimage.setAlpha(0.5f);
                                            winlevelsound.start();

                                            //                                    ImageView ll = findViewById(R.id.ll);
                                            ll.setVisibility(View.VISIBLE);
                                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                            final Handler DuckBackNavigate = new Handler();
                                            DuckBackNavigate.postDelayed(() -> {

                                                Intent IDB = new Intent(CustomMainPuzzlePlayActivity.this,ParrotActivity.class);
                                                //                                          IDB.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                IDB.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(IDB);
                                            }, 5000);

                                            score = 0;
                                            SharedPreferences.Editor editorr = myPuzzle.edit();
                                            editorr.putInt("score", score);
                                            saveData(loadData());
                                            editorr.commit();
                                            // textView.setText("Score : " + score);
                                        }


                                    }
                                });

        //                        editor.commit();


                            }

                        }
                    });
                    break;
            }
            return true;
        }
    };


    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
//        Toast.makeText(this, "Purchased!", Toast.LENGTH_SHORT).show();
//        bp.consumePurchase(productId);
    }

    @Override
    public void onPurchaseHistoryRestored() {
//        Toast.makeText(this, "Already Purchased!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
//        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBillingInitialized() {

    }

    public ArrayList<CustomMainPuzzle> loadData() {
        ArrayList<CustomMainPuzzle> customMainPuzzlesList= new Gson().fromJson(myPuzzle.getString("customPuzzles", null), TYPE);

        return customMainPuzzlesList;
    }
    public ArrayList<CustomMainPuzzle> loadSavedData() {
        ArrayList<CustomMainPuzzle> customMainPuzzlesList = new Gson().fromJson(myPuzzle.getString("customPuzzlesValues", null), TYPE);

        return customMainPuzzlesList;
    }

    public void saveData (ArrayList<CustomMainPuzzle> list) {
        SharedPreferences.Editor editor = myPuzzle.edit();

        if(list!= null) {
            editor.putString("customPuzzlesValues", new Gson().toJson(list));
        }
        else {
            editor.putString("customPuzzlesValues", null);
        }
        editor.putBoolean("isComplete", true);
        editor.putInt("score", score);
        editor.commit();
//        customMainPuzzles = list;
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
