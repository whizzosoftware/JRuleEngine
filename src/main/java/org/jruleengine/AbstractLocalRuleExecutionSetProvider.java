package org.jruleengine;

import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;
import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

/**
 * <p>Title: JRuleEngine Project</p>
 * <p>Description: Local Rule Execution Set Provider Abstract Implementation.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of JRuleEngine project.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *       The author may be contacted at:
 *           maurocarniel@tin.it</p>
 *
 * @author Mauro Carniel
 * @version 1.0
 */
abstract public class AbstractLocalRuleExecutionSetProvider implements LocalRuleExecutionSetProvider {
    /**
     * Analyze className to find out if it contains a synonymn.
     * @param className className + "." + methodName
     * @param synonymns collection of name + classname
     * @return className + "." + methodName, without synonymn
     */
    protected String parseClassName(String className,Hashtable synonymns) {
        if (className.indexOf(".")==-1)
            return className;
        String methodName = className.substring(className.lastIndexOf(".")+1);
        className = className.substring(0,className.lastIndexOf("."));
        String realClassName = (String)synonymns.get(className);
        if (realClassName!=null)
            className = realClassName;
        return className+"."+methodName;
    }

    /**
     * Method called from createRuleExecutionSet.
     */
    protected RuleExecutionSet createRuleExecutionSetFromRuleList(Collection rules,Map props)
            throws RuleExecutionSetCreateException, IOException {
        try {
            RuleExecutionSetImpl rs;
            String name = "Untitled";
            String description = "Generic rule execution set";
            if (props != null) {
                if (props.get("name") != null) {
                    name = (String) props.get("name");
                }
                if (props.get("description") != null) {
                    description = (String) props.get("description");
                }
            }
            rs = new RuleExecutionSetImpl(name, description, null);
            rs.getRules().addAll(rules);

            return rs;
        }
        catch (Exception ex) {
            throw new RuleExecutionSetCreateException("Internal error", ex);
        }
    }
}
