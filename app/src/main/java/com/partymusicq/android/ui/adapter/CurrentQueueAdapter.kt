package com.partymusicq.android.ui.adapter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.partymusicq.android.R
import com.partymusicq.android.pojo.Queue


class CurrentQueueAdapter(val context: Context,
                          val currentQueues: ArrayList<Queue>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return currentQueues.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.current_queue_list_item, parent, false)
        return CurrentQueueViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = holder as CurrentQueueViewHolder
        view.partyName.text = context.getString(R.string.home_activity_current_queue_party_name_label, currentQueues[position].partyName)
        view.roomCode.text = context.getString(R.string.home_activity_current_queue_room_code_label, currentQueues[position].roomCode)

        if (position == currentQueues.size - 1) {
            val marginPixels = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10f,
                context.resources.displayMetrics
            ).toInt()
            val marginLayoutParams = ViewGroup.MarginLayoutParams(view.rootCont.layoutParams)
            marginLayoutParams.bottomMargin = marginPixels
            view.rootCont.layoutParams = marginLayoutParams
            view.itemDivider.visibility = View.GONE
        }

        view.rootCont.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                Toast.makeText(context, "queue clicked on", Toast.LENGTH_SHORT).show()
            }

        })
    }

}

class CurrentQueueViewHolder: RecyclerView.ViewHolder {
    var partyName: TextView
    var roomCode: TextView
    var rootCont: View
    var itemDivider: View

    constructor(view: View) : super(view) {
        partyName = view.findViewById(R.id.current_queue_party_name)
        roomCode = view.findViewById(R.id.current_queue_room_code)
        rootCont = view.findViewById(R.id.root_queue_list_item_cont)
        itemDivider = view.findViewById(R.id.item_divider)
    }

}