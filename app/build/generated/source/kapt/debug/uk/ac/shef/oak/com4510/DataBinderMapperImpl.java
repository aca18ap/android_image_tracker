package uk.ac.shef.oak.com4510;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import uk.ac.shef.oak.com4510.databinding.ActivityMainBindingImpl;
import uk.ac.shef.oak.com4510.databinding.FragmentEditImageBindingImpl;
import uk.ac.shef.oak.com4510.databinding.FragmentGalleryBindingImpl;
import uk.ac.shef.oak.com4510.databinding.FragmentHomeBindingImpl;
import uk.ac.shef.oak.com4510.databinding.FragmentNewTripBindingImpl;
import uk.ac.shef.oak.com4510.databinding.FragmentShowImageBindingImpl;
import uk.ac.shef.oak.com4510.databinding.FragmentTravellingBindingImpl;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_ACTIVITYMAIN = 1;

  private static final int LAYOUT_FRAGMENTEDITIMAGE = 2;

  private static final int LAYOUT_FRAGMENTGALLERY = 3;

  private static final int LAYOUT_FRAGMENTHOME = 4;

  private static final int LAYOUT_FRAGMENTNEWTRIP = 5;

  private static final int LAYOUT_FRAGMENTSHOWIMAGE = 6;

  private static final int LAYOUT_FRAGMENTTRAVELLING = 7;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(7);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(uk.ac.shef.oak.com4510.R.layout.activity_main, LAYOUT_ACTIVITYMAIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(uk.ac.shef.oak.com4510.R.layout.fragment_edit_image, LAYOUT_FRAGMENTEDITIMAGE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(uk.ac.shef.oak.com4510.R.layout.fragment_gallery, LAYOUT_FRAGMENTGALLERY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(uk.ac.shef.oak.com4510.R.layout.fragment_home, LAYOUT_FRAGMENTHOME);
    INTERNAL_LAYOUT_ID_LOOKUP.put(uk.ac.shef.oak.com4510.R.layout.fragment_new_trip, LAYOUT_FRAGMENTNEWTRIP);
    INTERNAL_LAYOUT_ID_LOOKUP.put(uk.ac.shef.oak.com4510.R.layout.fragment_show_image, LAYOUT_FRAGMENTSHOWIMAGE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(uk.ac.shef.oak.com4510.R.layout.fragment_travelling, LAYOUT_FRAGMENTTRAVELLING);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_ACTIVITYMAIN: {
          if ("layout/activity_main_0".equals(tag)) {
            return new ActivityMainBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_main is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTEDITIMAGE: {
          if ("layout/fragment_edit_image_0".equals(tag)) {
            return new FragmentEditImageBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_edit_image is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTGALLERY: {
          if ("layout/fragment_gallery_0".equals(tag)) {
            return new FragmentGalleryBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_gallery is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTHOME: {
          if ("layout/fragment_home_0".equals(tag)) {
            return new FragmentHomeBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_home is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTNEWTRIP: {
          if ("layout/fragment_new_trip_0".equals(tag)) {
            return new FragmentNewTripBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_new_trip is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTSHOWIMAGE: {
          if ("layout/fragment_show_image_0".equals(tag)) {
            return new FragmentShowImageBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_show_image is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTTRAVELLING: {
          if ("layout/fragment_travelling_0".equals(tag)) {
            return new FragmentTravellingBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_travelling is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(1);

    static {
      sKeys.put(0, "_all");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(7);

    static {
      sKeys.put("layout/activity_main_0", uk.ac.shef.oak.com4510.R.layout.activity_main);
      sKeys.put("layout/fragment_edit_image_0", uk.ac.shef.oak.com4510.R.layout.fragment_edit_image);
      sKeys.put("layout/fragment_gallery_0", uk.ac.shef.oak.com4510.R.layout.fragment_gallery);
      sKeys.put("layout/fragment_home_0", uk.ac.shef.oak.com4510.R.layout.fragment_home);
      sKeys.put("layout/fragment_new_trip_0", uk.ac.shef.oak.com4510.R.layout.fragment_new_trip);
      sKeys.put("layout/fragment_show_image_0", uk.ac.shef.oak.com4510.R.layout.fragment_show_image);
      sKeys.put("layout/fragment_travelling_0", uk.ac.shef.oak.com4510.R.layout.fragment_travelling);
    }
  }
}
