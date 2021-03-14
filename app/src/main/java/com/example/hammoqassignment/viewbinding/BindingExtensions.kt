package com.example.hammoqassignment.viewbinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.reflect.Method
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.staticFunctions

/**
 * Use reflection to get the static inflate method of the [ViewBinding] class
 *
 * @return the static inflate [KFunction] used to inflate a view with a [ViewBinding]
 */
fun <T : ViewBinding> getInflateMethod(kClass: KClass<T>): KFunction<*> {
    val functions =
        kClass.staticFunctions.filter { it.name == "inflate" && it.parameters.size == 3 }

    // Validate that the method was found properly
    if (functions.isNullOrEmpty()) {
        throw ReflectiveOperationException("Could not find inflate(inflater, container, attachToParent) for ${kClass.simpleName}")
    }

    return functions[0]
}

/**
 * Using reflection, inflate a [ViewBinding] and return it
 *
 * @return [ViewBinding] class
 */
fun <T : ViewBinding> KClass<T>.inflate(
    inflater: LayoutInflater,
    container: ViewGroup?,
    attachToParent: Boolean = false
): T {
    @Suppress("UNCHECKED_CAST")
    return getInflateMethod(this).call(inflater, container, attachToParent) as T
}

/**
 * Use reflection to get the static bind method of the [ViewBinding] class
 *
 * @return the static bind [Method] used to inflate a view with a [ViewBinding]
 */
fun <T : ViewBinding> getBindMethod(kClass: KClass<T>): KFunction<*> {
    val functions = kClass.staticFunctions.filter { it.name == "bind" && it.parameters.size == 1 }

    // Validate that the method was found properly
    if (functions.isNullOrEmpty()) {
        throw ReflectiveOperationException("Could not find bind(view) for ${kClass.simpleName}")
    }

    return functions[0]
}

/**
 * Using reflection, bind a [View] to the [ViewBinding]
 *
 * @return [ViewBinding] class
 */
fun <T : ViewBinding> KClass<T>.bind(view: View): T {
    @Suppress("UNCHECKED_CAST")
    return getBindMethod(this).call(view) as T
}

/**
 * Helper method to automatically inflate a [ViewBinding] and delegate it to a property
 *
 * @param T the [ViewBinding] class for the view you wish to inflate
 * @return [ReadOnlyProperty] that is lifecycle aware
 */
inline fun <reified T : ViewBinding> ComponentActivity.viewBinding(): ReadOnlyProperty<ComponentActivity, T> {
    return ViewBindingProperty {
        T::class.inflate(layoutInflater, null, false)
    }
}

/**
 * Helper method to automatically inflate a [ViewBinding] and delegate it to a property
 *
 * @param T the [ViewBinding] class for the view you wish to bind to
 * @return [ReadOnlyProperty] that is lifecycle aware
 */
inline fun <reified T : ViewBinding> Fragment.viewBinding(): ReadOnlyProperty<Fragment, T> {
    return ViewBindingProperty { T::class.bind(requireView()) }
}

/**
 * Helper method to create a [BindingViewHolder] in your Recycler Adapter
 *
 * @param T the [ViewBinding] class generated for your ViewHolder layout
 * @param parent the parent of the ViewHolder
 * @see [BindingViewHolder]
 */
inline fun <reified T : ViewBinding> createBindingViewHolder(parent: ViewGroup): BindingViewHolder<T> {
    return BindingViewHolder(
        T::class.inflate(LayoutInflater.from(parent.context), parent, false)
    )
}