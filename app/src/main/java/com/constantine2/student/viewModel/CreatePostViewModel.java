package com.constantine2.student.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.constantine2.student.model.Client;
import com.constantine2.student.model.Fire;
import com.constantine2.student.repository.CreatePostRepository;

import okhttp3.MediaType;

public class CreatePostViewModel extends ViewModel {

    public final static int IDEAL_POST = 0;
    public final static int ON_SAVE_POST = 1;
    public final static int FAIL_SAVE_POST = 2;
    public final static int DONE_SAVE_POST = 3;

    CreatePostRepository repository;

    public CreatePostViewModel() {
        repository = new CreatePostRepository();
    }

    public void setClient(Client client) {
        repository.setClient(client);
    }

    public void setFire(Fire fire) {
        repository.setFire(fire);
    }

    public void createPost(String content, String picturePath,MediaType mediaType) {
        repository.createPost(content, picturePath,mediaType);
    }

    public Fire getFire() {
        return repository.getFire();
    }

    public LiveData<Integer> getPostState() {
        return repository.getPostState();
    }

    Boolean onWait= true;
    public boolean waitCLientFire() {
        return  onWait;
    }

    public void donewait(){
        onWait= false;
    }
}
