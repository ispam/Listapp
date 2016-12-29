package tech.destinum.listapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.facebook.login.LoginManager;

import java.io.File;
import java.util.ArrayList;

public class QR extends AppCompatActivity {

    private ImageView mImageView;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        mImageView = (ImageView) findViewById(R.id.imageViewQR);
        mBitmap = getIntent().getParcelableExtra("qrcode");
        mImageView.setImageBitmap(mBitmap);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.share_options:
                mBitmap = getIntent().getParcelableExtra("qrcode");

                String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                        mBitmap, "Design", null);

                Uri uri = Uri.parse(path);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setPackage("com.whatsapp");
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(share, "Share QR Code"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

