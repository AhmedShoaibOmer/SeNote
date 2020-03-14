/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.editor;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import com.aso.senote.R;
import com.aso.senote.data.models.Note;
import com.aso.senote.databinding.FragmentNoteBinding;
import com.aso.senote.databinding.FragmentNoteEditorActionbarBinding;
import com.aso.senote.databinding.FragmentNotePreferencePanelBinding;
import com.aso.senote.util.KeyboardUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;

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
    private static final String TAG = "SEditor";

    private final FragmentNoteEditorActionbarBinding editorActionbarBinding;
    private final FragmentNotePreferencePanelBinding preferencesPanelBinding;
    private final Context mContext;
    private final View mView;
    private final TextInputEditText textInputET, noteTitleET;
    private final MaterialToolbar actionBarToolBar;
    private Note selectedNote;

    private EditorAction editorAction;
    private Markwon markwon;
    private BottomSheetBehavior mEditorActionbarBottomSheetBehavior, mPreferencesBottomSheetBehavior;

    private boolean isKeyboardVisible = false;
    private boolean isPreferencesVisible = false;
    private boolean isActionbarBottomSheetVisible = false;
    private boolean isInEditMode = false;
    private boolean isNewNote = false;
    private Menu menu;
    private NoteListener noteListener;
//    private AddEditNoteFragment.BottomToolbarListener bottomToolbarListener ;


    public SEditor(Context mContext, FragmentNoteBinding binding, NoteListener listener) {
        this.mContext = mContext;
        this.mView = binding.getRoot();
        this.textInputET = binding.contentInput;
        this.noteTitleET = binding.noteTitle;
        this.actionBarToolBar = binding.toolbar;
        this.noteListener = listener;
        this.editorActionbarBinding = binding.editorActionbar;
        this.preferencesPanelBinding = binding.editorPreferencesPanel;
    }

/*    public void initializeEditor() {
        initToolbar();
        initKeyboardListener();
        initPreferencePanel();
        initEditorActionsBottomSheet();
        initMarkwon();
        setOnClickListener();
    }

    public void startPreviewMode() {
        isInEditMode = false;
        if (isActionbarBottomSheetVisible) toggleShowEditorActionbarBottomSheet();
        //toggleShowBottomToolbar();
        toggleTopToolbarNavigationIcon();
        menu.findItem(R.id.action_save).setVisible(false);
        menu.findItem(R.id.action_edit).setVisible(true);
        menu.findItem(R.id.action_more).setVisible(true);

        // parse markdown to commonmark-java Node
        final Node node;
        if (!selectedNote.getNoteContent().isEmpty() && selectedNote.getNoteContent() != null) {
            node = markwon.parse(selectedNote.getNoteContent());
            Log.d(TAG, "startPreviewMode: it do has content");
        } else node = markwon.parse("");

        // create styled text from parsed Node
        final Spanned markdown = markwon.render(node);

        // use it on a TextView
        markwon.setParsedMarkdown(textInputET, markdown);
        noteTitleET.setText(selectedNote.getNoteTitle());

        //Preview mode
        textInputET.setClickable(false);
        textInputET.setLongClickable(false);
        textInputET.setFocusableInTouchMode(false);
        textInputET.setFocusable(false);
        noteTitleET.setClickable(false);
        noteTitleET.setLongClickable(false);
        noteTitleET.setFocusableInTouchMode(false);
        noteTitleET.setFocusable(false);
    }

    public void startEditMode() {
        isInEditMode = true;
        //toggleShowBottomToolbar();
        if (isPreferencesVisible) toggleShowPreferences();
        toggleTopToolbarNavigationIcon();

        menu.findItem(R.id.action_save).setVisible(true);
        menu.findItem(R.id.action_edit).setVisible(false);
        menu.findItem(R.id.action_more).setVisible(false);
        noteTitleET.setText(selectedNote.noteTitle);
        textInputET.setText(selectedNote.noteContent);
        //Edit Mode
        textInputET.setClickable(true);
        textInputET.setLongClickable(true);
        textInputET.setFocusableInTouchMode(true);
        textInputET.setFocusable(true);
        noteTitleET.setClickable(true);
        noteTitleET.setLongClickable(true);
        noteTitleET.setFocusableInTouchMode(true);
        noteTitleET.setFocusable(true);
        textInputET.requestFocus();
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
        linaSP = preferencesPanelBinding.lineSpacingSetter;
        marginLeft = preferencesPanelBinding.leftMarginSpacingSetter;
        marginRight = preferencesPanelBinding.rightMarginSpacingSetter;

        linaSP.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textInputET.setLineSpacing((float) progress, 1.0f);
                noteTitleET.setLineSpacing((float) progress, 1.0f);
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

                noteTitleET.setPadding(
                        noteTitleET.getPaddingLeft(),
                        noteTitleET.getPaddingTop(), dpAsPxls,
                        noteTitleET.getPaddingBottom());
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
        KeyboardUtils.addKeyboardToggleListener(mView, mContext, isVisible -> {
            if (isVisible) {
                if (!isActionbarBottomSheetVisible) toggleShowEditorActionbarBottomSheet();
                isKeyboardVisible = true;
            } else {
                if (isActionbarBottomSheetVisible) toggleShowEditorActionbarBottomSheet();
                isKeyboardVisible = false;
            }
        });

    }

    private void initToolbar() {
        actionBarToolBar.inflateMenu(R.menu.top_note_fragment_menu);
        menu = actionBarToolBar.getMenu();
        actionBarToolBar.setNavigationOnClickListener(v -> {
            if (isInEditMode) {
                if (!selectedNote.noteContent.isEmpty() && selectedNote.noteContent != null) {
                    startPreviewMode();
                } else {
                    if (isKeyboardVisible) {
                        KeyboardUtils.forceCloseKeyboard(mView);
                    }
                    Navigation.findNavController(mView).navigateUp();
                }
            } else {
                Navigation.findNavController(mView).navigateUp();
            }
        });


        actionBarToolBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_edit:
                    // TODO: 3/7/20 Animate icons toggle

                    startEditMode();

                    return true;
                case R.id.action_save:
                    saveNote();
                    startPreviewMode();
                    return true;
                default:
                    return false;
            }
        });
    }*/
