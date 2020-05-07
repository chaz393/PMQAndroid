package com.partymusicq.android.ui.handler

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.partymusicq.android.pojo.Party
import com.partymusicq.android.pojo.Song
import kotlin.math.floor

class HostPartyHandler(private val partyName: String) {

    fun handle(): String? {
        val party =
            Party(partyName, FirebaseAuth.getInstance().currentUser?.uid, generateUniquePassCode())
        saveParty(party)

        return party.id
    }

    private fun generatePassCode(): String {
        var result = ""
        val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val charactersLength = characters.length
        for (i in 0..6) {
            result += characters[floor(Math.random() * charactersLength).toInt()]
        }
        return result
    }

    private fun generateUniquePassCode(): String {
        var result = ""
        while (result == "") {
            result = generatePassCode()
        }
        return result
    }

    private fun saveParty(party: Party) {
        val firestore = FirebaseFirestore.getInstance()
        val documentRef = firestore.collection("parties").document()
        val newId = documentRef.id
        party.id = newId
        documentRef.set(party)

        //TODO remove this
        //setup a queue in the db to test with
        var songRef = firestore.collection("parties/" + party.id +"/queue").document()
        songRef.set(Song("spotify:track:2KtS31kaWNHs12Q5B43Ixh", 0))
        songRef = firestore.collection("parties/" + party.id +"/queue").document()
        songRef.set(Song("spotify:track:492PZFHvGTm3RZZYeeUVWT", 1))
        songRef = firestore.collection("parties/" + party.id +"/queue").document()
        songRef.set(Song("spotify:track:0ILIuCVbnekvEFpQCWhP8a", 2))
    }
}
