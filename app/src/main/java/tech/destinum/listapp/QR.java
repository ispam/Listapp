package tech.destinum.listapp;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.facebook.login.LoginManager;

public class QR extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        mImageView = (ImageView) findViewById(R.id.imageViewQR);
        Bitmap bitmap = getIntent().getParcelableExtra("qrcode");
        mImageView.setImageBitmap(bitmap);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
