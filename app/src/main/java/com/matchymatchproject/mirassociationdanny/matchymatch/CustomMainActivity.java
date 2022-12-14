package com.matchymatchproject.mirassociationdanny.matchymatch;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matchymatchproject.mirassociationdanny.matchymatch.DBHelper.DBHelper;
import com.matchymatchproject.mirassociationdanny.matchymatch.Utils.AlarmSoundService;
import com.matchymatchproject.mirassociationdanny.matchymatch.Utils.Utils;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CustomMainActivity extends AppCompatActivity {


    private static final int SELECT_PHOTO = 7777;
    private static final int CAM_REQUEST = 1313;
    DBHelper dbHelper;
    ImageView invisible_imageView;

    ImageView image_one,image_two,image_three,image_four,image_five,image_six,image_seven,image_eight,image_nine,image_ten;
    TextView TV_ONE,TV_TWO,TV_THREE,TV_FOUR,TV_FIVE,TV_SIX,TV_SEVEN,TV_EIGHT,TV_NINE,TV_TEN;

    // select photo popup
    ImageView circle_cancel_select_popup;
    Button gallery_btn,camera_btn;
    Dialog photoDialog;

    //rename and upload popup
    ImageView circle_cancel_rename_popup;

    Button rename_uploadphoto_btn,puzzle_name_btn,rename_delete_btn,puzzle_play_btn;
    Dialog renameDialog;

    // save photo popup
    ImageView circle_cancel_save_popup;
    Button cancel_btn,save_image_btn;
    Dialog saveDialog;

    // warning photo popup
    ImageView circle_cancel_warning_popup;
    Dialog warningDialog;

    // create puzzles popup
    ImageView circle_cancel_create_popup;
    EditText create_name_EDT;
    Button create_done_btn;
    Dialog createDialog;

    ImageView homebtn;
    private static final String LEN_PREFIX = "Count_";
    private static final String VAL_PREFIX = "IntValue_";



    private ArrayList<CustomPuzzle> customPuzzles;
    private static final Type TYPE = new TypeToken<ArrayList<CustomPuzzle>>() {}.getType();
    SharedPreferences myPuzzle, myCustomPuzzle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_custom_main);

        //isi me add karo aur isi ko serialize/deserialize karo.
//        customPuzzles = CustomPuzzleManager.getInstance().getCustomPuzzles();


        myPuzzle = this.getSharedPreferences("MyPuzzle", Context.MODE_PRIVATE);
//
//
//        puzzleNames = getFromPrefs("puzzleNames", myPuzzle);
//        dbHelper = new DBHelper(this);
//        puzzleImages = dbHelper.GetBitmaps();

        customPuzzles = loadData();

        homebtn = findViewById(R.id.homebtn);

        homebtn.setOnClickListener(v -> {
            Intent CM = new Intent(CustomMainActivity.this , MainActivity.class);
            CM.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(CM);
        });

        photoDialog = new Dialog(this);
        warningDialog = new Dialog(this);
        renameDialog = new Dialog(this);
        saveDialog = new Dialog(this);
        createDialog = new Dialog(this);
        invisible_imageView  = findViewById(R.id.save_image_uri);


        // declear images
        image_one   = findViewById(R.id.image_one);
        image_two   = findViewById(R.id.image_two);
        image_three = findViewById(R.id.image_three);
        image_four  = findViewById(R.id.image_four);
        image_five  = findViewById(R.id.image_five);
        image_six   = findViewById(R.id.image_six);

        //declear textview
        TV_ONE   = findViewById(R.id.puzzle_name_one);
        TV_TWO   = findViewById(R.id.puzzle_name_two);
        TV_THREE = findViewById(R.id.puzzle_name_three);
        TV_FOUR  = findViewById(R.id.puzzle_name_four);
        TV_FIVE  = findViewById(R.id.puzzle_name_five);
        TV_SIX   = findViewById(R.id.puzzle_name_six);
