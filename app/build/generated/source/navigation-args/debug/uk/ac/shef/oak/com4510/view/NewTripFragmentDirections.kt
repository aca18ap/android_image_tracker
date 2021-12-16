package uk.ac.shef.oak.com4510.view

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import uk.ac.shef.oak.com4510.R

public class NewTripFragmentDirections private constructor() {
  public companion object {
    public fun actionNewTripFragmentToTravellingFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_newTripFragment_to_travellingFragment)
  }
}
