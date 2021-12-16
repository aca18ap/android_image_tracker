package uk.ac.shef.oak.com4510.view

import android.os.Bundle
import androidx.navigation.NavDirections
import kotlin.Int
import uk.ac.shef.oak.com4510.R

public class GalleryFragmentDirections private constructor() {
  private data class ActionGalleryFragmentToShowImageFragment(
    public val position: Int = -1
  ) : NavDirections {
    public override fun getActionId(): Int = R.id.action_galleryFragment_to_showImageFragment

    public override fun getArguments(): Bundle {
      val result = Bundle()
      result.putInt("position", this.position)
      return result
    }
  }

  public companion object {
    public fun actionGalleryFragmentToShowImageFragment(position: Int = -1): NavDirections =
        ActionGalleryFragmentToShowImageFragment(position)
  }
}