//        TV_NINE   = findViewById(R.id.puzzle_name_nine);
//        TV_TEN  = findViewById(R.id.puzzle_name_ten);


        //-------------------------------------this code for changing puzzle name start here....................

        //change puzzle name in first textView


        ArrayList<String> puzzleNames = new ArrayList<>();
        puzzleNames.add("Yellow");
        puzzleNames.add("LightBlue");
        puzzleNames.add("Green");
        puzzleNames.add("Pink");
        puzzleNames.add("Orange");
        puzzleNames.add("Purple");

        if(customPuzzles != null) {

            setText(puzzleNames);

        }
        setImage(puzzleNames);

        //----------------------------------------end for fetching------------------------------------------

        image_one.setOnClickListener(v -> {

            int id = v.getId();

            renamePopup(id, 0);
        });

        image_two.setOnClickListener(v -> {

            int id = v.getId();
            renamePopup(id, 1);
        });

        image_three.setOnClickListener(v -> {

            int id = v.getId();
            renamePopup(id, 2);
        });

        image_four.setOnClickListener(v -> {

            int id = v.getId();
            renamePopup(id, 3);
        });

        image_five.setOnClickListener(v -> {

            int id = v.getId();
            renamePopup(id, 4);
        });

        image_six.setOnClickListener(v -> {

            int id = v.getId();
            renamePopup(id, 5);
        });

    }

    private void setImage(ArrayList<String> puzzleNames)
    {

        ArrayList<ImageView> imageViews = new ArrayList<>();
        imageViews.add(image_one);
        imageViews.add(image_two);
        imageViews.add(image_three);
        imageViews.add(image_four);
        imageViews.add(image_five);
        imageViews.add(image_six);

        for(int i = 0; i < imageViews.size(); i++) {

            try {
                if (imageViews.get(i).getDrawable() == null || MainPuzzleActivity.areDrawablesIdentical(imageViews.get(i).getDrawable() ,getResources().getDrawable(R.drawable.image_placeholder))) {

//                byte[] data = dbHelper.GetBitmapByName("firstimage");
                    Bitmap bitmap = null;
                    myCustomPuzzle = this.getSharedPreferences(puzzleNames.get(i)+"_MyAwesomePuzzle", Context.MODE_PRIVATE);
                    if (customPuzzles != null && customPuzzles.get(i) != null && !customPuzzles.get(i).getBackgroundImage().equalsIgnoreCase("")) {
                        byte[] b = Base64.decode(customPuzzles.get(i).getBackgroundImage(), Base64.DEFAULT);
                        bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    }

                    if (bitmap != null) {
//                    Bitmap bitmap = Utils.getImage(data);
                        imageViews.get(i).setImageBitmap(bitmap);
                    } else {
                        imageViews.get(i).setImageDrawable(getDrawable(R.drawable.image_placeholder));
                    }

                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
                imageViews.get(i).setImageDrawable(getDrawable(R.drawable.image_placeholder));
            }
        }
    }

    private void setText(ArrayList<String> puzzleNames)
    {
        ArrayList<TextView> textViews = new ArrayList<>();
        textViews.add(TV_ONE);
        textViews.add(TV_TWO);
        textViews.add(TV_THREE);
        textViews.add(TV_FOUR);
        textViews.add(TV_FIVE);
        textViews.add(TV_SIX);

        for (int i = 0; i < customPuzzles.size(); i++) {
            try {

                myCustomPuzzle = this.getSharedPreferences(puzzleNames.get(i)+"_MyAwesomePuzzle", Context.MODE_PRIVATE);
                if (customPuzzles.get(i) != null) {
                    textViews.get(i).setText(customPuzzles.get(i).getName());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }



    public ArrayList<CustomPuzzle> loadData() {
        customPuzzles = new Gson().fromJson(myPuzzle.getString("customPuzzles", null), TYPE);

        return customPuzzles;
    }

    public void saveData (ArrayList<CustomPuzzle> list) {
        SharedPreferences.Editor editor = myPuzzle.edit();

        editor.putString("customPuzzles", new Gson().toJson(list));
        editor.commit();
        customPuzzles = list;
    }


    //------------------------------start here warningPopup-------------------------------
    public void warningPopup(){

        warningDialog.setContentView(R.layout.image_warning);
        circle_cancel_warning_popup = warningDialog.findViewById(R.id.circle_cancel_warning_popup);
        circle_cancel_warning_popup.setOnClickListener(v -> warningDialog.dismiss());


        warningDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        warningDialog.show();

    }

    //---------------------------warning popup end here....................................

    //------------------------------start here savePopup-------------------------------
    public void savePopup(final int id){

        saveDialog.setContentView(R.layout.save_btn_popup);
        circle_cancel_save_popup = saveDialog.findViewById(R.id.circle_cancel_save_popup);
        circle_cancel_save_popup.setOnClickListener(v -> saveDialog.dismiss());
        cancel_btn = saveDialog.findViewById(R.id.cancelbtn);
        cancel_btn.setOnClickListener(v -> saveDialog.dismiss());
        save_image_btn = saveDialog.findViewById(R.id.savebtn);
        save_image_btn.setOnClickListener(v -> {

            if (image_one.getId() == id) {


                if (image_one.getDrawable() == null || MainPuzzleActivity.areDrawablesIdentical(image_one.getDrawable() ,getResources().getDrawable(R.drawable.image_placeholder))) {

                    saveImage(id);

                }else {
                    Toast.makeText(CustomMainActivity.this, "Delete Previous Image", Toast.LENGTH_LONG).show();
                }

            }if (image_two.getId() == id){

                if (image_two.getDrawable() == null || MainPuzzleActivity.areDrawablesIdentical(image_two.getDrawable() ,getResources().getDrawable(R.drawable.image_placeholder))){

                    saveImage(id);

                }else {
                    Toast.makeText(CustomMainActivity.this, "Delete Previous Image", Toast.LENGTH_LONG).show();
                }


            } if (image_three.getId() == id){

                if (image_three.getDrawable() == null || MainPuzzleActivity.areDrawablesIdentical(image_three.getDrawable() ,getResources().getDrawable(R.drawable.image_placeholder))){

                    saveImage(id);

                }else {
                    Toast.makeText(CustomMainActivity.this, "Delete Previous Image", Toast.LENGTH_LONG).show();
                }

            } if (image_four.getId() == id){

                if (image_four.getDrawable() == null || MainPuzzleActivity.areDrawablesIdentical(image_four.getDrawable() ,getResources().getDrawable(R.drawable.image_placeholder))){

                    saveImage(id);
                }else {
                    Toast.makeText(CustomMainActivity.this, "Delete Previous Image", Toast.LENGTH_LONG).show();
                }

            } if (image_five.getId() == id){

                if (image_five.getDrawable() == null || MainPuzzleActivity.areDrawablesIdentical(image_five.getDrawable() ,getResources().getDrawable(R.drawable.image_placeholder))){

                    saveImage(id);
                }else {
                    Toast.makeText(CustomMainActivity.this, "Delete Previous Image", Toast.LENGTH_LONG).show();
                }


                //Toast.makeText(CustomMainActivity.this, "five "+id, Toast.LENGTH_SHORT).show();
            } if (image_six.getId() == id){

                if (image_six.getDrawable() == null || MainPuzzleActivity.areDrawablesIdentical(image_six.getDrawable() ,getResources().getDrawable(R.drawable.image_placeholder))){

                    saveImage(id);

                }else {
                    Toast.makeText(CustomMainActivity.this, "Delete Previous Image", Toast.LENGTH_LONG).show();
                }

            }

            saveDialog.dismiss();

        });

        saveDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        saveDialog.show();

    }

    private void saveImage(int id)
    {
        final Bitmap bitmap = ((BitmapDrawable) invisible_imageView.getDrawable()).getBitmap();

        int max = 1300;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        if (max >= w && max >= h) {
//                        dbHelper.addBitmap(pick_image_first, Utils.getBytes(bitmap));
            CustomPuzzle customPuzzle = new CustomPuzzle();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            bitmap.recycle();
            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            customPuzzle.setBackgroundImage(encodedImage);
            createPopup(id, customPuzzle);
            saveDialog.dismiss();
            Toast.makeText(CustomMainActivity.this, "Save Success", Toast.LENGTH_LONG).show();
        }else {
            warningPopup();
        }
    }

    //---------------------------save popup end here....................................


    //--------------------------------------start here renamepopup work-------------------------------------
    public void renamePopup(final int id, int index){

        renameDialog.setContentView(R.layout.rename_with_upload_photo);
        circle_cancel_rename_popup = renameDialog.findViewById(R.id.circle_cancel_rename_popup);
        circle_cancel_rename_popup.setOnClickListener(v -> renameDialog.dismiss());
        rename_uploadphoto_btn = renameDialog.findViewById(R.id.upload_photo_btn);
        rename_uploadphoto_btn.setOnClickListener(v -> {

            photoPopup(id);
            renameDialog.dismiss();
        });
        puzzle_name_btn = renameDialog.findViewById(R.id.puzzle_name_btn);
        puzzle_name_btn.setOnClickListener(v -> {

            createPopup(id, customPuzzles.get(index));
            renameDialog.dismiss();
        });

        rename_delete_btn = renameDialog.findViewById(R.id.delete_btn);
        rename_delete_btn.setOnClickListener(v -> {



            if (image_one.getId() == id) {

                if (image_one.getDrawable() == null || MainPuzzleActivity.areDrawablesIdentical(image_one.getDrawable() ,getResources().getDrawable(R.drawable.image_placeholder))) {

                    photoPopup(id);

                }else {

//                    String dele = dbHelper.DeleteImage("firstimage");
                    Toast.makeText(CustomMainActivity.this, "Image Deleted", Toast.LENGTH_LONG).show();

//                        Utils.removePuzzleName("MyFirstPuzzle", CustomMainActivity.this, "keyFirst");
//                    puzzleNames.add(0, "");
//                    myPuzzleEditor = myPuzzle.edit();
//                    storeValues("puzzleNames", puzzleNames, myPuzzleEditor);
                    customPuzzles.set(0, null);
                    saveData(customPuzzles);
                    // Restart the Activity
                    startActivity(getIntent());
                    finish();
                    overridePendingTransition( 0, 0);
                }

            }if (image_two.getId() == id){

                if (image_two.getDrawable() == null ||  MainPuzzleActivity.areDrawablesIdentical(image_two.getDrawable() ,getResources().getDrawable(R.drawable.image_placeholder))){

                    photoPopup(id);

                }else {

//                    String dele = dbHelper.DeleteImage("secondimage");
                    Toast.makeText(CustomMainActivity.this, "Image Deleted", Toast.LENGTH_LONG).show();

//                        Utils.removePuzzleName("MySecondPuzzle", CustomMainActivity.this, "keySecond");
//                    puzzleNames.add(1, "");
//                    myPuzzleEditor = myPuzzle.edit();
//                    storeValues("puzzleNames", puzzleNames, myPuzzleEditor);
                    customPuzzles.set(1, null);
                    saveData(customPuzzles);
                    // Restart the Activity
                    startActivity(getIntent());
                    finish();
                    overridePendingTransition( 0, 0);
                }



            } if (image_three.getId() == id){

                if (image_three.getDrawable() == null ||  MainPuzzleActivity.areDrawablesIdentical(image_three.getDrawable() ,getResources().getDrawable(R.drawable.image_placeholder))){

                    photoPopup(id);

                }else {

//                    String dele = dbHelper.DeleteImage("thirdimage");
                    Toast.makeText(CustomMainActivity.this, "Image Deleted", Toast.LENGTH_LONG).show();

//                        Utils.removePuzzleName("MyThirdPuzzle", CustomMainActivity.this, "keyThird");
//                    puzzleNames.add(2, "");
//                    myPuzzleEditor = myPuzzle.edit();
//                    storeValues("puzzleNames", puzzleNames, myPuzzleEditor);
                    customPuzzles.set(2, null);
                    saveData(customPuzzles);
                    // Restart the Activity
                    startActivity(getIntent());
                    finish();
                    overridePendingTransition( 0, 0);
                }

            } if (image_four.getId() == id){

                if (image_four.getDrawable() == null ||  MainPuzzleActivity.areDrawablesIdentical(image_four.getDrawable() ,getResources().getDrawable(R.drawable.image_placeholder))){

                    photoPopup(id);

                }else {

//                    String dele = dbHelper.DeleteImage("fourtimage");
                    Toast.makeText(CustomMainActivity.this, "Image Deleted", Toast.LENGTH_LONG).show();

//                        Utils.removePuzzleName("MyFourtPuzzle", CustomMainActivity.this, "keyFourt");
//                    puzzleNames.add(3, "");
//                    myPuzzleEditor = myPuzzle.edit();
//                    storeValues("puzzleNames", puzzleNames, myPuzzleEditor);
                    customPuzzles.set(3, null);
                    saveData(customPuzzles);
                    // Restart the Activity
                    startActivity(getIntent());
                    finish();
                    overridePendingTransition( 0, 0);
                }

            } if (image_five.getId() == id){

                if (image_five.getDrawable() == null ||  MainPuzzleActivity.areDrawablesIdentical(image_five.getDrawable() ,getResources().getDrawable(R.drawable.image_placeholder))){

                    photoPopup(id);

                }else {

//                    String dele = dbHelper.DeleteImage("fiveimage");
                    Toast.makeText(CustomMainActivity.this, "Image Deleted", Toast.LENGTH_LONG).show();

//                        Utils.removePuzzleName("MyFivePuzzle", CustomMainActivity.this, "keyFive");
//                    puzzleNames.add(4, "");
//                    myPuzzleEditor = myPuzzle.edit();
//                    storeValues("puzzleNames", puzzleNames, myPuzzleEditor);
                    customPuzzles.set(4, null);
                    saveData(customPuzzles);
                    // Restart the Activity
                    startActivity(getIntent());
                    finish();
                    overridePendingTransition( 0, 0);
                }


            } if (image_six.getId() == id){

                if (image_six.getDrawable() == null ||  MainPuzzleActivity.areDrawablesIdentical(image_six.getDrawable() ,getResources().getDrawable(R.drawable.image_placeholder))){

                    photoPopup(id);

                }else {

//                    String dele = dbHelper.DeleteImage("siximage");
                    Toast.makeText(CustomMainActivity.this, "Image Deleted", Toast.LENGTH_LONG).show();

//                        Utils.removePuzzleName("MySixPuzzle", CustomMainActivity.this, "keySix");
//                    puzzleNames.add(5, "");
//                    myPuzzleEditor = myPuzzle.edit();
//                    storeValues("puzzleNames", puzzleNames, myPuzzleEditor);
                    customPuzzles.set(5, null);
                    saveData(customPuzzles);
                    // Restart the Activity
                    startActivity(getIntent());
                    finish();
                    overridePendingTransition( 0, 0);
                }

            }


            renameDialog.dismiss();
        });
        puzzle_play_btn = renameDialog.findViewById(R.id.play_puzzle_btn);
        puzzle_play_btn.setOnClickListener(v -> {

            if (image_one.getId() == id) {

                startActivity(R.drawable.yellowbackground, "Yellow", image_one, id);


            }if (image_two.getId() == id){

                startActivity(R.drawable.bluebackground, "LightBlue", image_two, id);


            } if (image_three.getId() == id){

                startActivity(R.drawable.greenbackground, "Green", image_three, id);

            } if (image_four.getId() == id){

                startActivity(R.drawable.pinkbackground, "Pink", image_four, id);

            } if (image_five.getId() == id){

                startActivity(R.drawable.orangebackground, "Orange", image_five, id);

            } if (image_six.getId() == id){

                startActivity(R.drawable.purplebackground, "Purple", image_six, id);

            }

        });

        renameDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        renameDialog.show();

    }


    private void startActivity(int bg, String puzzle, ImageView imageView, int id)
    {

        if (imageView.getDrawable() == null ||  MainPuzzleActivity.areDrawablesIdentical(imageView.getDrawable() ,getResources().getDrawable(R.drawable.image_placeholder))) {
            photoPopup(id);
            Toast.makeText(CustomMainActivity.this, "Insert Image", Toast.LENGTH_LONG).show();
        }else {

            Intent intent = new Intent(CustomMainActivity.this, CustomMainPuzzleActivity.class);
            intent.putExtra("bgColor", bg);
            intent.putExtra("puzzleName", puzzle);
            intent.putExtra("isCreating", true);

            startActivity(intent);


        }
    }

    //---------------------------rename popup end here....................................

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

    //------------------------create puzzles popup start here......................................

    public void createPopup(final int id, CustomPuzzle customPuzzle){

        createDialog.setContentView(R.layout.custom_create_popup);
        create_name_EDT = createDialog.findViewById(R.id.create_name_EDT);
        circle_cancel_create_popup = createDialog.findViewById(R.id.circle_cancel_create_popup);
        circle_cancel_create_popup.setOnClickListener(v -> {
            // Restart the Activity
            startActivity(getIntent());
            finish();
            overridePendingTransition( 0, 0);

            createDialog.dismiss();
        });
        create_done_btn = createDialog.findViewById(R.id.create_done_btn);
        create_done_btn.setOnClickListener(v -> {

            if(customPuzzles == null)
            {
                customPuzzles = new ArrayList<>();

                customPuzzles.add(null);
                customPuzzles.add(null);
                customPuzzles.add(null);
                customPuzzles.add(null);
                customPuzzles.add(null);
                customPuzzles.add(null);
            }

            if (image_one.getId() == id) {

                String firstPuzzleName = create_name_EDT.getText().toString();

//                    SharedPreferences myFirst = getSharedPreferences("MyFirstPuzzle", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = myFirst.edit();
//                    editor.putString("keyFirst", firstPuzzleName);
//                    editor.commit();

                customPuzzle.setName(firstPuzzleName);
                customPuzzles.set(0,customPuzzle);

//                puzzleNames.add(0, firstPuzzleName);

            }if (image_two.getId() == id){

                String secondPuzzleName = create_name_EDT.getText().toString();

//                    SharedPreferences mySecond = getSharedPreferences("MySecondPuzzle", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = mySecond.edit();
//                    editor.putString("keySecond", secondPuzzleName);
//                    editor.commit();
                customPuzzle.setName(secondPuzzleName);
                customPuzzles.set(1,customPuzzle);
//                puzzleNames.add(1, secondPuzzleName);

            } if (image_three.getId() == id){

                String thirdPuzzleName = create_name_EDT.getText().toString();

//                    SharedPreferences myThird = getSharedPreferences("MyThirdPuzzle", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = myThird.edit();
//                    editor.putString("keyThird", thirdPuzzleName);
//                    editor.commit();
                customPuzzle.setName(thirdPuzzleName);
                customPuzzles.set(2,customPuzzle);
//                puzzleNames.add(2, thirdPuzzleName);

            } if (image_four.getId() == id){

                String fourtPuzzleName = create_name_EDT.getText().toString();

//                    SharedPreferences myFourt = getSharedPreferences("MyFourtPuzzle", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = myFourt.edit();
//                    editor.putString("keyFourt", fourtPuzzleName);
//                    editor.commit();
                customPuzzle.setName(fourtPuzzleName);
                customPuzzles.set(3,customPuzzle);
//                puzzleNames.add(3, fourtPuzzleName);

            } if (image_five.getId() == id){

                String fivePuzzleName = create_name_EDT.getText().toString();

//                    SharedPreferences myFive = getSharedPreferences("MyFivePuzzle", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = myFive.edit();
//                    editor.putString("keyFive", fivePuzzleName);
//                    editor.commit();
                customPuzzle.setName(fivePuzzleName);
                customPuzzles.set(4,customPuzzle);
//                puzzleNames.add(4, fivePuzzleName);

            } if (image_six.getId() == id){

                String sixPuzzleName = create_name_EDT.getText().toString();

//                    SharedPreferences mySix = getSharedPreferences("MySixPuzzle", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = mySix.edit();
//                    editor.putString("keySix", sixPuzzleName);
//                    editor.commit();
                customPuzzle.setName(sixPuzzleName);
                customPuzzles.set(5,customPuzzle);
//                puzzleNames.add(5, sixPuzzleName);

            }


//            myPuzzleEditor = myPuzzle.edit();
//            storeValues("puzzleNames", puzzleNames, myPuzzleEditor);
            saveData(customPuzzles);

            // Restart the Activity
            startActivity(getIntent());
            finish();
            overridePendingTransition( 0, 0);

        });

        createDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        createDialog.show();

    }



    //---------------------------create puzzles popup end here....................................

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null)
        {
            Uri pickedimage = data.getData();
            if(pickedimage != null)
                invisible_imageView.setImageURI(pickedimage);


        }if (requestCode == CAM_REQUEST && resultCode == RESULT_OK && data != null)
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            invisible_imageView.setImageBitmap(bitmap);

        }
    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    @Override
    protected void onPause() {

        super.onPause();
        //Stop the Media Player Service to stop sound
        stopService(new Intent(CustomMainActivity.this, AlarmSoundService.class));
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
