package com.kali_corporation.freeonlinetranslation.fee.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.kali_corporation.freeonlinetranslation.fee.adapter.holder.ModuleItemHolder;
import com.kali_corporation.freeonlinetranslation.fee.adapter.holder.ModuleTitleHolder;
import com.kali_corporation.freeonlinetranslation.fee.bean.modul.ModuleBean;
import com.kali_corporation.freeonlinetranslation.fee.bean.modul.ModuleBeanWrapper;

import java.util.ArrayList;
import java.util.List;
import com.kali_corporation.freeonlinetranslation.R;
public class ModuleSelectionAdapter extends RecyclerView.Adapter {
    public final static int TITLE_ITEM = 0;
    public final static int GALLERY_ITEM = 1;
    private List<ModuleBean> list = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public ModuleSelectionAdapter(ModuleBeanWrapper wrapper) {
        convert(wrapper);
    }

    private void convert(ModuleBeanWrapper wrapper) {
        for (ModuleBean moduleBean : wrapper.getModules()) {
            list.add(moduleBean);
            for (ModuleBean bean : wrapper.getData()) {
                if (moduleBean.getModuleName().equals(bean.getModuleName())) {
                    list.add(bean);
                }
            }
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TITLE_ITEM:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.module_title, parent, false);
                return new ModuleTitleHolder(view);
            default:
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.module_item, parent, false);
                return new ModuleItemHolder(view1);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case TITLE_ITEM:
                ModuleTitleHolder moduleTitleHolder = (ModuleTitleHolder)holder;
                TextView textView = moduleTitleHolder.itemView.findViewById(R.id.tv_module_title);
                textView.setText(list.get(position).getModuleName());
                break;
            case GALLERY_ITEM:
                ModuleItemHolder moduleItemHolder = (ModuleItemHolder)holder;
                TextView textView1 = moduleItemHolder.itemView.findViewById(R.id.tv_module_name);
                textView1.setText(list.get(position).getName());
                if(onItemClickListener != null){
                    moduleItemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemClickListener.onClick(holder.itemView,list.get(position));

                        }
                    });
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (list.get(position).getShowType()) {
            case TITLE_ITEM:

                return TITLE_ITEM;

            case GALLERY_ITEM:

                return GALLERY_ITEM;
        }

        return -1;

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onClick(View view, ModuleBean bean);
    }
}
