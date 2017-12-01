package androidee.com.eeandroid;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int BILL_REQUEST_CODE = 1235;
    private static final String URL_FOR_LOGIN = "https://4c249bf0.ngrok.io/users/sign_in.json";
    private static final String TAG = ".MainActivity";
    private Uri ImgURI;

    LinearLayout nameBox, phoneBox, emailBox, bankBox, accBox, icBox, billBox;
    EditText nameET, phoneET, emailET, passET, bankET, accET, icET, billET;
    Button billBtn, signupBtn, loginBtn;
    RadioButton rbSignup, rbLogin;
    RadioGroup rg;
    Bitmap img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameBox = (LinearLayout) findViewById(R.id.nameBox);
        phoneBox = (LinearLayout) findViewById(R.id.phoneBox);
        emailBox = (LinearLayout) findViewById(R.id.emailBox);
        bankBox = (LinearLayout) findViewById(R.id.bankBox);
        accBox = (LinearLayout) findViewById(R.id.accBox);
        icBox = (LinearLayout) findViewById(R.id.photoBox);
        billBox = (LinearLayout) findViewById(R.id.billBox);

        nameET = (EditText) findViewById(R.id.nameET);
        phoneET = (EditText) findViewById(R.id.phoneET);
        emailET = (EditText) findViewById(R.id.emailET);
        passET = (EditText) findViewById(R.id.passET);
        bankET = (EditText) findViewById(R.id.bankET);
        accET = (EditText) findViewById(R.id.accountET);
        icET = (EditText) findViewById(R.id.icET);
        billET = (EditText) findViewById(R.id.billET);

        billBtn = (Button) findViewById(R.id.btnBill);
        signupBtn = (Button) findViewById(R.id.btnSignUp);
        loginBtn = (Button) findViewById(R.id.btnLogin);

        rg = (RadioGroup) findViewById(R.id.rbGroup);
        rbLogin = (RadioButton) findViewById(R.id.rbLogin);
        rbSignup = (RadioButton) findViewById(R.id.rbSignUp);
        rbLogin.setChecked(true);

        emailET.setText("silva_vino@hotmail.com");
        passET.setText("dddddddD9_");

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
                        loginBtn.setVisibility(View.VISIBLE);
                    }else{
                        loginBtn.setVisibility(View.GONE);
                        nameBox.setVisibility(View.VISIBLE);
                        phoneBox.setVisibility(View.VISIBLE);
                        bankBox.setVisibility(View.VISIBLE);
                        accBox.setVisibility(View.VISIBLE);
                        icBox.setVisibility(View.VISIBLE);
                        billBox.setVisibility(View.VISIBLE);
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
                signUpData.add(nameET.getText().toString().trim());
                signUpData.add(emailET.getText().toString().trim());
                signUpData.add(passET.getText().toString().trim());
                signUpData.add(phoneET.getText().toString().trim());
                signUpData.add(bankET.getText().toString().trim());
                signUpData.add(accET.getText().toString().trim());
                signUpData.add(icET.getText().toString().trim());

                Log.d("arrayItems", signUpData.get(0).toString());


                // Do Something Here....
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BILL_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null ){
            ImgURI = data.getData();

            try {
                img = MediaStore.Images.Media.getBitmap(this.getContentResolver(),ImgURI);
                billET.setText(String.valueOf(System.currentTimeMillis()));
            } catch (IOException e) {
                e.printStackTrace();
            }
//            File file = new File(ImgURI.toString());

        }
    }

    public String getImageExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    public void loginUser(final String email, final String password){
        String cancel_req_tag = "login";

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

        AppSingleton.getInstance(getBaseContext()).addToRequestQueue(strReq,cancel_req_tag);

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
    private void signUp(final Bitmap bitmap){
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL_FOR_LOGIN, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    JSONObject obj = new JSONObject(new String(response.data));
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Add params here
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("img", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        Volley.newRequestQueue(getBaseContext()).add(volleyMultipartRequest);
    }
}
