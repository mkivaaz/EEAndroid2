package androidee.com.eeandroid;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int IC_REQUEST_CODE = 1234;
    private static final int BILL_REQUEST_CODE = 1235;
    private Uri ImgURI;

    LinearLayout nameBox, phoneBox, emailBox, bankBox, accBox, photoBox, billBox;
    EditText nameET, phoneET, emailET, usernameET, passET, bankET, accET, photoET, billET;
    Button photoBtn, billBtn, signupBtn, loginBtn;
    RadioButton rbSignup, rbLogin;
    RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameBox = (LinearLayout) findViewById(R.id.nameBox);
        phoneBox = (LinearLayout) findViewById(R.id.phoneBox);
        emailBox = (LinearLayout) findViewById(R.id.emailBox);
        bankBox = (LinearLayout) findViewById(R.id.bankBox);
        accBox = (LinearLayout) findViewById(R.id.accBox);
        photoBox = (LinearLayout) findViewById(R.id.photoBox);
        billBox = (LinearLayout) findViewById(R.id.billBox);

        nameET = (EditText) findViewById(R.id.nameET);
        phoneET = (EditText) findViewById(R.id.phoneET);
        emailET = (EditText) findViewById(R.id.emailET);
        usernameET = (EditText) findViewById(R.id.usernameET);
        passET = (EditText) findViewById(R.id.passET);
        bankET = (EditText) findViewById(R.id.bankET);
        accET = (EditText) findViewById(R.id.accountET);
        photoET = (EditText) findViewById(R.id.icET);
        billET = (EditText) findViewById(R.id.billET);

        photoBtn = (Button) findViewById(R.id.btnIC);
        billBtn = (Button) findViewById(R.id.btnBill);
        signupBtn = (Button) findViewById(R.id.btnSignUp);
        loginBtn = (Button) findViewById(R.id.btnLogin);

        rg = (RadioGroup) findViewById(R.id.rbGroup);
        rbLogin = (RadioButton) findViewById(R.id.rbLogin);
        rbSignup = (RadioButton) findViewById(R.id.rbSignUp);
        rbSignup.setChecked(true);
        loginBtn.setVisibility(View.GONE);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                RadioButton rb = radioGroup.findViewById(i);
                if(null != rb && i > -1){
                    if (rb.getText().toString().equals(rbLogin.getText().toString())){
                        nameBox.setVisibility(View.GONE);
                        phoneBox.setVisibility(View.GONE);
                        emailBox.setVisibility(View.GONE);
                        bankBox.setVisibility(View.GONE);
                        accBox.setVisibility(View.GONE);
                        photoBox.setVisibility(View.GONE);
                        billBox.setVisibility(View.GONE);
                        signupBtn.setVisibility(View.GONE);
                        loginBtn.setVisibility(View.VISIBLE);
                    }else{
                        loginBtn.setVisibility(View.GONE);
                        nameBox.setVisibility(View.VISIBLE);
                        phoneBox.setVisibility(View.VISIBLE);
                        emailBox.setVisibility(View.VISIBLE);
                        bankBox.setVisibility(View.VISIBLE);
                        accBox.setVisibility(View.VISIBLE);
                        photoBox.setVisibility(View.VISIBLE);
                        billBox.setVisibility(View.VISIBLE);
                        signupBtn.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent,"Select Image"),IC_REQUEST_CODE);
            }
        });

        billBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent,"Select Image"),BILL_REQUEST_CODE);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = usernameET.getText().toString().trim();
                String Password = passET.getText().toString().trim();

                if(Username.equals("User") && Password.equals("password")){
                    startActivity(new Intent(getBaseContext(),HomeActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(getBaseContext(),"Username or Password is Password is Wrong",Toast.LENGTH_SHORT).show();
                }
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Do Something Here....
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IC_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null ){
            ImgURI = data.getData();

            File file = new File(ImgURI.toString());
            photoET.setText(String.valueOf(System.currentTimeMillis()));
        }
        if (requestCode == BILL_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null ){
            ImgURI = data.getData();

            File file = new File(ImgURI.toString());
            billET.setText(String.valueOf(System.currentTimeMillis()));
        }
    }

    public String getImageExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
