package com.google.eeandroid;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidee.com.eeandroid.R;

public class MainActivity extends AppCompatActivity {

    private static final int BILL_REQUEST_CODE = 1235;

    private static final String BASE_URL = "https://b6a69293.ngrok.io";
    private static final String URL_FOR_LOGIN =BASE_URL + "/users/sign_in.json";
    private static final String URL_FOR_SIGNUP = BASE_URL + "/users.json";
    private static final String TAG = ".MainActivity";
    private Uri ImgURI;

    LinearLayout nameBox, phoneBox, emailBox, bankBox, accBox, icBox, billBox, conf_passBox, usernameBox, addBox;
    EditText nameET, phoneET, emailET, passET, accET, icET, billET, conf_passET, usernameET, addET;
    Spinner bankSpinner;
    Button billBtn, signupBtn, loginBtn;
    RadioButton rbSignup, rbLogin;
    RadioGroup rg;
    Bitmap img;
    LoginButton fbLogin;
    CallbackManager callbackManager;

    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        privateKey();
        nameBox = (LinearLayout) findViewById(R.id.nameBox);
        phoneBox = (LinearLayout) findViewById(R.id.phoneBox);
        emailBox = (LinearLayout) findViewById(R.id.emailBox);
        bankBox = (LinearLayout) findViewById(R.id.bankBox);
        accBox = (LinearLayout) findViewById(R.id.accBox);
        icBox = (LinearLayout) findViewById(R.id.photoBox);
        billBox = (LinearLayout) findViewById(R.id.billBox);
        conf_passBox = (LinearLayout) findViewById(R.id.conf_passBox);
        usernameBox = (LinearLayout) findViewById(R.id.usernameBox);
        addBox = (LinearLayout) findViewById(R.id.addBox);

        nameET = (EditText) findViewById(R.id.nameET);
        phoneET = (EditText) findViewById(R.id.phoneET);
        emailET = (EditText) findViewById(R.id.emailET);
        passET = (EditText) findViewById(R.id.passET);
        bankSpinner = (Spinner) findViewById(R.id.bankSpinner);
        accET = (EditText) findViewById(R.id.accountET);
        icET = (EditText) findViewById(R.id.icET);
        billET = (EditText) findViewById(R.id.billET);
        conf_passET = (EditText) findViewById(R.id.conf_passET);
        usernameET = (EditText) findViewById(R.id.usernameET);
        addET = (EditText) findViewById(R.id.addET);

        billBtn = (Button) findViewById(R.id.btnBill);
        signupBtn = (Button) findViewById(R.id.btnSignUp);
        loginBtn = (Button) findViewById(R.id.btnLogin);
        fbLogin = findViewById(R.id.fbLogin);

        rg = (RadioGroup) findViewById(R.id.rbGroup);
        rbLogin = (RadioButton) findViewById(R.id.rbLogin);
        rbSignup = (RadioButton) findViewById(R.id.rbSignUp);

        callbackManager = CallbackManager.Factory.create();
        fbLogin.setReadPermissions(Arrays.asList(
                "public_profile", "email"));

        rbLogin.setChecked(true);
        String[] bank_array = {"CIMB Bank Berhad","Hong Leong Bank Berhad","Affin Bank Berhad","Alliance Bank Malaysia Berhad","AmBank (M) Berhad","Malayan Banking Berhad","Public Bank Berhad","RHB Bank Berhad","Affin Islamic Bank Berhad","Alliance Islamic Bank Berhad","AmIslamic Bank Berhad","Bank Islam Malaysia Berhad","Bank Muamalat Malaysia Berhad","CIMB Islamic Bank Berhad","Hong Leong Islamic Bank Berhad","Maybank Islamic Berhad","Public Islamic Bank Berhad","RHB Islamic Bank Berhad"};
        List<String> banks = new ArrayList<>();
        for (String bank : bank_array) {
            banks.add(bank);
        }

