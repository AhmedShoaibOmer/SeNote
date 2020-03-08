/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.editor;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.Spanned;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.aso.senote.R;
import com.aso.senote.util.KeyboardUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.commonmark.node.Node;

import java.util.Objects;
import java.util.concurrent.Executors;

import io.noties.markwon.AbstractMarkwonPlugin;
import io.noties.markwon.Markwon;
import io.noties.markwon.core.MarkwonTheme;
import io.noties.markwon.editor.MarkwonEditor;
import io.noties.markwon.editor.MarkwonEditorTextWatcher;
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin;
import io.noties.markwon.ext.tables.TablePlugin;
import io.noties.markwon.image.AsyncDrawable;
import io.noties.markwon.image.glide.GlideImagesPlugin;

public class SEditor implements View.OnClickListener {

    private Context mContext;
    private View mView;
    private EditorAction editorAction;
    private Markwon markwon;

    private CoordinatorLayout parentLayout;
    private TextInputEditText textInputET, noteTitleET;
    private BottomSheetBehavior mEditorActionbarBottomSheetBehavior, mPreferencesBottomSheetBehavior;
    private FloatingActionButton copyFab;
    private BottomAppBar bottomToolbar;
    private MaterialToolbar actionBarToolBar;

    private boolean isKeyboardVisible = false;
    private boolean isPreferencesVisible = false;
    private boolean isActionbarBottomSheetVisible = false;
    private boolean isInEditMode = false;


    public SEditor(Context mContext, View mView, CoordinatorLayout parentLayout,
                   TextInputEditText textInputET, TextInputEditText noteTitleET,
                   FloatingActionButton copyFab, BottomAppBar bottomToolbar,
                   MaterialToolbar actionBarToolBar) {
        this.mContext = mContext;
        this.mView = mView;
        this.parentLayout = parentLayout;
        this.textInputET = textInputET;
        this.noteTitleET = noteTitleET;
        this.copyFab = copyFab;
        this.bottomToolbar = bottomToolbar;
        this.actionBarToolBar = actionBarToolBar;
        initializeEditor();
    }

    private void initializeEditor() {
        initToolbar();
        initKeyboardListener();
        initPreferencePanel();
        initBottomToolbar();
        initEditorActionsBottomSheet();
        initMarkwon();
        setOnClickListener();
    }


    public void startPreviewMode() {
        isInEditMode = false;
        if (isActionbarBottomSheetVisible) toggleShowEditorActionbarBottomSheet();
        toggleShowBottomToolbar();
        toggleTopToolbarNavigationIcon();

        // parse markdown to commonmark-java Node
        final Node node = markwon.parse(Objects.requireNonNull(textInputET.getText()).toString());

        // create styled text from parsed Node
        final Spanned markdown = markwon.render(node);

        // use it on a TextView
        markwon.setParsedMarkdown(textInputET, markdown);

        //Preview mode
        textInputET.setFocusableInTouchMode(false);
        textInputET.setFocusable(false);
        noteTitleET.setFocusableInTouchMode(false);
        noteTitleET.setFocusable(false);
        textInputET.setInputType(InputType.TYPE_NULL);
        noteTitleET.setInputType(InputType.TYPE_NULL);
    }

