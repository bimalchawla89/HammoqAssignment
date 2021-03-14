package com.example.hammoqassignment.viewbinding

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


/**
 * A lifecycle aware [ReadOnlyProperty] to utilize delegation in kotlin for ViewBinding
 *
 * @param R the [ComponentActivity] or [Fragment] that you are using ViewBinding with
 * @param T the [ViewBinding] class generated for your view
 * @property viewBinder method that returns a [ViewBinding] instance for the view to utilize
 * @constructor makes a [ReadOnlyProperty] from a binder method
 */
class ViewBindingProperty<R : LifecycleOwner, T : ViewBinding>(
    private val viewBinder: (R) -> T
) : ReadOnlyProperty<R, T> {
    internal var viewBinding: T? = null

    /**
     * Get the appropriate lifecycle owner from the Fragment/Activity
     *
     * @param thisRef is the reference of the Fragment/Activity
     * @return [LifecycleOwner] attached to your view
     */
    private fun getLifecycleOwner(thisRef: R): LifecycleOwner {
        return when (thisRef) {
            is Fragment -> thisRef.viewLifecycleOwner
            is ComponentActivity -> thisRef
            else -> {
                // This should never happen unless the wrong activity type is used
                throw IllegalArgumentException("getLifecycleOwner only accepts Fragment or ComponentActivity")
            }
        }
    }

    /**
     * Get the value of the backing field, set it if not already set. Lifecycle Aware.
     *
     * @param thisRef is the [LifecycleOwner] of the [ViewBinding]
     * @param property is unused for our purposes here
     */
    @MainThread
    override fun getValue(thisRef: R, property: KProperty<*>): T {
        // If we have a non-null viewBinding then we can just return the value
        viewBinding?.let { return it }

        val lifecycle = getLifecycleOwner(thisRef).lifecycle

        // Don't allow attempting to re-create the viewBinding after DESTROYED. This would crash
        // on it's own with a more cryptic message later, so lets just prevent it here and make it
        // clear to the developer what the issue is.
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            throw IllegalStateException("Can not access view binding after destroy")
        }

        // Add an observer to set the viewBinding to null on DESTROYED to avoid memory leaks
        // from holding the references to views after DESTROYED.
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                owner.lifecycle.removeObserver(this)
                viewBinding = null
            }
        })

        // We passed the lifecycle checks and viewBinding is null, so lets create the binding
        // using the binder method (which is using reflection to get the bind/inflate methods)
        // and then return it to the UI. This won't be run until some code requests the binding.
        return viewBinder(thisRef).also { viewBinding = it }
    }
}
