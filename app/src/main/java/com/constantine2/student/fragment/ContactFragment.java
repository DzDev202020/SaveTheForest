package com.constantine2.student.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;

import com.constantine2.student.R;
import com.constantine2.student.databinding.FragmentContactsBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class ContactFragment extends Fragment {


    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentContactsBinding bind;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bind = FragmentContactsBinding.inflate(inflater);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.simple_text, R.id.contact);

        List<String> contacts = new ArrayList<String>();
        contacts.add("Name1 /Phone: 065412378");
        contacts.add("Name2 /Phone: 054412348");
        contacts.add("Name3 /Phone: 078412388");
        contacts.add("Name4 /Phone: 065489378");

        adapter.addAll(contacts);

        bind.contactList.setAdapter(adapter);


        return bind.getRoot();

    }
}