package com.katyshevtseva.kikiorgmobile.view.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.RtSettingService;
import com.katyshevtseva.kikiorgmobile.core.model.RtSetting;
import com.katyshevtseva.kikiorgmobile.view.QuestionDialog;
import com.katyshevtseva.kikiorgmobile.view.SettingCreationActivity;

import java.util.List;

public class SettingRecycleView {

    static class SettingHolder extends RecyclerView.ViewHolder {
        private final SettingListAdapter settingListAdapter;
        private final AppCompatActivity context;

        SettingHolder(View view, SettingListAdapter settingListAdapter, AppCompatActivity context) {
            super(view);
            this.settingListAdapter = settingListAdapter;
            this.context = context;
        }

        void bind(RtSetting setting) {
            ((TextView) itemView.findViewById(R.id.setting_desc_view))
                    .setText(RtSettingService.INSTANCE.getRtSettingDesc(setting));
            itemView.findViewById(R.id.edit_setting_button).setOnClickListener(view ->
                    context.startActivity(SettingCreationActivity.newIntent(context, setting)));
            itemView.findViewById(R.id.delete_setting_button).setOnClickListener(view ->
                    new QuestionDialog("Delete?", getDeletionDialogAnswerHandler(setting))
                            .show(context.getSupportFragmentManager(), "dialog111"));
        }

        private QuestionDialog.AnswerHandler getDeletionDialogAnswerHandler(final RtSetting setting) {
            return answer -> {
                if (answer) {
                    RtSettingService.INSTANCE.deleteRtSetting(setting);
                    Toast.makeText(context, "Deleted!", Toast.LENGTH_LONG).show();
                    settingListAdapter.updateContent();
                }
            };
        }
    }

    public static class SettingListAdapter extends RecyclerView.Adapter<SettingHolder> {
        private List<RtSetting> settings;
        private final AppCompatActivity context;

        public SettingListAdapter(AppCompatActivity context) {
            this.context = context;
            updateContent();
        }

        @NonNull
        @Override
        public SettingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.rt_setting_list_item, parent, false);
            return new SettingHolder(view, this, context);
        }

        @Override
        public void onBindViewHolder(@NonNull SettingHolder holder, int position) {
            RtSetting setting = settings.get(position);
            holder.bind(setting);
        }

        @Override
        public int getItemCount() {
            return settings.size();
        }

        public void updateContent() {
            settings = RtSettingService.INSTANCE.getAllRtSettings();
            notifyDataSetChanged();
        }
    }
}
