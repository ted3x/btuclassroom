package ge.ted3x.core.base

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    abstract fun setToolbar(
        title: String,
        isHomeEnabled: Boolean,
        toolbarButtonIcon: Drawable?,
        onClick: () -> Unit
    )

    abstract fun showToolbar()
    abstract fun hideToolbar()
    abstract fun showBottomNav()
    abstract fun hideBottomNav()
}