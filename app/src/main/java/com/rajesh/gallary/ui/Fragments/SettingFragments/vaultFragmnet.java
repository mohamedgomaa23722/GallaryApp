package com.rajesh.gallary.ui.Fragments.SettingFragments;

import static com.rajesh.gallary.common.Constant.DATA;
import static com.rajesh.gallary.common.Constant.DELETE_SELECTED;
import static com.rajesh.gallary.common.Constant.FROM_VAULT_TO_SETTINGS;
import static com.rajesh.gallary.common.Constant.MOVE_TO_VAULT;
import static com.rajesh.gallary.common.Constant.SELECT_ALL;
import static com.rajesh.gallary.common.Constant.SHARE_SELECTED;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rajesh.gallary.ui.Adapter.allPicsAndVideos.MediaAdapter;
import com.rajesh.gallary.R;
import com.rajesh.gallary.databinding.FragmentVaultFragmnetBinding;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.network.onItemClickListener;
import com.rajesh.gallary.network.onLongSelected;
import com.rajesh.gallary.ui.Activities.DisplayActivity;
import com.rajesh.gallary.ui.BottomSheetss.CopyBottomSheet;
import com.rajesh.gallary.ui.BottomSheetss.DeleteBottomSheet;
import com.rajesh.gallary.ui.viewModels.SettingsViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class vaultFragmnet extends Fragment implements onItemClickListener<mediaModel>, View.OnClickListener, onLongSelected {
    private FragmentVaultFragmnetBinding binding;
    private SettingsViewModel viewModel;
    private MediaAdapter adapter;

    private List<mediaModel> mediaModels = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVaultFragmnetBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(SettingsViewModel.class);

        adapter = new MediaAdapter(getActivity());
        binding.VaultRecucler.setHasFixedSize(true);
        binding.VaultRecucler.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        adapter.setGridCount(3);
        viewModel.getVaultMedia().observe(getViewLifecycleOwner(), mediaModelList -> {
            mediaModels.clear();
            adapter.setMediaModelList(mediaModelList);
            mediaModels.addAll(mediaModelList);
        });

        binding.VaultBack.setOnClickListener(this);
        adapter.setOnLongSelected(this);
        adapter.setVault(true);
    }

    @Override
    public void onClickedItem(mediaModel itemData) {
        Intent intent = new Intent(getContext(), DisplayActivity.class);
        intent.putExtra(DATA, itemData);
        intent.putExtra(FROM_VAULT_TO_SETTINGS, true);
        startActivity(intent);
    }

    @Override
    public void onLongTouch(mediaModel itemData) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.VaultBack)
            viewModel.setSettingsData(FROM_VAULT_TO_SETTINGS);
    }

    @Override
    public void longClicked(boolean isEnable, boolean isSelected, int Operation) {
        if (Operation == SELECT_ALL) {
            SelectAllItems(isSelected);
        } else if (Operation == DELETE_SELECTED) {
            DisplayDeleteBottomSheet();
        } else if (Operation == SHARE_SELECTED) {
            ShareMedia();
        }  else if (Operation == MOVE_TO_VAULT) {
            mediaModels.clear();
            viewModel.RemoveFromVault(GetSelectedItems());
            Toast.makeText(getContext(), "Added To Vault", Toast.LENGTH_SHORT).show();
        } else {
            // remove selection from all items
            SelectAllItems(false);
        }
    }

    @Override
    public void OnChildSelected(List<mediaModel> data) {

    }

    private void SelectAllItems(boolean isSelected) {
        List<mediaModel> result = new ArrayList<>();
        for (mediaModel mediaModel : mediaModels) {
            mediaModel.setSelected(isSelected);
            result.add(mediaModel);
        }
        adapter.setMediaModelList(result);
    }

    private void ShareMedia() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.setType("*/*");
        shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, SelectedUri(shareIntent));
        startActivity(Intent.createChooser(shareIntent, null));
    }

    private ArrayList<Uri> SelectedUri(Intent intent) {
        ArrayList<Uri> uris = new ArrayList<>();
        for (mediaModel data : GetSelectedItems()) {
            Uri uri = FileProvider.getUriForFile(getContext(), getActivity().getApplicationContext().getPackageName() + ".provider", new File(data.getMediaPath()));
            uris.add(uri);
            @SuppressLint("QueryPermissionsNeeded") List<ResolveInfo> resInfoList = getActivity().getApplicationContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                getActivity().grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
        return uris;
    }

    /**
     * Display Delete Bottom Sheet
     */
    public void DisplayDeleteBottomSheet() {
        // show the bottom sheet
        DeleteBottomSheet frag = new DeleteBottomSheet(GetSelectedItems());
        frag.show(getActivity().getSupportFragmentManager(), "Delete");
    }

    /**
     * Display Copy Bottom Sheet
     */
    public void DisplayCopyBottomSheet() {
        //Initialize Bottom Sheet
        CopyBottomSheet frag = new CopyBottomSheet(GetSelectedItems());
        frag.show(getActivity().getSupportFragmentManager(), "Copy");
    }

    private List<mediaModel> GetSelectedItems() {
        List<mediaModel> result = new ArrayList<>();
        for (mediaModel data : adapter.getMediaModelList()) {
            if (data.getSelected())
                result.add(data);
        }
        return result;
    }
}