package com.katyshevtseva.kikiorgmobile.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kikiorgmobile.R;
import com.katyshevtseva.kikiorgmobile.core.Service;
import com.katyshevtseva.kikiorgmobile.core.model.Log;

import java.util.List;

public class LogsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        RecyclerView logList = findViewById(R.id.log_list);
        logList.setLayoutManager(new LinearLayoutManager(this));
        logList.setAdapter(new LogListAdapter(this));
    }

    private static class LogHolder extends RecyclerView.ViewHolder {

        LogHolder(View view) {
            super(view);
        }

        void bind(final Log log) {
            ((TextView) itemView.findViewById(R.id.log_text_view)).setText(log.getFullDesc());
        }
    }

    private static class LogListAdapter extends RecyclerView.Adapter<LogHolder> {
        private final List<Log> logs;
        private final LogsActivity context;

        LogListAdapter(LogsActivity context) {
            this.context = context;
            logs = Service.INSTANCE.getLogs();
        }

        @NonNull
        @Override
        public LogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.log_item, parent, false);
            return new LogHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LogHolder holder, int position) {
            holder.bind(logs.get(position));
        }

        @Override
        public int getItemCount() {
            return logs.size();
        }
    }
}