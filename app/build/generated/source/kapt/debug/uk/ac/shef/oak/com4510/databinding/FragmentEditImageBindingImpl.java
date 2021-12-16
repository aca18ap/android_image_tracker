package uk.ac.shef.oak.com4510.databinding;
import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentEditImageBindingImpl extends FragmentEditImageBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.edit_appBarLayout, 1);
        sViewsWithIds.put(R.id.editor_toolbar, 2);
        sViewsWithIds.put(R.id.edit_image, 3);
        sViewsWithIds.put(R.id.edit_image_title_label, 4);
        sViewsWithIds.put(R.id.edit_image_title, 5);
        sViewsWithIds.put(R.id.edit_image_description_label, 6);
        sViewsWithIds.put(R.id.edit_image_description, 7);
        sViewsWithIds.put(R.id.delete_button, 8);
        sViewsWithIds.put(R.id.save_button, 9);
        sViewsWithIds.put(R.id.cancel_button, 10);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentEditImageBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 11, sIncludes, sViewsWithIds));
    }
    private FragmentEditImageBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.Button) bindings[10]
            , (android.widget.Button) bindings[8]
            , (com.google.android.material.appbar.AppBarLayout) bindings[1]
            , (android.widget.ImageView) bindings[3]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[0]
            , (com.google.android.material.textfield.TextInputEditText) bindings[7]
            , (com.google.android.material.textfield.TextInputLayout) bindings[6]
            , (com.google.android.material.textfield.TextInputEditText) bindings[5]
            , (com.google.android.material.textfield.TextInputLayout) bindings[4]
            , (androidx.appcompat.widget.Toolbar) bindings[2]
            , (android.widget.Button) bindings[9]
            );
        this.editImageConstraint.setTag(null);
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