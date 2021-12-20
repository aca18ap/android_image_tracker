package uk.ac.shef.oak.com4510.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Class ViewPagerAdapter
 * @param FragmentActivity: gets the current fragment the viewpage is located into
 *
 *  This adapter is required by the tabs n the ViewTripDetailsFragment
 */
class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){

    /**
     * List containing all the fragments that will make up the tabs in the ViewPage.
     * This allows for flexibility in the number of tabs displayed
     */
    private val mFragmentList = ArrayList<Fragment>()


    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }

    /**
     * function addFragment, adds the fragment passed as parameter to the list of fragments
     */
    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)

    }



}