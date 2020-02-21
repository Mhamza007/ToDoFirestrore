package com.mhamza007.todofirestrore.adapter

import android.view.View

interface ItemClickListener {
    fun onClick(
        view: View?,
        position: Int,
        isLingClick: Boolean
    )
}