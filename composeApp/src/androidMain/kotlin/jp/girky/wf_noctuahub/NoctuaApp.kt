package jp.girky.wf_noctuahub

import android.app.Application
import android.content.Context

class NoctuaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private var instance: NoctuaApp? = null
        fun getContext(): Context = instance!!.applicationContext
    }
}
