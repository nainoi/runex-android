package com.think.runex.feature.qr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.think.runex.R
import com.think.runex.base.BaseActivity

class ScanQRActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr)
    }
}