/*
 *
 * Magnetic Stripe Parser
 * https://github.com/sualeh/magnetictrackparser
 * Copyright (c) 2014, Sualeh Fatehi.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */
package us.fatehi.magnetictrack.bankcard;


import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.threeten.bp.YearMonth;

/**
 * From <a
 * href="https://en.wikipedia.org/wiki/ISO/IEC_7813#Magnetic_tracks"
 * >Wikipedia - ISO/IEC 7813</a><br/>
 * The Track 1 structure is specified as:
 * <ol>
 * <li>STX: Start sentinel "%"</li>
 * <li>FC: Format code "B" (The format described here. Format "A" is
 * reserved for proprietary use.)</li>
 * <li>PAN: Primary Account Number, up to 19 digits</li>
 * <li>FS: Separator "^"</li>
 * <li>NM: Name, 2 to 26 characters (including separators, where
 * appropriate, between surname, first name etc.)</li>
 * <li>FS: Separator "^"</li>
 * <li>ED: Expiration data, 4 digits or "^"</li>
 * <li>SC: Service code, 3 digits or "^"</li>
 * <li>DD: Discretionary data, balance of characters</li>
 * <li>ETX: End sentinel "?"</li>
 * <li>LRC: Longitudinal redundancy check, calculated according to
 * ISO/IEC 7811-2</li>
 * </ol>
 * The maximum record length is 79 alphanumeric characters.
 *
 * @see <a
 *      href="https://en.wikipedia.org/wiki/ISO/IEC_7813#Magnetic_tracks">Wikipedia
 *      - ISO/IEC 7813</a>
 */
public class Track1FormatB
  extends BaseTrack
{

  private static final long serialVersionUID = 3020739300944280022L;

  private static final Pattern track1FormatBPattern = Pattern
    .compile("(%([A-Z])([0-9]{1,19})\\^([^\\^]{2,26})\\^([0-9]{4}|\\^)([0-9]{3}|\\^)?([^\\?]+)?\\?)[\\t\\n\\r ]?.*");

  private final String formatCode;
  private final PrimaryAccountNumber pan;
  private final Name name;
  private final YearMonth expirationDate;
  private final ServiceCode serviceCode;

  public Track1FormatB(final String track)
  {

    final Matcher matcher;
    final boolean matches;
    if (!isBlank(track))
    {
      matcher = track1FormatBPattern.matcher(track);
      matches = matcher.matches();
    }
    else
    {
      matcher = null;
      matches = false;
    }

    if (matches)
    {
      trackData = getGroup(matcher, 1);
      formatCode = getGroup(matcher, 2);
      pan = new PrimaryAccountNumber(getGroup(matcher, 3));
      name = new Name(getGroup(matcher, 4));
      expirationDate = parseExpirationDate(matcher, 5);
      serviceCode = new ServiceCode(getGroup(matcher, 6));
      discretionaryData = getGroup(matcher, 7);
    }
    else
    {
      trackData = null;
      formatCode = null;
      pan = null;
      name = null;
      expirationDate = null;
      serviceCode = null;
      discretionaryData = null;
    }
  }

  /**
   * @see us.fatehi.magnetictrack.bankcard.Track#exceedsMaximumLength()
   */
  @Override
  public boolean exceedsMaximumLength()
  {
    return hasTrackData() && trackData.length() > 79;
  }

  /**
   * @return the expirationDate
   */
  public YearMonth getExpirationDate()
  {
    return expirationDate;
  }

  /**
   * @return the formatCode
   */
  public String getFormatCode()
  {
    return formatCode;
  }

  /**
   * @return the name
   */
  public Name getName()
  {
    return name;
  }

  /**
   * @return the pan
   */
  public PrimaryAccountNumber getPrimaryAccountNumber()
  {
    return pan;
  }

  /**
   * @return the serviceCode
   */
  public ServiceCode getServiceCode()
  {
    return serviceCode;
  }

  public boolean hasExpirationDate()
  {
    return expirationDate != null && pan.hasPrimaryAccountNumber();
  }

  public boolean hasFormatCode()
  {
    return !isBlank(formatCode);
  }

  public boolean hasName()
  {
    return name != null && name.hasName();
  }

  public boolean hasPrimaryAccountNumber()
  {
    return pan != null;
  }

  public boolean hasServiceCode()
  {
    return serviceCode != null && serviceCode.hasServiceCode();
  }

}
