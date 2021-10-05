package com.kali_corporation.freeonlinetranslation.fee.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
//import com.wong.adapter.ModuleSelectionAdapter;
//import com.wong.bean.ModuleBean;
//import com.wong.bean.ModuleBeanWrapper;
//import com.wong.decor.ItemOffsetDecoration;

import com.kali_corporation.freeonlinetranslation.fee.adapter.ModuleSelectionAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.kali_corporation.freeonlinetranslation.R;
import com.kali_corporation.freeonlinetranslation.fee.bean.modul.ModuleBean;
import com.kali_corporation.freeonlinetranslation.fee.bean.modul.ModuleBeanWrapper;
import com.kali_corporation.freeonlinetranslation.fee.decor.ItemOffsetDecoration;

public class ModuleSelectionActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTVBack;
    private TextView mTVTitle;
    private RecyclerView mRVModule;

    private ModuleSelectionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_selection);
        initViews();
        initListeners();
        initData();
    }

    private void initViews() {
        mTVBack = findViewById(R.id.tv_start);
        mTVTitle = findViewById(R.id.tv_title);
        mRVModule = findViewById(R.id.rv_module);

    }

    private void initListeners() {
        mTVBack.setOnClickListener(this);
    }

    private void initData() {
        mTVTitle.setText(getString(R.string.module_selection));
        ModuleBeanWrapper wrapper = initListData();

        if (wrapper != null) {
            adapter = new ModuleSelectionAdapter(wrapper);
            adapter.setOnItemClickListener(new ModuleSelectionAdapter.OnItemClickListener() {
                @Override
                public void onClick(View view, ModuleBean bean) {
                    Intent intent = new Intent();
                    intent.putExtra("module",bean);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });
            GridLayoutManager manager = new GridLayoutManager(this, 6,GridLayoutManager.VERTICAL,false);
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

                @Override
                public int getSpanSize(int position) {
                    switch (adapter.getItemViewType(position)) {
                        case ModuleSelectionAdapter.TITLE_ITEM:
                            return 6;
                        case ModuleSelectionAdapter.GALLERY_ITEM:
                            return 2;
                        default:
                            return 5;
                    }
                }
            });
            mRVModule.setAdapter(adapter);
            mRVModule.setLayoutManager(manager);
            float space = getResources().getDimension(R.dimen.space);
            mRVModule.addItemDecoration(new ItemOffsetDecoration((int) space));

        }

    }

    private ModuleBeanWrapper initListData() {
        try {
            InputStream is = getAssets().open("module.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String str = null;
            StringBuilder stringBuilder = new StringBuilder();
            while ((str = bufferedReader.readLine()) != null) {
                stringBuilder.append(str);
            }
            return JSON.parseObject(stringBuilder.toString(), ModuleBeanWrapper.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start:
                finish();
                break;
        }
    }
}
