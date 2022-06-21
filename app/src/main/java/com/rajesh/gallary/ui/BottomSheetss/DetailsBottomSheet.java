package com.rajesh.gallary.ui.BottomSheetss;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rajesh.gallary.R;
import com.rajesh.gallary.model.mediaModel;

import java.text.SimpleDateFormat;

public class DetailsBottomSheet extends BottomSheetDialogFragment {
    private mediaModel data;

    public DetailsBottomSheet(mediaModel data) {
        this.data = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.details_layout , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Define Views
        TextView Name_txt = view.findViewById(R.id.Media_Name_txt);
        TextView Path_txt = view.findViewById(R.id.Media_Path_txt);
        TextView Size_txt = view.findViewById(R.id.Media_Size_txt);
        TextView Date_txt = view.findViewById(R.id.Media_Date_txt);
        //observe data into the view
        Name_txt.setText("Name : " + data.getMediaName());
        Path_txt.setText("Path : " + data.getMediaPath());
        Size_txt.setText("Size : " + sizeConverter(data.getMediaSize()));
        Date_txt.setText("Created On : " + new SimpleDateFormat("dd MMM ,yyyy").format(data.getMediaDate() * 1000));
    }

    /**
     * Get The Size of the media item Converter
     *
     * @param size
     * @return
     */
    private String sizeConverter(String size) {
        float converterSize = Float.parseFloat(size) / 1000f;
        if (converterSize < 1000)
            return converterSize + " KB";
        else if (converterSize > 1000) {
            converterSize = converterSize / 1000;
            return converterSize + "MB";
        } else {
            return (converterSize / 1000) + "G";
        }
    }
}
