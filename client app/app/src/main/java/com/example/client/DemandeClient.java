package com.example.client;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.client.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;

public class DemandeClient extends AppCompatActivity {
    public static final int CODE_IMG_GALERY=1;
    public static final String SAMPLE_CROP="SampleCropImg";

    private Context context;
    private SessionManager sessionManager;
    CircleImageView profile;
    ImageButton replace;
    Uri uri;
    Bitmap bitmap;
    private RequestQueue queue;
    private MyRequest request;
    public ViewPager viewPager;
    //private SamLocationRequestService samLocationRequestService;
    //RequestOptions option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demande_client);
        //option = new RequestOptions().placeholder(R.drawable.photo_).error(R.drawable.photo_);
        viewPager = findViewById(R.id.view_pager);
        //end

        sessionManager=new SessionManager(this);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        queue = VolleySinglton.getInstance(this).getRequestQueue();
        request=new MyRequest(this,queue);

        profile=(CircleImageView) findViewById(R.id.img_profile);
        replace=(ImageButton) findViewById(R.id.img_plus);
        //Glide.with(getApplicationContext()).load(sessionManager.getClient().getProfileImg()).apply(option).into(profile);

        TextView email=(TextView) findViewById(R.id.email);
        if (sessionManager.isLogged()){
            //email.setText(sessionManager.getEmail());
            Client client;
            client = sessionManager.getClient();

            LoadImage loadImage= new LoadImage(profile);
            loadImage.execute(sessionManager.getClient().getProfileImg());

            email.setText(client.getEmail());
            replace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent()
                            .setAction(Intent.ACTION_GET_CONTENT)
                            .setType("image/*"),CODE_IMG_GALERY);

                }
            });
            /*samLocationRequestService = new SamLocationRequestService(DemandeClient.this, new SamLocationRequestService.SamLocationListener() {
                @Override
                public void onLocationUpdate(Location location, Address address) {
                    Log.i("TAG", "onLocationUpdateLocation: "+location);
                    Log.i("TAG", "onLocationUpdateAddress: "+address);
                    Toast.makeText(DemandeClient.this,"mapped",Toast.LENGTH_SHORT).show();
                }
            },10);

            replace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CropImage.startPickImageActivity(DemandeClient.this);

                }
            });

             */

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10){
            //samLocationRequestService.startLocationUpdates();
        }
        if (requestCode == CODE_IMG_GALERY && resultCode == RESULT_OK){
            Uri imageUri=data.getData();
            if (imageUri != null) {
                startCrop(imageUri);
            }
        }else if(requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK){
            Uri imageResultCrop = UCrop.getOutput(data);

            if (imageResultCrop != null){
                try {
                    bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imageResultCrop);
                    profile.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                uploadImage(ImageToString(bitmap),sessionManager.getClient().getId_User());
                GetLink getLink = new GetLink();
                getLink.execute("https://rzbusinessma.000webhostapp.com/Test/getImg.php?idU="+sessionManager.getClient().getId_User());
                //profile.setImageURI(imageResultCrop);
            }
        }
        /*
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE
                && resultCode == Activity.RESULT_OK
        ) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                uri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

            } else {
                startCrop(imageUri);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (requestCode == RESULT_OK) {
                profile.setImageURI(result.getUri());
            }
        }

         */
    }

    public class GetLink extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String...params){
            try{
                URL url = new URL(params[0]);
                URLConnection con = url.openConnection();
                InputStreamReader inputStreamReader=new InputStreamReader(con.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                JSONObject json = new JSONObject(bufferedReader.readLine());
                String link = json.getString("image").toString().trim();
                return link;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Client client=sessionManager.getClient();
            client.setProfileImg(s);
            sessionManager.insertClient(client);
        }
    }

    private void startCrop(@Nullable Uri uri){
        String destination=SAMPLE_CROP;
        destination+=".jpg";
        UCrop uCrop=UCrop.of(uri,Uri.fromFile(new File(getCacheDir(),destination)));
        uCrop.withAspectRatio(1,1);
        uCrop.withMaxResultSize(450,450);
        uCrop.withOptions(getCropOptions());
        uCrop.start(DemandeClient.this);
    }

    private UCrop.Options getCropOptions(){
        UCrop.Options options=new UCrop.Options();
        options.setCompressionQuality(70);

        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);

        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));

        options.setToolbarTitle("Recorter Image");

        return options;

    }

    public void showDetails(View view){
        Intent intent =new Intent(this,DetailsClient.class);
        startActivity(intent);
        finish();
    }

    private void uploadImage(String image,int id){
        request.UploadProfileImg(image, id, new MyRequest.RegisterCallBack() {
            @Override
            public void onSuccess(String message) {
                Log.i("TAG", "onSuccess: "+message);
            }

            @Override
            public void inputErr(Map<String, String> errors) {
                Log.i("TAG", "inputErr: ");
            }

            @Override
            public void onErr(String message) {
                Log.i("TAG", "onErr: "+message);
            }
        });
    }

    private String ImageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] imgBytes=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }


    private class LoadImage extends AsyncTask<String,Void,Bitmap> {
        CircleImageView imageView;
        public LoadImage(CircleImageView profile) {
            imageView=profile;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlLink=strings[0];
            Bitmap bitmap=null;
            try {
                InputStream inputStream=new java.net.URL(urlLink).openStream();
                bitmap= BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}