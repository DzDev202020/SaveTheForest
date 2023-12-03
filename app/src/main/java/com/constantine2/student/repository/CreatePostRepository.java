package com.constantine2.student.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.constantine2.student.model.Client;
import com.constantine2.student.model.Fire;
import com.constantine2.student.model.Post;
import com.constantine2.student.retrofit.PostApi;
import com.constantine2.student.retrofit.RetrofitApi;
import com.constantine2.student.viewModel.CreatePostViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostRepository {


    PostApi postApi;

    MutableLiveData<Integer> postState;

    Client client;
    Fire fire;

    public CreatePostRepository() {

        postApi = RetrofitApi.getPostApi();
        postState = new MutableLiveData<>(CreatePostViewModel.IDEAL_POST);
    }

    public LiveData<Integer> getPostState() {
        return postState;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setFire(Fire fire) {
        this.fire = fire;
    }

    public Fire getFire() {
        return fire;
    }


    public void createPost(String content, String pictureUri, MediaType mediaType) {
        Log.e("TAG", "createPost: ");
        postState.setValue(CreatePostViewModel.ON_SAVE_POST);
        Post post = new Post();

        post.setId(0);
        post.setId_c(client.getId());
        post.setId_f(fire.getId());

//        post.setrClient(client);
//        post.setrFire(fire);
        post.setContent(content);


        Log.e("TAG", "createPost: content:"+content);
        Log.e("TAG", "createPost: c: "+client.getId());
        Log.e("TAG", "createPost: f: " + fire.getId());

        File localFile = new File(pictureUri);
        RequestBody requestBodyPicture = RequestBody.create(mediaType, localFile);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("picture", "dummy_name", requestBodyPicture);

        postApi.createPostPicture(filePart).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                Log.e("TAG", "createPost: ");
                if (response.isSuccessful()) {
                    Log.e("TAG", "createPost: picture isSuccessful");
                    String path = response.body();
                    post.setPhotoFilePath(path);

                    postApi.createPostOnly(post).enqueue(new Callback<Post>() {
                        @Override
                        public void onResponse(@NotNull Call<Post> call, @NotNull Response<Post> response) {
                            Log.e("TAG", "createPost: ");
                            if (response.isSuccessful()) {
                                Log.e("TAG", "createPost: post isSuccessful");
                                localFile.delete();
                                postState.setValue(CreatePostViewModel.DONE_SAVE_POST);
                            } else {
                                Log.e("TAG", "createPost: post no isSuccessful ");
                                postState.setValue(CreatePostViewModel.FAIL_SAVE_POST);
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<Post> call, @NotNull Throwable t) {
                            Log.e("TAG", "createPost: onFailure post: " + t.getMessage());
                            postState.setValue(CreatePostViewModel.FAIL_SAVE_POST);
                        }
                    });
                } else {
                    Log.e("TAG", "createPost:  picture no isSuccessful ");
                    postState.setValue(CreatePostViewModel.FAIL_SAVE_POST);
                }
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                postState.setValue(CreatePostViewModel.FAIL_SAVE_POST);
                Log.e("TAG", "createPost: onFailure picture: " + t.getMessage());
            }
        });
//        postApi.createPost(post, filePart).enqueue(new Callback<Post>() {
//            @Override
//            public void onResponse(@NotNull Call<Post> call, @NotNull Response<Post> response) {
//                if (response.isSuccessful()) {
//                    localFile.delete();
//                    postState.setValue(CreatePostViewModel.DONE_SAVE_POST);
//                } else {
//                    postState.setValue(CreatePostViewModel.FAIL_SAVE_POST);
//                }
//            }
//
//            @Override
//            public void onFailure(@NotNull Call<Post> call, @NotNull Throwable t) {
//                postState.setValue(CreatePostViewModel.FAIL_SAVE_POST);
//            }
//        });

    }
}
