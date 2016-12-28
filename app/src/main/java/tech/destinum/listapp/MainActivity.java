package tech.destinum.listapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView mName;
    private DBHelper mDBHelper;
    private ArrayAdapter<String> mAdapter;
    private ListView mListView;
    private Button mButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);

        mDBHelper = new DBHelper(this);
        mAuth = FirebaseAuth.getInstance();

        mListView = (ListView) findViewById(R.id.list);
        mButton = (Button) findViewById(R.id.buttonQR);

        loadItemList();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> itemList = mDBHelper.getItemList();
                MultiFormatWriter multiFW = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFW.encode(String.valueOf(itemList), BarcodeFormat.QR_CODE, 200, 200);
                    BarcodeEncoder enconder = new BarcodeEncoder();
                    Bitmap bitmap =  enconder.createBitmap(bitMatrix);
                    Intent intent = new Intent(getApplicationContext(), QR.class);
                    intent.putExtra("qrcode", bitmap);
                    startActivity(intent);

                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null){
                    goAuthenticate();
                }
            }
        };

//        if (AccessToken.getCurrentAccessToken() == null){
//            goAuthenticate();
//        }
    }

    private void loadItemList() {
        ArrayList<String> itemList = mDBHelper.getItemList();
        if (mAdapter==null){
            mAdapter = new ArrayAdapter<String>(this, R.layout.row, R.id.item_name, itemList);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(itemList);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void goAuthenticate(){
        Intent mLogin = new Intent(MainActivity.this, Authentication.class);
        mLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mLogin);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                break;
            case R.id.addNewItem:
                final EditText itemEditText = new EditText(this);
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Add New Item")
                        .setView(itemEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String item = String.valueOf(itemEditText.getText());
                                mDBHelper.insertNewItem(item);
                                loadItemList();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                alertDialog.show();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    public void deleteItem(View view){
        TextView itemTextView = (TextView) findViewById(R.id.item_name);
        String item = String.valueOf(itemTextView.getText());
        mDBHelper.deleteItem(item);
        loadItemList();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
