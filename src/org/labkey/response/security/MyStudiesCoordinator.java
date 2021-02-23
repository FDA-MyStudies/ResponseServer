package org.labkey.response.security;

import org.labkey.api.security.roles.AbstractModuleScopedRole;
import org.labkey.response.ResponseModule;

public class MyStudiesCoordinator extends AbstractModuleScopedRole
{
    public MyStudiesCoordinator()
    {
        super("MyStudies Coordinator", "May generate batches of enrollment tokens.", ResponseModule.class, GenerateEnrollmentTokensPermission.class);
        excludeGuests();
    }
}
