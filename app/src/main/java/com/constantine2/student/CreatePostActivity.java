package com.constantine2.student;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.constantine2.student.databinding.ActivityCreatePostBinding;
import com.constantine2.student.model.Client;
import com.constantine2.student.model.Fire;
import com.constantine2.student.viewModel.CreatePostViewModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;

public class CreatePostActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1998;
    ActivityCreatePostBinding bind;
    CreatePostViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(CreatePostViewModel.class);

        bind = ActivityCreatePostBinding.inflate(getLayoutInflater());

        hideProgress();

        Intent i = getIntent();
        Bundle b = i.getExtras();

        if (b != null && viewModel.waitCLientFire()) {

            Client client = (Client) b.getSerializable("client");

            if(client == null)
                Log.e("TAG", "onCreate: client: null" );

            else{
                Log.e("TAG", "onCreate: client: "+client.getId() );
                Log.e("TAG", "onCreate: client: "+client.getFirstName() );
                Log.e("TAG", "onCreate: client: "+client.getLastName() );
            }
            Fire fire = (Fire) b.getSerializable("fire");
            if(fire == null)
                Log.e("TAG", "onCreate: fire: null" );
            else{
                Log.e("TAG", "onCreate: client: "+fire.getId() );
                Log.e("TAG", "onCreate: client: "+fire.getLatitude() );
                Log.e("TAG", "onCreate: client: "+fire.getLongitude() );
            }


            if (client != null)
                viewModel.setClient(client);

            if (fire != null) {
                viewModel.setFire(fire);
                String address = fire.getStreet() + ", " + fire.getCity() + ", " + fire.getCountry() + ", " + fire.getZipcode();
                Log.e("TAG", "onCreate: fire address "+ address );
                bind.fireAddress.setText(address);

            }
        } else {

            Fire fire = viewModel.getFire();
            if (fire != null) {
                viewModel.setFire(fire);
                String address = fire.getStreet() + ", " + fire.getCity() + ", " + fire.getCountry() + ", " + fire.getZipcode();
                Log.e("TAG", "onCreate: fire address "+ address );
                bind.fireAddress.setText(address);
            }
        }

        bind.fireImage.setOnClickListener(v -> dispatchTakePictureIntent());
        bind.createPost.setOnClickListener(v -> createPost());

        viewModel.getPostState().observe(this, integer -> {
            switch (integer) {
                case CreatePostViewModel.IDEAL_POST:
                    break;
                case CreatePostViewModel.ON_SAVE_POST:
                    hideSaveButton();
                    showProgress();
                    disableInput();
                    break;
                case CreatePostViewModel.FAIL_SAVE_POST:
                    hideProgress();
                    showSaveButton();
                    enableInput();
                    break;
                case CreatePostViewModel.DONE_SAVE_POST:
                    finish();
                    break;
            }
        });
        setContentView(bind.getRoot());
    }

    private void disableInput() {
        bind.fireImage.setEnabled(false);
        bind.editTextContent.setEnabled(false);
    }

    private void enableInput() {
        bind.fireImage.setEnabled(true);
        bind.editTextContent.setEnabled(true);
    }

    private void hideSaveButton() {
        bind.createPost.setVisibility(View.GONE);
    }

    private void showSaveButton() {
        bind.createPost.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        bind.progressBar.setVisibility(View.GONE);
        bind.progressText.setVisibility(View.GONE);
    }

    private void showProgress() {
        bind.progressBar.setVisibility(View.VISIBLE);
        bind.progressText.setVisibility(View.VISIBLE);
    }

    private void createPost() {
        Log.e("TAG", "createPost: ");
        String content = bind.editTextContent.getEditText().getText().toString();
        if (content.equals("")) {
            Toast.makeText(getBaseContext(), R.string.should_write_some_content, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!gotPicture) {
            Toast.makeText(getBaseContext(), R.string.should_capture_picture, Toast.LENGTH_SHORT).show();
            return;
        }
        viewModel.createPost(content, picturePath, MediaType.parse(getContentResolver().getType(photoURI)));

    }

    String picturePath;
    boolean gotPicture;
    Uri photoURI;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                picturePath = photoFile.getPath();

                photoURI = FileProvider.getUriForFile(this,
                        "com.constantine2.student.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                return;
            }
            Toast.makeText(getBaseContext(), R.string.cant_find_camera, Toast.LENGTH_SHORT).show();
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                "picture",  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                gotPicture = true;
                File file = new File(picturePath);
                Picasso.get()
                        .load(file)
                        .into(bind.fireImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                Log.e("TAG", "onSuccess: ");
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e("TAG", "onError: " + e.getMessage());
                            }
                        });
            } else {
                gotPicture = false;
                Toast.makeText(getBaseContext(), R.string.picture_error, Toast.LENGTH_SHORT).show();
            }

        }
    }
}