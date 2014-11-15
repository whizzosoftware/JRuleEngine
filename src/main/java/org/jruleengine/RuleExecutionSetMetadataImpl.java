package org.jruleengine;

import javax.rules.RuleExecutionSetMetadata;


/**
 * <p>Title: JRuleEngine Project</p>
 * <p>Description: This class exposes some simple properties of the RuleExecutionSet to the runtime user.
 * This interface can be extended by rule engine providers to expose additional proprietary properties to the runtime user.
 * It is recommended but not required that any properties that are exposed in such extensions be read only,
 * and that their values be static for the duration of the RuleSession. </p>
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
public class RuleExecutionSetMetadataImpl implements RuleExecutionSetMetadata {

  /** rule execution set URI */
  private String uri;

  /** rule execution set name */
  private String name;

  /** rule execution set description */
  private String description;


  /**
   * @param impl rule execution set implementation
   */
  RuleExecutionSetMetadataImpl(RuleExecutionSetImpl impl) {
    name = impl.getName();
    description = impl.getDescription();
    uri = impl.getUri();
  }


  /**
   * @return rule execution set URI
   */
  public final String getUri() {
    return uri;
  }


  /**
   * @return rule execution set name
   */
  public final String getName() {
    return name;
  }


  /**
   * @return rule execution set description
   */
  public final String getDescription() {
    return description;
  }

}
