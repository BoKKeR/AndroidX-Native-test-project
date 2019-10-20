package com.example.otto.sql.worker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otto.R;

import java.util.ArrayList;
import java.util.List;

public class WorkerAdapter extends RecyclerView.Adapter  <WorkerAdapter.JobHolder>{
    private List<Worker> workers = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public JobHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_item, parent, false);
        return new JobHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JobHolder holder, int position) {
        Worker currentWorker = workers.get(position);
        holder.textViewTitle.setText(currentWorker.getTitle());
        holder.textViewDescription.setText(currentWorker.getDescription());
        holder.textViewPriority.setText(String.valueOf(currentWorker.getPriority()));
    }

    @Override
    public int getItemCount() {
        return workers.size();
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
        notifyDataSetChanged();
    }

    public Worker getJobAt(int position) {
        return workers.get(position);
    }

    class JobHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;

        public JobHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(workers.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Worker worker);
    }

    public void setOnItemClickListener (OnItemClickListener listener) {
        this.listener = listener;

    }
}
