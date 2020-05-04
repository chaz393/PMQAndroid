package com.partymusicq.android.pojo

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Queue(
    val name: String? = null,
    val owner: String? = null,
    val passCode: String? = null,
    val id: String? = null) {

    companion object {
        const val FIELD_NAME = "name"
        const val FIELD_OWNER = "owner"
        const val FIELD_PASSC0DE = "passCode"
        const val FIELD_ID = "id"
    }

}