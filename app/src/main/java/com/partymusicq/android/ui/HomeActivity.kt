package com.partymusicq.android.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.partymusicq.android.R
import com.partymusicq.android.pojo.Queue
import com.partymusicq.android.ui.adapter.CurrentQueueAdapter

class HomeActivity : BaseActivity() {

    private val PARTY_NAME = "partyName"
    private val ROOM_CODE = "roomCode"

    private lateinit var partyNameEditText: EditText
    private lateinit var roomCodeEditTExt: EditText
    private lateinit var hostButton: Button
    private lateinit var joinButton: Button
    private lateinit var currentQueuesRecyclerView: RecyclerView
    private lateinit var currentQueuesCont: View

    private var currentQueues = ArrayList<Queue>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        partyNameEditText = findViewById(R.id.party_name_edit_text)
        roomCodeEditTExt= findViewById(R.id.room_code_edit_text)
        hostButton = findViewById(R.id.host_button)
        joinButton = findViewById(R.id.join_button)
        currentQueuesRecyclerView = findViewById(R.id.current_queues_list_view)
        currentQueuesCont = findViewById(R.id.current_queues_cont)

        if (savedInstanceState != null) {
            partyNameEditText.setText(savedInstanceState.getString(PARTY_NAME) ?: "")
            roomCodeEditTExt.setText(savedInstanceState.getString(ROOM_CODE) ?: "")
        }

        fetchCurrentQueues()
        setupOnClicks()
        setupAdapter()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PARTY_NAME, partyNameEditText.text.toString())
        outState.putString(ROOM_CODE, roomCodeEditTExt.text.toString())
    }

    private fun setupOnClicks() {
        hostButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                Toast.makeText(p0?.context, "host pressed", Toast.LENGTH_SHORT).show()
                //TODO: do something
            }
        })

        joinButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                Toast.makeText(p0?.context, "join pressed", Toast.LENGTH_SHORT).show()
                //TODO: do something
            }
        })
    }

    private fun fetchCurrentQueues() {
        currentQueues.add(Queue("Really long name just to test the constraints because line wrapping could break things", "test2"))
        currentQueues.add(Queue("test3", "test4"))
        currentQueues.add(Queue("test5", "test5"))
        currentQueues.add(Queue("test6", "test6"))
    }

    private fun setupAdapter() {
        currentQueuesRecyclerView.itemAnimator = DefaultItemAnimator()
        bindAdapter()
    }

    private fun bindAdapter() {
        currentQueuesRecyclerView.adapter = CurrentQueueAdapter(this, currentQueues)
    }

}