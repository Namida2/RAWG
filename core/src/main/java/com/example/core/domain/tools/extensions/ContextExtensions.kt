package com.example.core.domain.tools.extensions

import android.content.Context
import com.example.core.domain.entities.Message
import com.example.core.presentaton.dialogs.MessageAlertDialog

fun Context.createMessageAlertDialog(message: Message, action: () -> Unit = {}): MessageAlertDialog? =
    MessageAlertDialog.getNewInstance(
        this.resources.getString(message.titleResourceId),
        this.resources.getString(message.messageResourceId),
        action
    )