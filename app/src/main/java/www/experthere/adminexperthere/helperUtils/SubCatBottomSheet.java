package www.experthere.adminexperthere.helperUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import www.experthere.adminexperthere.R;
import www.experthere.adminexperthere.adapters.SubCategoryAdapter;
import www.experthere.adminexperthere.dataModel.Subcategory;


public class SubCatBottomSheet extends BottomSheetDialogFragment {

    Activity activity;
    List<Subcategory> subcategories;

    public SubCatBottomSheet(Activity activity, List<Subcategory> categories) {
        this.activity = activity;
        this.subcategories = categories;
    }

    public interface SubCatBottomSheetListener {
        void onItemSelected(String id,String subCatName );
    }

    private SubCatBottomSheetListener mListener;

    public void setSubCatBottomSheetListener(SubCatBottomSheetListener listener) {
        this.mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sub_cat_select_dilog, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerCat);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        SubCategoryAdapter adapter = new SubCategoryAdapter(subcategories, activity, new SubCategoryAdapter.OnSubCategorySelectedListener() {
            @Override
            public void onSubCategorySelected(String id ,String subCatName) {
                if (mListener != null) {
                    mListener.onItemSelected(id,subCatName);
                }

                dismiss();
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }
}
