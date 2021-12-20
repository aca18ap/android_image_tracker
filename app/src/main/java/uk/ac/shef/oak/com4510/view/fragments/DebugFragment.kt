package uk.ac.shef.oak.com4510.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import uk.ac.shef.oak.com4510.R
import uk.ac.shef.oak.com4510.databinding.FragmentDebugBinding
import uk.ac.shef.oak.com4510.model.data.database.EntryData
import uk.ac.shef.oak.com4510.model.data.database.TripData
import uk.ac.shef.oak.com4510.viewModel.TravelViewModel

class DebugFragment : Fragment() {
    private lateinit var binding : FragmentDebugBinding
    private val model: TravelViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding  = DataBindingUtil.inflate(inflater,
            R.layout.fragment_debug, container, false)

        binding.dontPressMeBtn.setOnClickListener {
            /*
            Add all sorts of things into the database
            A trip that has multiple entries
            Entries to have some images

            Could I make a function that adds an image to an entry
            Could I make a function that
            */
            //Get the first 5 images from the database
            val imageList = model.debug_getImages()

            if (imageList?.size!! >= 5)
            {
                // I'll be potentially using the first 5 images in the image list.(By id)
                // I need to have 3 trips
                // Each trip would have some entries
                // Some of those entries would have images
                // At least one trip will have every entry have no image
                //model.insertTripReturnId(TripData(1, title = "My day around Sheffield", country = "UK",trip_timestamp =  1639698825f))
                //Add entries
                //model.insertEntryReturnId(EntryData(1,100.0,102.0,1639698825,20f,30f,1))
                //model.insertEntryReturnId(EntryData(2,150.0,150.0,1639698825,25f,35f,1))
                //model.insertEntryReturnId(EntryData(3,200.0,200.0,1639698825,23f,43f,1))
                // Attach the 2nd entry with an image
                //model.updateImageInDatabase(imageList[0], entry_id = 2)

                //model.insertTripReturnId(TripData(2,"Dic","Bonanza",1639698825f))
                //model.insertEntryReturnId(EntryData(4,300.0,300.0,1639698825,20f,30f,2))
                //model.insertEntryReturnId(EntryData(5,350.0,350.0,1639698825,25f,35f,2))
                //model.insertEntryReturnId(EntryData(6,400.0,400.0,1639698825,23f,43f,2))
                //Attach the 1st entry here with an image
                //model.updateImageInDatabase(imageList[1], entry_id = 4)

                // This trip's entries will have no images attached to it
                //model.insertTripReturnId(TripData(3,"At home","Stardew valley",1639698825f))
                //model.insertEntryReturnId(EntryData(7,600.0,600.0,1639698825,20f,30f,3))
                //model.insertEntryReturnId(EntryData(8,650.0,650.0,1639698825,25f,35f,3))
                //model.insertEntryReturnId(EntryData(9,700.0,700.0,1639698825,23f,43f,3))


            }
            else
            {
                Snackbar.make(binding.root,
                    "You must have 5 images or more in the database to run this. Nothing happened"
                    , Snackbar.LENGTH_LONG).show()
            }

        }

        binding.DebugButton1.setOnClickListener {

            //model.create_insert_entry(1,12f,13f,100.0,1234.0,1639698825)
        }

        binding.DebugButton2.setOnClickListener {
            model.create_insert_trip("Created by DebugButton2","DebugButton2World",1639698825f)
        }


        return binding.root
    }

}