/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.data.models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "notebook_table", indices = @Index(value = "notebookId"))
public class Notebook {
    @ColumnInfo(name = "date_created")
    private Calendar dateCreated;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "notebookId")
    private long notebookId;
    private String notebookTitle;
    private String notebookCoverUri;
    private Calendar lastModified;

    public Notebook(String notebookTitle, String notebookCoverUri) {
        this.notebookTitle = notebookTitle;
        this.notebookCoverUri = notebookCoverUri;
        lastModified = Calendar.getInstance();
        // TODO: 3/14/20 Deal with the Time zone Differing
        dateCreated = Calendar.getInstance();
    }

    public Calendar getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Calendar dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getNotebookId() {
        return notebookId;
    }

    public void setNotebookId(long notebookId) {
        this.notebookId = notebookId;
    }

    public String getNotebookTitle() {
        return notebookTitle;
    }

    public void setNotebookTitle(String notebookTitle) {
        this.notebookTitle = notebookTitle;
    }

    public String getNotebookCoverUri() {
        return notebookCoverUri;
    }

    public void setNotebookCoverUri(String notebookCoverUri) {
        this.notebookCoverUri = notebookCoverUri;
    }

    public Calendar getLastModified() {
        return lastModified;
    }

    public void setLastModified(Calendar lastModified) {
        this.lastModified = lastModified;
    }
}
