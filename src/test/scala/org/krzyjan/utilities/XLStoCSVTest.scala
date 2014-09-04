package org.krzyjan.utilities

import org.krzyjan.common.UnitFlatSpec
import java.io.FileInputStream

class XLStoCSVTest extends UnitFlatSpec {
  trait Values {
    val fileSep = System.getProperty("file.separator");
    val topDir = "src/test/resources"

    val converter = new POIXLStoCSV
    val escapeSpreadSheet = topDir + fileSep + "EscapeEmbedded.xls"
  }
  
  it should "correctly escape commas and quotes" in new Values {
    val rows = converter.generateRows(new FileInputStream(escapeSpreadSheet), ",")
    
    assert(rows.size == 1)
    
    assert(rows(0) == "\"2,300\",How's that?,\"This is a \"\"good\"\" quote.\",\"We've seen \"\"the light\"\", and it was good.\",\"This goes over\ntwo lines.\"")
  }
}