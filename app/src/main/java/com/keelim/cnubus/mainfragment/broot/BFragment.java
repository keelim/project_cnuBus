package com.keelim.cnubus.mainfragment.broot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.keelim.cnubus.R;

public class BFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BViewModel homeViewModel = ViewModelProviders.of(this).get(BViewModel.class);
        View root = inflater.inflate(R.layout.fragment_broot, container);

        ArrayAdapter<CharSequence> arrayAdapterA = ArrayAdapter.createFromResource(getActivity(), R.array.bList,
                android.R.layout.simple_list_item_1);
        ListView listView = container.findViewById(R.id.b_listview);
        listView.setAdapter(arrayAdapterA);

        return root;
    }
}