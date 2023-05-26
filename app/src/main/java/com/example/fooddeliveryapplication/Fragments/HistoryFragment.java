package com.example.fooddeliveryapplication.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddeliveryapplication.Adapters.HistoryAdapter;
import com.example.fooddeliveryapplication.Model.HistoryProduct;
import com.example.fooddeliveryapplication.Model.InfoCurrentProduct;
import com.example.fooddeliveryapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView rcvHistory;
    private HistoryAdapter historyAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<HistoryProduct> mListHistoryProducts;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CurrentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_history, container, false);

        rcvHistory = view.findViewById(R.id.rcv_History_product);
        
        return view;
    }

    private List<HistoryProduct> getListHistoryProducts() {
        List<HistoryProduct> list = new ArrayList<>();

        list.add(new HistoryProduct(R.drawable.pop_1, "Product1", "State", "2000"));
        list.add(new HistoryProduct(R.drawable.pop_1, "Product2", "State", "3000"));
        list.add(new HistoryProduct(R.drawable.pop_1, "Product3", "State", "4000"));


        return list;
    }
}