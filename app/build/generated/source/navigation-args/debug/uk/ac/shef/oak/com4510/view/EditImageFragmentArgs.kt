package uk.ac.shef.oak.com4510.view

import android.os.Bundle
import androidx.navigation.NavArgs
import kotlin.Int
import kotlin.jvm.JvmStatic

public data class EditImageFragmentArgs(
  public val position: Int = -1
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putInt("position", this.position)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): EditImageFragmentArgs {
      bundle.setClassLoader(EditImageFragmentArgs::class.java.classLoader)
      val __position : Int
      if (bundle.containsKey("position")) {
        __position = bundle.getInt("position")
      } else {
        __position = -1
      }
      return EditImageFragmentArgs(__position)
    }
  }
}
