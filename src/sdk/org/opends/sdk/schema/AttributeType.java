/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at
 * trunk/opends/resource/legal-notices/OpenDS.LICENSE
 * or https://OpenDS.dev.java.net/OpenDS.LICENSE.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at
 * trunk/opends/resource/legal-notices/OpenDS.LICENSE.  If applicable,
 * add the following below this CDDL HEADER, with the fields enclosed
 * by brackets "[]" replaced with your own identifying information:
 *      Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *      Copyright 2009 Sun Microsystems, Inc.
 */

package org.opends.sdk.schema;



import static org.opends.messages.SchemaMessages.*;
import static org.opends.sdk.schema.SchemaConstants.SCHEMA_PROPERTY_APPROX_RULE;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.opends.sdk.util.StaticUtils;
import org.opends.sdk.util.Validator;
import org.opends.messages.Message;



/**
 * This class defines a data structure for storing and interacting with
 * an attribute type, which contains information about the format of an
 * attribute and the syntax and matching rules that should be used when
 * interacting with it.
 * <p>
 * Where ordered sets of names, or extra properties are provided, the
 * ordering will be preserved when the associated fields are accessed
 * via their getters or via the {@link #toString()} methods.
 */
public final class AttributeType extends SchemaElement implements
    Comparable<AttributeType>
{

  // The approximate matching rule for this attribute type.
  private final String approximateMatchingRuleOID;

  // The attribute usage for this attribute type.
  private final AttributeUsage attributeUsage;

  // The definition string used to create this objectclass.
  private final String definition;

  // The equality matching rule for this attribute type.
  private final String equalityMatchingRuleOID;

  // Indicates whether this attribute type is declared "collective".
  private final boolean isCollective;

  // Indicates whether this attribute type is declared
  // "no-user-modification".
  private final boolean isNoUserModification;

  // Indicates whether this definition is declared "obsolete".
  private final boolean isObsolete;

  // Indicates whether this attribute type is declared "single-value".
  private final boolean isSingleValue;

  // The set of user defined names for this definition.
  private final List<String> names;

  // The OID that may be used to reference this definition.
  private final String oid;

  // The ordering matching rule for this attribute type.
  private final String orderingMatchingRuleOID;

  // The substring matching rule for this attribute type.
  private final String substringMatchingRuleOID;

  // The superior attribute type from which this attribute type
  // inherits.
  private final String superiorTypeOID;

  // The syntax for this attribute type.
  private final String syntaxOID;

  // True if this type has OID 2.5.4.0.
  private final boolean isObjectClassType;

  // The normalized name of this attribute type.
  private final String normalizedName;

  // The superior attribute type from which this attribute type
  // inherits.
  private AttributeType superiorType;

  // The equality matching rule for this attribute type.
  private MatchingRule equalityMatchingRule;

  // The ordering matching rule for this attribute type.
  private MatchingRule orderingMatchingRule;

  // The substring matching rule for this attribute type.
  private MatchingRule substringMatchingRule;

  // The approximate matching rule for this attribute type.
  private MatchingRule approximateMatchingRule;

  // The syntax for this attribute type.
  private Syntax syntax;



  AttributeType(String oid, List<String> names, String description,
      boolean obsolete, String superiorType,
      String equalityMatchingRule, String orderingMatchingRule,
      String substringMatchingRule, String approximateMatchingRule,
      String syntax, boolean singleValue, boolean collective,
      boolean noUserModification, AttributeUsage attributeUsage,
      Map<String, List<String>> extraProperties, String definition)
  {
    super(description, extraProperties);

    Validator.ensureNotNull(oid, names, description, attributeUsage);
    Validator.ensureTrue(superiorType != null || syntax != null,
        "superiorType and/or syntax must not be null");
    Validator.ensureNotNull(extraProperties);

    this.oid = oid;
    this.names = names;
    this.isObsolete = obsolete;
    this.superiorTypeOID = superiorType;
    this.equalityMatchingRuleOID = equalityMatchingRule;
    this.orderingMatchingRuleOID = orderingMatchingRule;
    this.substringMatchingRuleOID = substringMatchingRule;
    this.approximateMatchingRuleOID = approximateMatchingRule;
    this.syntaxOID = syntax;
    this.isSingleValue = singleValue;
    this.isCollective = collective;
    this.isNoUserModification = noUserModification;
    this.attributeUsage = attributeUsage;

    if (definition != null)
    {
      this.definition = definition;
    }
    else
    {
      this.definition = buildDefinition();
    }

    this.isObjectClassType = oid.equals("2.5.4.0");
    this.normalizedName = StaticUtils.toLowerCase(getNameOrOID());
  }



  /**
   * Compares this attribute type to the provided attribute type. The
   * sort-order is defined as follows:
   * <ul>
   * <li>The {@code objectClass} attribute is less than all other
   * attribute types.
   * <li>User attributes are less than operational attributes.
   * <li>Lexicographic comparison of the primary name or OID.
   * </ul>
   *
   * @param type
   *          The attribute type to be compared.
   * @return A negative integer, zero, or a positive integer as this
   *         attribute type is less than, equal to, or greater than the
   *         specified attribute type.
   * @throws NullPointerException
   *           If {@code name} was {@code null}.
   */
  public int compareTo(AttributeType type) throws NullPointerException
  {
    if (isObjectClassType)
    {
      return type.isObjectClassType ? 0 : -1;
    }
    else if (type.isObjectClassType)
    {
      return 1;
    }
    else
    {
      boolean isOperational = getUsage().isOperational();
      boolean typeIsOperational = type.getUsage().isOperational();

      if (isOperational == typeIsOperational)
      {
        return normalizedName.compareTo(type.normalizedName);
      }
      else
      {
        return isOperational ? 1 : -1;
      }
    }
  }



  /**
   * Retrieves the matching rule that should be used for approximate
   * matching with this attribute type.
   *
   * @return The matching rule that should be used for approximate
   *         matching with this attribute type.
   */
  public MatchingRule getApproximateMatchingRule()
  {
    return approximateMatchingRule;
  }



  /**
   * Retrieves the matching rule that should be used for equality
   * matching with this attribute type.
   *
   * @return The matching rule that should be used for equality matching
   *         with this attribute type.
   */
  public MatchingRule getEqualityMatchingRule()
  {
    return equalityMatchingRule;
  }



  /**
   * Retrieves the name or OID for this schema definition. If it has one
   * or more names, then the primary name will be returned. If it does
   * not have any names, then the OID will be returned.
   *
   * @return The name or OID for this schema definition.
   */
  public String getNameOrOID()
  {
    if (names.isEmpty())
    {
      return oid;
    }
    return names.get(0);
  }



  /**
   * Retrieves an iterable over the set of user-defined names that may
   * be used to reference this schema definition.
   *
   * @return Returns an iterable over the set of user-defined names that
   *         may be used to reference this schema definition.
   */
  public Iterable<String> getNames()
  {
    return names;
  }



  /**
   * Retrieves the OID for this schema definition.
   *
   * @return The OID for this schema definition.
   */
  public String getOID()
  {

    return oid;
  }



  /**
   * Retrieves the matching rule that should be used for ordering with
   * this attribute type.
   *
   * @return The matching rule that should be used for ordering with
   *         this attribute type.
   */
  public MatchingRule getOrderingMatchingRule()
  {
    return orderingMatchingRule;
  }



  /**
   * Retrieves the matching rule that should be used for substring
   * matching with this attribute type.
   *
   * @return The matching rule that should be used for substring
   *         matching with this attribute type.
   */
  public MatchingRule getSubstringMatchingRule()
  {
    return substringMatchingRule;
  }



  /**
   * Retrieves the superior type for this attribute type.
   *
   * @return The superior type for this attribute type, or
   *         <CODE>null</CODE> if it does not have one.
   */
  public AttributeType getSuperiorType()
  {
    return superiorType;
  }



  /**
   * Retrieves the syntax for this attribute type.
   *
   * @return The syntax for this attribute type.
   */
  public Syntax getSyntax()
  {
    return syntax;
  }



  /**
   * Retrieves the usage indicator for this attribute type.
   *
   * @return The usage indicator for this attribute type.
   */
  public AttributeUsage getUsage()
  {
    return attributeUsage;
  }



  @Override
  public int hashCode()
  {
    return oid.hashCode();
  }



  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }

    if (o instanceof AttributeType)
    {
      AttributeType other = (AttributeType) o;
      return oid.equals(other.oid);
    }

    return false;
  }



  /**
   * Indicates whether this schema definition has the specified name.
   *
   * @param name
   *          The name for which to make the determination.
   * @return {@code true} if the specified name is assigned to this
   *         schema definition, or {@code false} if not.
   */
  public boolean hasName(String name)
  {
    for (String n : names)
    {
      if (n.equalsIgnoreCase(name))
      {
        return true;
      }
    }
    return false;
  }



  /**
   * Indicates whether this schema definition has the specified name or
   * OID.
   *
   * @param value
   *          The value for which to make the determination.
   * @return {@code true} if the provided value matches the OID or one
   *         of the names assigned to this schema definition, or {@code
   *         false} if not.
   */
  public boolean hasNameOrOID(String value)
  {
    return hasName(value) || getOID().equals(value);
  }



  /**
   * Indicates whether this attribute type is declared "collective".
   *
   * @return {@code true} if this attribute type is declared
   *         "collective", or {@code false} if not.
   */
  public boolean isCollective()
  {
    return isCollective;
  }



  /**
   * Indicates whether this attribute type is declared
   * "no-user-modification".
   *
   * @return {@code true} if this attribute type is declared
   *         "no-user-modification", or {@code false} if not.
   */
  public boolean isNoUserModification()
  {
    return isNoUserModification;
  }



  /**
   * Indicates whether or not this attribute type is the {@code
   * objectClass} attribute type having the OID 2.5.4.0.
   *
   * @return {@code true} if this attribute type is the {@code
   *         objectClass} attribute type, or {@code false} if not.
   */
  public boolean isObjectClass()
  {
    return isObjectClassType;
  }



  /**
   * Indicates whether this schema definition is declared "obsolete".
   *
   * @return {@code true} if this schema definition is declared
   *         "obsolete", or {@code false} if not.
   */
  public boolean isObsolete()
  {
    return isObsolete;
  }



  /**
   * Indicates whether this attribute type is declared "single-value".
   *
   * @return {@code true} if this attribute type is declared
   *         "single-value", or {@code false} if not.
   */
  public boolean isSingleValue()
  {
    return isSingleValue;
  }



  /**
   * Indicates whether this is an operational attribute. An operational
   * attribute is one with a usage of "directoryOperation",
   * "distributedOperation", or "dSAOperation" (i.e., only
   * userApplications is not operational).
   *
   * @return {@code true} if this is an operational attribute, or
   *         {@code false} if not.
   */
  public boolean isOperational()
  {
    return attributeUsage.isOperational();
  }



  /**
   * Indicates whether or not this attribute type is a sub-type of the
   * provided attribute type.
   *
   * @param type
   *          The attribute type for which to make the determination.
   * @return {@code true} if this attribute type is a sub-type of the
   *         provided attribute type, or {@code false} if not.
   * @throws NullPointerException
   *           If {@code type} was {@code null}.
   */
  public boolean isSubTypeOf(AttributeType type)
  {
    AttributeType tmp = this;
    do
    {
      if (tmp.equals(type))
      {
        return true;
      }
      tmp = tmp.getSuperiorType();
    }
    while (tmp != null);
    return false;
  }



  /**
   * Retrieves the string representation of this schema definition in
   * the form specified in RFC 2252.
   *
   * @return The string representation of this schema definition in the
   *         form specified in RFC 2252.
   */
  @Override
  public String toString()
  {
    return definition;
  }



  AttributeType duplicate()
  {
    return new AttributeType(oid, names, description, isObsolete,
        superiorTypeOID, equalityMatchingRuleOID,
        orderingMatchingRuleOID, substringMatchingRuleOID,
        approximateMatchingRuleOID, syntaxOID, isSingleValue,
        isCollective, isNoUserModification, attributeUsage,
        extraProperties, definition);
  }



  /**
   * {@inheritDoc}
   */
  @Override
  void validate(List<Message> warnings, Schema schema)
      throws SchemaException
  {
    if (superiorTypeOID != null)
    {
      superiorType = schema.getAttributeType(superiorTypeOID);

      // If there is a superior type, then it must have the same usage
      // as the
      // subordinate type. Also, if the superior type is collective,
      // then so
      // must the subordinate type be collective.
      if (superiorType.getUsage() != getUsage())
      {
        Message message =
            WARN_ATTR_SYNTAX_ATTRTYPE_INVALID_SUPERIOR_USAGE.get(
                getNameOrOID(), getUsage().toString(), superiorType
                    .getNameOrOID());
        throw new SchemaException(message);
      }

      if (superiorType.isCollective() != isCollective())
      {
        Message message;
        if (isCollective())
        {
          message =
              WARN_ATTR_SYNTAX_ATTRTYPE_COLLECTIVE_FROM_NONCOLLECTIVE
                  .get(getNameOrOID(), superiorType.getNameOrOID());
        }
        else
        {
          message =
              WARN_ATTR_SYNTAX_ATTRTYPE_NONCOLLECTIVE_FROM_COLLECTIVE
                  .get(getNameOrOID(), superiorType.getNameOrOID());
        }
        throw new SchemaException(message);
      }
    }

    if (syntaxOID != null)
    {
      syntax = schema.getSyntax(syntaxOID);
    }
    else if (getSuperiorType() != null
        && getSuperiorType().getSyntax() != null)
    {
      // Try to inherit the syntax from the superior type if possible
      syntax = getSuperiorType().getSyntax();
    }

    if (equalityMatchingRuleOID != null)
    {
      // Use explicitly defined matching rule first.
      equalityMatchingRule =
          schema.getMatchingRule(equalityMatchingRuleOID);
    }
    else if (getSuperiorType() != null
        && getSuperiorType().getEqualityMatchingRule() != null)
    {
      // Inherit matching rule from superior type if possible
      equalityMatchingRule =
          getSuperiorType().getEqualityMatchingRule();
    }
    else if (getSyntax() != null
        && getSyntax().getEqualityMatchingRule() != null)
    {
      // Use default for syntax
      equalityMatchingRule = getSyntax().getEqualityMatchingRule();
    }

    if (orderingMatchingRuleOID != null)
    {
      // Use explicitly defined matching rule first.
      orderingMatchingRule =
          schema.getMatchingRule(orderingMatchingRuleOID);
    }
    else if (getSuperiorType() != null
        && getSuperiorType().getOrderingMatchingRule() != null)
    {
      // Inherit matching rule from superior type if possible
      orderingMatchingRule =
          getSuperiorType().getOrderingMatchingRule();
    }
    else if (getSyntax() != null
        && getSyntax().getOrderingMatchingRule() != null)
    {
      // Use default for syntax
      orderingMatchingRule = getSyntax().getOrderingMatchingRule();
    }

    if (substringMatchingRuleOID != null)
    {
      // Use explicitly defined matching rule first.
      substringMatchingRule =
          schema.getMatchingRule(substringMatchingRuleOID);
    }
    else if (getSuperiorType() != null
        && getSuperiorType().getSubstringMatchingRule() != null)
    {
      // Inherit matching rule from superior type if possible
      substringMatchingRule =
          getSuperiorType().getSubstringMatchingRule();
    }
    else if (getSyntax() != null
        && getSyntax().getSubstringMatchingRule() != null)
    {
      // Use default for syntax
      substringMatchingRule = getSyntax().getSubstringMatchingRule();
    }

    if (approximateMatchingRuleOID != null)
    {
      // Use explicitly defined matching rule first.
      approximateMatchingRule =
          schema.getMatchingRule(approximateMatchingRuleOID);
    }
    else if (getSuperiorType() != null
        && getSuperiorType().getApproximateMatchingRule() != null)
    {
      // Inherit matching rule from superior type if possible
      approximateMatchingRule =
          getSuperiorType().getApproximateMatchingRule();
    }
    else if (getSyntax() != null
        && getSyntax().getApproximateMatchingRule() != null)
    {
      // Use default for syntax
      approximateMatchingRule =
          getSyntax().getApproximateMatchingRule();
    }

    // If the attribute type is COLLECTIVE, then it must have a usage of
    // userApplications.
    if (isCollective()
        && getUsage() != AttributeUsage.USER_APPLICATIONS)
    {
      Message message =
          WARN_ATTR_SYNTAX_ATTRTYPE_COLLECTIVE_IS_OPERATIONAL
              .get(getNameOrOID());
      throw new SchemaException(message);
    }

    // If the attribute type is NO-USER-MODIFICATION, then it must not
    // have a
    // usage of userApplications.
    if (isNoUserModification()
        && getUsage() == AttributeUsage.USER_APPLICATIONS)
    {
      Message message =
          WARN_ATTR_SYNTAX_ATTRTYPE_NO_USER_MOD_NOT_OPERATIONAL
              .get(getNameOrOID());
      throw new SchemaException(message);
    }
  }



  @Override
  void toStringContent(StringBuilder buffer)
  {
    buffer.append(oid);

    if (!names.isEmpty())
    {
      Iterator<String> iterator = names.iterator();

      String firstName = iterator.next();
      if (iterator.hasNext())
      {
        buffer.append(" NAME ( '");
        buffer.append(firstName);

        while (iterator.hasNext())
        {
          buffer.append("' '");
          buffer.append(iterator.next());
        }

        buffer.append("' )");
      }
      else
      {
        buffer.append(" NAME '");
        buffer.append(firstName);
        buffer.append("'");
      }
    }

    if ((description != null) && (description.length() > 0))
    {
      buffer.append(" DESC '");
      buffer.append(description);
      buffer.append("'");
    }

    if (isObsolete)
    {
      buffer.append(" OBSOLETE");
    }

    if (superiorTypeOID != null)
    {
      buffer.append(" SUP ");
      buffer.append(superiorTypeOID);
    }

    if (equalityMatchingRuleOID != null)
    {
      buffer.append(" EQUALITY ");
      buffer.append(equalityMatchingRuleOID);
    }

    if (orderingMatchingRuleOID != null)
    {
      buffer.append(" ORDERING ");
      buffer.append(orderingMatchingRuleOID);
    }

    if (substringMatchingRuleOID != null)
    {
      buffer.append(" SUBSTR ");
      buffer.append(substringMatchingRuleOID);
    }

    if (syntaxOID != null)
    {
      buffer.append(" SYNTAX ");
      buffer.append(syntaxOID);
    }

    if (isSingleValue())
    {
      buffer.append(" SINGLE-VALUE");
    }

    if (isCollective())
    {
      buffer.append(" COLLECTIVE");
    }

    if (isNoUserModification())
    {
      buffer.append(" NO-USER-MODIFICATION");
    }

    if (attributeUsage != null)
    {
      buffer.append(" USAGE ");
      buffer.append(attributeUsage.toString());
    }

    if (approximateMatchingRuleOID != null)
    {
      buffer.append(" ");
      buffer.append(SCHEMA_PROPERTY_APPROX_RULE);
      buffer.append(" '");
      buffer.append(approximateMatchingRuleOID);
      buffer.append("'");
    }
  }
}
