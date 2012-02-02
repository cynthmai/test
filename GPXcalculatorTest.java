package edu.upenn.cis350.gpx;

import java.util.ArrayList;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

public class GPXcalculatorTest extends TestCase {
	
	ArrayList<GPXtrkseg> trksegs;
	ArrayList<GPXtrkpt> trkpts;
	GPXtrkpt trkpt0, trkpt1, trkpt2, trkpt3, trkpt4, trkpt5, trkpt6;
	
	
	public void setUp() {
		trksegs = new ArrayList<GPXtrkseg>();
		trkpts = new ArrayList<GPXtrkpt>();
		trkpt0 = new GPXtrkpt(0, 0, new Date());
		trkpt1 = new GPXtrkpt(50, 50, new Date());
		trkpt2 = new GPXtrkpt(60, 60, new Date());
		trkpt3 = new GPXtrkpt(-90, 0, new Date());
		trkpt4 = new GPXtrkpt(90, 0, new Date());
		trkpt5 = new GPXtrkpt(0, -180, new Date());
		trkpt6 = new GPXtrkpt(0, 180, new Date());
	}

	/**
	 * Normal case should return distance
	 */
	@Test
	public void testNormal() {
		trkpts.add(trkpt1);
		trkpts.add(trkpt2);
		GPXtrkseg trkseg = new GPXtrkseg(trkpts);
		trksegs.add(trkseg);
		GPXtrk trk = new GPXtrk("trk", trksegs);
		assertEquals(Math.sqrt(200), GPXcalculator.calculateDistanceTraveled(trk));
		
		trkpts.add(trkpt0);
		assertEquals(Math.sqrt(200) + Math.sqrt(7200), GPXcalculator.calculateDistanceTraveled(trk));
	}

	/**
	 * If the GPXtrk object is null, the method should return -1.
	 */
	@Test
	public void testNullObject() {
		assertEquals(-1, GPXcalculator.calculateDistanceTraveled(null));
	}

	/**
	 * If the GPXtrk contains no GPXtrkseg objects, the method should return -1.
	 */
	@Test
	public void testEmptyGPXtrk() {
		GPXtrk empty = new GPXtrk("Empty", trksegs);
		assertEquals(-1, GPXcalculator.calculateDistanceTraveled(empty));
		
	}

	/**
	 * If any GPXtrkseg in the GPXtrk is null, the distance traveled for that
	 *  GPXtrkseg should be considered 0.
	 */
	@Test 
	public void testNullGPXtrkseg() {
		trksegs.add(null);
		GPXtrk oneNull = new GPXtrk("oneNull", trksegs);
		assertEquals(0, GPXcalculator.calculateDistanceTraveled(oneNull));
	}

	/**
	 *  If a GPXtrkseg contains no GPXtrkpt objects, the distance traveled for
	 *   that GPXtrkseg should be considered 0
	 */
	@Test
	public void testNoGPXtrkpt() {
		GPXtrkseg noObj = new GPXtrkseg(trkpts);
		trksegs.add(noObj);
		GPXtrk oneEmpty = new GPXtrk("oneEmpty", trksegs);
		assertEquals(0,GPXcalculator.calculateDistanceTraveled(oneEmpty));
	}

	/**
	 * If a GPXtrkseg contains only one GPXtrkpt, the distance traveled for
	 *  that GPXtrkseg should be considered 0.
	 */
	@Test
	public void testOneGPXtrkpt() {
		trkpts.add(new GPXtrkpt(5, 5, new Date()));
		GPXtrkseg oneObj = new GPXtrkseg(trkpts);
		trksegs.add(oneObj);
		GPXtrk oneEmpty = new GPXtrk("oneEmpty", trksegs);
		assertEquals(0,GPXcalculator.calculateDistanceTraveled(oneEmpty));
	}

	/**
	 * If any GPXtrkpt in a GPXtrkseg is null, the distance traveled for that
	 *  GPXtrkseg should be considered 0.
	 */
	@Test
	public void testNullGPXtrkpt() {
		trkpts.add(null);
		GPXtrkseg oneObj = new GPXtrkseg(trkpts);
		trksegs.add(oneObj);
		GPXtrk trk = new GPXtrk("trk", trksegs);
		assertEquals(0,GPXcalculator.calculateDistanceTraveled(trk));
	}

	/**
	 * If any GPXtrkpt in a GPXtrkseg has a latitude that is greater than 90 or
	 *  less than -90, the distance traveled for that GPXtrkseg should be considered 0.
	 */
	@Test 
	public void testLargeSmallLat() {
		GPXtrkpt trkpt = new GPXtrkpt(-100, 80, new Date());
		trkpts.add(trkpt);
		GPXtrkseg small = new GPXtrkseg(trkpts);
		trksegs.add(small);
		GPXtrk trk = new GPXtrk("trk", trksegs);
		assertEquals(0,GPXcalculator.calculateDistanceTraveled(trk));
		
		trkpt = new GPXtrkpt(100, 80, new Date());
		assertEquals(0,GPXcalculator.calculateDistanceTraveled(trk));
		
	}

	/**
	 *  If any GPXtrkpt in a GPXtrkseg has a longitude that is greater than 180
	 *   or less than -180, the distance traveled for that GPXtrkseg should be considered 0
	 */
	@Test
	public void testLargeSmallLong() {
		GPXtrkpt trkpt = new GPXtrkpt(50, -200, new Date());
		trkpts.add(trkpt);
		GPXtrkseg small = new GPXtrkseg(trkpts);
		trksegs.add(small);
		GPXtrk trk = new GPXtrk("trk", trksegs);
		assertEquals(0,GPXcalculator.calculateDistanceTraveled(trk));
		
		trkpt = new GPXtrkpt(50, 200, new Date());
		assertEquals(0,GPXcalculator.calculateDistanceTraveled(trk));
	}
	
	/**
	 * Test if traveled no distance
	 */
	@Test
	public void testNoMove() {
		trkpts.add(trkpt1);
		trkpts.add(trkpt1);
		GPXtrkseg trkseg = new GPXtrkseg(trkpts);
		trksegs.add(trkseg);
		GPXtrk trk = new GPXtrk("trk", trksegs);
		assertEquals(0,GPXcalculator.calculateDistanceTraveled(trk));
	}
	
	/**
	 * Testing edge case of +/-90 latitude
	 */
	public void testEdgeLat() {
		trkpts.add(trkpt0);
		trkpts.add(trkpt3);
		GPXtrkseg trkseg = new GPXtrkseg(trkpts);
		trksegs.add(trkseg);
		GPXtrk trk = new GPXtrk("trk", trksegs);
		assertEquals(90, GPXcalculator.calculateDistanceTraveled(trk));
		
		trkpts.remove(trkpt3);
		trkpts.add(trkpt4);
		assertEquals(90, GPXcalculator.calculateDistanceTraveled(trk));
	}
	
	/**
	 * Testing edge case of +/-180 longitude
	 */
	public void testEdgeLong() {
		trkpts.add(trkpt0);
		trkpts.add(trkpt5);
		GPXtrkseg trkseg = new GPXtrkseg(trkpts);
		trksegs.add(trkseg);
		GPXtrk trk = new GPXtrk("trk", trksegs);
		assertEquals(180, GPXcalculator.calculateDistanceTraveled(trk));
		
		trkpts.remove(trkpt5);
		trkpts.add(trkpt6);
		assertEquals(180, GPXcalculator.calculateDistanceTraveled(trk));
	}
}
