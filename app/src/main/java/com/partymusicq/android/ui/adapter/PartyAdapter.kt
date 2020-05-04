package com.partymusicq.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.partymusicq.android.databinding.CurrentQueueListItemBinding
import com.partymusicq.android.pojo.Party

open class PartyAdapter(query: Query, private val listener: OnQueueSelectedListener) :
    FirestoreAdapter<PartyAdapter.ViewHolder>(query) {

    interface OnQueueSelectedListener {
        fun onQueueSelected(queue: DocumentSnapshot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CurrentQueueListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position), listener)
    }

    class ViewHolder(private val binding: CurrentQueueListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            snapshot: DocumentSnapshot,
            listener: OnQueueSelectedListener?
        ) {
            val queue = snapshot.toObject(Party::class.java) ?: return
            binding.currentQueuePartyName.text = queue.name
            binding.currentQueueRoomCode.text = queue.passCode
            binding.root.setOnClickListener {
                listener?.onQueueSelected(snapshot)
            }
        }
    }
}