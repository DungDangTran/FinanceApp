package com.example.simpleapp;

import android.app.Activity;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.simpleapp.database.Goods;
import com.example.simpleapp.database.GoodsDatabase;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {
    private EditText edtGoodsName;
    private EditText edtGoodsPrice;
    private RecyclerView rcvGoods;
    private CheckBox cbDung, cbAnh;
    private View view;
    private Button btnAdd;
    private GoodsAdapter goodsAdapter;
    private List<Goods> listGoods;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
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
        view =  inflater.inflate(R.layout.fragment_add, container, false);
        initUi();
        goodsAdapter = new GoodsAdapter();
        listGoods = new ArrayList<>();
        listGoods = GoodsDatabase.getInstance(getActivity()).goodsDAO().getListGoods();
        goodsAdapter.setData(listGoods);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcvGoods.setLayoutManager(linearLayoutManager);
        rcvGoods.setAdapter(goodsAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                try {
                    addGoods();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        return  view;
    }
    private void initUi() {
        edtGoodsName = view.findViewById(R.id.name_of_goods_fragment);
        edtGoodsPrice = view.findViewById(R.id.price_of_goods_fragment);
        btnAdd = view.findViewById(R.id.add_button_fragment);
        rcvGoods = view.findViewById(R.id.rcv_goods);
        cbDung = view.findViewById(R.id.cb_dung);
        cbAnh = view.findViewById(R.id.cb_anh);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addGoods() throws ParseException {
        String name = setStringName(edtGoodsName.getText().toString().trim());
        String price = edtGoodsPrice.getText().toString().trim();

        boolean blDung = cbDung.isChecked();
        boolean blAnh = cbAnh.isChecked();
        String personAdd;
        if (blAnh != blDung) {
            if (blDung) personAdd = "Đăng Dũng";
            else personAdd = "Trung Anh";
        } else {
            personAdd = "";
        }

        String date = new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault()).format(new Date());
        if (blAnh == true && blDung == true) {
            cbAnh.setChecked(false);
            cbDung.setChecked(false);
            Toast.makeText(getActivity(), "Bạn chỉ được chọn 1 người thêm!", Toast.LENGTH_LONG).show();
            return;
        }
        if (name.equals("") || price.equals("") || personAdd.equals("")) {
            Toast.makeText(getActivity(), "Bạn điền thiếu thông tin!", Toast.LENGTH_LONG).show();
            return;
        }

        Goods goods = new Goods(name, Integer.parseInt(price), personAdd, date);
        GoodsDatabase.getInstance(getActivity()).goodsDAO().insertGoods(goods);
        Toast.makeText(getActivity(), "Thêm thành công!", Toast.LENGTH_LONG).show();

        edtGoodsName.setText("");
        edtGoodsPrice.setText("");
        cbAnh.setChecked(false);
        cbDung.setChecked(false);
        listGoods = GoodsDatabase.getInstance(getActivity()).goodsDAO().getListGoods();
        goodsAdapter.setData(listGoods);
    }

    private String setStringName(String s) {
        String tg = "";
        if (s.length() != 0) {
            if (s.charAt(0) >= 'a' && s.charAt(0) <= 'z')
                tg += String.valueOf(s.charAt(0)).toUpperCase() + "";
            else tg += s.charAt(0) + "";
            for (int i = 1; i < s.length(); i++) {

                tg += s.charAt(i) + "";
            }
        }
        return tg;
    }
}