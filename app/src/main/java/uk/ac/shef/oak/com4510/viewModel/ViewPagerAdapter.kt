package uk.ac.shef.oak.com4510.viewModel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uk.ac.shef.oak.com4510.view.TripEntriesTabFragment
import uk.ac.shef.oak.com4510.view.TripImagesTabFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle){

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                TripImagesTabFragment()
            }
            1->{
                TripEntriesTabFragment()
            }
            else->{
                Fragment()
            }
        }
    }


}