/*
    private void saveNote() {
        if (selectedNote.noteContent.isEmpty() && selectedNote.noteTitle.isEmpty())
            isNewNote = true;

        selectedNote.noteContent = Objects.requireNonNull(textInputET.getText()).toString();
        selectedNote.noteTitle = Objects.requireNonNull(noteTitleET.getText()).toString();

        if (!selectedNote.noteTitle.isEmpty() && selectedNote.noteTitle != null) {

            if (!selectedNote.noteContent.isEmpty() && selectedNote.noteContent != null) {
                isNewNote = noteListener.saveNote(selectedNote, isNewNote);
            } else {
                selectedNote.noteContent = noteTitleET.getText().toString();
                isNewNote = noteListener.saveNote(selectedNote, isNewNote);
            }
        } else {
            if (!selectedNote.noteContent.isEmpty() && selectedNote.noteContent != null) {
                isNewNote = noteListener.saveNote(selectedNote, isNewNote);
            } else {
                if (isKeyboardVisible) KeyboardUtils.forceCloseKeyboard(mView);
                Navigation.findNavController(mView).navigateUp();
            }
        }
    }*/

    private void initEditorActionsBottomSheet() {
        ConstraintLayout actionBarBottomSheet = editorActionbarBinding.editorActionbar;
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

        NestedScrollView preferencesBottomSheet = preferencesPanelBinding.noteFragmentPreferences;
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
        editorActionbarBinding.heading.setOnClickListener(this);
        editorActionbarBinding.bold.setOnClickListener(this);
        editorActionbarBinding.italic.setOnClickListener(this);
        editorActionbarBinding.strikeThrough.setOnClickListener(this);
        editorActionbarBinding.quote.setOnClickListener(this);
        editorActionbarBinding.listNumber.setOnClickListener(this);
        editorActionbarBinding.listBullet.setOnClickListener(this);
        editorActionbarBinding.link.setOnClickListener(this);
        editorActionbarBinding.image.setOnClickListener(this);
        editorActionbarBinding.alignCenter.setOnClickListener(this);
        editorActionbarBinding.alignJustify.setOnClickListener(this);
        editorActionbarBinding.alignLeft.setOnClickListener(this);
        editorActionbarBinding.alignRight.setOnClickListener(this);
        editorActionbarBinding.underline.setOnClickListener(this);
        editorActionbarBinding.code.setOnClickListener(this);
        editorActionbarBinding.erase.setOnClickListener(this);
        editorActionbarBinding.color.setOnClickListener(this);
        editorActionbarBinding.indent.setOnClickListener(this);
        editorActionbarBinding.outdent.setOnClickListener(this);
        editorActionbarBinding.actionUndo.setOnClickListener(this);
        editorActionbarBinding.actionRedo.setOnClickListener(this);

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

    public void copyToClipboard() {
        //TODO Implement copy to clipboard method
    }

    private void toggleShowEditorActionbarBottomSheet() {

        if (isPreferencesVisible) {
            toggleShowPreferences();
        }
        mEditorActionbarBottomSheetBehavior.setState(isActionbarBottomSheetVisible ? BottomSheetBehavior.STATE_HIDDEN : BottomSheetBehavior.STATE_EXPANDED);
    }

    public void toggleShowPreferences() {
        mPreferencesBottomSheetBehavior.setState(isPreferencesVisible ? BottomSheetBehavior.STATE_HIDDEN : BottomSheetBehavior.STATE_EXPANDED);
        isPreferencesVisible = !isPreferencesVisible;
    }

    /*private void toggleShowBottomToolbar() {
        bottomToolbarListener.toggleVisibility(isInEditMode);
    }*/

    private void toggleTopToolbarNavigationIcon() {
        if (isInEditMode) {
            actionBarToolBar.setNavigationIcon(R.drawable.ic_note_fragment_close_24dp);
        } else {
            actionBarToolBar.setNavigationIcon(R.drawable.ic_note_fragment_back);

        }
    }

   /* public void setNote(Note note) {
        this.selectedNote = note;
        bottomToolbarListener.setStarred(selectedNote.isStarred());
    }

    public void setBottomToolbarListener(AddEditNoteFragment.BottomToolbarListener bottomToolbarListener) {
        this.bottomToolbarListener = bottomToolbarListener;

    }*/

    public Note getNote() {
        return this.selectedNote;
    }


    public interface NoteListener {
        boolean saveNote(Note note, boolean isNewNote);
    }

    public interface BottomToolbarMenuListener {
        void togglePreferencesMenu();

        void setStarred(boolean isStarred);
    }
}
