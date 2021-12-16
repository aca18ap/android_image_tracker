package uk.ac.shef.oak.com4510.view

import android.os.Bundle
import androidx.navigation.NavDirections
import kotlin.Int
import uk.ac.shef.oak.com4510.R

public class ShowImageFragmentDirections private constructor() {
  private data class ActionShowImageFragmentToEditImageFragment(
    public val position: Int = -1
  ) : NavDirections {
    public override fun getActionId(): Int = R.id.action_showImageFragment_to_editImageFragment

    public override fun getArguments(): Bundle {
      val result = Bundle()
      result.putInt("position", this.position)
      return result
    }
  }

  public companion object {
    public fun actionShowImageFragmentToEditImageFragment(position: Int = -1): NavDirections =
        ActionShowImageFragmentToEditImageFragment(position)
  }
}
