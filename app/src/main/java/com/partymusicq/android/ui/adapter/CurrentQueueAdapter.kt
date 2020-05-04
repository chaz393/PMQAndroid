package com.partymusicq.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.partymusicq.android.databinding.CurrentQueueListItemBinding
import com.partymusicq.android.pojo.Queue

open class CurrentQueueAdapter(query: Query, private val listener: OnQueueSelectedListener) :
    FirestoreAdapter<CurrentQueueAdapter.CurrentQueueViewHolder>(query) {

    interface OnQueueSelectedListener {
        fun onQueueSelected(queue: DocumentSnapshot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentQueueViewHolder {
        return CurrentQueueViewHolder(
            CurrentQueueListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CurrentQueueViewHolder, position: Int) {
        holder.bind(getSnapshot(position), listener)
    }

    class CurrentQueueViewHolder(private val binding: CurrentQueueListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            snapshot: DocumentSnapshot,
            listener: OnQueueSelectedListener?
        ) {
            val queue = snapshot.toObject(Queue::class.java) ?: return
            binding.currentQueuePartyName.text = queue.name
            binding.currentQueueRoomCode.text = queue.passCode
            binding.root.setOnClickListener {
                listener?.onQueueSelected(snapshot)
            }
        }
    }
}