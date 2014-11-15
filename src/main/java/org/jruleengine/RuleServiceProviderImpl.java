package org.jruleengine;

import javax.rules.*;
import javax.rules.admin.RuleAdministrator;


/**
 * <p>Title: JRuleEngine Project</p>
 * <p>Description: Service Provider Implementation.</p>
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
public class RuleServiceProviderImpl extends RuleServiceProvider {

    public RuleServiceProviderImpl() { }


    public RuleRuntime getRuleRuntime() throws ConfigurationException {
      try {
        return (RuleRuntime) createInstance("org.jruleengine.RuleRuntimeImpl");
      }
      catch (Exception ex) {
        throw new ConfigurationException("Can't create RuleRuntime", ex);
      }
    }


    public RuleAdministrator getRuleAdministrator() throws ConfigurationException {
      try {
        return (RuleAdministrator) createInstance(
            "org.jruleengine.RuleAdministratorImpl");
      }
      catch (Exception ex) {
        throw new ConfigurationException("Can't create RuleAdministrator", ex);
      }
    }


    static {
      try {
          RuleServiceProviderManager.registerRuleServiceProvider("org.jruleengine", RuleServiceProviderImpl.class);
      }
      catch(ConfigurationException ce) {
          ce.printStackTrace();
      }
    }
}
