package com.katyshevtseva.kikiorgmobile.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.katyshevtseva.kikiorgmobile.utils.GeneralUtil;
import com.katyshevtseva.kikiorgmobile.view.utils.SwipeManager;

import java.util.List;

public class LogsActivity extends AppCompatActivity {
    private SwipeManager swipeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
        GeneralUtil.setImmersiveStickyMode(getWindow());

        RecyclerView logList = findViewById(R.id.log_list);
        logList.setLayoutManager(new LinearLayoutManager(this));
        logList.setAdapter(new LogListAdapter(this));

        swipeManager = new SwipeManager(this);
        swipeManager.setLeftSwipeListener(this::finish);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Boolean result = swipeManager.dispatchTouchEvent(ev);
        return result == null ? super.dispatchTouchEvent(ev) : result;
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