package com.partymusicq.android.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.partymusicq.android.R
import com.partymusicq.android.pojo.Party
import com.partymusicq.android.ui.adapter.PartyAdapter
import com.partymusicq.android.ui.handler.HostPartyHandler

class HomeActivity : BaseActivity(), PartyAdapter.OnQueueSelectedListener {

    private val PARTY_NAME = "partyName"
    private val ROOM_CODE = "roomCode"

    private lateinit var partyNameEditText: EditText
    private lateinit var roomCodeEditTExt: EditText
    private lateinit var hostButton: Button
    private lateinit var joinButton: Button
    private lateinit var currentQueuesRecyclerView: RecyclerView
    private lateinit var currentQueuesCont: View

    private lateinit var firestore : FirebaseFirestore
    private lateinit var adapter : PartyAdapter

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

        firestore = FirebaseFirestore.getInstance()

        setupOnClicks()
        setupAdapter()
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PARTY_NAME, partyNameEditText.text.toString())
        outState.putString(ROOM_CODE, roomCodeEditTExt.text.toString())
    }

    private fun setupOnClicks() {
        hostButton.setOnClickListener { view ->
            hostParty()
        }

        joinButton.setOnClickListener { p0 ->
            Toast.makeText(p0?.context, "join pressed", Toast.LENGTH_SHORT).show()
            //TODO: do something
        }
    }
    
    private fun hostParty() {
        if (partyNameEditText.text.isNotEmpty()) {
            val handler = HostPartyHandler(partyNameEditText.text.toString())
            val partyId = handler.handle()
            if (!partyId.isNullOrEmpty()) {
                val intent = Intent(this, PartyActivity::class.java)
                intent.putExtra("partyId", partyId)
                startActivity(intent)
            }
        } else {
            Toast.makeText(this, "Party name can't be empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupAdapter() {
        currentQueuesRecyclerView.itemAnimator = DefaultItemAnimator()
        bindAdapter()
    }

    private fun bindAdapter() {
        val query = firestore.collection("parties")
        adapter = PartyAdapter(query, this)
        currentQueuesRecyclerView.adapter = adapter
    }

    override fun onQueueSelected(queue: DocumentSnapshot) {
        val party = queue.toObject(Party::class.java)
        val partyId = party?.id
        if (!partyId.isNullOrEmpty()) {
            val intent = Intent(this, PartyActivity::class.java)
            intent.putExtra("partyId", partyId)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
        }

    }
}