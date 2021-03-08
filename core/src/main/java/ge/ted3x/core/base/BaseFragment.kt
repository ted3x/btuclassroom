package ge.ted3x.core.base

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    abstract fun getLayout(): Int
    abstract fun getTitle(): String
    abstract fun isBackArrowEnabled() : Boolean
    abstract fun toolbarButtonIcon() : Drawable?

    protected var viewModel : VM? = null
    protected open var viewModelToken: Class<VM>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as BaseActivity).setToolbar(getTitle(), isBackArrowEnabled(), toolbarButtonIcon()) {
            onToolbarButtonClick()
        }
        return inflater.inflate(getLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbar()
        showBottomNav()
        if (viewModel != null)
            onBindViewModel(viewModel!!)
    }

    @CallSuper
    open fun onBindViewModel(viewModel: VM) {
        viewModel.message.observe{ message->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    open fun onToolbarButtonClick(){}

    protected fun hideToolbar(){
        (activity as BaseActivity).hideToolbar()
    }

    protected fun hideBottomNav(){
        (activity as BaseActivity).hideBottomNav()
    }

    protected fun <T> LiveData<T>.observe(action : (T) -> Unit){
        this.observe(viewLifecycleOwner, {
            action(it)
        })
    }

    private fun showToolbar(){
        (activity as BaseActivity).showToolbar()
    }

    private fun showBottomNav(){
        (activity as BaseActivity).showBottomNav()
    }
}
