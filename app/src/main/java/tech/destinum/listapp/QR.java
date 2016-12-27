package tech.destinum.listapp;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class QR extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        mImageView = (ImageView) findViewById(R.id.imageViewQR);
        Bitmap bitmap = getIntent().getParcelableExtra("qrcode");
        mImageView.setImageBitmap(bitmap);
    }
}
