package com.example.otto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class JobAdaptor extends RecyclerView.Adapter  <JobAdaptor.JobHolder>{
    private List<Job> jobs = new ArrayList<>();

    @NonNull
    @Override
    public JobHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_item, parent, false);
        return new JobHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JobHolder holder, int position) {
        Job currentJob = jobs.get(position);
        holder.textViewTitle.setText(currentJob.getTitle());
        holder.textViewDescription.setText(currentJob.getDescription());
        holder.textViewPriority.setText(String.valueOf(currentJob.getPriority()));
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
        notifyDataSetChanged();
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
        }
    }
}
