package org.labkey.mobileappsurvey.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.labkey.api.data.Container;
import org.labkey.api.security.User;

import java.util.Date;

/**
 * Model of database table representing a study in the mobileappsurvey schema.
 */
public class MobileAppStudy
{
    private Integer _rowId;
    private String _shortName;
    private Boolean _isEditable = true;
    private Container _container;
    private Date _created;
    private User _createdBy;

    public String getShortName()
    {
        return _shortName;
    }

    public void setShortName(String shortName)
    {
        _shortName = shortName;
    }

    public Boolean getEditable()
    {
        return _isEditable;
    }

    public void setEditable(Boolean editable)
    {
        _isEditable = editable;
    }

    public Container getContainer()
    {
        return _container;
    }

    public void setContainer(Container container)
    {
        _container = container;
    }

    public Date getCreated()
    {
        return _created;
    }

    public void setCreated(Date created)
    {
        _created = created;
    }

    @JsonIgnore
    public User getCreatedBy()
    {
        return _createdBy;
    }

    public void setCreatedBy(User createdBy)
    {
        _createdBy = createdBy;
    }

    public Integer getRowId()
    {
        return _rowId;
    }

    public void setRowId(Integer rowId)
    {
        _rowId = rowId;
    }
}
