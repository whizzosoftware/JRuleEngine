package org.jruleengine;

import javax.rules.Handle;


/**
 * <p>Title: JRuleEngine Project</p>
 * <p>Description: This class implements the handle interface.</p>
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
public class HandleImpl implements Handle {

  private Object object;


  public HandleImpl(Object o) {
    object = o;
  }


  public final Object getObject() {
    return object;
  }


  public final void setObject(Object o) {
    object = o;
  }


  public final boolean equals(Object o) {
    if(!(o instanceof HandleImpl))
      return false;
    else
      return object.equals(((HandleImpl)o).object);
  }


  public final int hashCode() {
    return object.hashCode();
  }


}