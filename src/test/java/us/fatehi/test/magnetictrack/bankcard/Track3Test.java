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
package us.fatehi.test.magnetictrack.bankcard;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import us.fatehi.magnetictrack.bankcard.Track3;

public class Track3Test {

	@Test
	public void track3__1() {
		final String track3Data = "+6202408082356005=15046200000010000000000004976?";
		final Track3 track3 = new Track3(track3Data);
		checkCardData(track3);
	}

	@Test
	public void track3__2() {
		final String track3Data = "%B378578692630345^ /                        ^1508121140165241?;378578692630345=150812114016524100000?+6202408082356005=15046200000010000000000004976?";
		final Track3 track3 = new Track3(track3Data);
		checkCardData(track3);
	}

	private void checkCardData(final Track3 track3) {
		assertEquals("+6202408082356005=15046200000010000000000004976?",
				track3.getTrackData());
		assertEquals("6202408082356005=15046200000010000000000004976",
				track3.getDiscretionaryData());
	}

}
