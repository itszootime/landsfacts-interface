package org.uncertweb.landsfacts.test;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.uncertweb.landsfacts.data.FieldDescription;
import org.uncertweb.landsfacts.data.InitialTransitionMatrix;

public class TestDataTest {

	@Test
	public void fieldDescriptions() {
		// load descriptions
		List<FieldDescription> fd = TestData.getFieldDescriptions();
		
		// check size
		Assert.assertEquals(7567, fd.size());
		
		// check a field
		FieldDescription f = fd.get(179);
		Assert.assertEquals(180, f.getId());
		Assert.assertEquals(272266.553891, f.getArea(), 0.0);
		Assert.assertEquals("Clayey", f.getSoilType());
	}
	
	@Test
	public void initialTransitionMatrices() {
		// load itms
		List<InitialTransitionMatrix> itms = TestData.getInitialTransitionMatrices();
		
		// check size
		Assert.assertEquals(4, itms.size());
		
		// check matrices
		InitialTransitionMatrix matrix = itms.get(0);
		Assert.assertEquals("Clayey", matrix.getSoilType());
		
		// crops
		Assert.assertEquals("BA1", matrix.getCrops().get(0));
		Assert.assertEquals("BP", matrix.getCrops().get(1));
		Assert.assertEquals("FH", matrix.getCrops().get(2));
		Assert.assertEquals("FSA", matrix.getCrops().get(3));
		Assert.assertEquals("FXLI", matrix.getCrops().get(4));
		Assert.assertEquals("GR", matrix.getCrops().get(5));
		Assert.assertEquals("OA1", matrix.getCrops().get(6));
		Assert.assertEquals("OAR", matrix.getCrops().get(7));
		Assert.assertEquals("OLA", matrix.getCrops().get(8));
		Assert.assertEquals("OSR", matrix.getCrops().get(9));
		Assert.assertEquals("PO1", matrix.getCrops().get(10));
		Assert.assertEquals("SU1", matrix.getCrops().get(11));
		Assert.assertEquals("VAS", matrix.getCrops().get(12));
		Assert.assertEquals("WH", matrix.getCrops().get(13));
		Assert.assertEquals("WOOD", matrix.getCrops().get(14));
		
		// values matrix
		double[][] values = matrix.getMatrix();
		Assert.assertEquals(0.401042625, values[0][0], 0.0);
		Assert.assertEquals(0.078626188, values[0][1], 0.0);
		Assert.assertEquals(0.000735971, values[0][2], 0.0);
		Assert.assertEquals(0.076847593, values[0][3], 0.0);
		Assert.assertEquals(0.02367372, values[0][4], 0.0);
		Assert.assertEquals(0.010978228, values[0][5], 0.0);
		Assert.assertEquals(0.01674333, values[0][6], 0.0);
		Assert.assertEquals(0.007789022, values[0][7], 0.0);
		Assert.assertEquals(0.001901257, values[0][8], 0.0);
		Assert.assertEquals(0.14216498, values[0][9], 0.0);
		Assert.assertEquals(0.017172646, values[0][10], 0.0);
		Assert.assertEquals(0.114627415, values[0][11], 0.0);
		Assert.assertEquals(0.010978228, values[0][12], 0.0);
		Assert.assertEquals(0.096473474, values[0][13], 0.0);
		Assert.assertEquals(0.000245324, values[0][14], 0.0);
	}
	
}