        passET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    Toast.makeText(getBaseContext(),"Password must have 8 characters including at least one Capital,Number,Special Character",Toast.LENGTH_SHORT).show();
                }
            }
        });
        conf_passET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    Toast.makeText(getBaseContext(),"Password must have 8 characters including at least one Capital,Number,Special Character",Toast.LENGTH_SHORT).show();
                }
            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_spinner_dropdown_item,banks);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bankSpinner.setAdapter(dataAdapter);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                RadioButton rb = radioGroup.findViewById(i);
                if(null != rb && i > -1){
                    if (rb.getText().toString().equals(rbLogin.getText().toString())){
                        nameBox.setVisibility(View.GONE);
                        phoneBox.setVisibility(View.GONE);
                        bankBox.setVisibility(View.GONE);
                        accBox.setVisibility(View.GONE);
                        icBox.setVisibility(View.GONE);
                        billBox.setVisibility(View.GONE);
                        signupBtn.setVisibility(View.GONE);
                        conf_passBox.setVisibility(View.GONE);
                        usernameBox.setVisibility(View.GONE);
                        addBox.setVisibility(View.GONE);
                        loginBtn.setVisibility(View.VISIBLE);
                        fbLogin.setVisibility(View.VISIBLE);
                    }else{
                        loginBtn.setVisibility(View.GONE);
                        fbLogin.setVisibility(View.GONE);
                        nameBox.setVisibility(View.VISIBLE);
                        phoneBox.setVisibility(View.VISIBLE);
                        bankBox.setVisibility(View.VISIBLE);
                        accBox.setVisibility(View.VISIBLE);
                        icBox.setVisibility(View.VISIBLE);
                        billBox.setVisibility(View.VISIBLE);
                        conf_passBox.setVisibility(View.VISIBLE);
                        usernameBox.setVisibility(View.VISIBLE);
                        addBox.setVisibility(View.VISIBLE);
                        signupBtn.setVisibility(View.VISIBLE);
                    }
                }
            }
        });



        billBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent,"Select Image"),BILL_REQUEST_CODE);
            }
        });

        fbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    String email = object.getString("email");
                                    String name = object.getString("name");
                                    Toast.makeText(getBaseContext(),name,Toast.LENGTH_SHORT).show();
                                    Log.v("LoginActivity", object.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString().trim();
                String password = passET.getText().toString().trim();

                loginUser(email,password);

            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList signUpData = new ArrayList();
                boolean fieldsOK = validate(new EditText[]{emailET,passET,conf_passET,usernameET,nameET,icET,phoneET,addET,accET});

                if(fieldsOK){
                    signUpData.add(emailET.getText().toString().trim());
                    signUpData.add(passET.getText().toString().trim());
                    signUpData.add(conf_passET.getText().toString().trim());
                    signUpData.add(usernameET.getText().toString().trim());
                    signUpData.add(nameET.getText().toString().trim());
                    signUpData.add(icET.getText().toString().trim());
                    signUpData.add(phoneET.getText().toString().trim());
                    signUpData.add(addET.getText().toString().trim());
                    signUpData.add(bankSpinner.getSelectedItem().toString().trim());
                    signUpData.add(accET.getText().toString().trim());

                    Toast.makeText(getBaseContext(),"Signing Up",Toast.LENGTH_SHORT).show();
                    signUp(img,signUpData);
                }else{
                    Toast.makeText(getBaseContext(),"Please Fill all fields",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BILL_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null ){
            ImgURI = data.getData();

            try {
                img = MediaStore.Images.Media.getBitmap(this.getContentResolver(),ImgURI);
                billET.setText(String.valueOf(System.currentTimeMillis()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public String getImageExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    public void loginUser(final String email, final String password){

        JSONObject params = new JSONObject();
        final JSONObject jsonObject = new JSONObject();
        try {
            params.put("email",email);
            params.put("password",password);
            params.put("remember_me","0");
            jsonObject.put("user",params);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, URL_FOR_LOGIN,jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jObj) {
                try {
                    String status = jObj.getString("status");
                    String email = jObj.getString("user");
                    String usertoken = jObj.getString("token");
                    Log.d("test",jObj.toString());
                    Toast.makeText(getBaseContext(),"Login " + status,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getBaseContext(),HomeActivity.class));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return super.getHeaders();
            }


        };

        Volley.newRequestQueue(getBaseContext()).add(strReq);

    }

    /*
        * The method is taking Bitmap as an argument
        * then it will return the byte[] array for the given bitmap
        * and we will send this array to the server
        * here we are using PNG Compression with 80% quality
        * you can give quality between 0 to 100
        * 0 means worse quality
        * 100 means best quality
        * */
    public byte[] getFileDataFromDrawable(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80,byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    // Sign up volley here
    private void signUp(final Bitmap bitmap, ArrayList signUpData){
        JSONObject params = new JSONObject();
        final JSONObject jsonObject = new JSONObject();
        try {
            params.put("email",signUpData.get(0));
            params.put("password",signUpData.get(1));
            params.put("password_confirmation",signUpData.get(2));
            params.put("username",signUpData.get(3));
            params.put("name",signUpData.get(4));
            params.put("ic",signUpData.get(5));
            params.put("hp_num",signUpData.get(6));
            params.put("address",signUpData.get(7));
            params.put("bank_name",signUpData.get(8));
            params.put("bank_account",signUpData.get(9));
            params.put("verification_document", "data:image/png;base64," + Base64.encodeToString(getFileDataFromDrawable(bitmap),Base64.DEFAULT));

            jsonObject.put("user",params);
            Log.d("test2",jsonObject.getJSONObject("user").getString("verification_document").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, URL_FOR_SIGNUP,jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jObj) {
                try {
                    String status = jObj.getString("status");
                    Log.d("test",jObj.toString());
                    if(status.equals("signed_up")){
                        Toast.makeText(getBaseContext(),"SignUp Successful",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getBaseContext(),HomeActivity.class));
                    }else{
                        JSONObject jsonObject1 = jObj.getJSONObject("errors");
                        Toast.makeText(getBaseContext(),"SignUp Failed",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "SignUp Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return super.getHeaders();
            }


        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getBaseContext()).add(strReq);

    }

    private boolean validate(EditText[] fields){
        for(int i = 0; i < fields.length; i++){
            EditText currentField = fields[i];
            if(currentField.getText().toString().length() <= 0){
                return false;
            }
        }
        return true;
    }

    public void privateKey(){
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);

            for(Signature signature : packageInfo.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:",Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
