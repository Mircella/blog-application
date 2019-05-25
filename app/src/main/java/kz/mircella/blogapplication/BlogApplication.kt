package kz.mircella.blogapplication

import android.app.Application
import kz.mircella.blogapplication.navigation.Navigator
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager

@Suppress("unused")
class BlogApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        XInjectionManager.bindComponentToCustomLifecycle(object : IHasComponent<Navigator> {
            override fun getComponent(): Navigator = Navigator()
        })
    }
}