package uk.ac.shef.oak.com4510.view

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import uk.ac.shef.oak.com4510.R

public class HomeFragmentDirections private constructor() {
  public companion object {
    public fun actionHomeFragmentToGalleryFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_homeFragment_to_galleryFragment)

    public fun actionHomeFragmentToNewTripFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_homeFragment_to_newTripFragment)
  }
}