    public void startEditMode() {
        isInEditMode = true;
        toggleShowBottomToolbar();
        if (isPreferencesVisible) toggleShowPreferences();
        toggleTopToolbarNavigationIcon();

        //Edit Mode
        textInputET.setFocusableInTouchMode(true);
        textInputET.setFocusable(true);
        noteTitleET.setFocusableInTouchMode(true);
        noteTitleET.setFocusable(true);
        textInputET.requestFocus();
        textInputET.setInputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        noteTitleET.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    public boolean handleOnBackPressed() {
        if (isKeyboardVisible) {
            KeyboardUtils.forceCloseKeyboard(mView);
            return true;
        } else // TODO: 3/8/20 Implement alert dialoge to save
            if (isPreferencesVisible) {
                toggleShowPreferences();
                return true;
            } else return isInEditMode;
    }

    private void initPreferencePanel() {
        SeekBar linaSP, marginLeft, marginRight;
        linaSP = mView.findViewById(R.id.line_spacing_setter);
        marginLeft = mView.findViewById(R.id.left_margin_spacing_setter);
        marginRight = mView.findViewById(R.id.right_margin_spacing_setter);

        linaSP.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textInputET.setLineSpacing((float) progress, 1.0f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        marginRight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float scale = mContext.getResources().getDisplayMetrics().density;
                int dpAsPxls = (int) (progress * scale + 0.5F);
                textInputET.setPadding(textInputET.getPaddingLeft(),
                        textInputET.getPaddingTop(), dpAsPxls,
                        textInputET.getPaddingBottom());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        marginLeft.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float scale = mContext.getResources().getDisplayMetrics().density;
                int dpAsPxls = (int) (progress * scale + 0.5F);
                textInputET.setPadding(dpAsPxls, textInputET.getPaddingTop()
                        , textInputET.getPaddingRight(), textInputET.getPaddingBottom());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void initKeyboardListener() {
        KeyboardUtils.addKeyboardToggleListener(mView, mContext, new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                if (isVisible) {
                    if (!isActionbarBottomSheetVisible) toggleShowEditorActionbarBottomSheet();
                    isKeyboardVisible = true;
                } else {
                    if (isActionbarBottomSheetVisible) toggleShowEditorActionbarBottomSheet();
                    isKeyboardVisible = false;
                }
            }
        });

    }

    private void initToolbar() {
        actionBarToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInEditMode) {

                } else {

                }
            }
        });
        actionBarToolBar.inflateMenu(R.menu.note_fragment_menu);
        final Menu menu = actionBarToolBar.getMenu();
        actionBarToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit:
                        // TODO: 3/7/20 Animate icons toggle
                        menu.findItem(R.id.action_save).setVisible(true);
                        menu.findItem(R.id.action_edit).setVisible(false);
                        startEditMode();

                        return true;
                    case R.id.action_save:

                        menu.findItem(R.id.action_save).setVisible(false);
                        menu.findItem(R.id.action_edit).setVisible(true);
                        startPreviewMode();

                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void initBottomToolbar() {


        bottomToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 3/7/20 Implement Change Paper Dialog
            }
        });

        bottomToolbar.inflateMenu(R.menu.note_fragment_bottom_menu);
        Menu menu = bottomToolbar.getMenu();
        bottomToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_more_note_fragment:
                        return true;
                    case R.id.action_preferences:
                        toggleShowPreferences();
                        return true;
                    default:
                        return false;
                }
            }
        });


    }

    private void initEditorActionsBottomSheet() {
        ConstraintLayout actionBarBottomSheet = mView.findViewById(R.id.note_fragment_editor_actionbar);
        mEditorActionbarBottomSheetBehavior = BottomSheetBehavior.from(actionBarBottomSheet);
        mEditorActionbarBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mEditorActionbarBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        KeyboardUtils.forceCloseKeyboard(mView);
                        isActionbarBottomSheetVisible = false;
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        isActionbarBottomSheetVisible = true;
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        NestedScrollView preferencesBottomSheet = mView.findViewById(R.id.note_fragment_preferences);
        mPreferencesBottomSheetBehavior = BottomSheetBehavior.from(preferencesBottomSheet);
        mPreferencesBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mPreferencesBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        isPreferencesVisible = false;
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        isPreferencesVisible = true;
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void initMarkwon() {
        if (editorAction == null) {
            editorAction = new EditorAction();
        }
        editorAction = new EditorAction(mContext, textInputET);

        markwon = Markwon.builder(mContext)
                .usePlugin(StrikethroughPlugin.create())
                .usePlugin(TablePlugin.create(mContext))
                .usePlugin(GlideImagesPlugin.create(new GlideImagesPlugin.GlideStore() {
                    @NonNull
                    @Override
                    public RequestBuilder<Drawable> load(@NonNull AsyncDrawable drawable) {
                        return Glide.with(mContext).load(drawable.getDestination());
                    }

                    @Override
                    public void cancel(@NonNull Target<?> target) {
                        Glide.with(mContext).clear(target);
                    }
                }))
                .usePlugin(new AbstractMarkwonPlugin() {
                    @Override
                    public void configureTheme(@NonNull MarkwonTheme.Builder builder) {
                        //builder
                        //.codeTextColor(Color.BLACK)
                        //.codeBackgroundColor(Color.GREEN);
                    }
                })
                .build();
        final MarkwonEditor editor = MarkwonEditor.create(markwon);
        textInputET.addTextChangedListener(MarkwonEditorTextWatcher.withPreRender(
                editor,
                Executors.newCachedThreadPool(),
                textInputET));
    }

    private void setOnClickListener() {
        //Note actionbar scrollView
        mView.findViewById(R.id.heading).setOnClickListener(this);
        mView.findViewById(R.id.bold).setOnClickListener(this);
        mView.findViewById(R.id.italic).setOnClickListener(this);
        mView.findViewById(R.id.strike_through).setOnClickListener(this);
        mView.findViewById(R.id.quote).setOnClickListener(this);
        mView.findViewById(R.id.list_number).setOnClickListener(this);
        mView.findViewById(R.id.list_bullet).setOnClickListener(this);
        mView.findViewById(R.id.link).setOnClickListener(this);
        mView.findViewById(R.id.image).setOnClickListener(this);
        mView.findViewById(R.id.align_center).setOnClickListener(this);
        mView.findViewById(R.id.align_justify).setOnClickListener(this);
        mView.findViewById(R.id.align_left).setOnClickListener(this);
        mView.findViewById(R.id.align_right).setOnClickListener(this);
        mView.findViewById(R.id.underline).setOnClickListener(this);
        mView.findViewById(R.id.code).setOnClickListener(this);
        mView.findViewById(R.id.erase).setOnClickListener(this);
        mView.findViewById(R.id.color).setOnClickListener(this);
        mView.findViewById(R.id.indent).setOnClickListener(this);
        mView.findViewById(R.id.outdent).setOnClickListener(this);
        mView.findViewById(R.id.action_undo).setOnClickListener(this);
        mView.findViewById(R.id.action_redo).setOnClickListener(this);

        copyFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Implenent copy to clipboard method
            }
        });
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.heading:
                editorAction.heading();
                break;
            case R.id.bold:
                editorAction.bold();
                break;
            case R.id.italic:
                editorAction.italic();
                break;
            case R.id.code:
                editorAction.insertCode();
                break;
            case R.id.quote:
                editorAction.quote();
                break;
            case R.id.list_number:
                editorAction.orderedList();
                break;
            case R.id.list_bullet:
                editorAction.unorderedList();
                break;
            case R.id.link:
                editorAction.insertLink();
                break;
            case R.id.align_center:
                editorAction.align(EditorAction.TextAlignment.CENTER);
                break;
            case R.id.align_justify:
                editorAction.align(EditorAction.TextAlignment.JUSTIFY);
                break;
            case R.id.align_left:
                editorAction.align(EditorAction.TextAlignment.LEFT);
                break;
            case R.id.align_right:
                editorAction.align(EditorAction.TextAlignment.RIGHT);
                break;
            case R.id.underline:
                editorAction.underline();
                break;
            case R.id.strike_through:
                editorAction.strikeThrough();
                break;
            case R.id.image:
                /*/ insert image
                AlertDialog.Builder inputDialog = new AlertDialog.Builder(mContext);
                inputDialog.setTitle(R.string.dialog_title_insert_image);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_insert_image, null);
                inputDialog.setView(dialogView);
                inputDialog.setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                inputDialog.setPositiveButton(R.string.dialog_btn_insert,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText imageDisplayText = dialogView.findViewById(
                                        R.id.image_display_text);
                                EditText imageUri = dialogView.findViewById(R.id.image_uri);
                                editorAction.insertImage(imageDisplayText.getText().toString(),
                                        imageUri.getText().toString());
                            }
                        });
                inputDialog.show();*/
                break;
            case R.id.indent:
                editorAction.indent();
                break;
            case R.id.outdent:
                editorAction.outdent();
                break;
            case R.id.erase:
                editorAction.erase();
                break;
            case R.id.action_undo:
                editorAction.undo();
                break;
            case R.id.color:
                editorAction.color();
                break;
            case R.id.action_redo:
                editorAction.redo();
                break;
            default:
                break;
        }
    }

    private void toggleShowEditorActionbarBottomSheet() {

        if (isPreferencesVisible) {
            toggleShowPreferences();
        }
        mEditorActionbarBottomSheetBehavior.setState(isActionbarBottomSheetVisible ? BottomSheetBehavior.STATE_HIDDEN : BottomSheetBehavior.STATE_EXPANDED);
    }

    private void toggleShowPreferences() {
        mPreferencesBottomSheetBehavior.setState(isPreferencesVisible ? BottomSheetBehavior.STATE_HIDDEN : BottomSheetBehavior.STATE_EXPANDED);
        isPreferencesVisible = !isPreferencesVisible;
    }

    private void toggleShowBottomToolbar() {

        Transition transition = new Slide(Gravity.BOTTOM);
        transition.setDuration(600);
        transition.addTarget(bottomToolbar);

        TransitionManager.beginDelayedTransition(parentLayout, transition);
        bottomToolbar.setVisibility(isInEditMode ? View.GONE : View.VISIBLE);
        if (isInEditMode) copyFab.hide();
        else copyFab.show();
    }

    private void toggleTopToolbarNavigationIcon() {
        if (isInEditMode) {
            actionBarToolBar.setNavigationIcon(R.drawable.ic_note_fragment_close_24dp);
        } else {
            actionBarToolBar.setNavigationIcon(R.drawable.ic_note_fragment_back);

        }
    }

}
