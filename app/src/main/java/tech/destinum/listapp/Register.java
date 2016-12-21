package tech.destinum.listapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class Register extends AppCompatActivity {

    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private Button mRegister;
    private TextView mAlreadyRegistered;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mName = (EditText) findViewById(R.id.editTextName);
        mEmail = (EditText) findViewById(R.id.editTextEmail);
        mPassword = (EditText) findViewById(R.id.editTextPassword);
        mRegister = (Button) findViewById(R.id.buttonRegister);
        mAlreadyRegistered = (TextView) findViewById(R.id.textView_already_registered);

        mAlreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Authentication.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });

        if (mName.getText().toString().length() <= 0){
            mName.setError("Name Can't Be Blank");
        } else {
            mName.setError(null);
        }

        if (mPassword.getText().toString().length() <= 0){
            mPassword.setError("Password Can't Be Blank");
        } else {
            mPassword.setError(null);
        }

        if (mEmail.getText().toString().length() <= 0){
            mEmail.setError("Email Can't be Blank");
        } else {
            mEmail.setError(null);
        }

    }

    private void startRegister() {

        final String name = mName.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            final ProgressDialog progressDialog = ProgressDialog.show(Register.this, "Please Wait...", "Processing...", true);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    progressDialog.dismiss();

                    if (task.isSuccessful()){

                        String user_id = mAuth.getCurrentUser().getUid();
                        mDatabaseReference.child(user_id);

                        DatabaseReference current_user_id = mDatabaseReference.child(user_id);
                        current_user_id.child("name").setValue(name);

                        Intent intent = new Intent(Register.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    } else {
                        Log.e("ERROR", task.getException().getMessage().toString());
                        Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }


                }
            });

        }
    }
}
