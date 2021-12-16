package uk.ac.shef.oak.com4510.databinding;
import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentShowImageBindingImpl extends FragmentShowImageBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.show_appBarLayout, 1);
        sViewsWithIds.put(R.id.show_toolbar, 2);
        sViewsWithIds.put(R.id.fab_edit, 3);
        sViewsWithIds.put(R.id.show_image, 4);
        sViewsWithIds.put(R.id.show_description_button, 5);
        sViewsWithIds.put(R.id.show_map_button, 6);
        sViewsWithIds.put(R.id.show_sensors_button, 7);
        sViewsWithIds.put(R.id.show_image_description, 8);
        sViewsWithIds.put(R.id.show_map, 9);
        sViewsWithIds.put(R.id.show_sensors, 10);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentShowImageBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 11, sIncludes, sViewsWithIds));
    }
    private FragmentShowImageBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.google.android.material.floatingactionbutton.FloatingActionButton) bindings[3]
            , (com.google.android.material.appbar.AppBarLayout) bindings[1]
            , (android.widget.Button) bindings[5]
            , (android.widget.ImageView) bindings[4]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[0]
            , (android.widget.TextView) bindings[8]
            , (com.google.android.gms.maps.MapView) bindings[9]
            , (android.widget.Button) bindings[6]
            , (android.widget.TextView) bindings[10]
            , (android.widget.Button) bindings[7]
            , (androidx.appcompat.widget.Toolbar) bindings[2]
            );
        this.showImageConstraint.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x1L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
            return variableSet;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        // batch finished
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}