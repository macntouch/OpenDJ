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

package org.opends.types;



/**
 * A distinguished name and sequence of attributes.
 */
public interface AttributeSequence
{

  /**
   * Ensures that this attribute sequence contains the provided
   * attribute values. Any existing values for the attribute will be
   * retained.
   *
   * @param attribute
   *          The attribute to be added.
   * @return This attribute sequence.
   * @throws UnsupportedOperationException
   *           If this attribute sequence does not permit attributes to
   *           be added.
   * @throws IllegalArgumentException
   *           If {@code attribute} was empty.
   * @throws NullPointerException
   *           If {@code attribute} was {@code null}.
   */
  AttributeSequence addAttribute(AttributeValueSequence attribute)
      throws UnsupportedOperationException, IllegalArgumentException,
      NullPointerException;



  /**
   * Removes all the attributes from this attribute sequence.
   *
   * @return This attribute sequence.
   * @throws UnsupportedOperationException
   *           If this attribute sequence does not permit attributes to
   *           be removed.
   */
  AttributeSequence clearAttributes()
      throws UnsupportedOperationException;



  /**
   * Gets the named attribute from this attribute sequence.
   *
   * @param attributeDescription
   *          The name of the attribute to be returned.
   * @return The named attribute, or {@code null} if it is not included
   *         with this attribute sequence.
   * @throws NullPointerException
   *           If {@code attributeDescription} was {@code null}.
   */
  AttributeValueSequence getAttribute(String attributeDescription)
      throws NullPointerException;



  /**
   * Returns the number of attributes in this attribute sequence.
   *
   * @return The number of attributes.
   */
  int getAttributeCount();



  /**
   * Returns an {@code Iterable} containing the attributes in this
   * attribute sequence. The returned {@code Iterable} may be used to
   * remove attributes if permitted by this attribute sequence.
   *
   * @return An {@code Iterable} containing the attributes.
   */
  Iterable<? extends AttributeValueSequence> getAttributes();



  /**
   * Returns the distinguished name of this attribute sequence.
   *
   * @return The distinguished name.
   */
  String getName();



  /**
   * Indicates whether or not this attribute sequence has any
   * attributes.
   *
   * @return {@code true} if this attribute sequence has any attributes,
   *         otherwise {@code false}.
   */
  boolean hasAttributes();



  /**
   * Removes the named attribute from this attribute sequence.
   *
   * @param attributeDescription
   *          The name of the attribute to be removed.
   * @return The removed attribute, or {@code null} if the attribute is
   *         not included with this attribute sequence.
   * @throws UnsupportedOperationException
   *           If this attribute sequence does not permit attributes to
   *           be removed.
   * @throws NullPointerException
   *           If {@code attributeDescription} was {@code null}.
   */
  AttributeValueSequence removeAttribute(String attributeDescription)
      throws UnsupportedOperationException, NullPointerException;



  /**
   * Sets the distinguished name of this attribute sequence.
   *
   * @param dn
   *          The distinguished name.
   * @return This attribute sequence.
   * @throws UnsupportedOperationException
   *           If this attribute sequence does not permit the
   *           distinguished name to be set.
   * @throws NullPointerException
   *           If {@code dn} was {@code null}.
   */
  AttributeSequence setName(String dn)
      throws UnsupportedOperationException, NullPointerException;

}
