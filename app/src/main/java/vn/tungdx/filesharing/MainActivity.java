package vn.tungdx.filesharing;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static final String AUTHORITIES_NAME = "vn.tungdx.filesharing.fileprovider";
    static final String FOLDER_NAME = "docs";
    static final String FILE_NAME = "data.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.save_file).setOnClickListener(this);
        findViewById(R.id.share_file_internal).setOnClickListener(this);
        findViewById(R.id.share_file_cache).setOnClickListener(this);
        findViewById(R.id.share_file_external).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_file:
                saveFileToMultipleLocations(this);
                break;
            case R.id.share_file_internal:
                File imagePath = new File(getFilesDir(), FOLDER_NAME);
                File fileSharing = new File(imagePath, FILE_NAME);
                share(fileSharing);
                break;
            case R.id.share_file_cache:
                imagePath = new File(getCacheDir(), FOLDER_NAME);
                fileSharing = new File(imagePath, FILE_NAME);
                share(fileSharing);
                break;
            case R.id.share_file_external:
                imagePath = new File(getExternalFilesDir(null), FOLDER_NAME);
                fileSharing = new File(imagePath, FILE_NAME);
                share(fileSharing);
                break;
        }
    }

    private void saveFile(String data, File out) {
        try {
            FileWriter writer = new FileWriter(out);
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void share(File fileSharing) {
        Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), AUTHORITIES_NAME, fileSharing);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(contentUri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    private File getFile(File root, String fileName) {
        File folder = new File(root, FOLDER_NAME);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return new File(folder, fileName);
    }

    private void saveFileToMultipleLocations(Context context) {
        String content = "Share files using FileProvider!";
        //save to external storage
        saveFile(content, getFile(context.getExternalFilesDir(null), FILE_NAME));

        //save to internal storage
        saveFile(content, getFile(context.getFilesDir(), FILE_NAME));

        //save to cache directory
        saveFile(content, getFile(context.getCacheDir(), FILE_NAME));
    }
}